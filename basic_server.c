#include "pipe_networking.h"

static void sighandler(int signo){
  if(signo == 2){
    unlink(ACK);
    char newname[10];
    sprintf(newname, "%d", getpid());
    unlink(newname);
    exit(0);
  }
}

int main() {

  signal(SIGINT, sighandler);

  server_handshake();
}
