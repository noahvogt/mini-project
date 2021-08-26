import imaplib

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
