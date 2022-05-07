# Windows implementation

import os
import sys
import socket

def send_file(socket, filename):
    try:
        with open(filename, 'rb') as f:
            data = f.read()
            
        # Add substitute character to signal end of file
        socket.sendall(data + b'^Z')
        return True, None
    except FileNotFoundError:
        return False, "File not found"

def recv_file(socket, filename):
    all_data = bytearray(0)
    while True:
        data = socket.recv(4096)
        if len(data) == 0:
            break
        all_data += data
    
    # Check for end-of-file character (substitute)
    if all_data.endswith(b'^Z'):
        try:
            with open(filename, 'xb') as f:
                f.write(all_data[:-2])
            return True, None
        except FileExistsError:
            return False, "File exists"
    else:
        return False, "File not sent completely"

def send_listing(socket):
    data = "\n".join(os.listdir())
    socket.sendall(data.encode())
    
    confirm = socket.recv(1024).decode()
    if confirm == "Got it!":
        return True, None
    else:
        return False, "Client did not receive everything"

def recv_listing(socket):
    all_data = socket.recv(4096).decode()
    print(all_data)
    
    socket.sendall("Got it!".encode())
    
def status_msg(check, addr, request, err = ""):
    if check:
        print(addr + " " + request + ": success")
    else:
        print(addr + " " + request + ": failure - " + err)