package Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Models.Credentials;
import Utils.EncryptionUtils;

public class CredentialController {

	private Credentials credentials;
	private static final String CREDENTIALS_FILE = "credentials.json";
	private String absolutePath;
	
	public CredentialController() {
		absolutePath = (Paths.get("").toAbsolutePath().toString() + "\\resources\\" + CREDENTIALS_FILE);
	}

	public void loadCredentials() {
		Gson gson = new GsonBuilder().create();
		File file = new File(absolutePath);	
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				InputStream inputStream = new FileInputStream(absolutePath);
				Reader reader = new InputStreamReader(inputStream, "UTF-8");
				credentials = gson.fromJson(reader, Credentials.class);
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
	}
	
	public void setCredentials(String name, String plainPassword) throws UnsupportedEncodingException {
		credentials = new Credentials();
		credentials.setName(name);
		credentials.setEncryptedPassword(EncryptionUtils.encrypt(plainPassword));
	}
	
	public String getLoginName() {
		return credentials.getName();
	}
	
	public String getDecryptedPassword() throws UnsupportedEncodingException {
		return EncryptionUtils.decrypt(credentials.getEncryptedPassword());
	}
	
	public String getPlainPassword() {
		return credentials.getEncryptedPassword();
	}
	
	public void storeCredentials() throws FileNotFoundException, IOException {
		File file = new File(absolutePath);	
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(absolutePath), StandardCharsets.UTF_8)) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(credentials, writer);
		}
	}
	
	public void deleteCredentials() throws FileNotFoundException, IOException {
		credentials = new Credentials();
		credentials.setName("");
		credentials.setEncryptedPassword("");
		deleteCredentialFile();
	}
	
	public void deleteCredentialFile() {
		File file = new File(absolutePath);	
		if(file.exists()) {
			file.delete();
		}
	}
}
