package Test;

import Business.SystemeGestionReservationsImpl;
import Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Hebergement> hebergements = lireHebergementsDepuisFichier("/Users/carchaf/Downloads/hebergements.txt");

        SystemeGestionReservationsImpl reservationSystem = new SystemeGestionReservationsImpl();

        for (Hebergement hebergement : hebergements) {
            reservationSystem.ajouterLieuHebergement(hebergement);
        }

        Client client = new Client("John Doe", "john@example.com", "samia@gmail.com", "0768090656");
        reservationSystem.ajouterClient(client);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Quel Type d'Hebergement? \n" +
                "A: Hotel \n" +
                "B: Couette Et Cafe \n" +
                "C: Motel \n");

        String choixTypeHebergement = scanner.nextLine().trim();
        TypeHebergement typeHebergement = null;

        switch (choixTypeHebergement.toUpperCase()) {
            case "A":
                typeHebergement = TypeHebergement.Hotel;
                break;
            case "B":
                typeHebergement = TypeHebergement.COUETTE_ET_CAFE;
                break;
            case "C":
                typeHebergement = TypeHebergement.Motel;
                break;
            default:
                System.out.println("Type d'hébergement non valide.");
                return;
        }

        System.out.println("Dans quelle ville souhaitez-vous réserver?");
        String villeRecherche = scanner.nextLine().trim();
        System.out.println("La ville que vous avez choisie : " + villeRecherche);

        System.out.println("Quel type de chambre? \n" +
                "1: Simple \n" +
                "2: Double \n" +
                "3: Suite \n");

        String choixTypeChambre = scanner.nextLine().trim();
        TypeDeChambre typeDeChambreRecherche;
        switch (choixTypeChambre) {
            case "1":
                typeDeChambreRecherche = TypeDeChambre.Simple;
                break;
            case "2":
                typeDeChambreRecherche = TypeDeChambre.Double;
                break;
            case "3":
                typeDeChambreRecherche = TypeDeChambre.Suite;
                break;
            default:
                System.out.println("Type de chambre non valide.");
                return;
        }

        System.out.println("Quels services spécifiques souhaitez-vous (séparés par des virgules) ?\n" +
                "Options : piscine, cuisinette, salle_de_conditionnement_physique, stationnement, accès_handicapé, dépanneur, restaurant");
        String choixServices = scanner.nextLine().trim();
        System.out.println("Les services que vous avez choisis : " + choixServices);

        List<String> servicesRecherches = Arrays.asList(choixServices.split(","));
        servicesRecherches.replaceAll(String::trim);

        System.out.println("Avez-vous un budget maximum ? (oui/non)");
        String choixBudget = scanner.nextLine().trim();

        double budgetMax = Double.MAX_VALUE;
        if (choixBudget.equalsIgnoreCase("oui")) {
            System.out.println("Veuillez indiquer votre budget maximum (en euros) :");
            String budgetMaxStr = scanner.nextLine().trim();
            try {
                budgetMax = Double.parseDouble(budgetMaxStr);
            } catch (NumberFormatException e) {
                System.out.println("Budget non valide. Utilisation du budget illimité.");
            }
        }

        List<Hebergement> hebergementsFiltres = reservationSystem.filterByTypeHebergement(typeHebergement);
        List<Hebergement> hebergementsDisponibles = new ArrayList<>();

        for (Hebergement hebergement : hebergementsFiltres) {
            if (hebergement.getVille().equalsIgnoreCase(villeRecherche)) {
                List<Chambre> chambresCompatibles = new ArrayList<>();

                for (Chambre chambre : hebergement.getChambres()) {
                    if (chambre.getTypeDeChambre() == typeDeChambreRecherche && chambre.getDisponibilite()) {
                        boolean correspondAuxServices = true;
                        System.out.println("Available services for this accommodation: " + hebergement.getServicesDisponibles());

                        for (String service : servicesRecherches) {
                            try {
                                String normalizedService = service.trim();
                                if (normalizedService.contains("_")) {
                                    normalizedService = normalizedService.replace("_", "");
                                }

                                if (!hebergement.getServicesDisponibles().contains(ServicesSupp.valueOf(normalizedService))) {
                                    correspondAuxServices = false;
                                    System.out.println("Service requested but not available: " + service);
                                    break;
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid service input: " + service);
                                correspondAuxServices = false;
                                break;
                            }
                        }

                        if (correspondAuxServices && chambre.getPrix() <= budgetMax) {
                            chambresCompatibles.add(chambre);
                        }
                    }
                }

                if (!chambresCompatibles.isEmpty()) {
                    Hebergement hebergementFiltre = new Hebergement(
                            hebergement.getType(),
                            chambresCompatibles,
                            hebergement.getPays(),
                            hebergement.getProvince(),
                            hebergement.getVille(),
                            hebergement.getRue(),
                            hebergement.getServicesDisponibles()
                    );
                    hebergementsDisponibles.add(hebergementFiltre);
                }
            }
        }

        if (!hebergementsDisponibles.isEmpty()) {
            System.out.println("Hébergements disponibles :");
            for (Hebergement hebergement : hebergementsDisponibles) {
                System.out.println(hebergement);
            }

            Hebergement hebergementChoisi = hebergementsDisponibles.get(0);
            Chambre chambreChoisie = hebergementChoisi.getChambres().get(0);

            LocalDate dateArrivee = LocalDate.of(2024, 10, 1);
            LocalDate dateDepart = LocalDate.of(2024, 10, 5);
            Reservation reservation = new Reservation(client, LocalDate.now(), chambreChoisie, dateArrivee, dateDepart);
            reservationSystem.effectuerReservation(reservation);

            System.out.println("Réservation confirmée :");
            System.out.println(reservation);

            System.out.println("ici : " + reservationSystem.getReservations().get(0).getClient());
        } else {
            System.out.println("Aucun hébergement disponible correspondant à vos critères.");
        }
    }

    private static List<Hebergement> lireHebergementsDepuisFichier(String nomFichier) {
        List<Hebergement> hebergements = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.startsWith("#") || ligne.trim().isEmpty()) {
                    continue;
                }

                String[] parties = ligne.split(", ");
                String typeHebergementStr = parties[1];
                TypeHebergement typeHebergement = TypeHebergement.valueOf(typeHebergementStr);

                String[] chambresStr = parties[2].split("; ");
                List<Chambre> chambres = new ArrayList<>();
                for (String chambreStr : chambresStr) {
                    String[] chambreDetails = chambreStr.split("\\|");
                    int numChambre = Integer.parseInt(chambreDetails[0]);
                    TypeDeChambre typeDeChambre = TypeDeChambre.valueOf(chambreDetails[1]);
                    double prix = Double.parseDouble(chambreDetails[2]);
                    boolean disponibilite = Boolean.parseBoolean(chambreDetails[3]);
                    chambres.add(new Chambre(numChambre, typeDeChambre, prix, disponibilite));
                }

                String[] servicesStr = parties[7].split(",");
                List<ServicesSupp> services = new ArrayList<>();
                for (String service : servicesStr) {
                    try {
                        services.add(ServicesSupp.valueOf(service.trim()));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Service non reconnu : " + service.trim());
                    }
                }

                Hebergement hebergement = new Hebergement(
                        typeHebergement,
                        chambres,
                        parties[3],
                        parties[4],
                        parties[5],
                        parties[6],
                        services
                );
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
