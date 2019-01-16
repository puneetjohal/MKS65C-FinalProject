#include "pipe_networking.h"

void subserver(int from_client_1, int from_client_2);

int main() {

  int listen_socket = server_setup();
  int clients = 0;
  int f;

  while (1) {
    int client_socket_1 = server_connect(listen_socket);
    int client_socket_2 = server_connect(listen_socket);
    f = fork();
    if (f == 0)
      subserver(client_socket_1, client_socket_2);
    else
      close(client_socket_1);
      close(client_socket_1);
  }
}

void subserver(int client_socket_1, int client_socket_2) {
  char buffer[BUFFER_SIZE];


  write(client_socket_1, "ready", sizeof("ready"));
  write(client_socket_2, "ready", sizeof("ready"));

  int f = fork();
  if(f){
    read(client_socket_1, buffer, sizeof(buffer));
    printf("subserver received: %s from c1\n", buffer);
    write(client_socket_2, buffer, sizeof(buffer));
  }else{
    read(client_socket_2, buffer, sizeof(buffer));
    printf("subserver received: %s from c2\n", buffer);
    write(client_socket_1, buffer, sizeof(buffer));
  }
}
