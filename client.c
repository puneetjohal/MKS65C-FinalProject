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
    printf("\nPick a game mode:\n1 : Single Player\n2 : PvP\n3 : Tournament\n4 : Exit\n");
    char gameMode[10];
    fgets(gameMode, 10, stdin);
    gameMode[strlen(gameMode)-1] = 0;

    //single player
    if (strcmp(gameMode,"1")==0) {
      char* command[5];
      command[0] = "java";
      command[1] = "-cp";
      command[2] = "single/";
      command[3] = "Tetris";
      command[4] = NULL;
      printf("starting game...\n");
      execvp(command[0], command);
    }

    //PvP
    if (strcmp(gameMode,"2")==0) {
      char javapipeIN[10];
      char javapipeOUT[10];
      sprintf(javapipeIN, "%d", getpid() + 10);
      sprintf(javapipeOUT, "%d", getpid() + 20);
      mkfifo(javapipeIN, 0777);
      mkfifo(javapipeOUT, 0777);
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
        while(read(server_socket, buffer, sizeof(buffer)) == -1);
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
          write(server_socket, sig, sizeof(sig));
          if(strcmp(sig, "0") == 0){
            printf("You Lost!\n");
            exit(0);
          }
        }
      }else{
        fcntl(server_socket, F_SETFL, O_NONBLOCK);
        while(1){
          char sig[10];
          read(server_socket, sig, 10);
          fifo = open(javapipeOUT, O_WRONLY);
          if(strcmp(sig, "0") == 0){
            printf("You Won!\n");
            write(fifo, sig, strlen(sig));
            close(fifo);
            exit(0);
          }
          if(strcmp(sig, "1") == 0 || strcmp(sig, "2") == 0 || strcmp(sig, "3") == 0 || strcmp(sig, "4") == 0){
            printf("Your opponent attacked you!\n");
            write(fifo, sig, strlen(sig));
            strcpy(sig, "");
          }
          close(fifo);
        }
      }
    }

    if (strcmp(gameMode,"3")==0) {
      char javapipeIN[10];
      char javapipeOUT[10];
      sprintf(javapipeIN, "%d", getpid() + 10);
      sprintf(javapipeOUT, "%d", getpid() + 20);
      mkfifo(javapipeIN, 0777);
      mkfifo(javapipeOUT, 0777);
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
        while(read(server_socket, buffer, sizeof(buffer)) == -1);
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
          write(server_socket, sig, sizeof(sig));
          if(strcmp(sig, "0") == 0){
            printf("You Lost!\n");
            exit(0);
          }
        }
      }else{
        fcntl(server_socket, F_SETFL, O_NONBLOCK);
        while(1){
          char sig[10];
          read(server_socket, sig, 10);
          fifo = open(javapipeOUT, O_WRONLY);
          if(strcmp(sig, "0") == 0){
            printf("You Won!\nYou made it to round 2!\n");
            write(fifo, sig, strlen(sig));
            close(fifo);
            break;
          }
          if(strcmp(sig, "1") == 0 || strcmp(sig, "2") == 0 || strcmp(sig, "3") == 0 || strcmp(sig, "4") == 0){
            printf("Your opponent attacked you!\n");
            write(fifo, sig, strlen(sig));
            strcpy(sig, "");
          }
          close(fifo);
        }
        if (argc == 2)
          server_socket = client_setup( argv[1] );
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
          while(read(server_socket, buffer, sizeof(buffer)) == -1);
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
            write(server_socket, sig, sizeof(sig));
            if(strcmp(sig, "0") == 0){
              printf("You Lost!\n");
              exit(0);
            }
          }
        }else{
          fcntl(server_socket, F_SETFL, O_NONBLOCK);
          while(1){
            char sig[10];
            read(server_socket, sig, 10);
            fifo = open(javapipeOUT, O_WRONLY);
            if(strcmp(sig, "0") == 0){
              printf("You Won The Tournament!\n");
              write(fifo, sig, strlen(sig));
              close(fifo);
              exit(0);
            }
            if(strcmp(sig, "1") == 0 || strcmp(sig, "2") == 0 || strcmp(sig, "3") == 0 || strcmp(sig, "4") == 0){
              printf("Your opponent attacked you!\n");
              write(fifo, sig, strlen(sig));
              strcpy(sig, "");
            }
            close(fifo);
          }
        }
      }
    }

    //exiting
    if (strcmp(gameMode,"4")==0) {
      playing = 0;
      printf("Goodbye, %s\n", user);
      exit(0);
    }
  }
}
