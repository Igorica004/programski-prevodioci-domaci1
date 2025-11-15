program TransponovanjeMatrice;

procedura podatak[4][3] transponuj(podatak[3][4] mat; podatak m; podatak n) promenljive {
    postavi podatak i;
    postavi podatak j;
    postavi podatak[4][3] rezultat;
} {
    ponovi (i podesi 0; i < m; i podesi i plus 1;) {
        ponovi (j podesi 0; j < n; j podesi j plus 1;) {
            rezultat[j][i] podesi mat[i][j];
        }
    }
    posalji rezultat;
}

promenljive {
    postavi podatak i;
    postavi podatak j;
    postavi podatak[3][4] matrica;
    postavi podatak[4][3] transponovana;
}

pocni {
    ponovi (i podesi 0; i < 3; i podesi i plus 1;) {
        ponovi (j podesi 0; j < 4; j podesi j plus 1;) {
            matrica[i][j] podesi (i puta 4) plus j plus 1;
        }
    }
    transponovana podesi transponuj(matrica; 3; 4);
}