package br.com.novva.views;

import java.util.Scanner;

public class RelatorioView {
    public String[] coletarDadosRelatorio(Scanner scanner) {
        System.out.println("--- Relatório de Faturamento ---");
        System.out.print("Digite o CPF (formato: 123.456.789-00 ou 12345678900): ");
        String cpf = scanner.nextLine();
        System.out.print("Digite o mês (1-12): ");
        String mes = scanner.nextLine();
        System.out.print("Digite o ano (ex.: 25 para 2025): ");
        String ano = scanner.nextLine();
        return new String[]{cpf, mes, ano};
    }

    public void exibirRelatorio(int quantidadeNotas, double totalFaturamento) {
        System.out.println("--- Relatório de Faturamento ---");
        System.out.println("Quantidade de notas emitidas: " + quantidadeNotas);
        System.out.println("Total do faturamento: R$ " + String.format("%.2f", totalFaturamento));
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}