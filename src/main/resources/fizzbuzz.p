program FizzBuzz;

procedura fizzbuzz(podatak n) promenljive {
    postavi podatak i;
    postavi podatak ostatak;
} {
    ponovi (i podesi 1; i <= n; i podesi i plus 1;) {
        ostatak podesi i povrat 15;
        proveri (ostatak == 0) pa {
        } pak {
            ostatak podesi i povrat 3;
            proveri (ostatak == 0) pa {
            } pak {
                ostatak podesi i povrat 5;
                proveri (ostatak == 0) pa {
                    
                } pak {
                    //pisi(i)
                }
            }
        }
    }
}
promenljive {}
pocni {
    fizzbuzz(25);
}