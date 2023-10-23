/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encyptiondecryptionproject;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import javax.crypto.Cipher;
import java.util.Base64;

/**
 *a
 * @author bemen3
 */
public class AES {

    /**
     * @param args the command line arguments
     */
   
    //definir la talla del secret key
    private static int KEY_SIZE=128;
   
    
 
    public static SecretKey  initializeKey()throws Exception{
        
        //obtain a KeyGenerator object for a specific encryption algorithm.
        // no hace falta crear una instancia porque getInstance es un methodo estatico
        KeyGenerator generator=KeyGenerator.getInstance("AES");
        
        //This method initializes the key generator with the specified key size (in bits).
        //For AES, typical key sizes are 128, 192, or 256 bits.
        generator.init(KEY_SIZE);
        
        
        
        //This method generates a new secret key based on the initialization parameters set with init().
        SecretKey secretKey=generator.generateKey();
        return secretKey;
   
    }
    
    public static byte[] encrypt(String message,SecretKey secKey) throws Exception{
        //convertir el mensaje que quiero enciptar en bytes
       

        
        //Cipher provides a way to perform various cryptographic operations, including symmetric and asymmetric encryption, decryption, 
       //and other cryptographic transformations.
       Cipher encryptionCipher=Cipher.getInstance("AES");


       //especificar la operacion(encryption/decryption y el key secreto )
       encryptionCipher.init(Cipher.ENCRYPT_MODE,secKey);


       //hace la encripcion o la decripcion , genera un array de bytes
       byte[] encryptedBytes= encryptionCipher.doFinal(message.getBytes());
       return encryptedBytes; 

    }
    
    
      public static String decrypt(String encyptedMessage,SecretKey secKey) throws Exception{
       
       Cipher decryptionCipher=Cipher.getInstance("AES");



       decryptionCipher.init(Cipher.DECRYPT_MODE,secKey);

       byte[] decryptedBytes= decryptionCipher.doFinal(hexToByte(encyptedMessage));
       return new String(decryptedBytes); 

    }
    
      
      
      
      
      
      
      
       public static byte[] encode(byte[] data) {
        return Base64.getEncoder().encode(data);
    }
       
    public static byte[] hexToByte(String txt) {
    int length = txt.length();
    if (length % 2 != 0) {
        throw new IllegalArgumentException("Hex string must have an even number of characters");
    }

    byte[] result = new byte[length / 2];
    for (int i = 0; i < length; i += 2) {
        result[i / 2] = (byte) ((Character.digit(txt.charAt(i), 16) << 4) + Character.digit(txt.charAt(i + 1), 16));
    }

    return result;
}
    public static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);
    for (byte b : hash) {
        String hex = Integer.toHexString(0xFF & b);
        if (hex.length() == 1) {
            hexString.append('0');
        }
        hexString.append(hex);
    }
    return hexString.toString();
}
     
    
   
    

 
     
     
   public static void main(String[] args)  {
        try{
         String plainText = "Hello World";
        System.out.println("Original Text:" + plainText);
        SecretKey secKey = initializeKey();
        System.out.println("AES Key (Hex Form):"+bytesToHex(secKey.getEncoded()));
        String encryptedText = bytesToHex(encrypt(plainText, secKey));
        System.out.println("Encrypted Text (Hex Form):"+encryptedText);
        String decryptedText = decrypt(encryptedText, secKey);
        System.out.println("Decrypted Text:"+decryptedText);}
        catch(Exception ignored){}
        
       
     
    }


}
