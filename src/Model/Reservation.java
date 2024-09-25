package Model;

import java.util.Date;

public class Reservation {
    private Client client;
    private Hebergement hebergement;
    private TypeDeChambre typeDeChambre;
    private Date dateDebut;
    private Date dateFin;
    private boolean annuler;

    public Reservation(Client client, Hebergement hebergement, TypeDeChambre typeDeChambre, Date dateDebut, Date dateFin) {
        this.client = client;
        this.hebergement = hebergement;
        this.typeDeChambre = typeDeChambre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.annuler = false;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Hebergement getHebergement() {
        return hebergement;
    }

    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;
    }

    public TypeDeChambre getTypeDeChambre() {
        return typeDeChambre;
    }

    public void setTypeDeChambre(TypeDeChambre typeDeChambre) {
        this.typeDeChambre = typeDeChambre;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isAnnuler() {
        return annuler;
    }

    public void setAnnuler(boolean annuler) {
        this.annuler = annuler;
    }
}
