package encyptiondecryptionproject;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.w3c.dom.Text;

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
    public static final String ANSI_BOLD = "\u001b[1m";

    private static final int AES_KEY_SIZE = 128;
    private static final String TXT_EXTENSION = ".txt";

    // Global variables
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
        String file, textToEncrypt, newFile, encrypt = "";

        Texto("yellow", "║" + ANSI_RESET + "Choose the file to encrypt:" + ANSI_YELLOW + "\n╠═════════════════════════════", true);

        do {
            file = choose_file_from_dir(encryption_directory, "encrypt");
            textToEncrypt = getTextFromFile(encryption_directory + "\\" + file, true);

            if (textToEncrypt == null)
                Texto("red", ANSI_YELLOW + "║" + ANSI_BOLD + "❌ Error, empty file, select another.", true);

        } while (textToEncrypt == null);

        newFile = newNameFile(decryption_directory);

        Texto("yellow", "║ " + ANSI_RESET + "Would you like to create and use a secret key?", true);
        Texto("yellow", "║ 1." + ANSI_RESET + " Yes", true);
        Texto("yellow", "║ 2." + ANSI_RESET + " No", true);
        Texto("yellow", "║ Option: ", false);

        int option = OptionFunc();
        switch (option) {
            case 1:
                encrypt = encrypt_with_secret_key(textToEncrypt, newFile);
                break;
            case 2:
                encrypt = encrypt_without_secretkey(textToEncrypt);
                break;
            default:
                Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "❌ Not a valid option. Try Again.", true);
                Texto("yellow", "║ Option: ", false);
        }

        store_in_file(encrypt, decryption_directory + "\\" + newFile);
        askPDF(newFile, decryption_directory);
    }
    @Override
    public int OptionFunc() {
        int option;
        do {
            option = numero();

            if (option != 1 && option != 2) {
                Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "❌ Not a valid option. Try Again.", true);
                Texto("yellow", "║ Option: ", false);
            }

        } while (option != 1 && option != 2);
        return option;
    }
    @Override
    public String newNameFile(String dir) {
        String newFile;
        String extension = TXT_EXTENSION;
        if (dir == pdf_directory)
            extension = ".pdf";
        Texto("yellow", "║ " + ANSI_CYAN + "New file name : ", false);
        do {
            newFile = sc.next();
            if (!newFile.contains(extension)) {
                newFile = newFile + extension;
            }

            if (!checkFile(newFile, dir)) {
                Texto("yellow", "║ " + ANSI_RESET + "This file name already exists. Please type another filename: ", false);
            }

        } while (!checkFile(newFile, dir));
        return newFile;
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
        String fileName, decrptedtextfromfile, newFileDecryptedName, decryptado = "";

        Texto("yellow", "║" + ANSI_RESET + "Choose the file to decrypt :" + ANSI_YELLOW + "\n╠═════════════════════════════", true);
        do {
            fileName = choose_file_from_dir(decryption_directory, "decrypt");
            decrptedtextfromfile = getTextFromFile(decryption_directory + "\\" + fileName, false);
            if (decrptedtextfromfile == null)
                Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + " ❌ Error, empty file, select another.", true);
        } while (decrptedtextfromfile == null);
        //leer and store the text to decrypt

        Texto("yellow", "║ " + ANSI_RESET + "Do you have a secret key?", true);
        Texto("yellow", "╠═════════════════════════════", true);
        Texto("yellow", "║ 1." + ANSI_RESET + " Yes", true);
        Texto("yellow", "║ 2." + ANSI_RESET + " No", true);
        Texto("yellow", "║ Option: ", false);

        int option = OptionFunc();

        decryptado = switch (option) {
            case 1 -> decrypt_with_secret_key(decrptedtextfromfile, fileName);
            case 2 -> decrypt_without_secret_key(decrptedtextfromfile);
            default -> throw new IllegalStateException("Unexpected value: " + option);
        };
        if (decryptado == null) {
            decrypt_menu();
            return;
        }

        newFileDecryptedName = newNameFile(encryption_directory);
        //store decrypted text with the name specified
        store_in_file(decryptado, encryption_directory + "\\" + newFileDecryptedName);
        askPDF(newFileDecryptedName, encryption_directory);
    }


    @Override
    public void askPDF(String pdfName, String dir) {
        Texto("yellow", "║ "+ANSI_RESET + "Would you like to store this in PDF? Y/N", true);
        Texto("yellow", "║ Answer : ", false);
        String respuesta = sc.next();
        sc.nextLine();
        if (respuesta.equalsIgnoreCase("Y")) {
            if(pdfName.contains(".txt"))
                pdfName = pdfName.replace(".txt", "");
            storePDF(pdfName, dir + "\\" + pdfName + ".txt");
        }
    }

    @Override
    public String decrypt_without_secret_key(String encryptedText) throws Exception {
        try {
            return Unsafe_decrypt(encryptedText);
        } catch (Exception e) {
            Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "Error, this file needs a key", true);
            return null; // O devuelve un valor predeterminado en caso de error.
        }
    }



    @Override
    public String decrypt_with_secret_key(String encryptedText, String FileNameKey) throws Exception {
        String secretKeyFileName;
        // Leer y almacenar la clave secreta base64 que se utilizará para descifrar
        Texto("yellow", "║ "+ANSI_RESET+"Choose the file you want to retrieve the secret key from : ", true);
        Texto("yellow", "╠═════════════════════════════", true);
        secretKeyFileName = choose_file_from_dir(secretKeys_directory, FileNameKey);

        String keyString = getTextFromFile(secretKeys_directory + "\\" + secretKeyFileName, false);

        // Generar una clave secreta utilizando la cadena Base64 y luego descifrar
        SecretKey secKey = recuperar_secret_key(keyString);
        String decryptado = "";
        try {
            decryptado = decrypt(encryptedText, secKey);
        } catch (Exception e) {
            Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + " ❌ Secret key doesn't correspond to the file you want to decrypt!", true);
            decrypt_with_secret_key(encryptedText, null);
        }
        return decryptado;

    }

    @Override
    public boolean checkFile(String file, String dire) {
        // Revisa si ya existe un archivo con el mismo nombre en el mismo directorio, en el caso de que si devuelve un true, la booleana
        // sirve para que se pueda usar en ambos directorios.

        File dir = new File(dire);
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
        String[] fileNames;
        String file = "";

        // Verificar si el directorio existe y es un directorio válido
        if (folderRef.exists() && folderRef.isDirectory()) {
            // Obtener los nombres de archivos y directorios en el directorio
            fileNames = folderRef.list();

            // Si no hay archivos en el directorio, mostrar un mensaje
            if (fileNames != null && fileNames.length == 0)
                Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "There's no files here. Empty directory.", true);

            // Si hay archivos en el directorio
            if (fileNames != null && fileNames.length > 0) {
                // Mostrar los archivos en el directorio

                    show_files(folderRef, fileType);

                // Permitir al usuario elegir un archivo con un escáner, basado en el tipo de archivo
                file = user_choose_file_with_scanner(fileNames, fileType);
            } else {
                Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "There's no files in this directory.", true);
            }
        } else {
            Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "This directory doesn't exist", true);
        }

        return file;
    }

    @Override
    public void show_files(File folderRef, String KeyFile) {
        int i = 1;
        String[] fileNames = folderRef.list();
        if (fileNames != null) {
            for (String file : fileNames) {
                        if (file.contains("key_"+KeyFile)) {
                            Texto("yellow", "║ " + ANSI_GREEN + i + ". " + file + " - Suggested file", true);
                    } else {
                        Texto("yellow", "║ " + ANSI_RESET + i + ". " + file, true);
                    }

                i++;
            }
        }
        Texto("yellow", "║ File : ", false);
    }

    @Override
    public String user_choose_file_with_scanner(String[] fileNames, String fileType) {
        int option;
        do {
            // Solicitar al usuario que ingrese la opción del archivo que desea
            option = numero() - 1;
            try {
                // Verificar si la opción está fuera de los límites de la matriz de nombres de archivos
                if (option >= fileNames.length || option < 0) {
                    Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "❌ Not a valid option. Please enter a number", true);
                    Texto("yellow", "║ Option : ", false);
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        } while (option >= fileNames.length || option < 0);

        // Devolver el nombre del archivo seleccionado por el usuario
        return fileNames[option];
    }

    @Override
    public void storePDF(String file, String dir) {
        String FileName = newNameFile(pdf_directory);
        String pdfFilePath = pdf_directory + "\\" + FileName; // donde va a guardar el doc

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDType1Font font = new PDType1Font(FontName.HELVETICA);
            int fontSize = 12;
            float margin = 30; // Adjust as needed
            float yPosition = page.getMediaBox().getHeight() - margin;
            float pageWidth = page.getMediaBox().getWidth() - 2 * margin;
            float lineHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

            // busca el archivo y lo lee, yo le estoy dando texto directamente, por eso hay error
            BufferedReader reader = new BufferedReader(new FileReader(dir));
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


            Texto("yellow", "║ " + ANSI_BOLD + ANSI_GREEN + "✅ Conversion complete. PDF file saved in " + pdfFilePath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //=============================================================================================================\\
    //                                                AES                                                          \\
    //=============================================================================================================\\


    @Override
    public void Texto(String color, String texto, boolean saltoLinea) {
        String defaultColor = ANSI_RESET; // reset

        switch (color) {
            case "black" -> defaultColor = ANSI_BLACK;
            case "red" -> defaultColor = ANSI_RED;
            case "green" -> defaultColor = ANSI_GREEN;
            case "yellow" -> defaultColor = ANSI_YELLOW;
            case "blue" -> defaultColor = ANSI_BLUE;
            case "purple" -> defaultColor = ANSI_PURPLE;
            case "cyan" -> defaultColor = ANSI_CYAN;
            case "white" -> defaultColor = ANSI_WHITE;
            case "bold" -> defaultColor = ANSI_BOLD;
        }

        if (saltoLinea) {
            System.out.println(defaultColor + texto + ANSI_RESET);
        } else {
            System.out.print(defaultColor + texto + ANSI_RESET);
        }
    }

    @Override
    public int numero() {
        while (!sc.hasNextInt()) {
            sc.next();
            Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "❌ Not a valid option. Please enter a number", true);
            Texto("yellow", "║ Option : ", false);
        }
        return sc.nextInt();
    }

    @Override
    public SecretKey initializeKey() throws Exception {

        //obtain a KeyGenerator object for a specific encryption algorithm.
        // no hace falta crear una instancia porque getInstance es un methodo estatico
        KeyGenerator generator = KeyGenerator.getInstance("AES");

        //This method initializes the key generator with the specified key size (in bits).
        //For AES, typical key sizes are 128, 192, or 256 bits.
        generator.init(AES_KEY_SIZE);

        //This method generates a new secret key based on the initialization parameters set with init().
        return generator.generateKey();
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
            return null;
        } else
            return sc;
    }

    @Override
    public String getTextFromFile(String path, boolean encriptar) throws Exception {
        Scanner sc = null;
        try {
            sc = openFile(path);
            if (sc == null)
                return null;
        } catch (FileNotFoundException e) {
            Texto("yellow", "║ " + ANSI_BOLD + ANSI_RED + "❌ Error: " + e.getMessage(), true);
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
        Texto("yellow", "║ " + ANSI_BOLD + ANSI_GREEN + "✅ Succes, saving in " + path, true);
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
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < textToEncrypt.length(); i++) {
            encryptedText.append((int) (textToEncrypt.charAt(i))).append("|");
        }
        return encryptedText.toString();
    }


    @Override
    public String Unsafe_decrypt(String encryptedText) {


        ///////////////////// try aqui para arreglar error


        StringBuilder decryptedText = new StringBuilder();
        int ascciiValue;
        for (int i = 0; i < encryptedText.length(); i++) {
            for (int j = i; j <= encryptedText.length(); j++) {
                if (encryptedText.charAt(j) == '|') {
                    ascciiValue = Integer.parseInt(encryptedText.substring(i, j));
                    decryptedText.append((char) ascciiValue);
                    j++;
                    i = j;
                }
            }
        }
        return decryptedText.toString();
    }
}



