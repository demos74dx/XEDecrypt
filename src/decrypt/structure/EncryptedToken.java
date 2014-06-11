package decrypt.structure;

public class EncryptedToken {

	private int token;
	private int count;
	
	public EncryptedToken(int token) {
		this.token = token;
		this.count = 1;
	}
	
	public int getEncryptedToken() {
		return token;
	}
	
	public String decryptToken(int key) {
		char chaz = (char) (token - key);
		return String.valueOf(chaz);
	}
	
	public int getCount() {
		return count;
	}
	
	public void increment() {
		count++;
	}
	
}
