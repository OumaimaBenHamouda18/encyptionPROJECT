/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package encyptiondecryptionproject;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;

public class AES {
    //definir la talla del secret key
    private static final int KEY_SIZE = 128;

    public static SecretKey initializeKey() throws Exception {

        //obtain a KeyGenerator object for a specific encryption algorithm.
        // no hace falta crear una instancia porque getInstance es un methodo estatico
        KeyGenerator generator = KeyGenerator.getInstance("AES");

        //This method initializes the key generator with the specified key size (in bits).
        //For AES, typical key sizes are 128, 192, or 256 bits.
        generator.init(KEY_SIZE);

        //This method generates a new secret key based on the initialization parameters set with init().
        SecretKey secretKey = generator.generateKey();
        return secretKey;
    }

    public static byte[] encrypt(String message, SecretKey secKey) throws Exception {
        //convertir el mensaje que quiero enciptar en bytes
        //Cipher provides a way to perform various cryptographic operations, including symmetric and asymmetric encryption, decryption,
        //and other cryptographic transformations.
        Cipher encryptionCipher = Cipher.getInstance("AES");

        //especificar la operacion(encryption/decryption y el key secreto )
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secKey);

        //hace la encripcion o la decripcion , genera un array de bytes
        return encryptionCipher.doFinal(message.getBytes());
    }

    public static String decrypt(String encyptedMessage, SecretKey secKey) throws Exception {
        // Crea una instancia de Cipher para el algoritmo AES
        Cipher decryptionCipher = Cipher.getInstance("AES");

        // Inicializa el Cipher en modo descifrado con la clave secreta especificada
        decryptionCipher.init(Cipher.DECRYPT_MODE, secKey);

        // Descifra el mensaje cifrado y convierte los bytes descifrados en una cadena
        byte[] decryptedBytes = decryptionCipher.doFinal(hexToByte(encyptedMessage));
        return new String(decryptedBytes);
    }


    public static byte[] encode(byte[] data) {
        // Devuelve la codificación Base64 de los datos de entrada
        return Base64.getEncoder().encode(data);
    }


    public static byte[] hexToByte(String txt) {
        // Check if the hex string has an even number of characters
        int length = txt.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have an even number of characters");
        }

        // Convert the hex string to a byte array
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
                hexString.append('0');  // Añade un 0 a la izquierda si el valor es de un solo dígito en hexadecimal.
            }
            hexString.append(hex);  // Agrega el dígito hexadecimal al resultado.
        }
        return hexString.toString();  // Devuelve la representación hexadecimal como una cadena.
    }


}
