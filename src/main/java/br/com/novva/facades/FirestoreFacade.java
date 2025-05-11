package br.com.novva.facades;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import br.com.novva.config.FirebaseConfig;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;

public class FirestoreFacade {
    private Firestore db;

    public FirestoreFacade() {
        this.db = FirebaseConfig.getFirestore();
        if (db == null) {
            throw new IllegalStateException("Firestore não inicializado.");
        }
    }

    public String saveDocument(String collectionPath, String documentId, Map<String, Object> data) {
        try {
            DocumentReference docRef = db.collection(collectionPath).document(documentId);
            WriteResult result = docRef.set(data).get();
            System.out.println("Documento salvo em " + collectionPath + " com ID: " + docRef.getId() + " às " + result.getUpdateTime());
            return docRef.getId();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao salvar documento: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String saveDocumentWithAutoId(String collectionPath, Map<String, Object> data) {
        try {
            DocumentReference docRef = db.collection(collectionPath).document();
            WriteResult result = docRef.set(data).get();
            System.out.println("Documento salvo em " + collectionPath + " com ID: " + docRef.getId() + " às " + result.getUpdateTime());
            return docRef.getId();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao salvar documento: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<QueryDocumentSnapshot> getDocuments(String collectionPath) {
        try {
            System.out.println("Buscando documentos na coleção: " + collectionPath);
            List<QueryDocumentSnapshot> documents = db.collection(collectionPath).get().get().getDocuments();
            System.out.println("Documentos encontrados: " + documents.size());
            return documents;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erro ao buscar documentos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}