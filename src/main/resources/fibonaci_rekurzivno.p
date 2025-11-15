program FibonacciRekurzivno;

procedura podatak fib(podatak n) {
    proveri (n <= 1) pa {
        posalji n;
    }
    posalji fib(n potkresi 1) plus fib(n potkresi 2);
}

promenljive {
    postavi podatak i;
    postavi podatak rezultat;
    postavi podatak[15] fibNiz;
}

pocni {
    ponovi (i podesi 0; i < 15; i podesi i plus 1;) {
        rezultat podesi fib(i);
        fibNiz[i] podesi rezultat;
    }
}