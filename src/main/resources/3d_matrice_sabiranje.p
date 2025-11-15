program Matrice;

promenljive {
    postavi podatak i;
    postavi podatak j;

    postavi podatak[3][3] A;
    postavi podatak[3][3] B;
    postavi podatak[3][3] C;
}

pocni {
    ponovi (i podesi 0; i < 3; i podesi i plus 1;) {
        ponovi (j podesi 0; j < 3; j podesi j plus 1;) {
            A[i][j] podesi (i puta 3) plus j plus 1;
            B[i][j] podesi 9 potkresi ((i puta 3) plus j);
        }
    }

    ponovi (i podesi 0; i < 3; i podesi i plus 1;) {
        ponovi (j podesi 0; j < 3; j podesi j plus 1;) {
            C[i][j] podesi A[i][j] plus B[i][j];
        }
    }
}