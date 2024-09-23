package Model;

import java.time.LocalDate;

public class Reservation {
    Client client;
    LocalDate DateReservation;
    Chambre chambre;
    LocalDate dateArrivee;
    LocalDate dateDepart;
    public Reservation(Client client, LocalDate DateReservation, Chambre chambre, LocalDate dateArrivee, LocalDate dateDepart) {
        this.client = client;
        this.DateReservation = DateReservation;
        this.chambre = chambre;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public LocalDate getDateReservation() {
        return DateReservation;
    }
    public void setDateReservation(LocalDate DateReservation) {
        this.DateReservation = DateReservation;
    }
    public Chambre getChambre() {
        return chambre;
    }
    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }
    public LocalDate getDateArrivee() {
        return dateArrivee;
    }
    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }
    public LocalDate getDateDepart() {
        return dateDepart;
    }
    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }
    @Override
    public String toString() {
        return "Reservation{" +
                "client=" + client +
                ", DateReservation=" + DateReservation +
                ", chambre=" + chambre +
                ", dateArrivee=" + dateArrivee +
                ", dateDepart=" + dateDepart +
                '}';
    }
}
