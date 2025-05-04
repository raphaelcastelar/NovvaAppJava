package br.com.novva.views;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NotaFiscalView {
    public String[] coletarDadosNotaFiscal(Scanner scanner) {
        System.out.println("--- Emissão da Nota Fiscal ---");
        System.out.print("Digite o CPF (formato: 123.456.789-00 ou 12345678900): ");
        String cpf = scanner.nextLine();
        System.out.print("Digite o município: ");
        String municipio = scanner.nextLine();
        System.out.print("Digite o valor: ");
        String valor = scanner.nextLine();
        System.out.print("Digite a data (dd/mm/aa): ");
        String data = scanner.nextLine();
        return new String[]{cpf, municipio, valor, data};
    }

    public String[] coletarDadosCancelamento(Scanner scanner) {
        System.out.println("--- Solicitação de Cancelamento de Nota Fiscal ---");
        System.out.print("Digite o CPF (formato: 123.456.789-00 ou 12345678900): ");
        return new String[]{scanner.nextLine()};
    }

    public String[] coletarDadosTomador(Scanner scanner) {
        System.out.println("--- Gerenciar Tomadores ---");
        System.out.print("Digite o CPF (formato: 123.456.789-00 ou 12345678900): ");
        String cpf = scanner.nextLine();
        System.out.print("Digite o CNPJ (somente números): ");
        String cnpj = scanner.nextLine();
        System.out.print("Digite o apelido da empresa: ");
        String apelido = scanner.nextLine();
        return new String[]{cpf, cnpj, apelido};
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void exibirMeses(List<String> meses) {
        System.out.println("--- Meses com Notas Fiscais Emitidas ---");
        for (int i = 0; i < meses.size(); i++) {
            System.out.println((i + 1) + ". " + meses.get(i));
        }
    }

    public int selecionarMes(Scanner scanner, int max) {
        System.out.print("Digite o número do mês que deseja visualizar (1 a " + max + "): ");
        return Integer.parseInt(scanner.nextLine());
    }

    public void exibirNotas(List<Map<String, Object>> notas) {
        System.out.println("--- Notas Disponíveis ---");
        for (int i = 0; i < notas.size(); i++) {
            Map<String, Object> nota = notas.get(i);
            System.out.println((i + 1) + ". ID: " + nota.get("id") + ", Valor: " + nota.get("valor") + ", CNPJ: " + nota.get("cnpj"));
        }
    }

    public int selecionarNota(Scanner scanner, int max) {
        System.out.print("Digite o número da nota para cancelar (1 a " + max + "): ");
        return Integer.parseInt(scanner.nextLine());
    }

    public void exibirTomadores(List<Map<String, Object>> tomadores) {
        System.out.println("--- Tomadores Cadastrados ---");
        for (int i = 0; i < tomadores.size(); i++) {
            Map<String, Object> tomador = tomadores.get(i);
            System.out.println((i + 1) + ". Apelido: " + tomador.get("apelido") + ", CNPJ: " + tomador.get("cnpj"));
        }
    }

    public int selecionarTomador(Scanner scanner, int max) {
        System.out.print("Digite o número do tomador (1 a " + max + "): ");
        return Integer.parseInt(scanner.nextLine());
    }
}