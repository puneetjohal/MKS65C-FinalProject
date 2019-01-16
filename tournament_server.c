#include "pipe_networking.h"

void subserver(int from_client_1, int from_client_2, int from_client_3, int from_client_4);

int main() {

  int listen_socket = server_setup();
  int f;

  while (1) {
    int client_socket_1 = server_connect(listen_socket);
    int client_socket_2 = server_connect(listen_socket);
    int client_socket_3 = server_connect(listen_socket);
    int client_socket_4 = server_connect(listen_socket);
    f = fork();
    if (f == 0)
      subserver(client_socket_1, client_socket_2, client_socket_3, client_socket_4);
    else
      close(client_socket_1);
      close(client_socket_2);
      close(client_socket_3);
      close(client_socket_4);
  }
}

void subserver(int client_socket_1, int client_socket_2, int client_socket_3, int client_socket_4) {
  char buffer[BUFFER_SIZE];

  write(client_socket_1, "ready", sizeof("ready"));
  write(client_socket_2, "ready", sizeof("ready"));
  write(client_socket_3, "ready", sizeof("ready"));
  write(client_socket_4, "ready", sizeof("ready"));

  int shmid1 = shmget(525600, 2, 0666 | IPC_CREAT);
  int shmid2 = shmget(525601, 2, 0666 | IPC_CREAT);

  int f = fork();
  if(f){
    f = fork();
    if(f){
      f = fork();
      if(f){
        while(1){
          read(client_socket_1, buffer, sizeof(buffer));
          if(strcmp(buffer, "0") == 0 || strcmp(buffer, "1") == 0 || strcmp(buffer, "2") == 0 || strcmp(buffer, "3") == 0 || strcmp(buffer, "4") == 0){
            write(client_socket_2, buffer, sizeof(buffer));
          }
          if(strcmp(buffer, "0") == 0){
            char* data1 = shmat(shmid1, 0, 0);
            strcpy(data1, "2");
            shmdt(data1);
            exit(0);
          }
        }
      }else{
        while(1){
          read(client_socket_3, buffer, sizeof(buffer));
          if(strcmp(buffer, "0") == 0 || strcmp(buffer, "1") == 0 || strcmp(buffer, "2") == 0 || strcmp(buffer, "3") == 0 || strcmp(buffer, "4") == 0){
            write(client_socket_4, buffer, sizeof(buffer));
          }
          if(strcmp(buffer, "0") == 0){
            char* data2 = shmat(shmid2, 0, 0);
            strcpy(data2, "4");
            shmdt(data2);
            exit(0);
          }
        }
      }
    }else{
      f = fork();
      if(f){
        while(1){
          read(client_socket_2, buffer, sizeof(buffer));
          if(strcmp(buffer, "0") == 0 || strcmp(buffer, "1") == 0 || strcmp(buffer, "2") == 0 || strcmp(buffer, "3") == 0 || strcmp(buffer, "4") == 0){
            write(client_socket_1, buffer, sizeof(buffer));
          }
          if(strcmp(buffer, "0") == 0){
            char* data1 = shmat(shmid1, 0, 0);
            strcpy(data1, "1");
            shmdt(data1);
            exit(0);
          }
        }
      }else{
        while(1){
          read(client_socket_4, buffer, sizeof(buffer));
          if(strcmp(buffer, "0") == 0 || strcmp(buffer, "1") == 0 || strcmp(buffer, "2") == 0 || strcmp(buffer, "3") == 0 || strcmp(buffer, "4") == 0){
            write(client_socket_3, buffer, sizeof(buffer));
          }
          if(strcmp(buffer, "0") == 0){
            char* data2 = shmat(shmid2, 0, 0);
            strcpy(data2, "3");
            shmdt(data2);
            exit(0);
          }
        }
      }
    }
  }else{
    int status;
    wait(&status);
    char* data1 = shmat(shmid1, 0, 0);
    char* data2 = shmat(shmid2, 0, 0);
    int winner_1;
    int winner_2;
    if(strcmp(data1, "1") == 0){
      winner_1 = client_socket_1;
    }
    if(strcmp(data1, "2") == 0){
      winner_1 = client_socket_2;
    }
    if(strcmp(data2, "3") == 0){
      winner_2 = client_socket_3;
    }
    if(strcmp(data2, "4") == 0){
      winner_2 = client_socket_4;
    }

    shmdt(data1);
    shmdt(data2);

    write(winner_1, "ready", sizeof("ready"));
    write(winner_2, "ready", sizeof("ready"));

    int f = fork();
    if(f){
      while(1){
        read(winner_1, buffer, sizeof(buffer));
        if(strcmp(buffer, "0") == 0 || strcmp(buffer, "1") == 0 || strcmp(buffer, "2") == 0 || strcmp(buffer, "3") == 0 || strcmp(buffer, "4") == 0){
          write(winner_2, buffer, sizeof(buffer));
        }
      }
    }else{
      while(1){
        read(winner_2, buffer, sizeof(buffer));
        if(strcmp(buffer, "0") == 0 || strcmp(buffer, "1") == 0 || strcmp(buffer, "2") == 0 || strcmp(buffer, "3") == 0 || strcmp(buffer, "4") == 0){
          write(winner_1, buffer, sizeof(buffer));
        }
      }
    }
  }
}
