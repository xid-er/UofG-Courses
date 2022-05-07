# Windows implementation

import socket
import sys
import os

# sys.path idea from friend in course
# Change path for user's functions.py location
sys.path.insert(1, 'C:\\Users\\karli\\OneDrive\\Documents\\Uni Labs\\NOSE\\Assessed Exercise 1')
from functions import send_file, recv_file, recv_listing, status_msg

err = ""

valid = False
try:
    req_type = sys.argv[3]
except:
    print("No request type given.")
else:
    if req_type == 'put' or req_type == 'get':
        try:
            filename = sys.argv[4]
            valid = True
            request = req_type + " " + filename
        except IndexError:
            print("No filename given.")
    elif req_type == 'list':
        valid = True
        request = req_type
    else:
        print("No valid request type given.")
    

if valid:
    # Create the socket with which to connect to the server
    cli_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # The server's address is a tuple: IP address/hostname, and port number
    port = int(sys.argv[2])
    srv_addr = (sys.argv[1], port) # sys.argv[x] is the x'th argument on the command line

    # Convert to string
    srv_addr_str = "(" + str(socket.getaddrinfo('localhost', port)[1][4][0]) + ":" + str(port) + ")"

    # Catch port number out of range
    try:        
        cli_sock.connect(srv_addr)
    except ConnectionRefusedError:
        err = "Connection refused/Server down/Invalid port number"
        print(srv_addr_str + " " + request + ": failure - " + err)
        # Exit with a non-zero value, to indicate an error condition
        exit(1)
    except Exception as e:
        # Print the exception message
        print(srv_addr_str + " " + request + ": failure - " + str(e))
        exit(1)
    
    else:
        
        # Catch socket errors
        try:
            check = False
            
            # PUT: check directory, send file
            if req_type == 'put':
                # Send error
                if filename not in os.listdir():
                    err = "/".join(["ERR", req_type, filename, "File not found (client-side)"])
                    cli_sock.sendall(err.encode())
                    err = err.split(sep="/")[3]
                else:
                    # Send request
                    send_request = req_type + "/" + filename
                    cli_sock.sendall(send_request.encode())
                    err = cli_sock.recv(1024).decode()
                    
                    # Send file
                    if err == 'OK':
                        check, err = send_file(cli_sock, filename)
            
            
            # GET: check directory, receive file
            elif req_type == 'get':
                if filename in os.listdir():
                    # Send error
                    err = "/".join(["ERR", req_type, filename, "File already exists (client-side)"])
                    cli_sock.sendall(err.encode())
                    err = err.split(sep="/")[3]
                else:
                    # Send request
                    send_request = req_type + "/" + filename
                    cli_sock.sendall(send_request.encode())
                    err = cli_sock.recv(1024).decode()
                    
                    # Receive file
                    if err == 'OK':
                        cli_sock.sendall("Send!".encode())
                        check = recv_file(cli_sock, filename)
            
            
            # LIST: receive listing
            elif req_type == 'list':
                cli_sock.sendall(str.encode('list'))
                recv_listing(cli_sock)
                check = True
            
            
            # Status message
            status_msg(check, srv_addr_str, request, err)
        
        
        except ConnectionAbortedError:
            status_msg(False, srv_addr_str, request, err = "Connection lost")
        
        except Exception as e:
            status_msg(False, srv_addr_str, request, err = str(e))
        
        finally:
            cli_sock.close()

        # Exit with a zero value, to indicate success
        exit(0)