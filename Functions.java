package encyptiondecryptionproject;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

public class Functions implements Interface_Proyecto {
    //global variables
    static String decryption_directory = AppConfig.getDecryptionDirectory();
    static String encryption_directory = AppConfig.getEncryptionDirectory();
    static String secretKeys_directory = AppConfig.getSecretKeysDirectory();

    // Implementa otros métodos de la interfaz
    //=============================================================================================================\\
    //                                          MENUS Y RELACIONADOS                                               \\
    //=============================================================================================================\\

    @Override
    public void encrypt_menu() {
        String File = choose_file_from_dir(encryption_directory, "encrypt");
        Scanner sc = new Scanner(System.in);
        String newFile;


        // Pide el nombre del archivo
        System.out.println("New encrypted file name: ");

        do {
            // Scanner , para el nombre del nuevo archivo, si este no tiene el .txt, se le agrega.
            newFile = sc.next();
            if (!newFile.contains(".txt")) {
                newFile = newFile + ".txt";
            }

            // si ya existe un archivo con ese nombre vuelve al loop del while.
            if (!checkFile(newFile, true)) {
                System.out.println("This file name already exists please type another filename: ");
            }
        } while (!checkFile(newFile, true));

        try {
            // Genera la key
            SecretKey secKey = initializeKey();
            // Obtiene el texto que se va a encriptar
            String textToEncrypt = getTextFromFile(encryption_directory + "\\" + File);
            // Se encrypta lo devuelve en bytes y lo pasamos a Hex para guardarlo.
            byte[] test = encrypt(textToEncrypt, secKey);
            String encryptedText = bytesToHex(test);
            //Se guarda en forma de string
            store_in_file(encryptedText, decryption_directory + "\\" + newFile);
            // Encode de la key, se pasa a string y se guarda en un archivo
            byte[] keyBytes = secKey.getEncoded();
            String keyString = Base64.getEncoder().encodeToString(keyBytes);
            store_in_file(keyString, secretKeys_directory + "\\key_" + newFile);

            // Se guardan 2 archivos separados la key y el archivo encriptado.
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
    @Override
    public void decrypt_menu() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose the file you to decrypt:");
        String fileName = choose_file_from_dir(decryption_directory, "decrypt");
        String secretKeyFileName = "";
        //leer and store the text to decrypt
        String decrptedtextfromfile = getTextFromFile(decryption_directory + "\\" + fileName);

        //leer and store the secret key base64 that will be used to decrypt
        System.out.println("Choose the file you want to retrieve the secret key from:");
        secretKeyFileName = choose_file_from_dir(secretKeys_directory, "retrieve the secret key from");
        System.out.print("Type the name you want to store the decrypted file with:");

        String newFileDecryptedName = sc.next();

        String keyString = getTextFromFile(secretKeys_directory + "\\" + secretKeyFileName);

        //generate a secret key using the base64 string converting it to an array of bytes
        SecretKey secKey = recuperar_secret_key(keyString);

        //decrypt
        String decryptado = decrypt(decrptedtextfromfile, secKey);

        //store decrypted text with the name specified
        store_in_file(decryptado, encryption_directory + "\\" + newFileDecryptedName);
    }

    @Override
    public boolean checkFile(String file, boolean encriptar) {
        // Revisa si ya existe un archivo con el mismo nombre en el mismo directorio, en el caso de que si devuelve un true, la booleana
        // sirve para que se pueda usar en ambos directorios.
        String path = encryption_directory;
        if (encriptar) {
            path = decryption_directory;
        }

        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    if (Objects.equals(fileName, file)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String choose_file_from_dir(String dirPath, String fileType) {
        File folderRef = new File(dirPath);
        String[] fileNames = null;
        String file = "";

        // Verificar si el directorio existe y es un directorio válido
        if (folderRef.exists() && folderRef.isDirectory()) {
            // Obtener los nombres de archivos y directorios en el directorio
            fileNames = folderRef.list();

            // Si no hay archivos en el directorio, mostrar un mensaje
            if (fileNames.length == 0) System.out.println("There's no files here. \nEmpty directory.");

            // Si hay archivos en el directorio
            if (fileNames != null && fileNames.length > 0) {
                // Mostrar los archivos en el directorio
                show_files(folderRef);
                // Permitir al usuario elegir un archivo con un escáner, basado en el tipo de archivo
                file = user_choose_file_with_scanner(fileNames, fileType);
            } else {
                System.out.println("There's no files in this directory");
            }
        } else {
            System.out.println("This directory doesn't exist");
        }

        return file;
    }
    @Override
    public void show_files(File folderRef) {

        int i = 1;
        String[] fileNames = folderRef.list();
        for (String file : fileNames) {
            System.out.println(i + "-" + file);
            i++;
        }
    }

    @Override
    public String user_choose_file_with_scanner(String[] fileNames, String fileType) {
        Scanner sc = new Scanner(System.in);
        int option;
        do {
            // Solicitar al usuario que ingrese la opción del archivo que desea
            option = sc.nextInt() - 1;
            try {
                // Verificar si la opción está fuera de los límites de la matriz de nombres de archivos
                if (option >= fileNames.length || option < 0) {
                    System.out.println("Incorrect Option!");
                    System.out.print("Choose the file you want to " + fileType + " : ");
                }
            } catch (IndexOutOfBoundsException e) {
            }
        } while (option >= fileNames.length || option < 0);

        // Devolver el nombre del archivo seleccionado por el usuario
        return fileNames[option];
    }

    //=============================================================================================================\\
    //                                                AES                                                          \\
    //=============================================================================================================\\

    //definir la talla del secret key
    private static final int KEY_SIZE = 128;

    @Override
    public SecretKey initializeKey() throws Exception {

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

    @Override
    public byte[] encrypt(String message, SecretKey secKey) throws Exception {
        //convertir el mensaje que quiero enciptar en bytes
        //Cipher provides a way to perform various cryptographic operations, including symmetric and asymmetric encryption, decryption,
        //and other cryptographic transformations.
        Cipher encryptionCipher = Cipher.getInstance("AES");

        //especificar la operacion(encryption/decryption y el key secreto )
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secKey);

        //hace la encripcion o la decripcion , genera un array de bytes
        return encryptionCipher.doFinal(message.getBytes());
    }

    @Override
    public  String decrypt(String encyptedMessage, SecretKey secKey) throws Exception {
        // Crea una instancia de Cipher para el algoritmo AES
        Cipher decryptionCipher = Cipher.getInstance("AES");

        // Inicializa el Cipher en modo descifrado con la clave secreta especificada
        decryptionCipher.init(Cipher.DECRYPT_MODE, secKey);

        // Descifra el mensaje cifrado y convierte los bytes descifrados en una cadena
        byte[] decryptedBytes = decryptionCipher.doFinal(hexToByte(encyptedMessage));
        return new String(decryptedBytes);
    }

    @Override
    public byte[] encode(byte[] data) {
        // Devuelve la codificación Base64 de los datos de entrada
        return Base64.getEncoder().encode(data);
    }

    @Override
    public byte[] hexToByte(String txt) {
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

    @Override
    public String bytesToHex(byte[] hash) {
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

    //=============================================================================================================\\
    //                                          EncryptionDecryption                                               \\
    //=============================================================================================================\\

    @Override
    public Scanner openFile(String path) throws Exception {

        File fileRef = new File(path);
        Scanner sc = new Scanner(fileRef);
        if (!sc.hasNextLine()) {
            throw new Exception("File is empty");
        } else
            return sc;
    }

     @Override
    public  String getTextFromFile(String path) throws Exception {
        Scanner sc = null;
        try {
            sc = openFile(path);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
         String textToEncrypt = "";
        if (sc != null)
            while (sc.hasNextLine())
                textToEncrypt += sc.nextLine();

        return textToEncrypt;
    }

    @Override
    public void store_in_file(String textToStore, String path) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(textToStore);
        bw.close();
        System.out.println("Succes, saving inf " + path);
    }

    @Override
    public SecretKey recuperar_secret_key(String keyString) throws Exception {

        //convert secret key from string to bytes
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        //define key specification
        // Create a SecretKey from the keyBytes
        return new SecretKeySpec(keyBytes, "AES");
    }

    //=============================================================================================================\\
    //                                                  UNSAFE                                                     \\
    //=============================================================================================================\\

    @Override
    static String encrypt(String textToEncrypt){
        String encryptedText="";
        for(int i=0;i<textToEncrypt.length();i++){
            encryptedText+=(int)(textToEncrypt.charAt(i))+"|";
        }
        return encryptedText;
    }


    static String decrypt(String encryptedText) throws Exception{
        String decryptedText="";
        int ascciiValue;
        for(int i=0;i<encryptedText.length();i++){
            for (int j=i;j<=encryptedText.length();j++){
                if(encryptedText.charAt(j)== '|'){
                    ascciiValue=Integer.parseInt(encryptedText.substring(i,j));
                    decryptedText+= (char) ascciiValue;
                    j++;
                    i=j;
                }

            }

        }
        return decryptedText;
    }
}



