package br.com.novva.models;

import br.com.novva.config.FirebaseConfig;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.DocumentReference;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CartaoCnpjModel {
    private Firestore db;

    public CartaoCnpjModel() {
        this.db = FirebaseConfig.getFirestore();
        if (db == null) {
            System.err.println("Falha ao inicializar Firestore. Verifique a configuração do Firebase.");
            throw new IllegalStateException("Firestore não inicializado.");
        }
    }

    public void solicitarEmissao(String cnpj, String razaoSocial, String dataSolicitacao) {
        System.out.println("Iniciando solicitação de emissão do Cartão CNPJ para: " + cnpj);
        try {
            if (db == null) {
                System.err.println("Firestore não está inicializado.");
                return;
            }
            String horarioEmissao = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            DocumentReference docRef = db.collection("cartoes_cnpj").document(cnpj);
            Map<String, Object> cartaoData = new HashMap<>();
            cartaoData.put("cnpj", cnpj);
            cartaoData.put("razao_social", razaoSocial);
            cartaoData.put("data_solicitacao", dataSolicitacao);
            cartaoData.put("horario_emissao", horarioEmissao);
            cartaoData.put("status", "Emitido");
            System.out.println("Dados preparados: " + cartaoData);

            WriteResult result = docRef.set(cartaoData).get();
            System.out.println("Solicitação de Cartão CNPJ registrada com sucesso! ID: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao registrar solicitação: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}