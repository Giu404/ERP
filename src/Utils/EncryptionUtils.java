package Utils;

import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

import net.sourceforge.jdpapi.*;

public class EncryptionUtils {
	
	static {
		String absolutePath = (Paths.get("").toAbsolutePath() + "\\lib\\jdpapi64.dll");
		System.load(absolutePath);
	}
	
	public static String encrypt(String plainString) throws UnsupportedEncodingException {
		DataProtector dataProtector = new DataProtector();
		byte[] encryptedBytes = dataProtector.protect(plainString);
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(encryptedBytes);
	}
	
	public static String decrypt(String encryptedString) throws UnsupportedEncodingException {
		DataProtector dataProtector = new DataProtector();
		Decoder d = Base64.getDecoder();
		byte[] bytes = d.decode(encryptedString);
		return dataProtector.unprotect(bytes);
	}
}
