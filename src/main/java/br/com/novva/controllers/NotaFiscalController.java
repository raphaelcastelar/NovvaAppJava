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

        List<Map<String, Object>> tomadores = model.listarTomadores(cpf);
        if (tomadores.isEmpty()) {
            view.exibirMensagem("Nenhum tomador cadastrado. Adicione um tomador antes de emitir a nota.");
            return;
        }

        view.exibirTomadores(tomadores);
        int escolhaCnpj = view.selecionarTomador(scanner, tomadores.size());
        if (escolhaCnpj < 1 || escolhaCnpj > tomadores.size()) {
            view.exibirMensagem("Escolha de tomador inválida!");
            return;
        }

        String cnpj = (String) tomadores.get(escolhaCnpj - 1).get("cnpj");
        double valor = Double.parseDouble(dados[2]);
        String data = dados[3];

        model.registrarNota(cpf, municipio, cnpj, valor, data);
        view.exibirMensagem("Emissão de nota fiscal concluída!");
    }

    public void gerenciarTomador(Scanner scanner) {
        String[] dados = view.coletarDadosTomador(scanner);
        String cpf = dados[0];
        String cnpj = dados[1];
        String apelido = dados[2];

        model.gerenciarTomador(cpf, cnpj, apelido);
        view.exibirMensagem("Tomador adicionado com sucesso!");
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