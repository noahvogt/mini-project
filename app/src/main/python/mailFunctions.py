import imaplib, smtplib, ssl, email, os, json
from itertools import chain

# format raw string you get from fetching mails
def stringCompiling(inputIterable):
    # remove first nested iterables
    try:
        unitered = list(chain.from_iterable(inputIterable))
    except TypeError:
        unitered = inputIterable

    # remove None Type entries
    nonNoneList = []
    try:
        for item in unitered:
            if item is not None:
                if type(item) is not str:
                    try:
                        nonNoneList.append(str(item.decode("utf-8")))
                    except UnicodeDecodeError:
                        nonNoneList.append(str(item.decode("iso-8859-1")))
                    except AttributeError:
                        #print(item)
                        #print(type(item))
                        #print(inputIterable)
                        #print(nonNoneList)
                        exit()
                else:
                    nonNoneList.append(item)

    except TypeError:
        return ""

    # return non empty values
    if len(nonNoneList) >= 1:
        return nonNoneList[0]
    else:
        return ""

def errorMsgExit(error_msg):
    print("Error: " + error_msg)

def checkConnection(host, username, password, port):
    try:
        connection = imaplib.IMAP4_SSL(host, port)
        connection.login(username, password)
        connection.logout()
        return True
    except Exception as e:
        #print(str(e))
        return False

def connect(host, username, password, port):
    connect = imaplib.IMAP4_SSL(host, port)
    connect.login(username, password)
    try:
        connect.enable("UTF8=ACCEPT")
    except:
        pass
    return connect

def listMailboxes(connection):
    mailboxes = connection.list()
    formatted_mailbox_list = []

    for items in mailboxes:
        if type(items) == list:
            for raw_box_string in items:
                box_string = str(raw_box_string)
                # TODO: handle cases when folder contains subfolders
                modified_box_string = (box_string[box_string.find('"/" ')+4:-1])

                # strip unneeded "'s surrounding the folder name
                if modified_box_string.startswith('"') and modified_box_string.endswith('"'):
                    modified_box_string = modified_box_string[1:-1]

                formatted_mailbox_list.append(modified_box_string)

    connection.logout()
    return formatted_mailbox_list



    # check that there are no bytes anymore that cannot be dumped into a json
def verifyNoBytes(messages, output_list):
    for messages in output_list:
        for item in messages:
            #print(type(item))
            #print(item)
            #print(messages["{}".format(item)])
            if type(messages["{}".format(item)]) is not str:
                print("ERROREXIT: .format failed")
                print(messages["{}".format(item)])
                print(type(messages["{}".format(item)]))

                exit()
            if type(item) is not str:
                print("ERROREXIT")
                exit()

def fetchMails(connection, inbox, folderLocal):
    #print("###" + inbox + "###")
    #print(type(inbox))
    try:
        status, messages = connection.select(inbox)

    except:
        return []
    #print("status-------\n" + status)
    #print("messages-------\n" + str(messages))

    # number of top emails to fetch
    #N = 3
    # total number of emails
    messages_int = int(messages[0])
    #print("message_int------\n" + str(messages_int))

    output_list = []

    for seentype in ['(UNSEEN)', '(SEEN)']:
        typ, data = connection.search(None, 'ALL', seentype)
        for num in data[0].split():
            output_dict = {}

            typ, data = connection.fetch(num, '(RFC822)')

            msg = email.message_from_bytes(data[0][1])

            #print(num)

            raw_string = email.header.decode_header(msg['Subject'])[0]
            #print("raw_string: " + str(raw_string))
            raw_from = email.header.decode_header(msg['From'])
            #print("raw_from" + str(raw_from))
            try:
                raw_to = email.header.decode_header(msg['To'])
            except TypeError:
                raw_to = [""]
            try:
                raw_cc = email.header.decode_header(msg['CC'])
            except TypeError:
                raw_cc = [""]
            try:
                raw_bcc = email.header.decode_header(msg['BCC'])
            except TypeError:
                raw_bcc = [""]
            #print("raw_to" + str(raw_to))
            raw_date = email.header.decode_header(msg['Date'])[0]
            #print("raw_to" + str(raw_date))

            raw_msg = str(msg)

            primitive_body = raw_msg[raw_msg.find('\n\n'):].strip()

            #raw_body = email.header.decode_header(msg['Body'])[0][0]

            # set subject to an empty string when not found
            try:
                if raw_string[1] == 'utf-8':
                    subject = raw_string[0].raw_string('utf-8')
                else:
                    subject = raw_string[0].decode("iso-8859-1")
                            #nonNoneList.append(str(item.decode("iso-8859-1")))
            except AttributeError:
                subject=""

            output_dict['subject'] = subject
            output_dict['from'] = stringCompiling(raw_from)
            output_dict['cc'] = stringCompiling(raw_cc)
            output_dict['bcc'] = stringCompiling(raw_bcc)
            output_dict['to'] = stringCompiling(raw_to)
            output_dict['date'] = stringCompiling(raw_date)
            output_dict['content'] = primitive_body
            output_dict['folder'] = folderLocal
            print('FolderServer: ' + inbox)
            print('FolderLocal: ' + folderLocal)
            print('From: ' + stringCompiling(raw_from))
            print('Outputdictionary: ' + str(output_dict))

            if seentype == '(SEEN)':
                output_dict['seen'] = "True"
            else:
                output_dict['seen'] = "False"
                # make sure the fetch command doesn't add a SEEN flag
                connection.store(num, '-FLAGS', '(\Seen)')

            output_list.append(output_dict)



    connection.close()
    connection.logout()

    verifyNoBytes(messages, output_list)

    print("Finstep")

    return json.dumps(output_list)


def sendStarttls(host, sendingMail, receivingMail, password, message="", subject="", port=587, cc=[], bcc=[]):
    context = ssl.create_default_context()

    if type(cc) is not str:
        cc = ",".join(cc)
    if type(bcc) is not str:
        bcc = ",".join(bcc)
    utf8Message = "Subject: " + subject + "\nCC: " + cc + "\nBCC: " + bcc + "\n\n" + message
    decoded=utf8Message.encode('cp1252').decode('utf-8')

    with smtplib.SMTP(host, port) as serverConnection:
        serverConnection.starttls(context=context)
        serverConnection.login(sendingMail, password)
        serverConnection.sendmail(sendingMail, receivingMail, decoded)
