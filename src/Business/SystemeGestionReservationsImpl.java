package Business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Model.*;

public class SystemeGestionReservationsImpl implements SystemeGestionReservations {
    private List<Client> clients;
    private List<Hebergement> hebergements;
    private List<Reservation> reservations;

    public SystemeGestionReservationsImpl() {
        this.clients = new ArrayList<>();
        this.hebergements = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    @Override
    public void ajouterClient(Client client) {
        clients.add(client);
    }

    @Override
    public void ajouterLieuHebergement(Hebergement hebergement) {
        hebergements.add(hebergement);
    }
    @Override
    public void effectuerReservation(Reservation reservation) {
        reservations.add(reservation);
    }
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

    @Override
    public void annulerReservation(Reservation reservation) {
        if (!reservation.isAnnuler()) {
            reservation.setAnnuler(true);
            System.out.println("La réservation a été annulée avec succès.");
        } else {
            System.out.println("Cette réservation est déjà annulée.");
        }
    }
}
