import imaplib, smtplib, ssl, email, os
from itertools import chain

mSubject = ""
mFrom = ""
mCC = ""
mBcc = ""
mTo = ""
mDate = ""
mContent = ""
output_list= []

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
        print(str(e))
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

def fetchMails(connection, inbox, outputType):
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

    typ, data = connection.search(None, 'ALL')
    global output_list

    for num in data[0].split():
        if outputType == "dict":
            output_dict = {}
        else:
           inner_output_list =[]

        typ, data = connection.fetch(num, '(RFC822)')
        msg = email.message_from_bytes(data[0][1])

        #print(msg)
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
                subject = raw_string[0]
        except AttributeError:
            subject=""

        #print("subject: {}".format(subject))

        if outputType == "dict":
            output_dict['subject'] = subject
            output_dict['from'] = stringCompiling(raw_from)
            output_dict['cc'] = stringCompiling(raw_cc)
            output_dict['bcc'] = stringCompiling(raw_bcc)
            output_dict['to'] = stringCompiling(raw_to)
            output_dict['date'] = stringCompiling(raw_date)
            output_dict['content'] = primitive_body

            output_list.append(output_dict)
        else:
            inner_output_list.append(subject)
            inner_output_list.append(stringCompiling(raw_from))
            inner_output_list.append(stringCompiling(raw_cc))
            inner_output_list.append(stringCompiling(raw_bcc))
            inner_output_list.append(stringCompiling(raw_to))
            inner_output_list.append(stringCompiling(raw_date))
            inner_output_list.append(primitive_body)

            output_list.append(inner_output_list)

            global mSubject, mFrom, mCC, mContent, mBcc, mTo, mDate
            print("subject " + subject)
            mSubject = subject
            mFrom = stringCompiling(raw_from)
            mCC = stringCompiling(raw_cc)
            mBcc = stringCompiling(raw_bcc)
            mTo = stringCompiling(raw_to)
            mDate = stringCompiling(raw_date)
            mContent = primitive_body


    connection.close()
    connection.logout()

    return output_list

def printSubject(messageIndex):
    print(output_list[messageIndex][0])
    return output_list[messageIndex][0]

def printFrom(messageIndex):
    print(output_list[messageIndex][1])
    return mFrom

def printCc(messageIndex):
    print(output_list[messageIndex][2])
    return mCC

def printBcc(messageIndex):
    print(output_list[messageIndex][3])
    return mBcc

def printTo(messageIndex):
    print(output_list[messageIndex][4])
    return mTo

def printDate(messageIndex):
    print(output_list[messageIndex][5])
    return mDate

def printContent(messageIndex):
    print(output_list[messageIndex][6])
    return mContent

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

def afetchMails(con):
    con.select("Sent")
    status, email_ids = con.search(None, "ALL")
    if status != 'OK':
        raise Exception("Error running imap search for spinvox messages: "
                        "%s" % status)

    print(email_ids[0])
    fetch_ids = ','.join(str(email_ids[0]).split())
    status, data = con.fetch(3, '(RFC822)')
    if status != 'OK':
        raise Exception("Error running imap fetch for spinvox message: "
                        "%s" % status)
    for i in range(3,4):
        header_msg = email.message_from_string(data[i * 3 + 0][1])
        subject = header_msg['Subject'],
        print(subject)
        date = header_msg['Date'],
        print(date)
        body = data[i * 3 + 1][1]
        print(body)
    connection.close()
    connection.logout()
