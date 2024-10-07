package Model;

import java.util.Date;

public class Reservation {
    private Client client;
    private Hebergement hebergement;
    private TypeDeChambre typeDeChambre;
    private Date dateDebut;
    private Date dateFin;
    private boolean annuler;

    /**
     * Constructeur de la classe Reservation.
     *
     * @param client        Le client effectuant la réservation.
     * @param hebergement   L'hébergement réservé.
     * @param typeDeChambre Le type de chambre réservé.
     * @param dateDebut     La date de début de la réservation.
     * @param dateFin       La date de fin de la réservation.
     */
    public Reservation(Client client, Hebergement hebergement, TypeDeChambre typeDeChambre, Date dateDebut, Date dateFin) {
        this.client = client;
        this.hebergement = hebergement;
        this.typeDeChambre = typeDeChambre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.annuler = false;
    }

    /**
     * Retourne le client ayant effectué la réservation.
     *
     * @return Le client.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Définit le client de la réservation.
     *
     * @param client Le nouveau client.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Retourne l'hébergement réservé.
     *
     * @return L'hébergement.
     */
    public Hebergement getHebergement() {
        return hebergement;
    }

    /**
     * Définit l'hébergement de la réservation.
     *
     * @param hebergement Le nouvel hébergement.
     */
    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;
    }

    /**
     * Retourne le type de chambre réservé.
     *
     * @return Le type de chambre.
     */
    public TypeDeChambre getTypeDeChambre() {
        return typeDeChambre;
    }

    /**
     * Définit le type de chambre réservé.
     *
     * @param typeDeChambre Le nouveau type de chambre.
     */
    public void setTypeDeChambre(TypeDeChambre typeDeChambre) {
        this.typeDeChambre = typeDeChambre;
    }

    /**
     * Retourne la date de début de la réservation.
     *
     * @return La date de début.
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Définit la date de début de la réservation.
     *
     * @param dateDebut La nouvelle date de début.
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Retourne la date de fin de la réservation.
     *
     * @return La date de fin.
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Définit la date de fin de la réservation.
     *
     * @param dateFin La nouvelle date de fin.
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

     /**
     * Vérifie si la réservation est annulée.
     *
     * @return true si la réservation est annulée, false sinon.
     */
    public boolean isAnnuler() {
        return annuler;
    }

    /**
     * Définit l'état d'annulation de la réservation.
     *
     * @param annuler true si la réservation est annulée, false sinon.
     */
    public void setAnnuler(boolean annuler) {
        this.annuler = annuler;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "client=" + client +
                ", hebergement=" + hebergement +
                ", typeDeChambre=" + typeDeChambre +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", annuler=" + annuler +
                '}';
    }
}
