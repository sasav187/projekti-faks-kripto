package com.cryptosim;

import java.io.IOException;
import java.util.Scanner;

public class Main {

   public static void main(String args[]) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Dobrodošli u aplikaciju.");
        System.out.println("Molimo vas da odaberete opciju.");

        System.out.println("1. Registracija");
        System.out.println("2. Prijava");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {

            System.out.print("Unesite korisničko ime: ");
            String username = scanner.nextLine();

            System.out.print("Unesite lozinku: ");
            String password = scanner.nextLine();

            User.registerUser(username, password);
        } else if (choice == 2) {

            System.out.print("Unesite putanju do sertifikata: ");
            String certPath = scanner.nextLine();

            System.out.print("Unesite korisničko ime: ");
            String username = scanner.nextLine();

            System.out.print("Unesite lozinku: ");
            String password = scanner.nextLine();

            Authentification auth = new Authentification();

            if (auth.authenticate(certPath, username, password)) {

                System.out.println("Prijava uspješna!");

                SimulationHistory history = new SimulationHistory();
                history.readSimulations(username);

                Menu.displayMenu(username);

            } else {
                System.out.println("Prijava neuspješna!");
            }
        }
   }
}
