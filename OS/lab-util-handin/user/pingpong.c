#include "kernel/types.h"
#include "kernel/stat.h"
#include "user/user.h"

int main(int argc, char *argv[]){
    int fds1[2], fds2[2], pid;
    char buf[512];

    pipe(fds1);
    pipe(fds2);

    pid = fork();

    if(pid){
        write(fds1[0], "Don't forget 3.Oct\n", 19);
    }else{
        read(fds1[1], buf, sizeof(buf));
        printf("%d: received ping\n", getpid());
        write(fds2[0], "I won't\n", 8);
        exit(0);
    }

    
    if(pid){
        wait(0);
        read(fds2[1], buf, sizeof(buf));
        printf("%d: received pong\n", getpid());
    }
    

    exit(0);
}