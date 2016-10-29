import sys
import json
from time import time

ALIAS_DATABASE = "./Aliases/aliases.json"
aliasDict = {}


def load_aliases():
    global aliasDict
    f = open(ALIAS_DATABASE, 'r')
    try:
        aliasDict = json.load(f)
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
        data = data.split(":", 1)
        if data[0] == "ALIAS":
            alias.alias = data[1]
            connection.sendall("RECV ALIAS\n")
        elif data[0] == "ID":
            alias.androidId = data[1]
            connection.sendall("RECV ID\n")
        elif data[0] == "IP":
            alias.ip = data[1]
            connection.sendall("RECV IP\n")
        elif data[0] == "IPV6":
            alias.ipv6 = data[1]
            connection.sendall("RECV IPV6\n")
        elif data[0] == "PORT":
            alias.port = data[1]
            connection.sendall("RECV PORT\n")
            break
        else:
            print >> sys.stderr, 'no more data'

    if exist_check(connection, alias):
        aliasDict[alias.alias] = {"alias": alias.alias, "androidId": alias.androidId, "ip": alias.ip, "ipv6": alias.ipv6, "port": alias.port, "createtime": alias.createtime, "onlinetime": alias.onlinetime}
        update_alias_database()
    return aliasDict


def exist_check(connection, alias):
    global aliasDict
    if alias.alias and alias.androidId:
        if alias.alias not in aliasDict:
            connection.sendall("ALIAS REGISTERED")
            return True

        elif aliasDict[alias.alias]["androidId"] == alias.androidId:
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
        data = data.split(":",1)
        if data[0] == "ALIAS":
            alias.alias = data[1]
            connection.sendall("RECV ALIAS\n")
        elif data[0] == "ID":
            alias.androidId = data[1]
            connection.sendall("RECV ID\n")
        elif data[0] == "IP":
            alias.ip = data[1]
            connection.sendall("RECV IP\n")
        elif data[0] == "IPV6":
            alias.ipv6 = data[1]
            connection.sendall("RECV IPV6\n")
        elif data[0] == "PORT":
            alias.port = data[1]
            connection.sendall("RECV PORT\n")
            break
        else:
            print >> sys.stderr, 'no more data'
    if alias.alias in aliasDict and (aliasDict[alias.alias]["androidId"] == alias.androidId):
        connection.sendall("UPDATE SUCCESS")
        aliasDict[alias.alias]["ip"] = alias.ip
        aliasDict[alias.alias]["port"] = alias.port
        aliasDict[alias.alias]["ipv6"] = alias.ipv6
        aliasDict[alias.alias]["onlinetime"] = int(time())
        update_alias_database()
    else:
        connection.sendall("WRONG ANDROIDID")
    return aliasDict


def update_onlinetime(connection):
    global aliasDict
    alias = ""
    id = ""
    data = ""
    while True:
        newdata = connection.recv(1024)
        if not newdata:
            break
        data += newdata
    data = data.split("//", 1)
    data = [i.split(":") for i in data]
    for line in data:
        if line[0] == "ALIAS":
            alias = line[1]
        elif line[0] == "ID":
            id = line[1]
    print data
    for entry in aliasDict:
        if entry == alias and id == aliasDict[alias]["androidId"]:
            aliasDict[entry]["onlinetime"] = int(time())
            update_alias_database()
            print "True"
        else:
            print "False"


class AliasObj:
    def __init__(self, alias="", android_id="", ip="", port="", ipv6="", createtime=int(time()), onlinetime=int(time())):
        self.alias = alias
        self.androidId = android_id
        self.ip = ip
        self.port = port
        self.ipv6 = ipv6
        self.createtime = createtime
        self.onlinetime = onlinetime


