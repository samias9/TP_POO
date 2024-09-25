package Business;

import java.util.ArrayList;
import java.util.List;

import Model.Client;
import Model.Hebergement;
import Model.Reservation;
import Model.TypeHebergement;

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
    public List<Hebergement> filterByTypeHebergement(TypeHebergement typeHebergement){
        List<Hebergement> filteredHebergements = new ArrayList<>();
        for (Hebergement hebergement : hebergements) {
            if (hebergement.getType() == typeHebergement) {
                filteredHebergements.add(hebergement);
            }
        }
        return filteredHebergements;
    }
}
