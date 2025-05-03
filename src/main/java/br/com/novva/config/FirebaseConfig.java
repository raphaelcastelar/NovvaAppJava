package br.com.novva.config;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.FirestoreOptions;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class FirebaseConfig {
    private static Firestore db;

    public static Firestore getFirestore() {
        if (db == null) {
            System.out.println("Inicializando Firestore...");
            try {
                // Verifica se o ClassLoader está disponível
                ClassLoader classLoader = FirebaseConfig.class.getClassLoader();
                if (classLoader == null) {
                    System.err.println("ClassLoader é null. Não é possível carregar recursos.");
                    return null;
                }
                System.out.println("ClassLoader encontrado: " + classLoader);

                // Carrega o arquivo do classpath
                InputStream serviceAccount = classLoader.getResourceAsStream("firebase-credentials.json");
                if (serviceAccount == null) {
                    System.err.println("Arquivo firebase-credentials.json não encontrado no classpath. Verifique se está em src/main/resources/.");
                    return null;
                }
                System.out.println("Arquivo de credenciais carregado com sucesso do classpath.");

                // Lê o conteúdo do arquivo e armazena em um byte array
                byte[] credentialBytes = serviceAccount.readAllBytes();
                serviceAccount.close(); // Fecha o InputStream original
                String jsonContent = new String(credentialBytes);
                System.out.println("Conteúdo do arquivo (resumido): " + jsonContent.substring(0, Math.min(jsonContent.length(), 100)) + "...");

                // Verifica se o project_id está presente no JSON
                if (!jsonContent.contains("\"project_id\": \"novva-d0b7a\"")) {
                    System.err.println("O project_id 'novva-d0b7a' não foi encontrado no arquivo firebase-credentials.json. Verifique o arquivo.");
                    return null;
                }

                // Cria um novo InputStream a partir do byte array para o FirebaseOptions
                InputStream firebaseStream = new ByteArrayInputStream(credentialBytes);
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(firebaseStream))
                    .setProjectId("novva-d0b7a")
                    .build();
                System.out.println("Opções de Firebase configuradas.");

                // Inicializa o FirebaseApp
                FirebaseApp.initializeApp(options);
                System.out.println("FirebaseApp inicializado.");

                // Cria um novo InputStream a partir do byte array para o FirestoreOptions
                InputStream firestoreStream = new ByteArrayInputStream(credentialBytes);
                FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                    .setProjectId("novva-d0b7a")
                    .setCredentials(GoogleCredentials.fromStream(firestoreStream))
                    .build();
                db = firestoreOptions.getService();
                if (db == null) {
                    System.err.println("Firestore não pôde ser inicializado: db é null.");
                } else {
                    System.out.println("Firestore instância criada com sucesso: " + db);
                }
            } catch (Exception e) {
                System.err.println("Erro ao inicializar Firebase: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Reutilizando instância existente de Firestore: " + db);
        }
        return db;
    }
}