package br.com.novva.models;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import br.com.novva.config.FirebaseConfig;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.TreeSet;

public class NotaFiscalModel {
    private Firestore db;

    public NotaFiscalModel() {
        this.db = FirebaseConfig.getFirestore();
        if (db == null) {
            System.err.println("Falha ao inicializar Firestore. Verifique a configuração do Firebase.");
            throw new IllegalStateException("Firestore não inicializado.");
        }
    }

    public void registrarNota(String cpf, String municipio, String cnpj, double valor, String data) {
        System.out.println("Iniciando registro da nota fiscal para CPF: " + cpf);
        try {
            if (db == null) {
                System.err.println("Firestore não está inicializado.");
                return;
            }

            DocumentReference docRef = db.collection("servicos")
                .document(normalizarCpf(cpf))
                .collection("notas")
                .document();

            Map<String, Object> notaData = new HashMap<>();
            notaData.put("cpf", normalizarCpf(cpf));
            notaData.put("municipio", municipio);
            notaData.put("cnpj", cnpj);
            notaData.put("valor", valor);
            notaData.put("data", data);
            notaData.put("horario_emissao", LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")));

            System.out.println("Dados preparados: " + notaData);

            WriteResult result = docRef.set(notaData).get();
            System.out.println("Nota fiscal registrada com sucesso! ID: " + docRef.getId() + " às " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao registrar nota fiscal: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<String> listarMesesDisponiveis(String cpf) {
        List<String> meses = new ArrayList<>();
        TreeSet<String> mesesUnicos = new TreeSet<>();
        try {
            if (db == null) {
                System.err.println("Firestore não está inicializado.");
                return meses;
            }

            String cpfNormalizado = normalizarCpf(cpf);
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
                        if (dateParts.length != 3) {
                            System.err.println("Formato inválido de horario_emissao (data): " + horarioEmissao);
                            continue;
                        }
                        int mesNumero = Integer.parseInt(dateParts[1]); // Extrai o mês (mm)
                        String mesNome = String.format("%02d", mesNumero); // Formata como "04" para abril
                        int ano = Integer.parseInt(dateParts[2]); // Extrai o ano (aa)
                        mesesUnicos.add(mesNome + "/" + ano); // Ex.: "04/25"
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.err.println("Erro ao processar horario_emissao: " + horarioEmissao + " - " + e.getMessage());
                        continue;
                    }
                }
            }
            meses.addAll(mesesUnicos);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao listar meses disponíveis: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        return meses;
    }

    public List<Map<String, Object>> listarNotasPorMes(String cpf, int mes, int ano) {
        List<Map<String, Object>> notas = new ArrayList<>();
        try {
            if (db == null) {
                System.err.println("Firestore não está inicializado.");
                return notas;
            }

            String cpfNormalizado = normalizarCpf(cpf);
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
                            Map<String, Object> nota = new HashMap<>(doc.getData());
                            nota.put("id", doc.getId());
                            notas.add(nota);
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.err.println("Erro ao processar horario_emissao para listagem de notas: " + horarioEmissao + " - " + e.getMessage());
                        continue;
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao listar notas por mês: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        return notas;
    }

    public void solicitarCancelamento(String cpf, String notaId) {
        System.out.println("Iniciando solicitação de cancelamento para a nota ID: " + notaId + " do CPF: " + cpf);
        try {
            if (db == null) {
                System.err.println("Firestore não está inicializado.");
                return;
            }

            String cpfNormalizado = normalizarCpf(cpf);
            String dataSolicitacao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
            String horarioSolicitacao = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

            DocumentReference docRef = db.collection("solicitacoes_cancelamento")
                .document(cpfNormalizado + "_" + notaId);

            Map<String, Object> cancelamentoData = new HashMap<>();
            cancelamentoData.put("cpf", cpfNormalizado);
            cancelamentoData.put("nota_id", notaId);
            cancelamentoData.put("data_solicitacao", dataSolicitacao);
            cancelamentoData.put("horario_solicitacao", horarioSolicitacao);
            cancelamentoData.put("status", "Pendente");

            WriteResult result = docRef.set(cancelamentoData).get();
            System.out.println("Solicitação de cancelamento registrada com sucesso! ID: " + docRef.getId() + " às " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao registrar solicitação de cancelamento: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String normalizarCpf(String cpf) {
        return cpf != null ? cpf.replaceAll("[^0-9]", "") : "";
    }
}