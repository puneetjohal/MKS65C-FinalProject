#include "pipe_networking.h"

static void sighandler(int signo){
  if(signo == 2){
    char name[10];
    sprintf(name, "%d", getpid());
    unlink(name);
    exit(1);
  }
}

int main() {

  signal(SIGINT, sighandler);

  client_handshake();
}
