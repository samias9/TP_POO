package Business;

import Model.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SystemeGestionReservationsImpl implements SystemeGestionReservations {
    private List<Client> clients;
    private List<Hebergement> hebergements;
    private List<Reservation> reservations;

    /**
     * Constructeur par défaut qui initialise les listes de clients, d'hébergements et de réservations.
     */
    public SystemeGestionReservationsImpl() {
        this.clients = new ArrayList<>();
        this.hebergements = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    /**
     * Ajoute un nouveau client au système.
     *
     * @param client Le client à ajouter.
     */
    @Override
    public void ajouterClient(Client client) {
        clients.add(client);
    }

    /**
     * Ajoute un nouvel hébergement au système.
     *
     * @param hebergement L'hébergement à ajouter.
     */
    @Override
    public void ajouterLieuHebergement(Hebergement hebergement) {
        hebergements.add(hebergement);
    }

    /**
     * Recherche un client par son adresse courriel.
     *
     * @param email L'adresse courriel du client à rechercher.
     * @return Le client trouvé ou null si aucun client ne correspond.
     */
    @Override
    public Client rechercherClientParEmail(String email) {
        for (Client client : clients) {
            if (client.getCourriel().equals(email)) return client;
        }
        
        return null;
    }

    /**
     * Vérifie la disponibilité d'un type de chambre dans un hébergement pour une période donnée.
     *
     * @param typeDeChambre Le type de chambre à vérifier.
     * @param hebergement   L'hébergement dans lequel vérifier.
     * @param dateArrive    La date d'arrivée.
     * @param dateDepart    La date de départ.
     * @return true si la chambre est disponible, false sinon.
     */
    @Override
    public boolean verifierDisponibilite(TypeDeChambre typeDeChambre, Hebergement hebergement, Date dateArrive, Date dateDepart) {
        List<Reservation> reservationsFiltrer = reservations.stream()
                // 0 si date = date paramètre
                // <0 si date < date paramètre
                // >0 si date > date paramètre
                .filter(reservation ->
                        (reservation.getDateDebut().compareTo(dateArrive) >= 0 && reservation.getDateDebut().compareTo(dateDepart) <= 0)
                        || (reservation.getDateFin().compareTo(dateArrive) >= 0 && reservation.getDateFin().compareTo(dateDepart) <= 0)
                )
                .filter(reservation -> reservation.getHebergement().equals(hebergement))
                .filter(reservation -> reservation.getTypeDeChambre().equals(typeDeChambre))
                .filter(reservation -> !reservation.isAnnuler())
                .toList();

        return reservationsFiltrer.size() < hebergement.getChambres(typeDeChambre);
    }

    /**
     * Effectue une réservation pour un client.
     *
     * @param client        Le client qui effectue la réservation.
     * @param hebergement   L'hébergement réservé.
     * @param dateArrivee   La date d'arrivée.
     * @param dateDepart    La date de départ.
     * @param typeChambre   Le type de chambre réservé.
     */
    @Override
    public void reserverChambre(Client client, Hebergement hebergement, Date dateArrivee, Date dateDepart, TypeDeChambre typeChambre) {
        //hebergement.decrementerChambre(typeChambre);
        Reservation reservation = new Reservation(client, hebergement, typeChambre, dateArrivee, dateDepart);
        reservations.add(reservation);
        System.out.println("Réservation effectuée avec succès YAAAAY :)");
    }

    /**
     * Chercher un hébergement en fonction des critères: type, ville, rue, province, pays, prixMax
     *
     * @param hebergementType Type d'hébergement recherché
     * @param ville           Ville de l'hébergement
     * @param rue             Rue de l'hébergement
     * @param province        Province de l'hébergement
     * @param pays            Pays de l'hébergement
     * @param prixMax         Prix maximal accepté pour l'hébergement
     * @return Liste des hébergements qui correspondent aux critères
     */
    @Override
    public List<Hebergement> chercherHebergement(TypeHebergement hebergementType, TypeDeChambre typeDeChambre, String ville, String rue, String province, String pays, double prixMax, Date dateArrive, Date dateDepart) {
        // Filtrer par type et les autres critères
        return hebergements.stream()
                .filter(h -> (hebergementType == null || h.getType() == hebergementType))
                .filter(h -> (ville == null || h.getVille().equalsIgnoreCase(ville)))
                .filter(h -> (rue == null || h.getRue().equalsIgnoreCase(rue)))
                .filter(h -> (province == null || h.getProvince().equalsIgnoreCase(province)))
                .filter(h -> (pays == null || h.getPays().equalsIgnoreCase(pays)))
                .filter(h -> (typeDeChambre == null || h.getChambres(typeDeChambre) > 0))

                // Parcourir les types de chambres et vérifier si le prix est inférieur ou égal à prixMax
                .filter(h -> h.getPrixChambres(typeDeChambre) <= prixMax)

                // Enfin vérifier si une chambre de ce type est libre pour cet hébergement
                .filter(h -> verifierDisponibilite(typeDeChambre, h, dateArrive, dateDepart))
                .toList();
    }

    /**
     * Cherche un client par son nom et prénom.
     *
     * @param nom    Le nom du client.
     * @param prenom Le prénom du client.
     * @return Le client trouvé ou null si aucun client ne correspond.
     */
    @Override
    public Client chercherClient(String nom, String prenom) {
        return clients.stream()
                .filter(client -> client.getNom().equalsIgnoreCase(nom) && client.getPrenom().equalsIgnoreCase(prenom))
                .findFirst()
                .orElse(null);
    }

    /**
     * Cherche la première réservation d'un client donné.
     *
     * @param client Le client dont on cherche la réservation.
     * @return La première réservation trouvée ou null si aucune n'est trouvée.
     */
    @Override
    public Reservation chercherReservation(Client client){
        return reservations.stream()
        .filter(reservation -> reservation.getClient().equals(client))
        .findFirst()
        .orElse(null);
    }

    /**
     * Retourne toutes les réservations d'un client donné.
     *
     * @param client Le client dont on cherche les réservations.
     * @return Une liste de réservations.
     */
    @Override
    public List<Reservation> chercherReservationsParClient(Client client) {
        return reservations.stream()
                .filter(reservation -> reservation.getClient().equals(client))  // Vérifier l'égalité basée sur `equals`
                .filter(reservation -> !reservation.isAnnuler())  // Exclure les réservations annulées
                .toList();
    }

    /**
     * Annule une réservation.
     *
     * @param reservation La réservation à annuler.
     */
    @Override
    public void annulerReservation(Reservation reservation) {
        if (!reservation.isAnnuler()) {
            reservation.setAnnuler(true);
            System.out.println("La réservation a été annulée avec succès.");
        } else {
            System.out.println("Cette réservation est déjà annulée.");
        }
    }

    /**
     * Retourne tous les hébergements du système.
     *
     * @return Une liste d'hébergements.
     */
    @Override
    public List<Hebergement> getTousLesHebergements() {
        return hebergements;
    }

    @Override
    public String toString() {
        return "SystemeGestionReservationsImpl{" +
                "clients=" + clients +
                ", hebergements=" + hebergements +
                ", reservations=" + reservations +
                '}';
    }
}
