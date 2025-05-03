package br.com.novva.controllers;

import br.com.novva.models.SimplesNacionalModel;
import br.com.novva.views.SimplesNacionalView;
import java.util.Scanner;

public class SimplesNacionalController {
    private SimplesNacionalModel model;
    private SimplesNacionalView view;

    public SimplesNacionalController(SimplesNacionalModel model, SimplesNacionalView view) {
        this.model = model;
        this.view = view;
    }

    public void solicitarEmissaoSimplesNacional(Scanner scanner) {
        String[] dados = view.coletarDadosSimplesNacional(scanner);
        String cpf = dados[0];
        String dataSolicitacao = dados[1];

        model.solicitarEmissao(cpf, dataSolicitacao);
        view.exibirMensagem("Solicitação de Simples Nacional concluída!");
    }
}