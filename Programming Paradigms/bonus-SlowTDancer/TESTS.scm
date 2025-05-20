(define total-counter 0)
(define counter 0)
(display "Arithmetic operations")
(display "TOTAL NUMBER OF TESTS: 30")
(if (equal? (+ 1 2 3 4 5) 15) (define counter (+ counter 1)) (define counter counter))
(if (equal? (+ (* -40 (/ 9 5)) 32 40) 0) (define counter (+ counter 1)) (define counter counter))
(if (equal? (* 1 2 3 4 5 6 7 8 9 10) 3628800) (define counter (+ counter 1)) (define counter counter))
(if (equal? (+ (quotient 15 6) (remainder 15 6)) 5) (define counter (+ counter 1)) (define counter counter))

(if (equal? (positive? (* 100 (* 5 100))) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (positive? (/ 100 -1)) #f) (define counter (+ counter 1)) (define counter counter))
(if (equal? (positive? (- (pow 4 2) (sqrt 256))) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (negative? (* 5 9)) #f) (define counter (+ counter 1)) (define counter counter))
(if (equal? (negative? (+ 5 -5)) #f) (define counter (+ counter 1)) (define counter counter))
(if (equal? (negative? (/ 100 (* 100 -1))) #t) (define counter (+ counter 1)) (define counter counter))

(if (equal? (zero? (pow 4 25)) #f) (define counter (+ counter 1)) (define counter counter))
(if (equal? (zero? 0) #t) (define counter (+ counter 1)) (define counter counter))

(if (equal? (pow 2 12) 4096) (define counter (+ counter 1)) (define counter counter))
(if (equal? (pow 2 (pow 3 3)) 134217728) (define counter (+ counter 1)) (define counter counter))

(if (equal? (sqrt (+ (* 5 5) (* 12 12))) 13) (define counter (+ counter 1)) (define counter counter))

(if (equal? (min 4 -1 32 43 5 45 6546 256) -1) (define counter (+ counter 1)) (define counter counter))
(if (equal? (max 10000000 324 54360365 312 43 4325 435 16 5 6256) 54360365) (define counter (+ counter 1)) (define counter counter))

(if (equal? (odd? (- 9 6)) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (odd? (- 9 (+ 6 3))) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (even? (sqrt (pow 2 20))) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (even? (/ (pow 6 20) (pow 2 20))) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (< (sqrt 4) (sqrt (pow 3 2)) (* 2 2)) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (< 2 (sqrt (sqrt (pow 2 4))) (max 2 3 4)) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (<= 2 (sqrt 9) (/ 27 3 3)) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (<= (+ 1 (/ 100 100)) (+ 1 1 1) (- (sqrt (sqrt 16)) 100)) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (> (pow 2 2) (- (pow 2 2) 1) 2) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (> (pow 2 2) (sqrt 16) (+ (* -40 (/ 9 5)) 32 40)) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (>= (sqrt (pow 2 (pow 2 2))) (sqrt 16) (+ (* -40 (/ 9 5)) 32 40)) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (>= (sqrt (pow 2 (pow 2 2))) (sqrt 25) (+ (* -40 (/ 9 5)) 32 40)) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (= (sqrt 4) (sqrt (pow 2 2)) (/ 4 2) 2) #t) (define counter (+ counter 1)) (define counter counter))
(display counter)
(define total-counter (+ total-counter counter))
(display -------------------------------)

(define counter 0)
(display "List operations")
(display "TOTAL NUMBER OF TESTS: 30")
(if (equal? (list law haru emperor life rei shiro) '(law haru emperor life rei shiro)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (car (list law haru emperor life rei shiro)) law) (define counter (+ counter 1)) (define counter counter))
(if (equal? (cdr (list law haru emperor life rei shiro)) '(haru emperor life rei shiro)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (car (cdr (list law haru emperor life rei shiro))) haru) (define counter (+ counter 1)) (define counter counter))

(if (equal? (null? (cdr (cdr '(saitama king)))) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (null? (cdr '(sasuke naruto))) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (length '(law haru emperor life rei shiro)) 6) (define counter (+ counter 1)) (define counter counter))
(if (equal? (length '()) 0) (define counter (+ counter 1)) (define counter counter))
(if (equal? (length (cdr '(one amae kei hermes))) 3) (define counter (+ counter 1)) (define counter counter))

(if (equal? (cons one '(amae kei hermes)) '(one amae kei hermes)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (cons one (cdr '(kei amae))) '(one amae)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (cons (car '(law haru emperor life rei shiro)) (cdr '(law haru emperor life rei shiro))) '(law haru emperor life rei shiro)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (car '((1 2) 3 4)) '(1 2)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (cdr '(1 (2 3) 4)) '((2 3) 4)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (car (cdr (cdr (cdr '(1 2 3 4 5 6 7))))) 4) (define counter (+ counter 1)) (define counter counter))
(if (equal? (car (cdr (cdr (list '(2 3 4) '(1 2 3) '(3 4 5))))) '(3 4 5)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (car (cdr(cdr '(1 2 3 4 5 6 7)))) 3) (define counter (+ counter 1)) (define counter counter))
(if (equal? (cdr(cdr(car(car '(((1 2 4 5) 6 (7 (8)) 9) 10 11 12))))) '(4 5)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (cdr (car (cdr (car '((Ilya (Aman Tony Sarah) Austin)))))) '(Tony Sarah)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (car(car(car(car '((((1) 2) 3 4) (6 (7))))))) 1) (define counter (+ counter 1)) (define counter counter))

(if (equal? (list 1 3 5 (+ 4 3) (/ 18 2)) '(1 3 5 7 9)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (list '() '() '()) '(() () ())) (define counter (+ counter 1)) (define counter counter))

(if (equal? (append '(1 2) '((1 2)) '((1) (2))) '(1 2 (1 2) (1) (2))) (define counter (+ counter 1)) (define counter counter))
(if (equal? (append '(1 2) (list '())) '(1 2 ())) (define counter (+ counter 1)) (define counter counter))
(if (equal? (append '(1 2) '() (list (+ 3 5) (- 8 1))) '(1 2 8 7)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (list? (car '((hellen arthur camille) good bad three))) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (list? (car (cdr (list sithet dzadzo gluncho dushki justzuka)))) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (reverse (list akali zed khazix rengar leblanc qiyana)) '(qiyana leblanc rengar khazix zed akali)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (reverse (cdr (list akali zed khazix rengar leblanc qiyana))) '(qiyana leblanc rengar khazix zed)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (reverse (car '((samira jarvan) braum))) '(jarvan samira)) (define counter (+ counter 1)) (define counter counter))
(display counter)
(define total-counter (+ total-counter counter))
(display -------------------------------)

(define counter 0)
(display "Bool operations")
(display "TOTAL NUMBER OF TESTS: 10")
(if (equal? (or) #f) (define counter (+ counter 1)) (define counter counter))
(if (equal? (or (and #t (or (= 2 3) (= 2 2))) #f (cdr '())) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (or (< (sqrt (* 2 2 2)) 3) (and #t #t)) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (or (not (< (sqrt (* 2 2 2)) 3)) (not (and #t #t))) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (and) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (and #t (or #t #f) #f (cdr '())) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (not (or (> 3 (pow 2 2)) (< (pow 2 3) (pow 3 2)))) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (list? vashli) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (equal? demetre demetre) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (equal? irakli demetre) #f) (define counter (+ counter 1)) (define counter counter))
(display counter)
(define total-counter (+ total-counter counter))
(display -------------------------------)

(define counter 0)
(display "Define functions")
(display "TOTAL NUMBER OF TESTS: 20")

(define (celsius->fahrenheit celsius) (+ (* 1.8 celsius) 32))
(define (num-occurrences seq eq?) (if(null? seq) '() (map (lambda(x) (eval (cons + (map (lambda(y) (if (eq? x y) 1 0))seq))))seq)))
(define (fib n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2)))))
(define (sorted? numbers) (or (< (length numbers) 2) (and (<= (car numbers) (cadr numbers)) (sorted? (cdr numbers)))))
(define (factorial n) (if (= n 0) 1 (* n (factorial (- n 1)))))
(define (sum ls) (if (null? ls) 0 (+ (car ls) (sum (cdr ls)))))
(define (leap-year? year) (or (and (zero? (remainder year 4)) (not (zero? (remainder year 100)))) (zero? (remainder year 400))))
(define (is-up-down? ls comp) (or (null? ls) (null? (cdr ls)) (and (comp (car ls) (cadr ls)) (is-up-down? (cdr ls) (lambda (one two) (comp two one))))))
(define (flatten-list ls) (if (null? ls) '() (if (not (list? ls)) (list ls) (apply append (map flatten-list ls)))))
(define (partition pivot num-list) (if (null? num-list) '(() ()) (if (< (car num-list) pivot) (list (cons (car num-list) (car (partition pivot (cdr num-list)))) (cadr (partition pivot (cdr num-list)))) (list (car (partition pivot (cdr num-list))) (cons (car num-list) (car (cdr (partition pivot (cdr num-list)))))))))
(define (quicksort num-list) (if (<= (length num-list) 1) num-list (append (quicksort (car (partition (car num-list) (cdr num-list)))) (list (car num-list)) (quicksort (cadr (partition (car num-list) (cdr num-list)))))))
(define (depth tree) (if (or (not (list? tree)) (null? tree)) 0 (+ 1 (apply max (map depth tree)))))
(define (remove ls elem) (if (null? ls) '() (if (equal? (car ls) elem) (remove (cdr ls) elem) (cons (car ls) (remove (cdr ls) elem)))))
(define (power-set set) (if (null? set) '(()) (append (power-set (cdr set)) (map (lambda (subset) (cons (car set) subset)) (power-set (cdr set))))))

(if (equal? (celsius->fahrenheit 100) 212) (define counter (+ counter 1)) (define counter counter))
(if (equal? (celsius->fahrenheit -40) -40) (define counter (+ counter 1)) (define counter counter))

(if (equal? (num-occurrences '(1 2 3 1 2) equal?) '(2 2 1 2 2)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (num-occurrences '(irakli irakli irakli irakli irakli irakli) equal?) '(6 6 6 6 6 6)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (num-occurrences '(naruto sasuke itachi shisui minato tobirama) equal?) '(1 1 1 1 1 1)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (fib 15) 987) (define counter (+ counter 1)) (define counter counter))
(if (equal? (fib 20) 10946) (define counter (+ counter 1)) (define counter counter))

(if (equal? (sorted? '(1 1 1 1 1 1 1 1)) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? (sorted? '(8 7 6 5 4 3 2 1 9 10 3213)) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (factorial 5) 120) (define counter (+ counter 1)) (define counter counter))
(if (equal? (factorial 10) 3628800) (define counter (+ counter 1)) (define counter counter))

(if (equal? (sum '(1 1 2 3 5 8 13 21 34 55 89 144)) 376) (define counter (+ counter 1)) (define counter counter))

(if (equal? (or (leap-year? 1996) (leap-year? 1997) (leap-year? 1998) (leap-year? 1999)) #t) (define counter (+ counter 1)) (define counter counter))

(if (equal? (is-up-down? '(1 6 2 4 3 5) >) #f) (define counter (+ counter 1)) (define counter counter))
(if (equal? (is-up-down? '(4 8 3 5 1 7 6 2) <) #f) (define counter (+ counter 1)) (define counter counter))

(if (equal? (flatten-list '(1 ((2) (1 3)) (3) 4)) '(1 2 1 3 3 4)) (define counter (+ counter 1)) (define counter counter))

(if (equal?(quicksort '(3 1 2 4 7 5)) '(1 2 3 4 5 7)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (remove '(1 2 3 4 5 6 7) 6) '(1 2 3 4 5 7)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (depth '(1 (2 3 (4)) (((6) 7 ((((8 9)) 10) (11)))) (4))) 7) (define counter (+ counter 1)) (define counter counter))

(if (equal? (power-set '(1 2 3)) '(() (3) (2) (2 3) (1) (1 3) (1 2) (1 2 3))) (define counter (+ counter 1)) (define counter counter))
(display counter)
(define total-counter (+ total-counter counter))
(display -------------------------------)

(define counter 0)
(display "Lambda functions")
(display "TOTAL NUMBER OF TESTS: 5")
(if (equal? ((lambda (x y) (+ x y)) 1 2) 3) (define counter (+ counter 1)) (define counter counter))
(if (equal? ((lambda (x) (car (cdr x))) '(kogami makishima akane tyler1)) makishima) (define counter (+ counter 1)) (define counter counter))
(if (equal? ((lambda (x) (pow x 2)) 14) 196) (define counter (+ counter 1)) (define counter counter))
(if (equal? ((lambda (x y) (y x 2)) 8 >) #t) (define counter (+ counter 1)) (define counter counter))
(if (equal? ((lambda (abcd sdx) ((lambda (sex mex) (* abcd sdx sex mex)) 12 45)) 123 35) 2324700) (define counter (+ counter 1)) (define counter counter))
(display counter)
(define total-counter (+ total-counter counter))
(display -------------------------------)

(define counter 0)
(display "Map/Apply/Eval functions")
(display "TOTAL NUMBER OF TESTS: 5")
(if (equal? (apply append (map (lambda (x) (list x (* 2 x)))  '(1 2 3))) '(1 2 2 4 3 6)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (map (lambda (x) (apply + (map (lambda (y) (if (= x y) 1 0)) '(1 2 2 3 3 3 4 4 5 5 5)))) '(1 2 2 3 3 3 4 4 5 5 5)) '(1 2 2 3 3 3 2 2 3 3 3)) (define counter (+ counter 1)) (define counter counter))
(if (equal? (map * '(1 2 3 4 5 6) '(1 2 3 4 5 6 7) '(1 2 3 4 5 6 7 8)) '(1 8 27 64 125 216)) (define counter (+ counter 1)) (define counter counter))

(if (equal? (eval (cons + '(1 2 3 4 5 6 7 8 9 10))) 55) (define counter (+ counter 1)) (define counter counter))
(if (equal? (eval (+ (* -40 (/ 9 5)) 32 40)) 0) (define counter (+ counter 1)) (define counter counter))
(display counter)
(define total-counter (+ total-counter counter))
(display -------------------------------)
(display total-counter)