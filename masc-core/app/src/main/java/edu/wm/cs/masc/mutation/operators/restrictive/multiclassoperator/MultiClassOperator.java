package edu.wm.cs.masc.mutation.operators.restrictive.multiclassoperator;

/*import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BrokenCryptoABMCCase1 {
    public static void main (String [] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        BrokenCryptoABMC1 bc = new BrokenCryptoABMC1();
        String crypto = "DES/ECB/PKCS5Padding";
        String cryptokey = "DES";
        bc.go(crypto,cryptokey);
    }
} */


/*import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BrokenCryptoABMC1 {
    public void go(String crypto, String cryptoKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGen = KeyGenerator.getInstance(cryptoKey);
        SecretKey key = keyGen.generateKey();
        Cipher cipher = Cipher.getInstance(crypto);
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}*/

//public class MultiClassOperator extends AMultiClassOperator {

    //public MultiClassOperator(MultiClassProperties p){}


    //@Override
    //public String mutation() {
    //    return null;
    //}
//}




