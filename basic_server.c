#include "pipe_networking.h"

void subserver(int from_client_1, int from_client_2);

int main() {

  int listen_socket = server_setup();
  int f;
  int * clients = (int *) calloc (10, sizeof(int));
  int * modes = (int *) calloc (10, sizeof(int));
  int n = 0;
  int pvp = 0;
  int tor = 0;

  while (1) {
    int client_socket = server_connect(listen_socket);
    char game_mode[10];
    read(client_socket, game_mode, 10);
    clients[n] = client_socket;
    modes[n] = game_mode;
    n++;
    if (game_mode==2) {tor++;}
    else {pvp++;}

    //pvp
    if (pvp==2){
      int opIndex;
      for (int i = 0; i < n-1; i++){
        if (strcmp(modes[i],modes[n-1])==0){
          opIndex = i;
        }
      }

      int client_socket_1 = client[opIndex];
      int client_socket_2 = client[n];
      //rm both from both arrays
      for (int i = opIndex; i < n-1; i++){
         clients[i] = clients[i+1];
         modes[i] = modes[i+1];
      }
      clients[n-2] = NULL;
      modes[n-2] = NULL;
      n=n-2;
      pvp=0;
      // do the fork
      f = fork();
      if (f == 0)
        subserver(client_socket_1, client_socket_2);
      else
        close(client_socket_1);
        close(client_socket_1);
    }

    //tournament
    if (tor==6){
      int * opIndicies = (int *) calloc (5, sizeof(int));
      int op = 0;
      while (op < 5){
        for (int i = 0; i < n-1; i++){
          if (strcmp(modes[i],modes[n-1])==0){
            opIndicies[op] = i;
          }
          break;
        }
        n--;
        op++;
      }

      int tor_socket_1 = clients[opIndicies[0]];
      int tor_socket_2 = client[opIndicies[1]];
      int tor_socket_3 = client[opIndicies[2]];
      int tor_socket_4 = client[opIndicies[3]];
      int tor_socket_5 = client[opIndicies[4]];
      int tor_socket_6 = client[n];

      //rm
      for (int x = 0; x < 5; x++){
        for (int i = opIndicies[x]; i < n-1; i++){
           clients[i] = clients[i+1];
           modes[i] = modes[i+1];
        }
        n--;
      }
      clients[n-1] = NULL;
      modes[n-1] = NULL;
      n--;
      tor=tor-6;

      // do the fork
      f = fork();
      if (f == 0)
        subserver(tor_socket_1, tor_socket_2);
      else {
        g = fork();
        if (g == 0){
          subserver(tor_socket_3, tor_socket_4);
        }
        else{
          h = fork();
          if (h == 0){
            subserver(tor_socket_5, tor_socket_6);
          }
        }
        //+ magically re-pair remaining opponents????
        close(client_socket_1);
        close(client_socket_1);
      }
    }
  }
}

void subserver(int client_socket_1, int client_socket_2) {
  char buffer[BUFFER_SIZE];


  write(client_socket_1, "ready", sizeof("ready"));
  write(client_socket_2, "ready", sizeof("ready"));

  int f = fork();
  if(f){
    while(1){
      read(client_socket_1, buffer, sizeof(buffer));
      if(strcmp(buffer, "0") == 0 || strcmp(buffer, "1") == 0 || strcmp(buffer, "2") == 0 || strcmp(buffer, "3") == 0 || strcmp(buffer, "4") == 0){
        write(client_socket_2, buffer, sizeof(buffer));
      }
    }
  }else{
    while(1){
      read(client_socket_2, buffer, sizeof(buffer));
      if(strcmp(buffer, "0") == 0 || strcmp(buffer, "1") == 0 || strcmp(buffer, "2") == 0 || strcmp(buffer, "3") == 0 || strcmp(buffer, "4") == 0){
        write(client_socket_1, buffer, sizeof(buffer));
      }
    }
  }
}
