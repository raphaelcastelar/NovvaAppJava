package br.com.novva.views;

import java.util.Scanner;

public class TerminalView {
    public String getInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}