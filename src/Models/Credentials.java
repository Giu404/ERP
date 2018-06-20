package Models;

public class Credentials {
	
	private String name;
	private String encryptedPassword;
	
	public Credentials() {
		name = "";
		encryptedPassword = "";
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	
}
