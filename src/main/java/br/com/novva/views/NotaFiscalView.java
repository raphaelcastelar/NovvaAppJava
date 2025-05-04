package br.com.novva.views;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NotaFiscalView {
    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public String[] coletarDadosNotaFiscal(Scanner scanner) {
        System.out.println("\n--- Emissão de Nota Fiscal ---");
        System.out.print("Digite o CPF (formato: 123.456.789-00 ou 12345678900): ");
        String cpf = scanner.nextLine();

        System.out.print("Digite o Município: ");
        String municipio = scanner.nextLine();

        System.out.print("Digite o CNPJ (formato: 12.345.678/0001-99): ");
        String cnpj = scanner.nextLine();

        System.out.print("Digite o Valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir nova linha

        System.out.print("Digite a Data (formato: dd/MM/yyyy): ");
        String data = scanner.nextLine();

        return new String[]{cpf, municipio, cnpj, String.valueOf(valor), data};
    }

    public String[] coletarDadosCancelamento(Scanner scanner) {
        System.out.println("\n--- Solicitação de Cancelamento de Nota Fiscal ---");
        System.out.print("Digite o CPF (formato: 123.456.789-00 ou 12345678900): ");
        String cpf = scanner.nextLine();

        return new String[]{cpf};
    }

    public void exibirMeses(List<String> meses) {
        if (meses.isEmpty()) {
            System.out.println("Nenhum mês com notas fiscais encontrado.");
            return;
        }

        System.out.println("\n--- Meses com Notas Fiscais Emitidas ---");
        for (int i = 0; i < meses.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, meses.get(i));
        }
    }

    public int selecionarMes(Scanner scanner, int totalMeses) {
        System.out.print("Digite o número do mês que deseja visualizar (1 a " + totalMeses + "): ");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha
        return escolha;
    }

    public void exibirNotas(List<Map<String, Object>> notas) {
        if (notas.isEmpty()) {
            System.out.println("Nenhuma nota fiscal encontrada para o mês selecionado.");
            return;
        }

        System.out.println("\n--- Lista de Notas Fiscais Emitidas ---");
        for (int i = 0; i < notas.size(); i++) {
            Map<String, Object> nota = notas.get(i);
            System.out.printf("%d. ID: %s | Município: %s | CNPJ: %s | Valor: %.2f | Data: %s | Horário: %s%n",
                i + 1, nota.get("id"), nota.get("municipio"), nota.get("cnpj"),
                nota.get("valor"), nota.get("data"), nota.get("horario_emissao"));
        }
    }

    public int selecionarNota(Scanner scanner, int totalNotas) {
        System.out.print("Digite o número da nota que deseja cancelar (1 a " + totalNotas + "): ");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha
        return escolha;
    }
}