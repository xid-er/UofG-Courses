import socket
import sys

print("Connect to " + sys.argv[1] + " " + sys.argv[2] + "?\ny/n")
answered = False;

try:
    while not answered:
        value = input();
        if value == "y":
            answered = True
            cli_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            cli_sock.connect((sys.argv[1], int(sys.argv[2])))
            
            in_process = True
            while in_process:
                print("karlis> ", end="")
                msg = input()
                cli_sock.sendall(msg.encode('utf-8'))
                
                if msg == "EXIT":
                    in_process = False
                else:
                    request = cli_sock.recv(1024)
                    print("Admin> " + request.decode('utf-8'))
                
            answered = True
        elif value == "n":
            print("Goodbye!")
            answered = True
        else:
            print("Sorry, did not understand that.")
except ConnectionAbortedError:
    print("The client lost connection.")
except:
    print("Unexpected error:", sys.exc_info()[0])
    cli_sock.close()