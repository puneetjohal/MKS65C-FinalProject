#include "pipe_networking.h"

/*=========================
  server_handshake
  args: int * to_client
  Performs the client side pipe 3 way handshake.
  Sets *to_client to the file descriptor to the downstream pipe.
  returns the file descriptor for the upstream pipe.
  =========================*/
void server_handshake() {
  int clients = 0;
  int fifo;
  char name[256];
  char name1[10];
  char name2[10];
  char in1[10];
  char in2[10];
  char message[256];
  char newname[10];
  while(clients < 2){
    unlink(ACK);
    if(mkfifo(ACK, 0666) == -1){
      printf("ERROR1: %s\n", strerror(errno));
      exit(1);
    }
    printf("Server created\n");
    fifo = open(ACK, O_RDONLY);
    if(read(fifo, name, 256) == -1){
      printf("ERROR2: %s\n", strerror(errno));
      exit(1);
    }
    close(fifo);
    fifo = open(name, O_WRONLY);
    if(fifo == -1){
      printf("ERROR3: %s\n", strerror(errno));
      exit(1);
    }
    if(write(fifo, "I gotchu", strlen("I gotchu")) == -1){
      printf("ERROR4: %s\n", strerror(errno));
      exit(1);
    }
    close(fifo);
    fifo = open(ACK, O_RDONLY);
    if(read(fifo, message, 256) == -1){
      printf("ERROR5: %s\n", strerror(errno));
      exit(1);
    }
    if(clients == 0){
      strcpy(name1,name);
    }else{
      strcpy(name2,name);
    }
    close(fifo);
    printf("Handshake Complete\n");

    sprintf(newname, "%d", getpid() + clients);
    if(clients == 0){
      sprintf(in1, "%d", getpid() + clients);
    }else{
      sprintf(in2, "%d", getpid() + clients);
    }
    mkfifo(newname, 0666);
    fifo = open(name, O_WRONLY);
    if(fifo == -1){
      printf("ERROR: %s\n", strerror(errno));
      exit(1);
    }
    if(write(fifo, newname, strlen(newname)) == -1){
      printf("ERROR: %s\n", strerror(errno));
      exit(1);
    }
    close(fifo);
    clients++;
  }
  int f = fork();
  if(!f){
    int fifo1 = open(name1, O_WRONLY);
    int fifo2 = open(name2, O_WRONLY);
    if(write(fifo1, "ready", strlen("ready")) == -1){
      printf("ERROR: %s\n", strerror(errno));
      exit(1);
    }
    if(write(fifo2, "ready", strlen("ready")) == -1){
      printf("ERROR: %s\n", strerror(errno));
      exit(1);
    }
    close(fifo1);
    close(fifo2);
    f = fork();
    if(f){
      while(1){
        fifo = open(in1, O_RDONLY);
        char sig[10];
        read(fifo, sig, 10);
        close(fifo);
        fifo = open(name2, O_WRONLY);
        write(fifo, sig, strlen(sig));
        close(fifo);
      }
    }else{
      while(1){
        fifo = open(in2, O_RDONLY);
        char sig[10];
        read(fifo, sig, 10);
        close(fifo);
        fifo = open(name1, O_WRONLY);
        write(fifo, sig, strlen(sig));
        close(fifo);
      }
    }
  }else{
    clients = 0;
  }
}



/*=========================
  client_handshake
  args: int * to_server
  Performs the client side pipe 3 way handshake.
  Sets *to_server to the file descriptor for the upstream pipe.
  returns the file descriptor for the downstream pipe.
  =========================*/
void client_handshake() {
  char name[10];
  sprintf(name, "%d", getpid());
  if(mkfifo(name, 0666) == -1){
    printf("ERROR: %s\n", strerror(errno));
    exit(1);
  }
  int fifo = open(ACK, O_WRONLY);
  if(fifo == -1){
    printf("ERROR: %s\n", strerror(errno));
    exit(1);
  }
  if(write(fifo, name, strlen(name)) == -1){
    printf("ERROR: %s\n", strerror(errno));
    exit(1);
  }
  close(fifo);
  fifo = open(name, O_RDONLY);
  if(fifo == -1){
    printf("ERROR: %s\n", strerror(errno));
    exit(1);
  }
  char message[256];
  if(read(fifo, message, 256) == -1){
    printf("ERROR: %s\n", strerror(errno));
    exit(1);
  }
  close(fifo);
  fifo = open(ACK, O_WRONLY);
  if(write(fifo, "Ayo", strlen("Ayo")) == -1){
    printf("ERROR: %s\n", strerror(errno));
    exit(1);
  }
  printf("Handshake Complete\n");
  fifo = open(name, O_RDONLY);
  if(fifo == -1){
    printf("ERROR: %s\n", strerror(errno));
    exit(1);
  }
  char newname[10];
  if(read(fifo, newname, 10) == -1){
    printf("ERROR: %s\n", strerror(errno));
    exit(1);
  }
  close(fifo);
  char javapipeIN[10];
  char javapipeOUT[10];
  sprintf(javapipeIN, "%d", getpid() + 10);
  sprintf(javapipeOUT, "%d", getpid() + 20);
  mkfifo(javapipeIN, 0666);
  mkfifo(javapipeOUT, 0666);
  int f = fork();
  if(!f){
    char* command[4];
    command[0] = "java";
    command[1] = "Tetris";
    command[2] = javapipeIN;
    command[3] = NULL;
    printf("waiting for other player to join\n");
    fifo = open(name, O_RDONLY);
    char ready[10];
    if(read(fifo, ready, 256) == -1){
      printf("ERROR: %s\n", strerror(errno));
      exit(1);
    }
    printf("good\n");
    execvp(command[0], command);
  }else{
    int status;
    wait(&status);
  }
  f = fork();
  if(f){
    while(1){
      fifo = open(javapipeIN, O_RDONLY);
      char sig[10];
      read(fifo, sig, 10);
      close(fifo);
      fifo = open(newname, O_WRONLY);
      write(fifo, sig, strlen(sig));
      close(fifo);
    }
  }else{
    while(1){
      fifo = open(name, O_RDONLY);
      char sig[10];
      read(fifo, sig, 10);
      close(fifo);
      fifo = open(javapipeOUT, O_WRONLY);
      write(fifo, sig, strlen(sig));
      close(fifo);
    }
  }
}
