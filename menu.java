package encyptiondecryptionproject;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class menu {
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
                    readPath();
                    break;
                case 2:
                    //decrypt_menu();
                case 3:
                    System.out.println("¡Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Not valid option. Try again.");
            }
        }
    }

    public static void decrypt_menu() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Path name where is the file to encrypt:");
        String data = sc.nextLine();

    }

    public static void encrypt_menu() {

    }

    public static void readPath(String path, boolean encriptar) {
        File dir = new File(path);
        String txt = "desencriptar";
        if (encriptar) {    txt = "encriptar";}

            if (dir.exists() && dir.isDirectory()) {

                String[] fileNames = dir.list();

                if (fileNames != null && fileNames.length > 0) {
                    System.out.println("Elige el fichero que deseas " + txt + ": ");
                    int i = 1;
                    for (String fileName : fileNames) {
                        System.out.println(i+fileName);
                        i++;
                    }
                } else {
                    System.out.println("Error 1");
                }
            } else {
                System.out.println("Error");
            }
        }
    }
}


