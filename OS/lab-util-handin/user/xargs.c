#include "kernel/types.h"
#include "kernel/stat.h"
#include "user/user.h"
#include "kernel/param.h"

#define true 1

int main(int argc, char *argv[]) {
    char *args[MAXARG];
    char buf[128], ch;
    int pid, curr;
    
    if (argc < 2) {
        printf("error");
        exit(0);
    }

    for (int i = 1; i < argc; i++) args[i - 1] = argv[i];
    args[argc] = 0;
    
    while (true) {
        curr = 0;
        
        while (read(0, &ch, sizeof(char)) == 1) {
            if (ch == '\n') break;
            buf[curr] = ch;
            curr++;
        }
        
        if (!curr) break;

        buf[curr] = '\0';

        pid = fork();
        
        if (!pid) {
            args[argc - 1] = buf;
            exec(args[0], args);
            printf("exec failed!\n");
            exit(1);
        } else {
            wait(0);
        }
    }

    exit(0);
}