package encyptiondecryptionproject;

import javax.crypto.SecretKey;
import java.io.File;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Scanner;

public class menu {


    static String decryption_directory= AppConfig.getDecryptionDirectory();
    static String encryption_directory= AppConfig.getEncryptionDirectory();
    static String secretKeys_directory= AppConfig.getSecretKeysDirectory();


    public static void main(String[] args) throws Exception {






        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Option menu:");
            System.out.println("1. File Encrypt");
            System.out.println("2. File Decrypt");
            System.out.println("3. Leave");
            System.out.print("Select an option: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    encrypt_menu();
                    break;
                case 2:
                    decrypt_menu();
                case 3:
                    System.out.println("¡Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Not valid option. Try again.");
            }
        }
    }

    public static void encrypt_menu() {
        String File = readPath(encryption_directory);
        Scanner sc = new Scanner(System.in);
        String newFile;

        System.out.println("New encypted file name: ");

        do {

            newFile = sc.next();
            if ( !newFile.contains(".txt")){
                newFile = newFile + ".txt";
                 }
            if (!checkFile(newFile, true)){
                System.out.println("This file name already exists please type another filename: ");
            }
        }while(!checkFile(newFile, true));
        EncyptionDecryptionProject.Encrypt(File, decryption_directory+"\\"+newFile);
    }



    public static void decrypt_menu() throws Exception {

        Scanner sc=new Scanner(System.in);


        System.out.println("Choose the file you want to decrypt: ");
        File folderRef= new File(decryption_directory);
        String fileName=readPath(folderRef);
        //leer and store the text to decrypt
        String decrptedtextfromfile=EncyptionDecryptionProject.getText(decryption_directory+"\\"+fileName);

        //leer and store the secret key base64 that will be used to decrypt
        String keyString=EncyptionDecryptionProject.getText(secretKeys_directory+"\\key_test.txt");

        //generate a secret key using the base64 string converting it to an array of bytes
        SecretKey secKey=EncyptionDecryptionProject.getSecretKey(keyString);

        //decrypt
        String decryptado=AES.decrypt(decrptedtextfromfile,secKey);

        String newFileDecryptedName= sc.next();

        //store decrypted text with the name specified
        EncyptionDecryptionProject.store_in_file(decryptado,encryption_directory +"\\"+newFileDecryptedName);


    }

















    public static boolean checkFile(String file, boolean encriptar){



        String path = encryption_directory;
        if (encriptar) {
            path = encryption_directory;
        }

        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();
            if (fileNames != null && fileNames.length > 0) {
                for (String fileName : fileNames) {
                    if (Objects.equals(fileName, file)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }




    //*****************************cambiar to   choose_file_option*******************//


    public static String readPath(File dir)

    {   Scanner sc=new Scanner(System.in);
        String [] fileNames=null;
        int option=-1;
        if(dir.exists() &&dir.isDirectory()){
            fileNames=dir.list();
            if(fileNames.length==0) System.out.println("There s no files to decrypt. Folder empty.");

            if(fileNames!=null && fileNames.length>0){
                int i=1;
                for(String file : fileNames){
                    System.out.println(i+"-"+file);
                    i++;
                }

                do{
                    System.out.println("Choose the file you want to decrypt: ");
                    option= sc.nextInt()-1;
                    try{
                        System.out.println(fileNames[option]);
                    }catch(IndexOutOfBoundsException e){
                        System.out.println("Incorrect Option!");
                    }


                }while((option)>= fileNames.length);
            }
            else {
                System.out.println("No exsisten archivos en este directorio");
            }

        }
        else {
        System.out.println("Este directorio no exsiste");
    }

        return fileNames[option];
    }
}

