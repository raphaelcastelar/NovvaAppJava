package br.com.novva.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    private static Firestore db;

    public static Firestore getFirestore() {
        if (db == null) {
            try {
                // Caminho absoluto do arquivo de credenciais (nome original)
                String credentialsPath = "/Users/selmajosecastelarferreira/Desktop/NovvaAppJava/src/main/resources/firebase-credentials.json";
                System.out.println("Verificando arquivo de credenciais em: " + credentialsPath);

                // Verificar se o arquivo existe
                File credentialsFile = new File(credentialsPath);
                if (!credentialsFile.exists()) {
                    System.err.println("Arquivo não encontrado: " + credentialsPath);
                    System.err.println("Por favor, confirme se o arquivo 'firebase-credentials.json' está no diretório src/main/resources/.");
                    throw new IOException("Arquivo de credenciais não encontrado: " + credentialsPath);
                }
                if (!credentialsFile.canRead()) {
                    System.err.println("Arquivo encontrado, mas não pode ser lido (problema de permissões): " + credentialsPath);
                    throw new IOException("Não é possível ler o arquivo de credenciais: " + credentialsPath);
                }

                System.out.println("Arquivo encontrado e legível: " + credentialsPath);
                FileInputStream serviceAccount = new FileInputStream(credentialsFile);

                // Inicializar o Firebase com as credenciais
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

                System.out.println("Inicializando FirebaseApp...");
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    System.out.println("FirebaseApp inicializado com sucesso.");
                } else {
                    System.out.println("FirebaseApp já inicializado.");
                }

                // Obter a instância do Firestore
                db = FirestoreClient.getFirestore();
                System.out.println("Firestore inicializado com sucesso.");
            } catch (IOException e) {
                System.err.println("Erro ao inicializar o Firestore: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Falha na inicialização do Firestore", e);
            } catch (Exception e) {
                System.err.println("Erro inesperado ao inicialização do Firestore: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Erro inesperado na inicialização do Firestore", e);
            }
        }
        return db;
    }
}