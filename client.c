#include "pipe_networking.h"

static void sighandler(int signo){
  if(signo == 2){
    char name[10];
    sprintf(name, "%d", getpid() + 10);
    unlink(name);
    sprintf(name, "%d", getpid() + 20);
    unlink(name);
    exit(1);
  }
}

int main(int argc, char **argv) {

  signal(SIGINT, sighandler);

  int server_socket;
  char buffer[BUFFER_SIZE];

  printf("Enter username: ");
  char user[100];
  fgets(user, 100, stdin);
  user[strlen(user)-1] = 0;
  printf("\nWelcome, %s\n", user);

  //game lobby
  int playing = 1;
  while (playing) {
    printf("\nPick a game mode:\n1 : Single Player\n2 : PvP\n3 : Exit\n");
    char gameMode[10];
    fgets(gameMode, 10, stdin);
    gameMode[strlen(gameMode)-1] = 0;

    char javapipeIN[10];
    char javapipeOUT[10];
    sprintf(javapipeIN, "%d", getpid() + 10);
    sprintf(javapipeOUT, "%d", getpid() + 20);
    mkfifo(javapipeIN, 0777);
    mkfifo(javapipeOUT, 0777);

    //single player
    if (strcmp(gameMode,"1")==0) {
      int f = fork();
      if(!f){
        chdir("/single");
        char* command[4];
        command[0] = "java";
        command[1] = "Tetris";
        command[2] = javapipeIN;
        command[3] = NULL;
        printf("starting game...\n");
        execvp(command[0], command);
      }else{
      }
    }

    //PvP
    if (strcmp(gameMode,"2")==0) {
      if (argc == 2)
        server_socket = client_setup( argv[1]);
      else
        server_socket = client_setup( TEST_IP );
      int fifo;
      int f = fork();
      if(!f){
        char* command[4];
        command[0] = "java";
        command[1] = "Tetris";
        command[2] = javapipeIN;
        command[3] = NULL;
        printf("\nwaiting for other player to join\n");
        printf("%d\n", read(server_socket, buffer, sizeof(buffer)));
        printf("starting game...\n");
        execvp(command[0], command);
      }else{
      }
      f = fork();
      if(f){
        while(1){
          fifo = open(javapipeIN, O_RDONLY);
          char sig[10];
          read(fifo, sig, 10);
          close(fifo);
          printf("client received %s from tetris\n", sig);
          write(server_socket, sig, sizeof(sig));
        }
      }else{
        fcntl(server_socket, F_SETFL, O_NONBLOCK);
        while(1){
          char sig[10];
          if(read(server_socket, sig, 10) != -1){
            printf("client received %s from server\n", sig);
          }
          fifo = open(javapipeOUT, O_WRONLY);
          write(fifo, sig, strlen(sig));
          close(fifo);
        }
      }
    }

    //exiting
    if (strcmp(gameMode,"3")==0) {
      playing = 0;
      printf("Goodbye, %s\n", user);
      exit(0);
    }
  }
}
