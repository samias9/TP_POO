package Test;

import Business.SystemeGestionReservations;
import Business.SystemeGestionReservationsImpl;
import Model.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Classe principale du système de gestion de réservations. 
 * Cette classe permet à l'utilisateur de rechercher et de réserver des hébergements 
 * ou d'annuler des réservations existantes via une interface de terminal.
 */
public class Main {
    static private final SystemeGestionReservations systemeGestionReservations = new SystemeGestionReservationsImpl();

    public static void main(String[] args) {
        // Creation des données
        createData();


        afficherTousLesHebergements();

        while(true) {
            // Saisie des informations de réservation par l'utilisateur
            Scanner scanner = new Scanner(System.in);

            boolean saisirTypeDemande = saisirTypeDemande(scanner);

            if(saisirTypeDemande){
                TypeHebergement typeHebergement = saisirTypeHebergement(scanner);
                if (typeHebergement == null) return;

                String ville = saisirVille(scanner);
                TypeDeChambre typeDeChambre = saisirTypeChambre(scanner);
                if (typeDeChambre == null) return;

                double budgetMax = saisirBudgetMax(scanner);
                if (budgetMax < 0) return;

                Date[] dates = saisirDates(scanner);
                if (dates == null) return;
                Date dateArrivee = dates[0];
                Date dateDepart = dates[1];

                // Recherche et affichage des résultats
                List<Hebergement> searchResults = systemeGestionReservations.chercherHebergement(typeHebergement, typeDeChambre, ville, null, null, null, budgetMax, dateArrivee, dateDepart);
                traiterResultatsRecherche(scanner, searchResults, dateArrivee, dateDepart, typeDeChambre);
            }
            else{
                traiterAnnulation(scanner);
            }
        }
            
    }
    /**
     * Affiche tous les hébergements disponibles dans le système.
     */
    private static void afficherTousLesHebergements() {
        System.out.println("Liste des hébergements disponibles :");
        List<Hebergement> hebergements = systemeGestionReservations.getTousLesHebergements();
        for (Hebergement hebergement : hebergements) {
            System.out.printf("Type: %s, Ville: %s, Rue: %s, Chambres disponibles: [Simple: %d, Double: %d, Suite: %d], Prix: [Simple: %.2f€, Double: %.2f€, Suite: %.2f€]\n",
                    hebergement.getType(),
                    hebergement.getVille(),
                    hebergement.getRue(),
                    hebergement.getChambres(TypeDeChambre.Simple),
                    hebergement.getChambres(TypeDeChambre.Double),
                    hebergement.getChambres(TypeDeChambre.Suite),
                    hebergement.getPrixChambres(TypeDeChambre.Simple),
                    hebergement.getPrixChambres(TypeDeChambre.Double),
                    hebergement.getPrixChambres(TypeDeChambre.Suite));
        }
    }

    /**
     * Saisit le type de demande : Réservation ou Annulation.
     *
     * @param scanner Le scanner pour lire l'entrée utilisateur.
     * @return true si l'utilisateur choisit de réserver, false pour annuler.
     */
    private static boolean saisirTypeDemande(Scanner scanner) {
        System.out.println("Bonjour! \n Que voulez-vous faire? (A/B) \n A. Réserver \n B. Annuler");
        String choix = scanner.nextLine().trim();
        return !"B".equalsIgnoreCase(choix);  // Retourne false si l'utilisateur choisit "B" ou "b".
    }

