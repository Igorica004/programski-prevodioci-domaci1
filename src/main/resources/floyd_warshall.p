program FloydWarshall;

promenljive {
    postavi podatak i;
    postavi podatak j;
    postavi podatak k;
    postavi podatak n;
    postavi podatak INF;
    
    postavi podatak[4][4] graf;
    postavi podatak[4][4] dist;
}

pocni {
    n podesi 4;
    INF podesi 9999;
    
    ponovi (i podesi 0; i < n; i podesi i plus 1;) {
        ponovi (j podesi 0; j < n; j podesi j plus 1;) {
            graf[i][j] podesi INF;
        }
    }
    
    graf[0][0] podesi 0;
    graf[1][1] podesi 0;
    graf[2][2] podesi 0;
    graf[3][3] podesi 0;
    
    graf[0][1] podesi 3;
    graf[0][2] podesi 8;
    graf[0][3] podesi INF;
    graf[1][0] podesi INF;
    graf[1][2] podesi INF;
    graf[1][3] podesi 1;
    graf[2][0] podesi INF;
    graf[2][1] podesi 4;
    graf[2][3] podesi INF;
    graf[3][0] podesi 2;
    graf[3][1] podesi INF;
    graf[3][2] podesi 5;
    
    ponovi (i podesi 0; i < n; i podesi i plus 1;) {
        ponovi (j podesi 0; j < n; j podesi j plus 1;) {
            dist[i][j] podesi graf[i][j];
        }
    }
    
    ponovi (k podesi 0; k < n; k podesi k plus 1;) {
        ponovi (i podesi 0; i < n; i podesi i plus 1;) {
            ponovi (j podesi 0; j < n; j podesi j plus 1;) {
                proveri (dist[i][j] > dist[i][k] plus dist[k][j]) pa {
                    dist[i][j] podesi dist[i][k] plus dist[k][j];
                }
            }
        }
    }
}