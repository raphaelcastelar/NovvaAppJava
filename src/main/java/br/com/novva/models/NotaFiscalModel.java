package br.com.novva.models;

import br.com.novva.config.FirebaseConfig;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.CollectionReference;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
//aa
public class NotaFiscalModel {
    private Firestore db;
//aaa
    public NotaFiscalModel() {
        this.db = FirebaseConfig.getFirestore();
        System.out.println("Firestore inicializado: " + (db != null ? "Sim" : "Não"));
    }

    public void solicitarEmissao(String cpf, String municipio, String cnpj, double valor, String data) {
        System.out.println("Iniciando solicitação de emissão para CPF: " + cpf);
        try {
            String horarioEmissao = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            System.out.println("Horário de emissão gerado: " + horarioEmissao);

            DocumentReference servicoRef = db.collection("servicos").document(cpf);
            System.out.println("Referência ao documento 'servicos/" + cpf + "' criada.");

            CollectionReference notasRef = servicoRef.collection("notas");
            System.out.println("Referência à subcoleção 'notas' criada.");

            Map<String, Object> notaData = new HashMap<>();
            notaData.put("municipio", municipio);
            notaData.put("cnpj", cnpj);
            notaData.put("data", data);
            notaData.put("valor", valor);
            notaData.put("horario_emissao", horarioEmissao);
            System.out.println("Dados preparados: " + notaData);

            WriteResult result = notasRef.document().set(notaData).get();
            System.out.println("Solicitação de nota fiscal registrada com sucesso! ID: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao registrar solicitação: " + e.getMessage());
            e.printStackTrace(); // Adiciona o stack trace para mais detalhes
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Adiciona o stack trace para mais detalhes
        }
    }
}