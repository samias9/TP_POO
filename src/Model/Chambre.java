package Model;

public class Chambre {
    int numChambre;
    TypeDeChambre typeDeChambre;
    Hebergement hebergement;
    Double prix;
    Boolean disponible;

    public Chambre(int numChambre, TypeDeChambre typeDeChambre, Hebergement hebergement, Double prix, Boolean disponible) {
        this.numChambre = numChambre;
        this.typeDeChambre = typeDeChambre;
        this.hebergement = hebergement;
        this.prix = prix;
        this.disponible = disponible;
    }
    public Chambre(int numChambre, TypeDeChambre typeDeChambre, Double prix, Boolean disponible) {
        this.numChambre = numChambre;
        this.typeDeChambre = typeDeChambre;
        this.hebergement = hebergement;
        this.prix = prix;
        this.disponible = disponible;
    }

    public int getNumChambre(){
        return numChambre;
    }
    public void setNumChambre(int numChambre){
        this.numChambre=numChambre;
    }
    public TypeDeChambre getTypeDeChambre(){
        return typeDeChambre;
    }
    public void setTypeDeChambre(TypeDeChambre typeDeChambre){
        this.typeDeChambre=typeDeChambre;
    }
    public Hebergement getHebergement(){
        return  this.hebergement;
    }
    public void setHebergement(Hebergement hebergement){
        this.hebergement=hebergement;
    }
    public Double getPrix(){
        return prix;
    }
    public void setPrix(Double prix){
        this.prix=prix;
    }
    public boolean getDisponibilite(){
        return disponible;
    }
    public void setDisponibilite(Boolean disponibilite){
        this.disponible=disponibilite;
    }
    @Override
    public String toString() {
        return "Chambre[numChambre=" + numChambre + ", typeDeChambre=" + typeDeChambre + ", hebergement=" + hebergement + ", prix=" + prix + ", disponible=" + disponible + "]";
    }
}
