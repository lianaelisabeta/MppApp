package services;

import models.Cursa;
import models.CursaDestinatie;
import models.Rezervare;

import java.util.List;

public interface IObserver {
    //void RezervareAdaugata(Rezervare rezervare);
    void UpdateCurse(List<CursaDestinatie> curse);
    //void UpdateLocuri(Cursa cursa);
}
