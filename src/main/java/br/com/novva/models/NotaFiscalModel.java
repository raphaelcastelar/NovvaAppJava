package br.com.novva.models;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import br.com.novva.facades.FirestoreFacade;
import br.com.novva.strategies.CpfNormalizationStrategy;
import br.com.novva.strategies.NormalizationStrategy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.TreeSet;

public class NotaFiscalModel {
    private FirestoreFacade dbFacade;
    private NormalizationStrategy normalizationStrategy;

    public NotaFiscalModel() {
        this.dbFacade = new FirestoreFacade();
        this.normalizationStrategy = new CpfNormalizationStrategy();
    }

    public Map<String, Object> emitirNotaFiscal(String[] dados, int escolhaCnpj) {
        Map<String, Object> resultado = new HashMap<>();
        String cpf = dados[0];
        String municipio = dados[1];
        String valorStr = dados[2];
        String data = dados[3];

        String cpfNormalizado = normalizationStrategy.normalize(cpf);
        List<Map<String, Object>> tomadores = listarTomadores(cpfNormalizado);

        // Validação: Verificar se há tomadores
        if (tomadores.isEmpty()) {
            resultado.put("sucesso", false);
            resultado.put("mensagem", "Nenhum tomador cadastrado. Adicione um tomador antes de emitir a nota.");
            return resultado;
        }

        // Validação: Verificar escolha do tomador
        if (escolhaCnpj < 1 || escolhaCnpj > tomadores.size()) {
            resultado.put("sucesso", false);
            resultado.put("mensagem", "Escolha de tomador inválida!");
            return resultado;
        }

        // Conversão do valor
        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            resultado.put("sucesso", false);
            resultado.put("mensagem", "Valor inválido!");
            return resultado;
        }

        // Registrar a nota
        String cnpj = (String) tomadores.get(escolhaCnpj - 1).get("cnpj");
        String collectionPath = "servicos/" + cpfNormalizado + "/notas";

        Map<String, Object> notaData = new HashMap<>();
        notaData.put("cpf", cpfNormalizado);
        notaData.put("municipio", municipio);
        notaData.put("cnpj", cnpj);
        notaData.put("valor", valor);
        notaData.put("data", data);
        notaData.put("horario_emissao", LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")));

        System.out.println("Dados preparados: " + notaData);
        dbFacade.saveDocumentWithAutoId(collectionPath, notaData);

        resultado.put("sucesso", true);
        resultado.put("mensagem", "Emissão de nota fiscal concluída!");
        return resultado;
    }

    public List<String> listarMesesDisponiveis(String cpf) {
        List<String> meses = new ArrayList<>();
        TreeSet<String> mesesUnicos = new TreeSet<>();

        String cpfNormalizado = normalizationStrategy.normalize(cpf);
        String collectionPath = "servicos/" + cpfNormalizado + "/notas";
        List<QueryDocumentSnapshot> documents = dbFacade.getDocuments(collectionPath);

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
        return meses;
    }

    public List<Map<String, Object>> listarNotasPorMes(String cpf, int mes, int ano) {
        List<Map<String, Object>> notas = new ArrayList<>();

        String cpfNormalizado = normalizationStrategy.normalize(cpf);
        String collectionPath = "servicos/" + cpfNormalizado + "/notas";
        List<QueryDocumentSnapshot> documents = dbFacade.getDocuments(collectionPath);

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
        return notas;
    }

    public void solicitarCancelamento(String cpf, String notaId) {
        System.out.println("Iniciando solicitação de cancelamento para a nota ID: " + notaId + " do CPF: " + cpf);
        String cpfNormalizado = normalizationStrategy.normalize(cpf);
        String collectionPath = "solicitacoes_cancelamento";
        String documentId = cpfNormalizado + "_" + notaId;

        String dataSolicitacao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        String horarioSolicitacao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

        Map<String, Object> cancelamentoData = new HashMap<>();
        cancelamentoData.put("cpf", cpfNormalizado);
        cancelamentoData.put("nota_id", notaId);
        cancelamentoData.put("data_solicitacao", dataSolicitacao);
        cancelamentoData.put("horario_solicitacao", horarioSolicitacao);
        cancelamentoData.put("status", "Pendente");

        dbFacade.saveDocument(collectionPath, documentId, cancelamentoData);
    }

    public void gerenciarTomador(String cpf, String cnpj, String apelido) {
        System.out.println("Iniciando gerenciamento de tomador para CPF: " + cpf + " com CNPJ: " + cnpj);
        String cpfNormalizado = normalizationStrategy.normalize(cpf);
        String collectionPath = "tomador/" + cpfNormalizado + "/CNPJ";

        Map<String, Object> tomadorData = new HashMap<>();
        tomadorData.put("apelido", apelido);
        tomadorData.put("horario_adicao", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")));

        dbFacade.saveDocument(collectionPath, cnpj, tomadorData);
    }

    public List<Map<String, Object>> listarTomadores(String cpf) {
        List<Map<String, Object>> tomadores = new ArrayList<>();

        String cpfNormalizado = normalizationStrategy.normalize(cpf);
        String collectionPath = "tomador/" + cpfNormalizado + "/CNPJ";
        List<QueryDocumentSnapshot> documents = dbFacade.getDocuments(collectionPath);

        for (QueryDocumentSnapshot doc : documents) {
            Map<String, Object> tomador = new HashMap<>(doc.getData());
            tomador.put("cnpj", doc.getId());
            tomadores.add(tomador);
        }
        return tomadores;
    }
}