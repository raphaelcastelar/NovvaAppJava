package br.com.novva;

import br.com.novva.controllers.CartaoCnpjController;
import br.com.novva.controllers.NotaFiscalController;
import br.com.novva.controllers.SimplesNacionalController;
import br.com.novva.models.CartaoCnpjModel;
import br.com.novva.models.NotaFiscalModel;
import br.com.novva.models.SimplesNacionalModel;
import br.com.novva.views.CartaoCnpjView;
import br.com.novva.views.NotaFiscalView;
import br.com.novva.views.SimplesNacionalView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1. Emissão da Nota Fiscal");
            System.out.println("2. Emissão do Cartão CNPJ");
            System.out.println("3. Emissão do Simples Nacional");
            System.out.println("4. Solicitação de Cancelamento de Nota Fiscal");
            System.out.println("5. Gerenciar Tomadores");
            System.out.println("0. Sair");
            System.out.print("Digite sua escolha: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            if (choice == 0) {
                running = false;
                System.out.println("Encerrando o programa...");
                break;
            }

            NotaFiscalModel model = new NotaFiscalModel();
            NotaFiscalView view = new NotaFiscalView();
            NotaFiscalController notaController = new NotaFiscalController(model, view);

            CartaoCnpjModel cartaoModel = new CartaoCnpjModel();
            CartaoCnpjView cartaoView = new CartaoCnpjView();
            CartaoCnpjController cartaoController = new CartaoCnpjController(cartaoModel, cartaoView);

            SimplesNacionalModel simplesModel = new SimplesNacionalModel();
            SimplesNacionalView simplesView = new SimplesNacionalView();
            SimplesNacionalController simplesController = new SimplesNacionalController(simplesModel, simplesView);

            switch (choice) {
                case 1:
                    notaController.solicitarEmissaoNota(scanner);
                    break;
                case 2:
                    cartaoController.solicitarEmissaoCartaoCnpj(scanner);
                    //System.out.println("Funcionalidade de Emissão do Cartão CNPJ ainda não implementada.");
                    break;
                case 3:
                    simplesController.solicitarEmissaoSimplesNacional(scanner);
                    //System.out.println("Funcionalidade de Emissão do Simples Nacional ainda não implementada.");
                    break;
                case 4:
                    notaController.solicitarCancelamentoNota(scanner);
                    break;
                case 5:
                    notaController.gerenciarTomador(scanner);
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
        scanner.close();
    }
}