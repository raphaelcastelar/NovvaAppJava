package br.com.novva.controllers;

import br.com.novva.models.NotaFiscalModel;
import br.com.novva.views.NotaFiscalView;
import java.util.Scanner;

public class NotaFiscalController {
    private NotaFiscalModel model;
    private NotaFiscalView view;

    public NotaFiscalController(NotaFiscalModel model, NotaFiscalView view) {
        this.model = model;
        this.view = view;
    }

    public void solicitarEmissaoNota(Scanner scanner) {
        String cpf = view.getInput(scanner, "Digite o CPF: ");
        String municipio = view.getInput(scanner, "Digite o Município: ");
        String cnpj = view.getInput(scanner, "Digite o CNPJ: ");
        String valorStr = view.getInput(scanner, "Digite o Valor: ");
        String data = view.getInput(scanner, "Digite a Data (dd/mm/aaaa): ");

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido!");
            return;
        }

        model.solicitarEmissao(cpf, municipio, cnpj, valor, data);
    }
}