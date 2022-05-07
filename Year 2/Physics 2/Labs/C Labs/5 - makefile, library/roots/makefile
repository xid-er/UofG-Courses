CC=gcc
CCFLAGS=-Wall
LIBS=-lm

# Build executable as 'mycode'
mycode: main.o complexmath.o validate.o
	$(CC) $(CCFLAGS) -o $@ $^ $(LIBS)

# Build object files from source files
%.o: %.c
	$(CC) $(CCFLAGS) -c $^

.PHONY: clean

# Remove object files and executable
clean:
	rm -f *.o mycode
