import json


def update_peer(connection, aliasDict):
    connection.sendall(str(len(aliasDict))+'\n')
    connection.sendall(json.dumps(aliasDict))

