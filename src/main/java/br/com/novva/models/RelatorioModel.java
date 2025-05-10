package br.com.novva.models;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import br.com.novva.facades.FirestoreFacade;
import br.com.novva.strategies.CpfNormalizationStrategy;
import br.com.novva.strategies.NormalizationStrategy;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class RelatorioModel {
    private FirestoreFacade dbFacade;
    private NormalizationStrategy normalizationStrategy;

    public RelatorioModel() {
        this.dbFacade = new FirestoreFacade();
        this.normalizationStrategy = new CpfNormalizationStrategy();
        if (dbFacade == null) {
            throw new IllegalStateException("FirestoreFacade não inicializado.");
        }
    }

    public Map<String, Object> gerarRelatorioFaturamento(String[] dados) {
        Map<String, Object> resultado = new HashMap<>();

        // Conversão de dados no Model
        String cpf = dados[0];
        int mes;
        int ano;
        try {
            mes = Integer.parseInt(dados[1]);
            ano = Integer.parseInt(dados[2]);
        } catch (NumberFormatException e) {
            resultado.put("sucesso", false);
            resultado.put("mensagem", "Mês ou ano inválido!");
            return resultado;
        }

        // Cálculo do faturamento
        String cpfNormalizado = normalizationStrategy.normalize(cpf);
        String collectionPath = "servicos/" + cpfNormalizado + "/notas";
        List<QueryDocumentSnapshot> documents = dbFacade.getDocuments(collectionPath);

        double totalFaturamento = 0.0;
        int quantidadeNotas = 0;

        for (QueryDocumentSnapshot doc : documents) {
            String horarioEmissao = (String) doc.get("horario_emissao");
            if (horarioEmissao != null) {
                try {
                    String[] dateParts = horarioEmissao.split(" ")[0].split("/"); // Extrai "dd/mm/aa"
                    int mesNota = Integer.parseInt(dateParts[1]);
                    int anoNota = Integer.parseInt(dateParts[2]);
                    if (mesNota == mes && anoNota == ano) {
                        double valor = ((Number) doc.get("valor")).doubleValue();
                        totalFaturamento += valor;
                        quantidadeNotas++;
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Erro ao processar horario_emissao para relatório: " + horarioEmissao + " - " + e.getMessage());
                    continue;
                }
            }
        }

        resultado.put("sucesso", true);
        resultado.put("quantidadeNotas", quantidadeNotas);
        resultado.put("totalFaturamento", totalFaturamento);
        return resultado;
    }
}