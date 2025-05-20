// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.


// while(true){
//     int i = 0
//     int n = 8192
//     int color = 0
//     if(KBD){
//         color = -1
//     }
//     while (i < n){
//         RAM[SCREEN + i] = color
//         i++
//     } 
// }

// while(true)
(LOOP)
//int i = 0
    @i
    M=0
//int n = 8192
    @8192
    D=A
    @n
    M=D
//int color = 0
    @color
    M=0
//if(KBD)
    @KBD
    D=M
    @PAINTING
    D;JEQ
//color = -1
    @color
    M=-1
//while(i < n)
    (PAINTING)
        @i
        D=M
        @n
        D=D-M
        @END
        D;JGE
//RAM[SCREEN + i] = color
        @SCREEN
        D=A
        @i
        D=D+M
        @address
        M=D
        @color
        D=M
        @address
        A=M
        M=D
//i++
        @i
        M=M+1
        @PAINTING
        0;JMP

(END)
    @LOOP
    0;JMP
