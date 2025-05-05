package br.com.novva.controllers;

import br.com.novva.models.RelatorioModel;
import br.com.novva.views.RelatorioView;
import java.util.Map;
import java.util.Scanner;

public class RelatorioController {
    private RelatorioModel model;
    private RelatorioView view;

    public RelatorioController(RelatorioModel model, RelatorioView view) {
        this.model = model;
        this.view = view;
    }

    public void solicitarRelatorioFaturamento(Scanner scanner) {
        String[] dados = view.coletarDadosRelatorio(scanner);
        String cpf = dados[0];
        int mes = Integer.parseInt(dados[1]);
        int ano = Integer.parseInt(dados[2]);

        Map<String, Object> resultado = model.calcularFaturamentoMensal(cpf, mes, ano);
        int quantidadeNotas = (int) resultado.get("quantidadeNotas");
        double totalFaturamento = (double) resultado.get("totalFaturamento");

        view.exibirRelatorio(quantidadeNotas, totalFaturamento);
    }
}