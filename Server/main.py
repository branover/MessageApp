#!/usr/bin/env python
import socket
import sys

import select

import AliasRegistrar
import PeerManager

# Initialize multiple socket server
servers = []

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET6, socket.SOCK_STREAM)
#sock6 = socket.socket(socket.AF_INET6, socket.SOCK_STREAM)

# Bind the socket to the port
server_address = ('', 6666)
server_address6 = ('::', 6666)
print >>sys.stderr, 'starting up on %s port %s' % server_address
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
#sock6.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
sock.bind(server_address)
#sock6.bind(server_address6)

# Listen for incoming connections
sock.listen(1)
# try:
#     sock6.listen(1)
# except:
#     pass

aliasDict = AliasRegistrar.load_aliases()

servers.append(sock)
#servers.append(sock6)


while True:
    # Wait for a connection
    print >>sys.stderr, 'waiting for a connection'
    #readable,_,_ = select.select(servers, [], [])
    #ready_server = readable[0]

    connection, client_address = sock.accept()

    try:
        print >> sys.stderr, 'connection from', client_address
        # Receive the data in small chunks and retransmit it
        while True:
            data = connection.recv(128)
            #print >> sys.stderr, 'received "%s"' % data
            if data:
                if data == "REGISTER":
                    aliasDict = AliasRegistrar.register(connection)
                    break
                elif data == "UPDATE":
                    aliasDict = AliasRegistrar.update_ip(connection)
                    break
                elif data == "UPDATEPEER":
                    PeerManager.update_peer(connection, aliasDict)
                    break
                elif data == "PING":
                    AliasRegistrar.update_onlinetime(connection)

            else:
                print >> sys.stderr, 'no more data from', client_address
                break

    finally:
        # Clean up the connection
        connection.close()
