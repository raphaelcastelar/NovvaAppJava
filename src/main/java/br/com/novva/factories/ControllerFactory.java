package br.com.novva.factories;

import br.com.novva.controllers.NotaFiscalController;
import br.com.novva.controllers.CartaoCnpjController;
import br.com.novva.controllers.SimplesNacionalController;
import br.com.novva.controllers.RelatorioController;
import br.com.novva.models.NotaFiscalModel;
import br.com.novva.models.CartaoCnpjModel;
import br.com.novva.models.SimplesNacionalModel;
import br.com.novva.models.RelatorioModel;
import br.com.novva.views.NotaFiscalView;
import br.com.novva.views.CartaoCnpjView;
import br.com.novva.views.SimplesNacionalView;
import br.com.novva.views.RelatorioView;

public class ControllerFactory {
    public static Object createController(String type) {
        switch (type) {
            case "NotaFiscal":
                return new NotaFiscalController(new NotaFiscalModel(), new NotaFiscalView());
            case "CartaoCnpj":
                return new CartaoCnpjController(new CartaoCnpjModel(), new CartaoCnpjView());
            case "SimplesNacional":
                return new SimplesNacionalController(new SimplesNacionalModel(), new SimplesNacionalView());
            case "Relatorio":
                return new RelatorioController(new RelatorioModel(), new RelatorioView());
            default:
                throw new IllegalArgumentException("Tipo de controller desconhecido: " + type);
        }
    }
}