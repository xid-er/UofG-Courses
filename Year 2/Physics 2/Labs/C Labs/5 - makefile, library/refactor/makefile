CC=gcc
EXE=mkImage

# Optimisation and Debugging flags
CFLAGS=-Wall
OPTCFLAGS=-O2
DEBUGCFLAGS=-g
C99CFLAGS=-std=c99

all: $(EXE)

debug: CFLAGS+=$(DEBUGCFLAGS)
debug: $(EXE)

c99: CFLAGS+=$(C99CFLAGS)
c99: $(EXE)

#Build main EXE as 'sim'
$(EXE): refactor.o printpgm.o
	$(CC) $^ -o $@

# Build object files with source files
%.o: %.c
	$(CC) $(CFLAGS) -c $<
	

.PHONY: clean
# Clean all build files
clean:
	rm *.o
	rm $(EXE)
