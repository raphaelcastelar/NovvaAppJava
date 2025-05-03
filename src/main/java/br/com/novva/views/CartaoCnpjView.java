package br.com.novva.views;

import java.util.Scanner;

public class CartaoCnpjView {
    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public String[] coletarDadosCartaoCnpj(Scanner scanner) {
        System.out.println("\n--- Solicitação de Cartão CNPJ ---");
        System.out.print("Digite o CNPJ (formato: 12.345.678/0001-99): ");
        String cnpj = scanner.nextLine();

        System.out.print("Digite a Razão Social: ");
        String razaoSocial = scanner.nextLine();

        System.out.print("Digite a Data de Solicitação (formato: dd/MM/yyyy): ");
        String dataSolicitacao = scanner.nextLine();

        return new String[]{cnpj, razaoSocial, dataSolicitacao};
    }
}