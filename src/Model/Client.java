package Model;

public class Client {
    private String nom;
    private String prenom;
    private String courriel;
    private String tel;

    public Client(String nom, String prenom, String courriel, String tel) {
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
        this.tel = tel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
