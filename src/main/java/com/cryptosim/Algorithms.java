package com.cryptosim;

import java.util.*;
import java.util.ArrayList;

public class Algorithms {

    public static String RailFence(String text, int key) {

        if (key <= 1) {
            return text;
        }

        char[] encrypted = new char[text.length()]; // Niz za čuvanje šifrata
        int n = 0;

        for (int k = 0; k < key; k++) {
            int index = k;
            boolean down = true; // Pravac kretanja, dolje ili gore

            while (index < text.length()) {
                encrypted[n++] = text.charAt(index);

                if (k == 0 || k == key - 1) {
                    index += 2 * (key - 1); // Specijalni slučajevi za prvi i posljednji red
                } else {
                    if (down) {
                        index += 2 * (key - k - 1);
                    } else {
                        index += 2 * k;
                    }
                    down = !down; // Promjena pravca
                }
            }
        }

        return new String(encrypted);
    }

    public static String Myszkowski(String text, String key)
    {

        ArrayList<String> list = new ArrayList<>();
        String[] keys = key.split(""); // Razdvajanje ključa u pojedinačne karaktere

        for (String s : keys)
            list.add(s);

        Collections.sort(list);
        list = removeDuplicates(list); // Uklanjanje duplikata (zbog programerske logike)

        int numRows = (int) Math.ceil((double) text.length() / key.length());
        int numCols = key.length();

        String[][] grid = new String[numRows][numCols];
        String[] chars = text.split("");

        List<String> characters = new ArrayList<>();

        StringBuilder encrypted = new StringBuilder();

        for (String s : chars)
            if (!s.equals(" "))
                characters.add(s); // Dodavanje karaktera koji nisu prazna mjesta

        for (int i = 0; i < numRows; i++)
            for (int j = 0 ; j < numCols; j++) {
                if (i * numCols + j < characters.size())
                    grid[i][j] = characters.get(i * numCols + j); // Popunjavanje grid-a
            }

        for (String s : list)
        {

            ArrayList<Integer> indexes = new ArrayList<>();

            int ind = -1;
            while ((ind = key.indexOf(s, ind)) != -1) {
                indexes.add(ind);
                ind++;
            }

            for (int i = 0; i < numRows; i++) {

                for (int idx : indexes) {
                    if(grid[i][idx] != null)
                        encrypted.append(grid[i][idx]);
                }
            }

        }

        return encrypted.toString();
    }

    // Pomoćna metoda za uklanjanje duplikata
    private static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        ArrayList<String> newList = new ArrayList<String>();

        for(String s : list) {
            if (!newList.contains(s)) {
                newList.add(s);
            }
        }

        return newList;
    }

    public static String Playfair(String text, String key) {

        char[][] matrix = generatePlayfairMatrix(key);
        text = preprocessText(text);

        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int[] posA = findPosition(matrix, a); // Pronalaženje pozicije prvog karaktera u matrici
            int[] posB = findPosition(matrix, b); // Pronalaženje pozicije drugog karaktera u matrici

            if (posA[0] == posB[0]) {
                // Ako su u istom redu
                encrypted.append(matrix[posA[0]][(posA[1] + 1) % 5]);
                encrypted.append(matrix[posB[0]][(posB[1] + 1) % 5]);
            } else if (posA[1] == posB[1]) {
                // Ako su u istoj koloni
                encrypted.append(matrix[(posA[0] + 1) % 5][posA[1]]);
                encrypted.append(matrix[(posB[0] + 1) % 5][posB[1]]);
            } else {
                // Ako su u različitim redovima i kolonama
                encrypted.append(matrix[posA[0]][posB[1]]);
                encrypted.append(matrix[posB[0]][posA[1]]);
            }
        }

        return encrypted.toString().toUpperCase();
    }

    // Metoda za generisanje matrice na osnovu ključa
    private static char[][] generatePlayfairMatrix(String key) {
        boolean[] seen = new boolean[26];
        char[][] matrix = new char[5][5];
        int k = 0;

        key = key.toLowerCase().replaceAll("[^a-z]", "").replace('j', 'i');
        for (char c : key.toCharArray()) {
            if (!seen[c - 'a']) {
                matrix[k / 5][k % 5] = c;
                seen[c - 'a'] = true;
                k++;
            }
        }

        for (char c = 'a'; c <= 'z'; c++) {
            if (!seen[c - 'a'] && c != 'j') {
                matrix[k / 5][k % 5] = c;
                seen[c - 'a'] = true;
                k++;
            }
        }
        return matrix;
    }

    // Metoda za prepocesiranje teksta
    private static String preprocessText(String text) {
        text = text.toLowerCase().replaceAll("[^a-z]", "").replace('j', 'i');
        StringBuilder processed = new StringBuilder(text);
        for (int i = 0; i < processed.length() - 1; i += 2) {
            if (processed.charAt(i) == processed.charAt(i + 1)) {
                processed.insert(i + 1, 'x'); // Umetanje 'x' između ponovljenih karaktera
            }
        }
        if (processed.length() % 2 != 0) {
            processed.append('x');
        }
        return processed.toString();
    }

    // Metoda za pronalaženje pozicija karaktera
    private static int[] findPosition(char[][] matrix, char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
}
