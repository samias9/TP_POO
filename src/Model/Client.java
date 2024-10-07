package Model;

import java.util.Objects;

public class Client {
    private String nom;
    private String prenom;
    private String courriel;
    private String tel;

    /**
     * Constructeur de la classe Client.
     *
     * @param nom      Le nom du client.
     * @param prenom   Le prénom du client.
     * @param courriel L'adresse courriel du client.
     * @param tel      Le numéro de téléphone du client.
     */
    public Client(String nom, String prenom, String courriel, String tel) {
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
        this.tel = tel;
    }

    /**
     * Retourne le nom du client.
     *
     * @return Le nom du client.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du client.
     *
     * @param nom Le nouveau nom du client.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prénom du client.
     *
     * @return Le prénom du client.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom du client.
     *
     * @param prenom Le nouveau prénom du client.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne l'adresse courriel du client.
     *
     * @return L'adresse courriel du client.
     */
    public String getCourriel() {
        return courriel;
    }

    /**
     * Définit l'adresse courriel du client.
     *
     * @param courriel La nouvelle adresse courriel du client.
     */
    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    /**
     * Retourne le numéro de téléphone du client.
     *
     * @return Le numéro de téléphone du client.
     */
    public String getTel() {
        return tel;
    }

    /**
     * Définit le numéro de téléphone du client.
     *
     * @param tel Le nouveau numéro de téléphone du client.
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * Vérifie l'égalité entre cet objet Client et un autre objet.
     *
     * @param o L'objet à comparer avec ce client.
     * @return true si les objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(nom, client.nom) && Objects.equals(prenom, client.prenom);
    }

    /**
     * Génère un hash code pour cet objet Client.
     *
     * @return Le hash code basé sur le nom et le prénom du client.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom);
    }

    @Override
    public String toString() {
        return "Client{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", courriel='" + courriel + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
