package Model;

import java.util.List;

public class Hebergement {
    private TypeHebergement type;
    private List<Chambre> chambres;
    private List<Chambre> chambresLibres;
    private String pays;
    private String province;
    private String ville;
    private String rue;
    private List<ServicesSupp> servicesDisponibles;

    public Hebergement(TypeHebergement type, List<Chambre> chambres, String pays, String province, String ville, String rue,List<ServicesSupp> servicesDisponibles) {
        this.type = type;
        this.chambres = chambres;
        this.pays = pays;
        this.province = province;
        this.ville = ville;
        this.rue = rue;
        this.servicesDisponibles = servicesDisponibles;
        this.chambresLibres=chambres;
    }
    public TypeHebergement getType() {
        return type;
    }
    public void setType(TypeHebergement type) {
        this.type = type;
    }
    public List<Chambre> getChambres() {
        return chambres;
    }
    public void setChambres(List<Chambre> chambres) {
        this.chambres = chambres;
    }
    public String getPays() {
        return pays;
    }
    public void setPays(String pays) {
        this.pays = pays;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getVille() {
        return ville;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }
    public String getRue() {
        return rue;
    }
    public void setRue(String rue) {
        this.rue = rue;
    }
    public List<ServicesSupp> getServicesDisponibles() {
        return servicesDisponibles;
    }
    public void setServicesDisponibles(List<ServicesSupp> servicesDisponibles) {
        this.servicesDisponibles = servicesDisponibles;
    }
    public void ajouterChambre(Chambre chambre){
        chambres.add(chambre);
    }
    public Chambre getChambrePrixMax(){
        return chambres.stream()
                .max((ch1, ch2) -> ch1.getPrix().compareTo(ch2.getPrix()))
                .orElse(null);
    }
    public void filterChambresLibres(){
        for (Chambre chambre : chambres) {
            if(chambre.disponible){
                chambresLibres.add(chambre);
            }
        }
    }

    @Override
    public String toString() {
        return "Hebergement {" +
                "Type = " + type +
                ", Chambres disponibles = " + chambres +
                ", Pays = '" + pays + '\'' +
                ", Province = '" + province + '\'' +
                ", Ville = '" + ville + '\'' +
                ", Rue = '" + rue + '\'' +
                ", Services disponibles = " + servicesDisponibles +
                '}';
    }
}
