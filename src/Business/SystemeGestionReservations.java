package Business;

import Model.*;
import java.util.Date;
import java.util.List;

public interface SystemeGestionReservations {

    void ajouterClient(Client client);

    void ajouterLieuHebergement(Hebergement hebergement);

    void effectuerReservation(Reservation reservation);

    boolean verifierDisponibilite(TypeDeChambre typeDeChambre, Hebergement hebergement, Date dateArrive, Date dateDepart);

    void annulerReservation(Reservation reservation);

    List<Hebergement> chercherHebergement(TypeHebergement hebergementType, TypeDeChambre typeDeChambre, String ville, String rue, String province, String pays, double prixMax, Date dateArrive, Date dateDepart);

    void reserverChambre(Client client, Hebergement hebergement, Date dateArrivee, Date dateDepart, TypeDeChambre typeChambre);

    public Client chercherClient(String nom, String prenom);

    public List<Reservation> chercherReservationsParClient(Client client);

    public Reservation chercherReservation(Client client);

    List<Hebergement> getTousLesHebergements();

}
