package Business;

import Model.*;
import java.util.Date;
import java.util.List;

public interface SystemeGestionReservations {

    void ajouterClient(Client client);

    void ajouterLieuHebergement(Hebergement hebergement);

    Client rechercherClientParEmail(String email);

    boolean verifierDisponibilite(TypeDeChambre typeDeChambre, Hebergement hebergement, Date dateArrive, Date dateDepart);

    void annulerReservation(Reservation reservation);

    List<Hebergement> chercherHebergement(TypeHebergement hebergementType, TypeDeChambre typeDeChambre, List<ServicesSupp> services, String ville, String rue, String province, String pays, double prixMax, Date dateArrive, Date dateDepart);

    void reserverChambre(Client client, Hebergement hebergement, Date dateArrivee, Date dateDepart, TypeDeChambre typeChambre);

    Client chercherClient(String nom, String prenom);

    List<Reservation> chercherReservationsParClient(Client client);

    List<Hebergement> getTousLesHebergements();

}
