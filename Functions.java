package encyptiondecryptionproject;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

public class Functions implements Interface_Proyecto {
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";
    //definir la talla del secret key
    private static final int KEY_SIZE = 128;
    //global variables
    static String decryption_directory = AppConfig.getDecryptionDirectory();
    static String encryption_directory = AppConfig.getEncryptionDirectory();
    static String secretKeys_directory = AppConfig.getSecretKeysDirectory();
    static String pdf_directory = AppConfig.getPDFDirectory();


    // Implementa otros métodos de la interfaz
    //=============================================================================================================\\
    //                                          MENUS Y RELACIONADOS                                               \\
    //=============================================================================================================\\
    static Scanner sc = new Scanner(System.in);

    @Override
    public void encrypt_menu() throws Exception {
        String File = choose_file_from_dir(encryption_directory, "encrypt");
        String newFile;
        String encrypt = "";


        // Pide el nombre del archivo
        System.out.println(ANSI_CYAN + "New encrypted file name: " + ANSI_RESET);

        do {
            // Scanner , para el nombre del nuevo archivo, si este no tiene el .txt, se le agrega.
            newFile = sc.next();
            if (!newFile.contains(".txt")) {
                newFile = newFile + ".txt";
            }

            // si ya existe un archivo con ese nombre vuelve al loop del while.
            if (!checkFile(newFile, true)) {
                System.out.println(ANSI_PURPLE + "This file name already exists please type another filename: " + ANSI_RESET);
            }
        } while (!checkFile(newFile, true));

        String textToEncrypt = getTextFromFile(encryption_directory + "\\" + File, true);

        System.out.println(ANSI_GREEN + "Would you like to create and use a secret key?\n1-Yes\n2-No" + ANSI_RESET);
        switch (sc.nextInt()) {
            case 1:
                encrypt = encrypt_with_secret_key(textToEncrypt, newFile);
                break;
            case 2:
                encrypt = encrypt_without_secretkey(textToEncrypt);
                break;
        }
        store_in_file(encrypt, decryption_directory + "\\" + newFile);

    }

    @Override
    public String encrypt_without_secretkey(String encrypetText) {
        return Unsafe_encrypt(encrypetText);
    }

    @Override
    public String encrypt_with_secret_key(String encryptedText, String newFile) throws Exception {
        // Genera la key
        SecretKey secKey = initializeKey();
        // Se encrypta lo devuelve en bytes y lo pasamos a Hex para guardarlo.
        byte[] test = encrypt(encryptedText, secKey);
        String encryptedTextHex = bytesToHex(test);
        // Encode de la key, se pasa a string y se guarda en un archivo
        byte[] keyBytes = secKey.getEncoded();
        String keyString = Base64.getEncoder().encodeToString(keyBytes);
        store_in_file(keyString, secretKeys_directory + "\\key_" + newFile);
        return encryptedTextHex;
    }

    @Override
    public void decrypt_menu() throws Exception {
        System.out.println("Choose the file you to decrypt:");
        String decryptado = "";
        String fileName = choose_file_from_dir(decryption_directory, "decrypt");
        //leer and store the text to decrypt
        String decrptedtextfromfile = getTextFromFile(decryption_directory + "\\" + fileName, false);
        System.out.println("Do you have a secret key?\n1-Yes\n2-No");
        switch (sc.nextInt()) {
            case 1:
                decryptado = decrypt_with_secret_key(decrptedtextfromfile);
                break;
            case 2:
                decryptado = decrypt_without_secret_key(decrptedtextfromfile);
                break;
        }
        System.out.print("Type the name you want to store the decrypted file with:");
        String newFileDecryptedName = sc.next();

        if (!newFileDecryptedName.contains(".txt")) {
            newFileDecryptedName = newFileDecryptedName + ".txt";
        }

        //store decrypted text with the name specified
        store_in_file(decryptado, encryption_directory + "\\" + newFileDecryptedName);
    }

    @Override
    public String decrypt_without_secret_key(String encryptedText) throws Exception {
        return Unsafe_decrypt(encryptedText);
    }

