package Test;

import Business.SystemeGestionReservations;
import Business.SystemeGestionReservationsImpl;
import Model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    static private final SystemeGestionReservations systemeGestionReservations = new SystemeGestionReservationsImpl();

    public static void main(String[] args) {
        // Creation des données
        createData();

        while(true) {
            // Saisie des informations de réservation par l'utilisateur
            Scanner scanner = new Scanner(System.in);
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
    }

    private static TypeHebergement saisirTypeHebergement(Scanner scanner) {
        System.out.println("Quel type d'hébergement souhaitez-vous réserver ? (Hotel/Motel)");
        String typeHebergementStr = scanner.nextLine().trim();
        typeHebergementStr = typeHebergementStr.substring(0, 1).toUpperCase() + typeHebergementStr.substring(1).toLowerCase();

        try {
            return TypeHebergement.valueOf(typeHebergementStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Type d'hébergement non valide.");
            return null;
        }
    }

    private static String saisirVille(Scanner scanner) {
        System.out.println("Dans quelle ville souhaitez-vous réserver ?");
        return scanner.nextLine().trim();
    }

    private static String saisirNom(Scanner scanner) {
        System.out.println("Quel est votre nom ?");
        return scanner.nextLine().trim();
    }

    private static String saisirPrenom(Scanner scanner) {
        System.out.println("Quelle est votre prénom ?");
        return scanner.nextLine().trim();
    }

    private static String saisirTel(Scanner scanner) {
        System.out.println("Quelle est votre numéro de téléphone ?");
        return scanner.nextLine().trim();
    }

    private static String saisirCourriel(Scanner scanner) {
        System.out.println("Quelle est votre adresse mail ?");
        return scanner.nextLine().trim();
    }

    private static TypeDeChambre saisirTypeChambre(Scanner scanner) {
        System.out.println("Quel type de chambre souhaitez-vous ? (Simple/Double/Suite)");
        String typeChambreStr = scanner.nextLine().trim();
        try {
            return TypeDeChambre.valueOf(typeChambreStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Type de chambre non valide.");
            return null;
        }
    }

    private static double saisirBudgetMax(Scanner scanner) {
        System.out.println("Avez-vous un budget maximum ? (en euros)");
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Budget non valide.");
            return -1;
        }
    }

    private static Date[] saisirDates(Scanner scanner) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            System.out.println("Veuillez entrer la date d'arrivée (format: yyyy-MM-dd): ");
            Date dateArrivee = dateFormat.parse(scanner.nextLine().trim());

            System.out.println("Veuillez entrer la date de départ (format: yyyy-MM-dd): ");
            Date dateDepart = dateFormat.parse(scanner.nextLine().trim());

            if (dateDepart.before(dateArrivee)) {
                System.out.println("La date de départ ne peut pas être antérieure à la date d'arrivée.");
                return null;
            }

            return new Date[]{dateArrivee, dateDepart};
        } catch (ParseException e) {
            System.out.println("Format de date invalide.");
            return null;
        }
    }

    private static void traiterResultatsRecherche(Scanner scanner, List<Hebergement> searchResults, Date dateArrivee, Date dateDepart, TypeDeChambre typeDeChambre) {
        if (searchResults.isEmpty()) {
            System.out.println("Aucun hébergement disponible correspondant à vos critères.");
        } else {
            System.out.println("Hébergements disponibles :");
            for (Hebergement hebergement : searchResults) {
                System.out.println(hebergement.getType() + " à " + hebergement.getVille() + " - Rue: " + hebergement.getRue());
            }

            // Assume the user chooses the first available accommodation
            Hebergement hebergementChoisi = searchResults.get(0);

            // Create client (assuming this is a new client)
            String nom = saisirNom(scanner);
            String prenom = saisirPrenom(scanner);
            String courriel = saisirCourriel(scanner);
            String tel = saisirTel(scanner);

            Client client = new Client(nom, prenom, courriel, tel);

            systemeGestionReservations.ajouterClient(client);

            // Reserve the room
            systemeGestionReservations.reserverChambre(client, hebergementChoisi, dateArrivee, dateDepart, typeDeChambre);
        }
    }

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
