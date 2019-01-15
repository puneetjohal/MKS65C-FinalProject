all: client server Tetris.class

client: client.o pipe_networking.o
	gcc -o client client.o pipe_networking.o

server: basic_server.o pipe_networking.o
	gcc -o server basic_server.o pipe_networking.o

Tetris.class: Tetris.java
	javac Tetris.java

client.o: client.c pipe_networking.h
	gcc -c client.c

basic_server.o: basic_server.c pipe_networking.h
	gcc -c basic_server.c

pipe_networking.o: pipe_networking.c pipe_networking.h
	gcc -c pipe_networking.c

clean:
	rm *.o
	rm *~

run_server: server
	./server

run_client: client
	./client
