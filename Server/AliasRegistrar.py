import sys
import json

ALIAS_DATABASE = "./Aliases/aliases.json"
aliasDict = {}


def load_aliases():
    global aliasDict
    f = open(ALIAS_DATABASE, 'r')
    try:
        aliasDict = json.load(f)
        print aliasDict
    # if the file is empty the ValueError will be thrown
    except ValueError:
        aliasDict = {}
    f.close()
    return aliasDict


def register(connection):
    global aliasDict
    alias = AliasObj()
    while True:
        data = connection.recv(128)
        if not data:
            break
        data = data.split(":")
        if data[0] == "ALIAS":
            alias.alias = data[1]
            connection.sendall("RECV ALIAS\n")
        elif data[0] == "ID":
            alias.android_id = data[1]
            connection.sendall("RECV ID\n")
        elif data[0] == "IP":
            alias.ip = data[1]
            connection.sendall("RECV IP\n")
        elif data[0] == "PORT":
            alias.port = data[1]
            connection.sendall("RECV PORT\n")
            break
        else:
            print >> sys.stderr, 'no more data'

    if exist_check(connection, alias):
        aliasDict[alias.alias] = {"android_id": alias.android_id, "ip": alias.ip, "port": alias.port}
        update_alias_database()


def exist_check(connection, alias):
    global aliasDict
    if alias.alias and alias.android_id:
        if alias.alias not in aliasDict:
            connection.sendall("ALIAS REGISTERED")
            return True

        elif aliasDict[alias.alias]["android_id"] == alias.android_id:
            connection.sendall("ALREADY REGISTERED")
            return False

        else:
            connection.sendall("ALIAS EXISTS")
            return False


def update_alias_database():
    global aliasDict
    f = open(ALIAS_DATABASE, "w")
    json.dump(aliasDict, f)
    f.close()


def update_ip(connection):
    alias = AliasObj()
    while True:
        data = connection.recv(128)
        if not data:
            break
        data = data.split(":")
        if data[0] == "ALIAS":
            alias.alias = data[1]
            connection.sendall("RECV ALIAS\n")
        elif data[0] == "ID":
            alias.android_id = data[1]
            connection.sendall("RECV ID\n")
        elif data[0] == "IP":
            alias.ip = data[1]
            connection.sendall("RECV IP\n")
        elif data[0] == "PORT":
            alias.port = data[1]
            connection.sendall("RECV PORT\n")
            break
        else:
            print >> sys.stderr, 'no more data'
    if alias.alias in aliasDict and (aliasDict[alias.alias]["android_id"] == alias.android_id):
        connection.sendall("UPDATE SUCCESS")
        aliasDict[alias.alias]["ip"] = alias.ip
        aliasDict[alias.alias]["port"] = alias.port
        update_alias_database()
    return None


class AliasObj:
    def __init__(self, alias="", android_id="", ip="", port=""):
        self.alias = alias
        self.android_id = android_id
        self.ip = ip
        self.port = port
