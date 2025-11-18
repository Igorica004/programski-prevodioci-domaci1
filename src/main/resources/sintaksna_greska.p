program Kompleksan;

postoji Tacka {
    podatak x;
    podatak y;
}



procedura plutajuci povrsina(Pravougaonik r)
promenljive {
    postavi plutajuci sirina;
    postavi plutajuci visina;
}
{
    sirina podesi r.donjiDesni.x potkresi r.gornjiLevi.x;
    visina podesi r.gornjiLevi.y potkresi r.donjiDesni.y;

    posalji sirina puta visina;
}

promenljive {
    postavi Pravougaonik[3] figure;
    postavi plutajuci[3] rezultati;
    postavi podatak i podesi 0;
}

pocni {
    postoji Pravougaonik {
        Tacka gornjiLevi;
        Tacka donjiDesni;
    }
    figure[0] podesi  {
        { 0 ; 10 } ;
        { 20 ; 0 }
    };

    figure[1] podesi {
        { potkresi 5 ; 8 } ;
        { 10 ; potkresi 2 }
    };

    figure[2] podesi {
        { 1 ; 1 } ;
        { 6 ; potkresi 9 }
    };

    ponovi ( i podesi 0; i < 3; i podesi i potkresi 1;) {
        rezultati[i] podesi povrsina(figure[i]);
    }

    rezultati[0] podesi rezultati[0] plus (figure[1].donjiDesni.x podigni 2 );

    figure[1].gornjiLevi.x podesi figure[1].gornjiLevi.x plus 100;
}
