import socket
import sys
import AliasRegistrar

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the port
server_address = ('0.0.0.0', 6666)
print >>sys.stderr, 'starting up on %s port %s' % server_address
sock.bind(server_address)

# Listen for incoming connections
sock.listen(1)
aliasDict = AliasRegistrar.load_aliases()


while True:
    # Wait for a connection
    print >>sys.stderr, 'waiting for a connection'
    connection, client_address = sock.accept()

    try:
        print >> sys.stderr, 'connection from', client_address
        # Receive the data in small chunks and retransmit it
        while True:
            data = connection.recv(128)
            #print >> sys.stderr, 'received "%s"' % data
            if data:
                if data == "REGISTER":
                    AliasRegistrar.register(connection)
                    break
                elif data == "UPDATE":
                    AliasRegistrar.update_ip(connection)
                    break


            else:
                print >> sys.stderr, 'no more data from', client_address
                break

    finally:
        # Clean up the connection
        connection.close()
