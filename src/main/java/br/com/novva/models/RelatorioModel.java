package br.com.novva.models;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import br.com.novva.config.FirebaseConfig;
import br.com.novva.strategies.CpfNormalizationStrategy;
import br.com.novva.strategies.NormalizationStrategy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;

public class RelatorioModel {
    private Firestore db;
    private NormalizationStrategy normalizationStrategy;
    public RelatorioModel() {
        this.db = FirebaseConfig.getFirestore();
        this.normalizationStrategy = new CpfNormalizationStrategy();
        if (db == null) {
            System.err.println("Falha ao inicializar Firestore. Verifique a configuração do Firebase.");
            throw new IllegalStateException("Firestore não inicializado.");
        }
    }

    public Map<String, Object> calcularFaturamentoMensal(String cpf, int mes, int ano) {
        Map<String, Object> resultado = new java.util.HashMap<>();
        double totalFaturamento = 0.0;
        int quantidadeNotas = 0;

        try {
            if (db == null) {
                System.err.println("Firestore não está inicializado.");
                return resultado;
            }

            String cpfNormalizado = normalizationStrategy.normalize(cpf);
            List<QueryDocumentSnapshot> documents = db.collection("servicos")
                .document(cpfNormalizado)
                .collection("notas")
                .get()
                .get()
                .getDocuments();

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
            resultado.put("quantidadeNotas", quantidadeNotas);
            resultado.put("totalFaturamento", totalFaturamento);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao calcular faturamento mensal: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        return resultado;
    }

}