    @Override
    public String decrypt_with_secret_key(String encryptedText) throws Exception {
        String secretKeyFileName = "";

        // Leer y almacenar la clave secreta base64 que se utilizará para descifrar
        System.out.println("Choose the file you want to retrieve the secret key from:");
        secretKeyFileName = choose_file_from_dir(secretKeys_directory, "retrieve the secret key from");

        String keyString = getTextFromFile(secretKeys_directory + "\\" + secretKeyFileName, false);

        // Generar una clave secreta utilizando la cadena Base64 y luego descifrar
        SecretKey secKey = recuperar_secret_key(keyString);
        String decryptado = "";
        try {
            decryptado = decrypt(encryptedText, secKey);
        } catch (Exception e) {
            System.out.println("secret key dosent correspond to the file you want to decrypt!");
            decrypt_with_secret_key(encryptedText);

        }
        return decryptado;

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
            if (fileNames.length == 0)
                System.out.println(ANSI_RED + "There's no files here. \nEmpty directory." + ANSI_RESET);

            // Si hay archivos en el directorio
            if (fileNames != null && fileNames.length > 0) {
                // Mostrar los archivos en el directorio
                show_files(folderRef);
                // Permitir al usuario elegir un archivo con un escáner, basado en el tipo de archivo
                file = user_choose_file_with_scanner(fileNames, fileType);
            } else {
                System.out.println(ANSI_RED + "There's no files in this directory" + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "This directory doesn't exist" + ANSI_RESET);
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
        int option;
        do {
            // Solicitar al usuario que ingrese la opción del archivo que desea
            option = sc.nextInt() - 1;
            try {
                // Verificar si la opción está fuera de los límites de la matriz de nombres de archivos
                if (option >= fileNames.length || option < 0) {
                    System.out.println(ANSI_RED + "Incorrect Option!" + ANSI_RESET);
                    System.out.print("Choose the file you want to " + fileType + " : ");
                }
            } catch (IndexOutOfBoundsException e) {
            }
        } while (option >= fileNames.length || option < 0);

        // Devolver el nombre del archivo seleccionado por el usuario
        return fileNames[option];
    }

    @Override
    public void menu_PDF() {
        String fileName = "";
        String dir = "";
        System.out.println(ANSI_CYAN + "Select the folder where you want to extract de file:\n1-Encrypted files\n2-Decrypted Files" + ANSI_RESET);
        switch (sc.nextInt()) {
            case 1:
                fileName = choose_file_from_dir(decryption_directory, "decrypted_files");
                dir = decryption_directory;
                break;
            case 2:
                fileName = choose_file_from_dir(encryption_directory, "encrypted_files");
                dir = encryption_directory;
                break;
        }
        storePDF(fileName, dir);
    }

    @Override
    public void storePDF(String file, String dir) {
        String directory = dir + "\\" + file;
        System.out.println(directory);
        String fileName = file;

        String pdfFilePath = pdf_directory + "\\" + fileName + ".pdf"; // donde va a guardar el doc
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDType1Font font = new PDType1Font(FontName.HELVETICA);
            int fontSize = 12;
            float margin = 70; // Adjust as needed
            float yPosition = page.getMediaBox().getHeight() - margin;
            float pageWidth = page.getMediaBox().getWidth() - 2 * margin;
            float lineHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

            // busca el archivo y lo lee, yo le estoy dando texto directamente, por eso hay error
            BufferedReader reader = new BufferedReader(new FileReader(directory));
            String line;
            while ((line = reader.readLine()) != null) {
                String wrappedLine = WordUtils.wrap(line, (int) (pageWidth / fontSize), "\n", false);
                String[] lines = wrappedLine.split("\n");

                for (String wrapped : lines) {
                    if (yPosition < margin + lineHeight) {
                        page = new PDPage();
                        document.addPage(page);
                        contentStream.close();
                        contentStream = new PDPageContentStream(document, page);
                        yPosition = page.getMediaBox().getHeight() - margin;
                    }
                    contentStream.setFont(font, fontSize);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(wrapped);
                    contentStream.endText();
                    yPosition -= lineHeight;
                }
            }
            reader.close();
            contentStream.close();

            document.save(pdfFilePath);
            document.close();

            System.out.println(ANSI_GREEN + "Conversion complete. PDF file saved to: " + pdfFilePath + ANSI_RESET);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //=============================================================================================================\\
    //                                                AES                                                          \\
    //=============================================================================================================\\

    @Override
    public String Colores(String color) {
        String defaultcolor = ANSI_RESET; //reset
        if (color == "black")
            defaultcolor = ANSI_BLACK;
        else if (color == "red")
            defaultcolor = ANSI_RED;
        else if (color == "green")
            defaultcolor = ANSI_GREEN;
        else if (color == "yellow")
            defaultcolor = ANSI_YELLOW;
        else if (color == "blue")
            defaultcolor = ANSI_BLUE;
        else if (color == "purple")
            defaultcolor = ANSI_PURPLE;
        else if (color == "cyan")
            defaultcolor = ANSI_CYAN;
        else if (color == "white")
            defaultcolor = ANSI_WHITE;
        return defaultcolor;
    }

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
    public String decrypt(String encyptedMessage, SecretKey secKey) throws Exception {
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
    public String getTextFromFile(String path, boolean encriptar) throws Exception {
        Scanner sc = null;
        try {
            sc = openFile(path);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        StringBuilder textToEncrypt = new StringBuilder();
        if (encriptar) {
            if (sc != null)
                while (sc.hasNextLine())
                    textToEncrypt.append(sc.nextLine()).append(System.lineSeparator());
        } else {
            if (sc != null)
                while (sc.hasNextLine())
                    textToEncrypt.append(sc.nextLine());
        }
        return String.valueOf(textToEncrypt);
    }

    @Override
    public void store_in_file(String textToStore, String path) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(textToStore);
        bw.close();
        System.out.println("Succes, saving in " + path);
    }


    @Override
    public SecretKey recuperar_secret_key(String keyString) {

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
    public String Unsafe_encrypt(String textToEncrypt) {
        String encryptedText = "";
        for (int i = 0; i < textToEncrypt.length(); i++) {
            encryptedText += (int) (textToEncrypt.charAt(i)) + "|";
        }
        return encryptedText;
    }


    @Override
    public String Unsafe_decrypt(String encryptedText) {
        String decryptedText = "";
        int ascciiValue;
        for (int i = 0; i < encryptedText.length(); i++) {
            for (int j = i; j <= encryptedText.length(); j++) {
                if (encryptedText.charAt(j) == '|') {
                    ascciiValue = Integer.parseInt(encryptedText.substring(i, j));
                    decryptedText += (char) ascciiValue;
                    j++;
                    i = j;
                }
            }
        }
        return decryptedText;
    }
}



