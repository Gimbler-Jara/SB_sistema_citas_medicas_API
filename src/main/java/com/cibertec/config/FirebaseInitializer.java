package com.cibertec.config;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class FirebaseInitializer {

	private final String json = "{\r\n"
		    + "  \"type\": \"service_account\",\r\n"
		    + "  \"project_id\": \"digital-world-8d4cc\",\r\n"
		    + "  \"private_key_id\": \"6dbb2e51c7c19eedf698c23591b02b92fd3f5dba\",\r\n"
		    + "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDsa1nC7uK6AKOg\\nKudqiG7msRuLlycPD7QV+mxU1US3PWMqgo2+jIV6fo43sg+KQ7Szxcoj0ZqOBN5P\\nmHnml2LCeQW4Hc6SV999f6YJob8Wy5HrWmphl+DWOeMHb4+BwNb+HaOFHPtJyEBZ\\nSq2Aundm991+TjJS2mTUXb8T9+ekAUarMWO8DNwYFtRdcYMzuJrLehWFjeb7ahFe\\nDWtaX9vLpCxEFN7oGLSQcCony+fvNvzQWClbq5CezETOZq1SKavfbAQEVjRT/moz\\nLQ2r9D/uUgAr6T6GEvfvvJgg7tFfstqng9NRLP9v1lYl1nLh15V8+mEDzsg7/spX\\n2RNNc9LJAgMBAAECggEANW0E+dI2FkZys40+goqWVTeMu/zKbNLlpVnUBU+0mZwb\\nvs2F7puexlG3Hcr1TA+E1GNMDNAfFxzuiBoy4fMv9DaZHkROHDATZy7jfKW6oxIZ\\nxY/HHorN2lt2fIWvbmj/rKlR/JDSEJhaQlNn5gE9Xl2BBjvxj1ufEYal2YNaK2tX\\nNACMKR3bwSD24XOYQ8giTHxLMk+HZTnjeya2xB/U94ruBNEKHMQl1h3qdesfHUYJ\\nj1hy/itK7sjU7Ez27u56T+FnU38lE03OTxOVGDTdCiiGuiB6nW4PS6iRPvSZiIl9\\n0/BGQ47FRrP+K3ALBNp+Jr1RtNRLDTvBMOlhHY1uCQKBgQD5Ib1H+O7Q3bAeioRm\\niohOcrDY+4UdDhqUrAcSm5y4UymgJqrw6dbMT9vCQAIqj0cD6E4oRByfSNYwQXp6\\nUduHYJrsVceZcFLlpcYbTVRuhxFGg7FRKUqbqnA0hEO3ppaUrn7trzdrv3YnEG1G\\nekZ5I+6T8q1BBitDx6B5rlhRlwKBgQDy7+R2T19eUgA9roO1i7FION3Yr+CWZx4J\\numM9sisBmgO7ywMiIUYLnTNJCUKeFLmcpX1CSF42eheekufJj4tHD3e9VOfbqwbu\\nSl7RzqLFAljOWp6o+2sb+RsTLEeqR8Sp29+x4eXvsS75VdA48DxBa+WF4dZoLGix\\ndGp8ZIjKnwKBgQCdCg6Fcr4fPM7Kwafp90MqqBMgA5QKF5zbwICI0zx72Dgx8o3o\\nZI54pZIyJST4bO5n4SR7L0wXf9M8AdpRho3qDRQ7C2tRJkOq7M8u4JTrQwq5t0Or\\nlGqqK0qlYAZLmp/qwv/RlZRKwBqbTziCp4sKy4KVEfDyPxBAnnJioWqcqwKBgQDd\\ndaB/1aVrlzcPA6qfceO/oc9ivBnxIbl5Rsc3M9cOk7b5l3yYvVpcNgbNerqW3u/B\\ng/7m0I/J+4zSirrvvIJL2ibemqruTj94NKWxfzxUeeo42tUt2Fvw2So8VS5TZS87\\nZOvL/dEdqxjWRMsXTLCxNsm6PYC14F3j/usf0In+owKBgHXuZTOXl2vJIGaxI2ih\\nukCYx2zDTT3mEwMwfUCf1vTA7RH2vamwdIl4IdhbynMVeYJJqch9PDhuz5jRwEVl\\ngJjUq4aPiggtnqBL2qkoM9sLP5Pf2BkG2AqNGZOJZgLAtv2692FTBnr8yjv8Dgdi\\nJ/Zqo4ykuv2EM9L2c5klpi25\\n-----END PRIVATE KEY-----\\n\",\r\n"
		    + "  \"client_email\": \"firebase-adminsdk-tbqlj@digital-world-8d4cc.iam.gserviceaccount.com\",\r\n"
		    + "  \"client_id\": \"117588190160810571624\",\r\n"
		    + "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\r\n"
		    + "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\r\n"
		    + "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\r\n"
		    + "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-tbqlj%40digital-world-8d4cc.iam.gserviceaccount.com\",\r\n"
		    + "  \"universe_domain\": \"googleapis.com\"\r\n"
		    + "}";


	@PostConstruct
	public void init() throws IOException {
		/*
		 * FileInputStream serviceAccount = new FileInputStream( new
		 * ClassPathResource("firebase-adminsdk.json").getFile() );
		 * 
		 * FirebaseOptions options = FirebaseOptions.builder()
		 * .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		 * .setStorageBucket("digital-world-8d4cc.appspot.com") .build();
		 * 
		 * if (FirebaseApp.getApps().isEmpty()) { FirebaseApp.initializeApp(options); }
		 */
		try {
			InputStream serviceAccount = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setStorageBucket("digital-world-8d4cc.appspot.com").build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				System.out.println("✅ Firebase inicializado con credenciales embebidas.");
			}
		} catch (Exception e) {
			throw new RuntimeException("❌ Error al inicializar Firebase con JSON embebido", e);
		}
	}
}