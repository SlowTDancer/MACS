#include "kernel/types.h"
#include "kernel/stat.h"
#include "user/user.h"

int main(int argc, char *argv[]){
    int fds[2], pid, n, p;
    
    pipe(fds);

    pid = fork();

    if(!pid){
        close(fds[0]);
        for(int i = 2; i <= 35; i++) write(fds[1], &i, sizeof(int));
        exit(0);
    }else{
        wait(0);
        close(fds[1]);

        while(read(fds[0], &p, sizeof(int)) > 0){
            printf("prime %d\n", p);

            int fdsn[2];
            pipe(fdsn);

            pid = fork();
            if(!pid){
                while(read(fds[0], &n, sizeof(int)) > 0) if(n % p) write(fdsn[1], &n, sizeof(int));
                exit(0);
            }else{
                wait(0);
                close(fds[0]);
                fds[0] = fdsn[0];
                close(fdsn[1]);
            }
        }
    }

    exit(0);
}