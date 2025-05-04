package br.com.novva.controllers;

import br.com.novva.models.NotaFiscalModel;
import br.com.novva.views.NotaFiscalView;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NotaFiscalController {
    private NotaFiscalModel model;
    private NotaFiscalView view;

    public NotaFiscalController(NotaFiscalModel model, NotaFiscalView view) {
        this.model = model;
        this.view = view;
    }

    public void solicitarEmissaoNota(Scanner scanner) {
        String[] dados = view.coletarDadosNotaFiscal(scanner);
        String cpf = dados[0];
        String municipio = dados[1];
        String cnpj = dados[2];
        double valor = Double.parseDouble(dados[3]);
        String data = dados[4];

        model.registrarNota(cpf, municipio, cnpj, valor, data);
        view.exibirMensagem("Emissão de nota fiscal concluída!");
    }

    public void solicitarCancelamentoNota(Scanner scanner) {
        String[] dados = view.coletarDadosCancelamento(scanner);
        String cpf = dados[0];

        List<String> meses = model.listarMesesDisponiveis(cpf);
        view.exibirMeses(meses);

        if (meses.isEmpty()) {
            view.exibirMensagem("Não há meses com notas para cancelar.");
            return;
        }

        int escolhaMes = view.selecionarMes(scanner, meses.size());
        if (escolhaMes < 1 || escolhaMes > meses.size()) {
            view.exibirMensagem("Escolha de mês inválida!");
            return;
        }

        String mesSelecionado = meses.get(escolhaMes - 1);
        String[] parts = mesSelecionado.split("/");
        int mes = Integer.parseInt(parts[0]); // Ex.: "04"
        int ano = Integer.parseInt(parts[1]); // Ex.: "25"

        List<Map<String, Object>> notas = model.listarNotasPorMes(cpf, mes, ano);
        view.exibirNotas(notas);

        if (notas.isEmpty()) {
            view.exibirMensagem("Não há notas para cancelar neste mês.");
            return;
        }

        int escolhaNota = view.selecionarNota(scanner, notas.size());
        if (escolhaNota < 1 || escolhaNota > notas.size()) {
            view.exibirMensagem("Escolha de nota inválida!");
            return;
        }

        String notaId = (String) notas.get(escolhaNota - 1).get("id");
        model.solicitarCancelamento(cpf, notaId);
        view.exibirMensagem("Solicitação de cancelamento registrada com sucesso!");
    }
}