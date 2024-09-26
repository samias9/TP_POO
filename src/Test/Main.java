package Test;

import Business.SystemeGestionReservationsImpl;
import Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SystemeGestionReservationsImpl reservationSystem = new SystemeGestionReservationsImpl();

        // Lecture des hébergements depuis un fichier
        List<Hebergement> hebergements = lireHebergementsDepuisFichier("src/Public/hebergements.txt");
        ajouterHebergements(reservationSystem, hebergements);

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
        List<Hebergement> searchResults = rechercherHebergement(reservationSystem, typeHebergement, typeDeChambre, ville, budgetMax);
        traiterResultatsRecherche(reservationSystem, searchResults, dateArrivee, dateDepart, typeDeChambre);
    }

    private static void ajouterHebergements(SystemeGestionReservationsImpl reservationSystem, List<Hebergement> hebergements) {
        int i = 0;
        for (Hebergement hebergement : hebergements) {
            reservationSystem.ajouterLieuHebergement(hebergement);
            System.out.println("Hebergement à " + reservationSystem.getHebergements().get(i).getVille() +
                    " de type " + reservationSystem.getHebergements().get(i).getType() +
                    " qui propose les services suivants: " +
                    reservationSystem.getHebergements().get(i).getServices() + " a été ajouté avec succès :)");
            i++;
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

    private static List<Hebergement> rechercherHebergement(SystemeGestionReservationsImpl reservationSystem, TypeHebergement typeHebergement, TypeDeChambre typeDeChambre, String ville, double budgetMax) {
        return reservationSystem.chercherHebergement(typeHebergement, typeDeChambre, ville, null, null, null, budgetMax);
    }

    private static void traiterResultatsRecherche(SystemeGestionReservationsImpl reservationSystem, List<Hebergement> searchResults, Date dateArrivee, Date dateDepart, TypeDeChambre typeDeChambre) {
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
            Client client = new Client("John Doe", "john@example.com", "samia@gmail.com", "0768090656");
            reservationSystem.ajouterClient(client);

            // Reserve the room
            reservationSystem.reserverChambre(client, hebergementChoisi, dateArrivee, dateDepart, typeDeChambre);
            System.out.println("Maintenant il reste " + hebergementChoisi.getChambres(typeDeChambre));
        }
    }

    private static List<Hebergement> lireHebergementsDepuisFichier(String nomFichier) {
        List<Hebergement> hebergements = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                Hebergement hebergement = construireHebergement(ligne);
                if (hebergement != null) hebergements.add(hebergement);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hebergements;
    }

    private static Hebergement construireHebergement(String ligne) {
        String[] parts = ligne.split(", ");
        if (parts.length < 8) return null;

        TypeHebergement type = TypeHebergement.valueOf(parts[0]);
        String pays = parts[1];
        String province = parts[2];
        String ville = parts[3];
        String rue = parts[4];

        Set<ServicesSupp> services = new HashSet<>();
        for (String service : parts[5].split(";")) {
            services.add(ServicesSupp.valueOf(service));
        }

        Map<TypeDeChambre, Integer> chambres = new HashMap<>();
        for (String chambreStr : parts[6].split(",")) {
            String[] chambreParts = chambreStr.split(":");
            chambres.put(TypeDeChambre.valueOf(chambreParts[0]), Integer.parseInt(chambreParts[1]));
        }

        Map<TypeDeChambre, Double> prixChambres = new HashMap<>();
        for (String prixPart : parts[7].split(",")) {
            String[] prixParts = prixPart.split(":");
            prixChambres.put(TypeDeChambre.valueOf(prixParts[0]), Double.parseDouble(prixParts[1]));
        }

        Hebergement hebergement = new Hebergement(type, pays, province, ville, rue);
        chambres.forEach(hebergement::ajouterChambre);
        hebergement.ajouterServices(new ArrayList<>(services));
        return hebergement;
    }

    public void createData() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("Carchaf", "Samia", "samia.carchaf@gmail.com", "0733556699"));
        clients.add(new Client("Legrix", "Jeremy", "jeremy.legrix@hotmail.com", "0681104817"));
        clients.add(new Client("Le Baron", "Thomas", "thomas.lebaron@gmail.com", "0855261151"));

        List<Hebergement> hebergements = new ArrayList<>();

        Hebergement LeMontagnais = new Hebergement(TypeHebergement.Hotel, "Canada", "Quebec", "Chicoutimi", "1080 Bd Talbot");
        LeMontagnais.ajouterChambre(TypeDeChambre.Suite, 2);
        LeMontagnais.ajouterChambre(TypeDeChambre.Double, 10);
        LeMontagnais.ajouterChambre(TypeDeChambre.Simple, 5);
        LeMontagnais.setPrixChambres(TypeDeChambre.Suite, 249.9);
        LeMontagnais.setPrixChambres(TypeDeChambre.Double, 99.95);
        LeMontagnais.setPrixChambres(TypeDeChambre.Simple, 44.98);
        LeMontagnais.ajouterService(ServicesSupp.Gym);
        LeMontagnais.ajouterService(ServicesSupp.Restaurant);
        LeMontagnais.ajouterService(ServicesSupp.Stationnement);
        LeMontagnais.ajouterService(ServicesSupp.Piscine);

        Hebergement LeFjord = new Hebergement(TypeHebergement.Hotel, "Canada", "Quebec", "Chicoutimi", "241 Rue Morin");
        LeFjord.ajouterChambre(TypeDeChambre.Double, 9);
        LeFjord.ajouterChambre(TypeDeChambre.Simple, 5);
        LeFjord.setPrixChambres(TypeDeChambre.Double, 59.9);
        LeFjord.setPrixChambres(TypeDeChambre.Double, 49.9);
        LeFjord.ajouterService(ServicesSupp.Restaurant);
        LeFjord.ajouterService(ServicesSupp.Stationnement);

        Hebergement Ramada = new Hebergement(TypeHebergement.Hotel, "Canada", "Quebec", "Montreal", "6445 Bd Décarie");
        Ramada.ajouterChambre(TypeDeChambre.Double, 8);
        Ramada.setPrixChambres(TypeDeChambre.Double, 50.9);
        Ramada.ajouterService(ServicesSupp.Stationnement);

        Hebergement Motel = new Hebergement(TypeHebergement.Motel, "Canada", "Quebec", "Montreal", "6500 Pl. Robert Joncas");
        Motel.ajouterChambre(TypeDeChambre.Simple, 7);
        Motel.setPrixChambres(TypeDeChambre.Simple, 38.9);
        Motel.ajouterService(ServicesSupp.Stationnement);

        Hebergement AubergeSaintLo = new Hebergement(TypeHebergement.COUETTE_ET_CAFE, "Canada", "Quebec", "Montreal", "1030 Rue Mackay");
        AubergeSaintLo.ajouterChambre(TypeDeChambre.Simple, 50);
        AubergeSaintLo.setPrixChambres(TypeDeChambre.Simple, 60.9);
        AubergeSaintLo.ajouterService(ServicesSupp.Stationnement);

        hebergements.add(AubergeSaintLo);
        hebergements.add(LeFjord);
        hebergements.add(Ramada);
        hebergements.add(Motel);
        hebergements.add(LeMontagnais);
    }
}
