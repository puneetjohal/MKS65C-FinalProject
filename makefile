all: client server tournament_server Tetris.class

client: client.o pipe_networking.o
	gcc -o client client.o pipe_networking.o

server: basic_server.o pipe_networking.o
	gcc -o server basic_server.o pipe_networking.o

tournament_server: tournament_server.o pipe_networking.o
		gcc -o tournament_server tournament_server.o pipe_networking.o

Tetris.class: Tetris.java
	javac Tetris.java

client.o: client.c pipe_networking.h
	gcc -c client.c

basic_server.o: basic_server.c pipe_networking.h
	gcc -c basic_server.c

tournament_server.o: tournament_server.c pipe_networking.h
		gcc -c tournament_server.c

pipe_networking.o: pipe_networking.c pipe_networking.h
	gcc -c pipe_networking.c

clean:
	rm *.o
	rm *~

run_server: server
	./server

run_client: client
	./client
