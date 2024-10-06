package Model;

import java.util.*;

public class Hebergement {
    private TypeHebergement type;
    private String pays;
    private String province;
    private String ville;
    private String rue;
    private final Set<ServicesSupp> services;
    private final Map<TypeDeChambre, Integer> chambres;
    private final Map<TypeDeChambre, Double> prixChambres;

    public Hebergement(TypeHebergement type, String pays, String province, String ville, String rue) {
        this.type = type;
        this.pays = pays;
        this.province = province;
        this.ville = ville;
        this.rue = rue;
        prixChambres = new HashMap<>();
        services = new HashSet<>();
        chambres = new HashMap<>();
    }

    public TypeHebergement getType() {
        return type;
    }

    public void setType(TypeHebergement type) {
        this.type = type;
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

    public Set<ServicesSupp> getServices() {
        return Collections.unmodifiableSet(services);
    }

    public void ajouterService(ServicesSupp servicesSupp) {
        this.services.add(servicesSupp);
    }

    public void ajouterServices(List<ServicesSupp> servicesSupp) {
        this.services.addAll(servicesSupp);
    }

    public void supprimerService(ServicesSupp servicesSupp) {
        this.services.remove(servicesSupp);
    }

    public void supprimerServices(List<ServicesSupp> servicesSupp) {
        servicesSupp.forEach(this.services::remove);
    }

    public Integer getChambres(TypeDeChambre typeDeChambre) {
        return chambres.get(typeDeChambre) == null ? 0 : chambres.get(typeDeChambre);
    }

    public Double getPrixChambres(TypeDeChambre typeDeChambre) {
        return prixChambres.get(typeDeChambre) == null ? 0 : prixChambres.get(typeDeChambre);
    }

    public void setPrixChambres(TypeDeChambre typeDeChambre, Double prix) {
        prixChambres.put(typeDeChambre, prix);
    }
    public void decrementerChambre(TypeDeChambre typeDeChambre) {
        if (chambres.containsKey(typeDeChambre) && chambres.get(typeDeChambre) > 0) {
            chambres.put(typeDeChambre, chambres.get(typeDeChambre) - 1);
        } else {
            System.out.println("Aucune chambre disponible pour ce type de chambre.");
        }
    }

    public void ajouterChambre(TypeDeChambre typeDeChambre, int nombre) {
        if (nombre <= 0) return;

        if (chambres.containsKey(typeDeChambre)) {
            chambres.put(typeDeChambre, chambres.get(typeDeChambre) + nombre);
        } else {
            chambres.put(typeDeChambre, nombre);
        }
    }

    public void ajouterChambre(TypeDeChambre typeDeChambre) {
        ajouterChambre(typeDeChambre, 1);
    }

    public void supprimerChambre(TypeDeChambre typeDeChambre) {
        chambres.remove(typeDeChambre);
    }
}
