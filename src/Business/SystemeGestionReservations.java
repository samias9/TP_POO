package Business;


import Model.*;

import java.util.Date;
import java.util.List;

public interface SystemeGestionReservations {

    void ajouterClient(Client client);

    void ajouterLieuHebergement(Hebergement hebergement);

    void effectuerReservation(Reservation reservation);

    boolean verifierDisponibilite(TypeDeChambre typeDeChambre, Hebergement hebergement, Date date);


    List<Hebergement> filterByTypeHebergement(TypeHebergement typeHebergement);
}