    /**
     * Gère l'annulation d'une réservation pour un client donné.
     *
     * @param scanner Scanner pour lire les informations de l'utilisateur.
     */
    private static void traiterAnnulation(Scanner scanner) {
        String nom = saisirNom(scanner);
        String prenom = saisirPrenom(scanner);
    
        Client client = systemeGestionReservations.chercherClient(nom, prenom);
        if (client == null) {
            System.out.println("Client introuvable.");
            return;
        }

        // Chercher toutes les réservations du client
        List<Reservation> reservations = systemeGestionReservations.chercherReservationsParClient(client);
    
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation trouvée pour ce client.");
        } else {
            System.out.println("Réservations trouvées :");
            for (int i = 0; i < reservations.size(); i++) {
                Reservation reservation = reservations.get(i);
                System.out.printf("%d. Hébergement: %s, Chambre: %s, Date d'arrivée: %s, Date de départ: %s\n",
                        i + 1,
                        reservation.getHebergement().getVille(),
                        reservation.getTypeDeChambre(),
                        reservation.getDateDebut(),
                        reservation.getDateFin());
            }
    
            System.out.println("Entrez le numéro de la réservation que vous souhaitez annuler :");
            int index = scanner.nextInt();
            scanner.nextLine(); 
    
            if (index > 0 && index <= reservations.size()) {
                Reservation reservationToCancel = reservations.get(index - 1);
                System.out.println("Voulez-vous vraiment annuler la réservation? (oui/non)");
                String reponse = scanner.nextLine();
    
                if (reponse.equalsIgnoreCase("oui")) {
                    systemeGestionReservations.annulerReservation(reservationToCancel);
                    System.out.println("Réservation annulée avec succès.");
                } else {
                    System.out.println("Annulation annulée.");
                }
            } else {
                System.out.println("Numéro de réservation invalide.");
            }
        }
    }

    /**
     * Saisie du type d'hébergement pour la réservation.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Le type d'hébergement sélectionné.
     */
    private static TypeHebergement saisirTypeHebergement(Scanner scanner) {
        System.out.println("Quel type d'hébergement souhaitez-vous réserver ? (Hotel/Motel)");
        String typeHebergementStr = scanner.nextLine().trim();
        typeHebergementStr = typeHebergementStr.substring(0, 1).toUpperCase() + typeHebergementStr.substring(1).toLowerCase();

        try {
            return TypeHebergement.valueOf(typeHebergementStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Type d'hébergement non valide.");
            return saisirTypeHebergement(scanner);
        }
    }

     /**
     * Saisie de la ville de l'hébergement à réserver.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return La ville saisie par l'utilisateur.
     */
    private static String saisirVille(Scanner scanner) {
        System.out.println("Dans quelle ville souhaitez-vous réserver ?");
        return scanner.nextLine().trim();
    }

    /**
     * Saisie du nom du client.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Le nom du client.
     */
    private static String saisirNom(Scanner scanner) {
        System.out.println("Quel est votre nom ?");
        return scanner.nextLine().trim();
    }

    /**
     * Saisie du prénom du client.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Le prénom du client.
     */
    private static String saisirPrenom(Scanner scanner) {
        System.out.println("Quelle est votre prénom ?");
        return scanner.nextLine().trim();
    }

    /**
     * Saisie du numéro de téléphone du client.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Le numéro de téléphone du client.
     */
    private static String saisirTel(Scanner scanner) {
        System.out.println("Quelle est votre numéro de téléphone ?");
        return scanner.nextLine().trim();
    }

    /**
     * Saisie du courriel du client.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Le courriel du client.
     */
    private static String saisirCourriel(Scanner scanner) {
        System.out.println("Quelle est votre adresse mail ?");
        return scanner.nextLine().trim();
    }

    /**
     * Saisie du type de la chambre pour la réservation.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Le type de chambre sélectionné.
     */
    private static TypeDeChambre saisirTypeChambre(Scanner scanner) {
        System.out.println("Quel type de chambre souhaitez-vous ? (Simple/Double/Suite)");
        String typeChambreStr = scanner.nextLine().trim();
        try {
            return TypeDeChambre.valueOf(typeChambreStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Type de chambre non valide.");
            return saisirTypeChambre(scanner);
        }
    }

    /**
     * Saisie du budget maximal pour la réservation.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Le budget maximum en euros.
     */
    private static double saisirBudgetMax(Scanner scanner) {
        System.out.println("Avez-vous un budget maximum ? ");
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Budget non valide.");
            return saisirBudgetMax(scanner);
        }
    }

    /**
     * Saisie des dates d'arrivée et de départ pour la réservation.
     *
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Un tableau contenant la date d'arrivée et la date de départ.
     */
    private static Date[] saisirDates(Scanner scanner) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            System.out.println("Veuillez entrer la date d'arrivée (format: yyyy-MM-dd): ");
            Date dateArrivee = dateFormat.parse(scanner.nextLine().trim());

            System.out.println("Veuillez entrer la date de départ (format: yyyy-MM-dd): ");
            Date dateDepart = dateFormat.parse(scanner.nextLine().trim());

            if (dateDepart.before(dateArrivee)) {
                System.out.println("La date de départ ne peut pas être antérieure à la date d'arrivée.");
                return saisirDates(scanner);
            }

            return new Date[]{dateArrivee, dateDepart};
        } catch (ParseException e) {
            System.out.println("Format de date invalide.");
            return saisirDates(scanner);
        }
    }

    /**
     * Sélection de l'hébergement à partir de la liste des résultats de recherche.
     *
     * @param scanner        Scanner pour lire l'entrée utilisateur.
     * @param searchResults  Liste des hébergements disponibles.
     * @return L'hébergement sélectionné par l'utilisateur.
     */
    private static Hebergement saisirHebergement(Scanner scanner, List<Hebergement> searchResults) {
        System.out.println("Hébergements disponibles :");
        for (int i = 0; i < searchResults.size(); i++) {
            Hebergement hebergement = searchResults.get(i);
            System.out.println(i+1 + ". " + hebergement.getType() + " à " + hebergement.getVille() + " - Rue: " + hebergement.getRue());
        }

        String hebergementStr = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(hebergementStr);

            return searchResults.get(index);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Index non valide.");
            return saisirHebergement(scanner, searchResults);
        }
    }

    /**
     * Gère les résultats de recherche d'hébergements et permet à l'utilisateur de réserver un hébergement.
     *
     * @param scanner       Scanner pour lire l'entrée utilisateur.
     * @param searchResults Liste des hébergements disponibles.
     * @param dateArrivee   Date d'arrivée.
     * @param dateDepart    Date de départ.
     * @param typeDeChambre Le type de chambre à réserver.
     */
    private static void traiterResultatsRecherche(Scanner scanner, List<Hebergement> searchResults, Date dateArrivee, Date dateDepart, TypeDeChambre typeDeChambre) {
        if (searchResults.isEmpty()) {
            System.out.println("Aucun hébergement disponible correspondant à vos critères.");
        } else {
            Hebergement hebergement = saisirHebergement(scanner, searchResults);

            String courriel = saisirCourriel(scanner);

            Client client = systemeGestionReservations.rechercherClientParEmail(courriel);

            if (client == null) {
                String nom = saisirNom(scanner);
                String prenom = saisirPrenom(scanner);
                String tel = saisirTel(scanner);

                client = new Client(nom, prenom, courriel, tel);
            }

            systemeGestionReservations.ajouterClient(client);

            systemeGestionReservations.reserverChambre(client, hebergement, dateArrivee, dateDepart, typeDeChambre);
        }
    }


    /**
     * Crée et ajoute des données d'hébergement au système.
     */
    public static void createData() {
        List<Hebergement> hebergements = new ArrayList<>();

        Hebergement LeMontagnais = new Hebergement(TypeHebergement.Hotel, "Canada", "Quebec", "Chicoutimi", "1080 Bd Talbot");
        LeMontagnais.ajouterChambre(TypeDeChambre.Suite, 1);
        LeMontagnais.ajouterChambre(TypeDeChambre.Double, 1);
        LeMontagnais.ajouterChambre(TypeDeChambre.Simple, 1);
        LeMontagnais.setPrixChambres(TypeDeChambre.Suite, 249.9);
        LeMontagnais.setPrixChambres(TypeDeChambre.Double, 99.95);
        LeMontagnais.setPrixChambres(TypeDeChambre.Simple, 44.98);
        LeMontagnais.ajouterService(ServicesSupp.Gym);
        LeMontagnais.ajouterService(ServicesSupp.Restaurant);
        LeMontagnais.ajouterService(ServicesSupp.Stationnement);
        LeMontagnais.ajouterService(ServicesSupp.Piscine);

        Hebergement LeFjord = new Hebergement(TypeHebergement.Hotel, "Canada", "Quebec", "Chicoutimi", "241 Rue Morin");
        LeFjord.ajouterChambre(TypeDeChambre.Double, 1);
        LeFjord.ajouterChambre(TypeDeChambre.Simple, 1);
        LeFjord.setPrixChambres(TypeDeChambre.Double, 59.9);
        LeFjord.setPrixChambres(TypeDeChambre.Double, 49.9);
        LeFjord.ajouterService(ServicesSupp.Restaurant);
        LeFjord.ajouterService(ServicesSupp.Stationnement);

        Hebergement Ramada = new Hebergement(TypeHebergement.Hotel, "Canada", "Quebec", "Montreal", "6445 Bd Décarie");
        Ramada.ajouterChambre(TypeDeChambre.Double, 1);
        Ramada.setPrixChambres(TypeDeChambre.Double, 50.9);
        Ramada.ajouterService(ServicesSupp.Stationnement);

        Hebergement Motel = new Hebergement(TypeHebergement.Motel, "Canada", "Quebec", "Montreal", "6500 Pl. Robert Joncas");
        Motel.ajouterChambre(TypeDeChambre.Simple, 1);
        Motel.setPrixChambres(TypeDeChambre.Simple, 38.9);
        Motel.ajouterService(ServicesSupp.Stationnement);

        Hebergement AubergeSaintLo = new Hebergement(TypeHebergement.COUETTE_ET_CAFE, "Canada", "Quebec", "Montreal", "1030 Rue Mackay");
        AubergeSaintLo.ajouterChambre(TypeDeChambre.Simple, 1);
        AubergeSaintLo.setPrixChambres(TypeDeChambre.Simple, 60.9);
        AubergeSaintLo.ajouterService(ServicesSupp.Stationnement);

        hebergements.add(AubergeSaintLo);
        hebergements.add(LeFjord);
        hebergements.add(Ramada);
        hebergements.add(Motel);
        hebergements.add(LeMontagnais);

        hebergements.forEach(systemeGestionReservations::ajouterLieuHebergement);
    }
}