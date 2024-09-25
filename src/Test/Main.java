package Test;

import Business.SystemeGestionReservationsImpl;
import Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        SystemeGestionReservationsImpl reservationSystem = new SystemeGestionReservationsImpl();
        List<Hebergement> hebergements = lireHebergementsDepuisFichier("/Users/carchaf/Downloads/hebergements.txt");
        int i=0;

        for (Hebergement hebergement : hebergements) {
            reservationSystem.ajouterLieuHebergement(hebergement);
            System.out.println(" Hebergement à "+ reservationSystem.getHebergements().get(i).getVille()+" de type "+ reservationSystem.getHebergements().get(i).getType()+" qui propose les services suivants: "+reservationSystem.getHebergements().get(i).getServices()+ " qui contient " +" a été ajouté avec succés :)");
            i++;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Quel type d'hébergement souhaitez-vous réserver ? (Hotel/Motel)");
        String typeHebergementStr = scanner.nextLine().trim();
        System.out.println("Vous avez choisis :   "+typeHebergementStr);

        TypeHebergement typeHebergement;
        try {
            typeHebergement = TypeHebergement.valueOf(typeHebergementStr);
            System.out.println("Process . . . "+typeHebergement);
        } catch (IllegalArgumentException e) {
            System.out.println("Type d'hébergement non valide.");
            return;
        }

        System.out.println("Dans quelle ville souhaitez-vous réserver ?");
        String ville = scanner.nextLine().trim();
        System.out.println("Ville Choisie : "+ville);

        System.out.println("Quel type de chambre souhaitez-vous ? (Simple/Double/Suite)");
        String typeChambreStr = scanner.nextLine().trim();
        System.out.println("Type de chambre choisie : "+typeChambreStr);

        TypeDeChambre typeDeChambre;
        try {
            typeDeChambre = TypeDeChambre.valueOf(typeChambreStr);
            System.out.println("Process . . . "+typeDeChambre);
        } catch (IllegalArgumentException e) {
            System.out.println("Type de chambre non valide.");
            return;
        }

        System.out.println("Avez-vous un budget maximum ? (en euros)");
        double budgetMax;
        try {
            budgetMax = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Budget non valide.");
            return;
        }

        // Search accommodations based on user input
        List<Hebergement> searchResults = reservationSystem.chercherHebergement(typeHebergement, typeDeChambre,ville, null, null, null, budgetMax);

        if (searchResults.isEmpty()) {
            System.out.println("Aucun hébergement disponible correspondant à vos critères.");
        } else {
            System.out.println("Hébergements disponibles :");
            for (Hebergement hebergement : searchResults) {
                System.out.println(hebergement.getType() + " à " + hebergement.getVille() + " - Rue: " + hebergement.getRue());
            }

            // Assume the user chooses the first available accommodation
            Hebergement hebergementChoisi = searchResults.get(0);

            // Get the current date for reservation
            Calendar calendar = Calendar.getInstance();
            calendar.set(2024, Calendar.OCTOBER, 1); // Arrival date
            Date dateArrivee = calendar.getTime();

            calendar.set(2024, Calendar.OCTOBER, 5); // Departure date
            Date dateDepart = calendar.getTime();

            // Create client (assuming this is a new client)
            Client client = new Client("John Doe", "john@example.com", "samia@gmail.com", "0768090656");
            reservationSystem.ajouterClient(client);

            // Reserve the room
            reservationSystem.reserverChambre(client, hebergementChoisi, dateArrivee, dateDepart, typeDeChambre);
        }
    }

    private static List<Hebergement> lireHebergementsDepuisFichier(String nomFichier) {
        List<Hebergement> hebergements = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;

            while ((ligne = br.readLine()) != null) {

                String[] parts = ligne.split(", ");

                TypeHebergement type = TypeHebergement.valueOf(parts[0]);
                String pays = parts[1];
                String province = parts[2];
                String ville = parts[3];
                String rue = parts[4];

                String[] servicesStr = parts[5].split(";");
                Set<ServicesSupp> services = new HashSet<>();
                for (String service : servicesStr) {
                    services.add(ServicesSupp.valueOf(service));
                }

                String[] chambresStr = parts[6].split(",");
                Map<TypeDeChambre, Integer> chambres = new HashMap<>();
                for (String chambreStr : chambresStr) {
                    String[] chambreParts = chambreStr.split(":");
                    TypeDeChambre typeDeChambre = TypeDeChambre.valueOf(chambreParts[0]);
                    int nombre = Integer.parseInt(chambreParts[1]);
                    chambres.put(typeDeChambre, nombre);
                }

                String[] prixStr = parts[7].split(",");
                Map<TypeDeChambre, Double> prixChambres = new HashMap<>();
                for (String prixPart : prixStr) {
                    String[] prixParts = prixPart.split(":");
                    TypeDeChambre typeDeChambre = TypeDeChambre.valueOf(prixParts[0]);
                    double prix = Double.parseDouble(prixParts[1]);
                    prixChambres.put(typeDeChambre, prix);
                }

                Hebergement hebergement = new Hebergement(type, pays, province, ville, rue);
                for (TypeDeChambre typeDeChambre : chambres.keySet()) {
                    hebergement.ajouterChambre(typeDeChambre, chambres.get(typeDeChambre));
                }
                hebergement.ajouterServices(new ArrayList<>(services));
                hebergements.add(hebergement);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hebergements;
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