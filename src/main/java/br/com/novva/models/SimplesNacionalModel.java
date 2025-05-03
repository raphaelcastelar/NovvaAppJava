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

public class SimplesNacionalModel {
    private Firestore db;

    public SimplesNacionalModel() {
        this.db = FirebaseConfig.getFirestore();
        if (db == null) {
            System.err.println("Falha ao inicializar Firestore. Verifique a configuração do Firebase.");
            throw new IllegalStateException("Firestore não inicializado.");
        }
    }

    public void solicitarEmissao(String cpf, String dataSolicitacao) {
        System.out.println("Iniciando solicitação de emissão do Simples Nacional para CPF: " + cpf);
        try {
            if (db == null) {
                System.err.println("Firestore não está inicializado.");
                return;
            }
            String horarioEmissao = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            DocumentReference docRef = db.collection("simples_nacional").document(cpf);
            Map<String, Object> simplesData = new HashMap<>();
            simplesData.put("cpf", cpf);
            simplesData.put("data_solicitacao", dataSolicitacao);
            simplesData.put("horario_emissao", horarioEmissao);
            simplesData.put("status", "Emitido");
            System.out.println("Dados preparados: " + simplesData);

            WriteResult result = docRef.set(simplesData).get();
            System.out.println("Solicitação de Simples Nacional registrada com sucesso! ID: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao registrar solicitação: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}