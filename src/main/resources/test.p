program TestProgram;

postoji Tacka {
    podataksssds x;
    podatak y;
}

postoji Pravougaonik {
    Tacka gornjiLevi;
    Tacka donjiDesni;
}

popis Boja {
    CRVENA;
    ZELENA;
    PLAVA;
    ZUTA;
}

procedura podatak saberi(podatak a; podatak b) {
    posalji a plus b;
}

procedura podatak pomnozi(podatak a; podatak b) {
    posalji a puta b;
}

procedura podatak[5] napraviNiz(podatak start; podatak n) promenljive {
    postavi podatak i;
    postavi podatak[5] rezultat;
} {
    ponovi (i podesi 0; i < n; i podesi i plus 1;) {
        rezultat[i] podesi start plus i;
    }
    posalji rezultat;
}

procedura podatak faktorijel(podatak n) {
    proveri (n <= 1) pa {
        posalji 1;
    }
    posalji n puta faktorijel(n potkresi 1);
}

procedura podatak max(podatak a; podatak b) {
    proveri (a > b) pa {
        posalji a;
    } pak {
        posalji b;
    }
}

procedura racunaj(podatak[10] niz; podatak n) promenljive {
    postavi podatak i;
    postavi podatak suma;
    postavi podatak proizvod;
} {
    suma podesi 0;
    proizvod podesi 1;
    
    ponovi (i podesi 0; i < n; i podesi i plus 1;) {
        suma podesi suma plus niz[i];
        proizvod podesi proizvod puta niz[i];
        
        proveri (niz[i] > 10) pa {
            prekini;
        }
    }
}

procedura obrada(podatak x) promenljive {
    postavi podatak i;
} {
    ponovi (i podesi 0; i < 10; i podesi i plus 1;) {
        proveri (i == 5) pa {
            produzi;
        }
        
        proveri (i > x) pa {
            prekini;
        }
    }
}

promenljive {
    postavi podatak x;
    postavi podatak y;
    postavi podatak z;
    postavi plutajuci pi;
    postavi pismo karakter;
    postavi poruka tekst;
    postavi pojam istina;
    postavi podatak[10] niz;
    postavi podatak[3][3] matrica;
    postavi Tacka tacka1;
    postavi Pravougaonik pravougaonik;
    postavi Boja boja;
    postavi podatak rezultat;
    postavi podatak i;
    postavi podatak j;
}

pocni {
    x podesi 10;
    y podesi 20;
    z podesi saberi(x; y);
    
    pi podesi 3.14;
    karakter podesi 'A';
    tekst podesi "Zdravo svete";
    istina podesi pozitivno;
    
    boja podesi CRVENA;
    
    tacka1.x podesi 5;
    tacka1.y podesi 10;
    
    pravougaonik.gornjiLevi.x podesi 0;
    pravougaonik.gornjiLevi.y podesi 0;
    pravougaonik.donjiDesni.x podesi 100;
    pravougaonik.donjiDesni.y podesi 50;
    
    ponovi (i podesi 0; i < 10; i podesi i plus 1;) {
        niz[i] podesi i puta 2;
    }
    
    ponovi (i podesi 0; i < 3; i podesi i plus 1;) {
        ponovi (j podesi 0; j < 3; j podesi j plus 1;) {
            matrica[i][j] podesi i plus j;
        }
    }
    
    rezultat podesi faktorijel(5); 
    rezultat podesi max(x; y);
    rezultat podesi (x plus y) puta z potkresi 5;
    rezultat podesi 2 podigni 8;
    
    proveri (x > 5 && y < 30) pa {
        z podesi x plus y;
    } pak {
        z podesi x potkresi y;
    }
    
    proveri (x == 10 || y != 15) pa {
        rezultat podesi 1;
    }
    
    proveri (!(x < 0)) pa {
        rezultat podesi 100;
    }
    
    proveri (x >= 10 && y <= 20) pa {
        z podesi 50;
    }
    
    ponovi (i podesi 0; i < 5; i podesi i plus 1;) {
        proveri (i == 2) pa {
            produzi;
        }
        
        proveri (i == 4) pa {
            prekini;
        }
        
        niz[i] podesi i;
    }
    
    racunaj(niz; 10);
    obrada(x);
    rezultat podesi (10 plus 20) podeljeno (5 potkresi 2);
    rezultat podesi 17 povrat 5;
    x podesi +15;
    y podesi -10;
    istina podesi pogresno;
    niz podesi napraviNiz(1; 5);
}