import imaplib, email, os

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
    connect.enable("UTF8=ACCEPT")
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
    N = 3
    # total number of emails
    messages_int = int(messages[0])
    print("message_int------\n" + str(messages_int))

    typ, data = connection.search(None, 'ALL')
    output_list = []
    for num in data[0].split():
        typ, data = connection.fetch(num, '(RFC822)')
        msg = email.message_from_bytes(data[0][1])
        #print(type(msg))
        #print(msg)
        raw_subject = email.header.decode_header(msg['Subject'])[0]
        #raw_body = email.header.decode_header(msg['Body'])[0][0]
        '''
        if decode[1] == 'utf-8':
            subject = decode[0].decode('utf-8')
        else:
            subject = decode[0]
        '''
        #print("subject: {}".format(subject))
        #input()
        #print('Message %s\n%s\n' % (num, data[0][1]))
        #print('Message %s\n%s\n' % (num, data[0][1].split()))
        #print('%s\n' % (len(data[0][1].split())))
        '''
        j = 0
        for i in range(len(str(msg))):
            if str(msg)[i] == "\n":
                print(str(msg)[j:i])
                j = int(i)
        '''


        output_list.append(str(raw_subject))

    connection.close()
    connection.logout()

    return output_list

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
