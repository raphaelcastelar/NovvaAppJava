package br.com.novva.views;

import java.util.Scanner;

public class SimplesNacionalView {
    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public String[] coletarDadosSimplesNacional(Scanner scanner) {
        System.out.println("\n--- Solicitação de Simples Nacional ---");
        System.out.print("Digite o CPF (formato: 123.456.789-00): ");
        String cpf = scanner.nextLine();

        System.out.print("Digite a Data de Solicitação (formato: dd/MM/yyyy): ");
        String dataSolicitacao = scanner.nextLine();

        return new String[]{cpf, dataSolicitacao};
    }
}