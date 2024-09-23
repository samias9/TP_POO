package Business;

import Model.Client;
import Model.Hebergement;
import Model.Reservation;
import Model.TypeHebergement;

import java.util.List;

public interface SystemeGestionReservations {

    void ajouterClient(Client client);

    void ajouterLieuHebergement(Hebergement hebergement);

    void effectuerReservation(Reservation reservation);


    List<Hebergement> filterByTypeHebergement(TypeHebergement typeHebergement);
}
