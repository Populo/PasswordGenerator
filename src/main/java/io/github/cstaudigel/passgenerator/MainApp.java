package io.github.cstaudigel.passgenerator;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainApp {

    private final double VERSION = 1.0;
    private final int MAXLENGTH = 128;
    private final int MAXGEN = 5;

    private String[] generatedPasswords;
    private int passLen;
    private boolean willContinue;
    private List<String[]> pastGenerations;

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.run();
    }

    public MainApp() {
        willContinue = true;
        generatedPasswords = new String[5];
        passLen = 15;
        pastGenerations = new ArrayList<>();
    }

    public void run() {
        System.out.println("################# Password Generator v" + VERSION + " ##############");
        Scanner in = new Scanner(System.in);
        String response = "";
        while (wantsToContinue()) {
            System.out.println("how long should the passwords be?");
            System.out.println("Leave blank for default (15)");
            System.out.print("> ");
            response = in.nextLine();

            try {
                passLen = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                System.out.println("Using default 15.");
            }

            if (passLen > MAXLENGTH) {
                passLen = MAXLENGTH;
                System.out.println("Defaulting to max (" + MAXLENGTH + ")");
            } else if (passLen <= 0) {
                passLen = 15;
                System.out.println("Using default 15.");
            }

            generatePasswords();

            System.out.print("\nWould you like to regenerate? (y/n): ");
            response = in.nextLine();
            if(response.toCharArray()[0] == 'n' || response.toCharArray()[0] == 'N') {
                setContinue(false);
            } else {
                System.out.print("\n");
            }
        }

        System.out.println("Would you like to print generations to a file? (y/n)");
        System.out.print("> ");
        response = in.nextLine();
        if (response.toCharArray()[0] == 'y' || response.toCharArray()[0] == 'Y') {
            printToFile();
        }
    }

    private void printToFile() {
        String fileName = "generatedpasswords-" + LocalDateTime.now() + ".txt";
        String toWrite = "";

        for(String[] groups : pastGenerations) {
            for (String s : groups) {
                toWrite += s + "\n";
            }
            toWrite += "\n";
        }

        try {
            FileWriter outFile = new FileWriter(fileName);
            outFile.write(toWrite);
            outFile.close();
            System.out.println("Wrote generated passwords to file called " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean wantsToContinue() {
        return willContinue;
    }

    private void setContinue(boolean cont) {
        willContinue = cont;
    }

    private void generatePasswords() {

        for(int i = 0; i < MAXGEN; i++) {
            String pass = randomPass();
            System.out.print("" + (i+1) + ": ");
            System.out.println(pass);
            generatedPasswords[i] = pass;
        }

        pastGenerations.add(generatedPasswords.clone());
    }

    private String randomPass() {
        Random r = new Random();

        String pass = "";
        short i = 0;
        short last = 0;

        while(i < passLen) {
            short c = (short) (r.nextInt(93) + 33);
            if (c != last) {
                pass += (char) c;
                last = c;
                i++;
            }
        }

        return pass;
    }
}