package br.com.novva;

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
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao NovvaApp");

        // Configuração MVC para Nota Fiscal
        NotaFiscalModel notaModel = new NotaFiscalModel();
        NotaFiscalView notaView = new NotaFiscalView();
        NotaFiscalController notaController = new NotaFiscalController(notaModel, notaView);

        // Configuração MVC para Cartão CNPJ
        CartaoCnpjModel cartaoModel = new CartaoCnpjModel();
        CartaoCnpjView cartaoView = new CartaoCnpjView();
        CartaoCnpjController cartaoController = new CartaoCnpjController(cartaoModel, cartaoView);

        // Configuração MVC para Simples Nacional
        SimplesNacionalModel simplesModel = new SimplesNacionalModel();
        SimplesNacionalView simplesView = new SimplesNacionalView();
        SimplesNacionalController simplesController = new SimplesNacionalController(simplesModel, simplesView);

        // Configuração MVC para Relatório
        RelatorioModel relatorioModel = new RelatorioModel();
        RelatorioView relatorioView = new RelatorioView();
        RelatorioController relatorioController = new RelatorioController(relatorioModel, relatorioView);

        while (true) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Emissão da Nota Fiscal");
            System.out.println("2. Emissão do Cartão CNPJ");
            System.out.println("3. Emissão do Simples Nacional");
            System.out.println("4. Solicitação de Cancelamento de Nota Fiscal");
            System.out.println("5. Gerenciar Tomadores");
            System.out.println("6. Relatório de Faturamento");
            System.out.println("0. Sair");
            System.out.print("Digite sua escolha: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            switch (choice) {
                case 1:
                    notaController.solicitarEmissaoNota(scanner);
                    break;
                case 2:
                    cartaoController.solicitarEmissaoCartaoCnpj(scanner);
                    break;
                case 3:
                    simplesController.solicitarEmissaoSimplesNacional(scanner);
                    break;
                case 4:
                    notaController.solicitarCancelamentoNota(scanner);
                    break;
                case 5:
                    notaController.gerenciarTomador(scanner);
                    break;
                case 6:
                    relatorioController.solicitarRelatorioFaturamento(scanner);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}