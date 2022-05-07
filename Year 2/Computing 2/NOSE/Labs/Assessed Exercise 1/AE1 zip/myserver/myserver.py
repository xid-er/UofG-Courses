# Windows implementation

import socket
import sys
import os

# sys.path idea from friend in course
# Change path for user's functions.py location
sys.path.insert(1, 'C:\\Users\\karli\\OneDrive\\Documents\\Uni Labs\\NOSE\\Assessed Exercise 1')
from functions import send_file, recv_file, send_listing, status_msg

err = ""

# Create the socket on which the server will receive new connections
srv_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

""" 
 Catch bound ports, invalid/missing
 command-line arguments, port number out of range, etc.
"""
try:
    # Register the socket with the OS kernel
    port = int(sys.argv[1])
    srv_sock.bind(("", port))
    
    srv_addr = str(socket.getaddrinfo('localhost', port)[1][4][0]) + ":" + str(port)
    print("(" + srv_addr + "): Server up and running")
    
    # Create a queue for new connection requests
    srv_sock.listen(5)
    
except ValueError:
    print("Port NUMBER must be a number")
    exit(1)
except IndexError:
    print("Must include port to use")
    exit(1)
except Exception as e:
    print(e)
    exit(1)

# Loop forever (or at least for as long as no fatal errors occur)
while True:
    
    # Catch socket errors
    try:
        # Receives request to connect, dequeues it, returns client socket and address
        cli_sock, cli_addr = srv_sock.accept()
        cli_addr_str = str(cli_addr)

        request = ""
        err = "Not connected"
        
        # Receive request from client and interpret
        data = cli_sock.recv(1024).decode()
        data_list = data.split(sep="/")
        header = data_list[0]
        
        check = False
        # Check for client-side error
        # If none, process request
        if header == "ERR":
            request = data_list[1] + " " + data_list[2]
            err = data_list[3]
        
        else:
            req_type = header
        
            # Division into request types
            
            # LIST: send listing
            if req_type == 'list':
                check, err = send_listing(cli_sock)
                request = req_type
            
            
            # PUT: get filename, check directory, receive file
            elif req_type == 'put':
                filename = data_list[1]
                request = req_type + " " + filename
                
                if filename not in os.listdir():
                    cli_sock.sendall('OK'.encode())
                    check, err = recv_file(cli_sock, filename)
                else:
                    err = "File already exists (server-side)"
                    cli_sock.sendall(err.encode())
            
            
            # GET: get filename, check directory, send file
            elif req_type == 'get':
                filename = data_list[1]
                request = req_type + " " + filename
                
                if filename in os.listdir():
                    cli_sock.sendall("OK".encode())
                    response = cli_sock.recv(1024).decode()
                    if response == 'Send!':
                        check, err = send_file(cli_sock, filename)
                else:
                    err = "File Not Found (server-side)"
                    cli_sock.sendall(err.encode())
        
        
        # Status message
        status_msg(check, cli_addr_str, request, err)
        
    except ConnectionAbortedError:
        print("Failure - connection lost")
        
    except Exception as e:
        print("Failure - " + str(e))
        
    finally:
        """
         If an error occurs or the client closes the connection, call close() on the
         connected socket to release the resources allocated to it by the OS.
        """
        cli_sock.close()
        

# Close the server socket as well to release its resources back to the OS
srv_sock.close()

# Exit with a zero value, to indicate success
exit(0)