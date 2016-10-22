import sys

aliasDict = {}
ALIAS_DATABASE = "./Aliases/aliases.csv"


def load_aliases():
    f = open(ALIAS_DATABASE,'r')
    for line in f.readlines():
        line = line.replace('\n', '').split(",")
        aliasDict[line[0]] = line[1:]
    f.close()
    return aliasDict


def register(connection):
    alias = ""
    android_id = ""
    ip = ""
    port = ""
    while True:
        data = connection.recv(128)
        if not data:
            break
        data = data.split(":")
        if data[0] == "ALIAS":
            alias = data[1]
            connection.sendall("RECV ALIAS\n")
        elif data[0] == "ID":
            android_id = data[1]
            connection.sendall("RECV ID\n")
        elif data[0] == "IP":
            ip = data[1]
            connection.sendall("RECV IP\n")
        elif data[0] == "PORT":
            port = data[1]
            connection.sendall("RECV PORT\n")
            break
        else:
            print >> sys.stderr, 'no more data'

    if exist_check(connection, android_id, alias):
        aliasDict[alias] = [android_id, ip, port]
        update_alias_database(android_id, alias, ip, port)


def exist_check(connection, android_id, alias):
    if alias and android_id:
        if alias not in aliasDict:
            connection.sendall("ALIAS REGISTERED")
            return True

        elif aliasDict[alias][0] == android_id:
            connection.sendall("ALREADY REGISTERED")
            return False

        else:
            connection.sendall("ALIAS EXISTS")
            return False


def update_alias_database(android_id, alias, ip, port):
    f = open(ALIAS_DATABASE, "a")
    f.write(str(alias + ',' + android_id + "," + ip + "," + port + '\n'))
    f.close()
