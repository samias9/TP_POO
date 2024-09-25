package Business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Hebergement> getHebergements() {
        return hebergements;
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
    public boolean verifierDisponibilite(TypeDeChambre typeDeChambre, Hebergement hebergement, Date date) {
        List<Reservation> reservationsFiltrer = reservations.stream()
                .filter(reservation -> date.compareTo(reservation.getDateFin()) <= 0)
                .filter(reservation -> date.compareTo(reservation.getDateDebut()) >= 0)
                .filter(reservation -> reservation.getHebergement().equals(hebergement))
                .filter(reservation -> reservation.getTypeDeChambre().equals(typeDeChambre))
                .filter(reservation -> !reservation.isAnnuler())
                .toList();

        return reservationsFiltrer.size() < hebergement.getChambres(typeDeChambre);
    }
    @Override
    public void reserverChambre(Client client, Hebergement hebergement, Date dateArrivee, Date dateDepart, TypeDeChambre typeChambre){
       //verifier si une reservation avec les memes infos existe
        Optional<Reservation> existingReservation = reservations.stream()
                .filter(r -> r.getClient().equals(client)
                        && r.getHebergement().equals(hebergement)
                        && r.getTypeDeChambre().equals(typeChambre)
                        && r.getDateDebut().equals(dateArrivee)
                        && r.getDateFin().equals(dateDepart))
                .findFirst();

        //si existe pas
        if (!existingReservation.isPresent()) {
            //verifier si disponible (méthode verifierDisponibilitev)
            boolean disponible = verifierDisponibilite(typeChambre, hebergement, dateArrivee);
            if (disponible) {
                Reservation reservation = new Reservation(client, hebergement, typeChambre, dateArrivee, dateDepart);
                reservations.add(reservation);
                System.out.println("Réservation effectuée avec succès YAAAAY :)");
            } else {
                System.out.println("OUPSIIIIII :( La chambre demandée n'est pas disponible pour ces dates.");
            }
        } else {
            System.out.println(":/ Une réservation avec les mêmes informations existe déjà.");
        }
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
    public List<Hebergement> chercherHebergement(TypeHebergement hebergementType, TypeDeChambre typeDeChambre, String ville, String rue, String province, String pays, double prixMax) {
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
                .toList();
    }

    @Override
    public void annulerReservation(Reservation reservation) {

        //Truver la reservation
        Optional<Reservation> reservationToCancel = reservations.stream()
                .filter(r -> r.equals(reservation))
                .findFirst();

        // Pas déjà annulée?=> la marquer comme annulée
        if (reservationToCancel.isPresent()) {
            Reservation foundReservation = reservationToCancel.get();
            if (!foundReservation.isAnnuler()) {
                foundReservation.setAnnuler(true);
                System.out.println("La réservation a été annulée avec succès.");
            } else {
                System.out.println("Cette réservation est déjà annulée.");
            }
        } else {
            System.out.println("Réservation non trouvée :3");
        }
    }
}
