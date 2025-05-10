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
        Map<String, Object> resultado = model.gerarRelatorioFaturamento(dados);

        if (!(Boolean) resultado.get("sucesso")) {
            view.exibirMensagem((String) resultado.get("mensagem"));
            return;
        }

        int quantidadeNotas = (int) resultado.get("quantidadeNotas");
        double totalFaturamento = (double) resultado.get("totalFaturamento");
        view.exibirRelatorio(quantidadeNotas, totalFaturamento);
    }
}