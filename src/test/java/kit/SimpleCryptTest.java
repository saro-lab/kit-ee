package kit;

import me.saro.kit.crypts.SimpleCrypt;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SimpleCryptTest {
    
    final static String CHARSET = "UTF-8";
    final static byte[] key = "d92liw93%1!9df0z".getBytes();
    final static byte[] iv = "12345qwert^%@!@f".getBytes();
    
    @Test
    public void cryptText() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        
        SimpleCrypt en = SimpleCrypt.encrypt("AES/CBC/PKCS5Padding", key, iv);
        SimpleCrypt de = SimpleCrypt.decrypt("AES/CBC/PKCS5Padding", key, iv);
        
        String text = "data";
        String encrypt = en.toHex(text.getBytes(Charset.forName(CHARSET)));
        String decrypt = new String(de.toBytesByHex(encrypt), Charset.forName(CHARSET));
        
        assertEquals(text, decrypt);
        
        text = "data";
        encrypt = en.toBase64(text.getBytes(Charset.forName(CHARSET)));
        decrypt = new String(de.toBytesByBase64(encrypt), Charset.forName(CHARSET));
        
        assertEquals(text, decrypt);
    }
    
//    @Test
//    public void cryptFile() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
//        Crypt en = Crypt.encrypt("AES/CBC/PKCS5Padding", key, iv);
//        Crypt de = Crypt.decrypt("AES/CBC/PKCS5Padding", key, iv);
//        
//        File data = new File("C:/Users/SARO/Desktop/abc.jpg");
//        File encf = new File("C:/Users/SARO/Desktop/abc_en.jpg");
//        File decf = new File("C:/Users/SARO/Desktop/abc_de.jpg");
//        
//        en.to(data, encf, true);
//        de.to(encf, decf, true);
//    }
}
