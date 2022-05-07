import socket
import sys

srv_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    srv_sock.bind(("", int(sys.argv[1])))
except OSError as err:
    print("OS error: {0}".format(err))
# from docs.python.org
    
srv_sock.listen(5)
try:
    while True:
        cli_sock, cli_addr = srv_sock.accept()
        
        in_process = True
        while in_process:
        
            request = cli_sock.recv(1024)
            
            if request.decode('utf-8') == "EXIT":
                print("Goodbye!")
                in_process = False
                cli_sock.close()
            else:
                print(str(cli_addr) + ": " + request.decode('utf-8'))
                
                print("Admin> ", end="")
                msg = input()
                if msg == "EXIT":
                    print("Goodbye!")
                    in_process = False
                    
                else:
                    cli_sock.sendall(msg.encode('utf-8'))
except ConnectionAbortedError:
    print("The client lost connection.")
except:
    print("Unexpected error:", sys.exc_info()[0])
finally:
    cli_sock.close()
# from docs.python.org