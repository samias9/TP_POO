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

    /**
     * Constructeur de la classe Hebergement.
     *
     * @param type     Le type d'hébergement.
     * @param pays     Le pays de l'hébergement.
     * @param province La province de l'hébergement.
     * @param ville    La ville de l'hébergement.
     * @param rue      La rue de l'hébergement.
     */
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

    /**
     * Retourne le type d'hébergement.
     *
     * @return Le type d'hébergement.
     */
    public TypeHebergement getType() {
        return type;
    }

    /**
     * Définit le type d'hébergement.
     *
     * @param type Le nouveau type d'hébergement.
     */
    public void setType(TypeHebergement type) {
        this.type = type;
    }

     /**
     * Retourne le pays de l'hébergement.
     *
     * @return Le pays de l'hébergement.
     */
    public String getPays() {
        return pays;
    }

    /**
     * Définit le pays de l'hébergement.
     *
     * @param pays Le nouveau pays de l'hébergement.
     */
    public void setPays(String pays) {
        this.pays = pays;
    }

    /**
     * Retourne la province de l'hébergement.
     *
     * @return La province de l'hébergement.
     */
    public String getProvince() {
        return province;
    }

    /**
     * Définit la province de l'hébergement.
     *
     * @param province La nouvelle province de l'hébergement.
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Retourne la ville de l'hébergement.
     *
     * @return La ville de l'hébergement.
     */
    public String getVille() {
        return ville;
    }

     /**
     * Définit la ville de l'hébergement.
     *
     * @param ville La nouvelle ville de l'hébergement.
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * Retourne la rue de l'hébergement.
     *
     * @return La rue de l'hébergement.
     */
    public String getRue() {
        return rue;
    }

     /**
     * Définit la rue de l'hébergement.
     *
     * @param rue La nouvelle rue de l'hébergement.
     */
    public void setRue(String rue) {
        this.rue = rue;
    }

    /**
     * Retourne les services supplémentaires offerts par l'hébergement.
     *
     * @return Un ensemble non modifiable de services supplémentaires.
     */
    public Set<ServicesSupp> getServices() {
        return Collections.unmodifiableSet(services);
    }

    /**
     * Ajoute un service supplémentaire à l'hébergement.
     *
     * @param servicesSupp Le service supplémentaire à ajouter.
     */
    public void ajouterService(ServicesSupp servicesSupp) {
        this.services.add(servicesSupp);
    }

    /**
     * Ajoute plusieurs services supplémentaires à l'hébergement.
     *
     * @param servicesSupp Une liste de services supplémentaires à ajouter.
     */
    public void ajouterServices(List<ServicesSupp> servicesSupp) {
        this.services.addAll(servicesSupp);
    }

    /**
     * Supprime un service supplémentaire de l'hébergement.
     *
     * @param servicesSupp Le service supplémentaire à supprimer.
     */
    public void supprimerService(ServicesSupp servicesSupp) {
        this.services.remove(servicesSupp);
    }

    /**
     * Supprime plusieurs services supplémentaires de l'hébergement.
     *
     * @param servicesSupp Une liste de services supplémentaires à supprimer.
     */
    public void supprimerServices(List<ServicesSupp> servicesSupp) {
        servicesSupp.forEach(this.services::remove);
    }

    /**
     * Retourne le nombre de chambres disponibles pour un type de chambre donné.
     *
     * @param typeDeChambre Le type de chambre.
     * @return Le nombre de chambres disponibles, 0 si aucun.
     */
    public Integer getChambres(TypeDeChambre typeDeChambre) {
        return chambres.get(typeDeChambre) == null ? 0 : chambres.get(typeDeChambre);
    }

    /**
     * Retourne le prix d'une chambre pour un type de chambre donné.
     *
     * @param typeDeChambre Le type de chambre.
     * @return Le prix de la chambre, 0 si aucun prix n'est défini.
     */
    public Double getPrixChambres(TypeDeChambre typeDeChambre) {
        return prixChambres.get(typeDeChambre) == null ? 0 : prixChambres.get(typeDeChambre);
    }

    /**
     * Définit le prix d'une chambre pour un type de chambre donné.
     *
     * @param typeDeChambre Le type de chambre.
     * @param prix          Le prix de la chambre.
     */
    public void setPrixChambres(TypeDeChambre typeDeChambre, Double prix) {
        prixChambres.put(typeDeChambre, prix);
    }

    /**
     * Ajoute un nombre de chambres pour un type de chambre donné.
     *
     * @param typeDeChambre Le type de chambre.
     * @param nombre        Le nombre de chambres à ajouter.
     */
    public void ajouterChambre(TypeDeChambre typeDeChambre, int nombre) {
        if (nombre <= 0) return;

        if (chambres.containsKey(typeDeChambre)) {
            chambres.put(typeDeChambre, chambres.get(typeDeChambre) + nombre);
        } else {
            chambres.put(typeDeChambre, nombre);
        }
    }

    /**
     * Ajoute une chambre pour un type de chambre donné.
     *
     * @param typeDeChambre Le type de chambre.
     */
    public void ajouterChambre(TypeDeChambre typeDeChambre) {
        ajouterChambre(typeDeChambre, 1);
    }

    /**
     * Supprime un type de chambre de l'hébergement.
     *
     * @param typeDeChambre Le type de chambre à supprimer.
     */
    public void supprimerChambre(TypeDeChambre typeDeChambre) {
        chambres.remove(typeDeChambre);
    }

    @Override
    public String toString() {
        return "Hebergement{" +
                "type=" + type +
                ", pays='" + pays + '\'' +
                ", province='" + province + '\'' +
                ", ville='" + ville + '\'' +
                ", rue='" + rue + '\'' +
                ", services=" + services +
                ", chambres=" + chambres +
                ", prixChambres=" + prixChambres +
                '}';
    }
}
