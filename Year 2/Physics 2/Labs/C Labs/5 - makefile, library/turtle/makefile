CC=gcc
EXEPOL=polygon
EXESQR=square

all: $(EXEPOL) $(EXESQR)

#Build main EXE
$(EXEPOL): turtle.o
	$(CC) $< -o $@ -L/usr/share/p2t/lab05/Exe2 -lturtle -lm

$(EXESQR): turtle.o
	$(CC) $^ -o $@ -L/usr/share/p2t/lab05/Exe2 -lturtle -lm

# Build object files with source files
%.o: %.c
	$(CC) -c $<
	

.PHONY: clean
# Clean all build files
clean:
	rm *.o
	rm $(EXEPOL) $(EXESQR)
