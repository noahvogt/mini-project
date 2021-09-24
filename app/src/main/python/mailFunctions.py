import imaplib, smtplib, ssl, email, os

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

def fetchMails(connection, inbox):
    status, messages = connection.select(inbox)
    print("status-------\n" + status)
    print("messages-------\n" + str(messages))
    # number of top emails to fetch
    #N = 3
    # total number of emails
    messages_int = int(messages[0])
    print("message_int------\n" + str(messages_int))

    typ, data = connection.search(None, 'ALL')
    output_list = []
    for num in data[0].split():
        output_dict = {}
        typ, data = connection.fetch(num, '(RFC822)')
        msg = email.message_from_bytes(data[0][1])

        #print(msg)
        print(num)

        raw_string = email.header.decode_header(msg['Subject'])[0]
        print("raw_string: " + str(raw_string))
        raw_from = email.header.decode_header(msg['From'])
        print("raw_from" + str(raw_from))
        try:
            raw_to = email.header.decode_header(msg['To'])
        except TypeError:
            raw_to = [("", None)]
        print("raw_to" + str(raw_to))
        raw_date = email.header.decode_header(msg['Date'])[0]
        print("raw_to" + str(raw_date))

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

        output_dict['subject'] = subject
        output_dict['from'] = raw_from[0]
        output_dict['to'] = raw_to[0]
        output_dict['date'] = raw_date[0]
        output_dict['content'] = primitive_body

        output_list.append(output_dict)

    connection.close()
    connection.logout()

    return output_list

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
