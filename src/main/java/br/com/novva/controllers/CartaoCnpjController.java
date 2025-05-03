package br.com.novva.controllers;

import br.com.novva.models.CartaoCnpjModel;
import br.com.novva.views.CartaoCnpjView;
import java.util.Scanner;

public class CartaoCnpjController {
    private CartaoCnpjModel model;
    private CartaoCnpjView view;

    public CartaoCnpjController(CartaoCnpjModel model, CartaoCnpjView view) {
        this.model = model;
        this.view = view;
    }

    public void solicitarEmissaoCartaoCnpj(Scanner scanner) {
        String[] dados = view.coletarDadosCartaoCnpj(scanner);
        String cnpj = dados[0];
        String razaoSocial = dados[1];
        String dataSolicitacao = dados[2];

        model.solicitarEmissao(cnpj, razaoSocial, dataSolicitacao);
        view.exibirMensagem("Solicitação de Cartão CNPJ concluída!");
    }
}