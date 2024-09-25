package Business;


import Model.*;

import java.util.Date;
import java.util.List;

public interface SystemeGestionReservations {

    void ajouterClient(Client client);

    void ajouterLieuHebergement(Hebergement hebergement);

    void effectuerReservation(Reservation reservation);

    boolean verifierDisponibilite(TypeDeChambre typeDeChambre, Hebergement hebergement, Date date);

    void annulerReservation(Reservation reservation);

    List<Hebergement> chercherHebergement(TypeHebergement hebergementType, TypeDeChambre typeDeChambre, String ville, String rue, String province, String pays, double prixMax);

    void reserverChambre(Client client, Hebergement hebergement, Date dateArrivee, Date dateDepart, TypeDeChambre typeChambre);
}
