program FunkcijeProgram;

procedura bubbleSort(podatak[10] niz; podatak n) promenljive {
    postavi podatak i;
    postavi podatak j;
    postavi podatak temp;
    postavi podatak zamena;
} {
    ponovi (i podesi 0; i < n potkresi 1; i podesi i plus 1;) {
        zamena podesi 0;

        ponovi (j podesi 0; j < n potkresi i potkresi 1; j podesi j plus 1;) {
            proveri (niz[j] > niz[j plus 1]) pa {
                temp podesi niz[j];
                niz[j] podesi niz[j plus 1];
                niz[j plus 1] podesi temp;
                zamena podesi 1;
            }
        }

        proveri (zamena == 0) pa {
            prekini;
        }
    }
}

procedura fibonacci(podatak[15] fib; podatak n) promenljive {
    postavi podatak i;
    postavi podatak a;
    postavi podatak b;
    postavi podatak sledeci;
} {
    a podesi 0;
    b podesi 1;

    fib[0] podesi a;
    fib[1] podesi b;

    ponovi (i podesi 2; i < n; i podesi i plus 1;) {
        sledeci podesi a plus b;
        fib[i] podesi sledeci;
        a podesi b;
        b podesi sledeci;
    }
}

promenljive {
    postavi podatak[10] niz;
    postavi podatak[15] fibNiz;
    postavi podatak i;
}

pocni {
    niz[0] podesi 64;
    niz[1] podesi 34;
    niz[2] podesi 25;
    niz[3] podesi 12;
    niz[4] podesi 22;
    niz[5] podesi 11;
    niz[6] podesi 90;
    niz[7] podesi 88;
    niz[8] podesi 45;
    niz[9] podesi 50;

    bubbleSort(niz; 10);

    fibonacci(fibNiz; 15);
}