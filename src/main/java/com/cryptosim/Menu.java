package com.cryptosim;

import java.util.Scanner;

public class Menu {

    public static void displayMenu(String username) {

        int choice;

        do {

            System.out.println("Odaberite algoritam za enkripciju:");
            System.out.println("1. Rail Fence");
            System.out.println("2. Myszkowski");
            System.out.println("3. Playfair");
            System.out.println("4. Izlaz");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Unesite opciju: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            SimulationHistory history = new SimulationHistory();

            if (choice == 1) {

                System.out.print("Unesite tekst za enkripciju: ");
                String text = scanner.nextLine();
                String encrypted = "";
                System.out.print("Unesite ključ: ");
                int key = scanner.nextInt();
                scanner.nextLine();
                encrypted = Algorithms.RailFence(text, key);

                System.out.println("Šifrat: " + encrypted);

                try {
                    history.saveSimulation(username, text, "Rail Fence", String.valueOf(key), encrypted);
                } catch (Exception e) {
                    System.out.println("Greška pri čuvanju Rail Fence simulacije.");
                }
            } else if (choice == 2) {

                System.out.print("Unesite tekst za enkripciju: ");
                String text = scanner.nextLine();
                String encrypted = "";
                System.out.print("Unesite ključ: ");
                String key = scanner.nextLine();
                encrypted = Algorithms.Myszkowski(text, key);

                System.out.println("Šifrat: " + encrypted);

                try {
                    history.saveSimulation(username, text, "Myszkowski", String.valueOf(key), encrypted);
                } catch (Exception e) {
                    System.out.println("Greška pri čuvanju Myszkowski simulacije.");
                }
            } else if (choice == 3) {

                System.out.print("Unesite tekst za enkripciju: ");
                String text = scanner.nextLine();
                String encrypted = "";

                System.out.print("Unesite ključ: ");
                String key = scanner.nextLine();
                encrypted = Algorithms.Playfair(text, key);

                System.out.println("Šifrat: " + encrypted);

                try {
                    history.saveSimulation(username, text, "Playfair", String.valueOf(key), encrypted);
                } catch (Exception e) {
                    System.out.println("Greška pri čuvanju Playfair simulacije.");
                }
            } else if (choice == 4) {
                System.out.println("Izlaz iz aplikacije.");
            } else {
                System.out.println("Nepostojeća opcija.");
            }

        } while (choice != 4);
    }
}
