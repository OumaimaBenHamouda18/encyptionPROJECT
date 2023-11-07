package encyptiondecryptionproject;

import java.util.Scanner;

public class menu {
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws Exception {
        Functions F = new Functions(); // Carga funciones de la clase EncryptionDecryption

        while (true) {
            System.out.println("Option menu:");
            System.out.println("1. File Encrypt");
            System.out.println("2. File Decrypt");
            System.out.println("3. Leave");
            System.out.print("Select an option: ");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:

                    F.encrypt_menu(); // Llama al método encrypt_menu
                    break;
                case 2:
                    F.decrypt_menu(); // Llama al método decrypt_menu
                    break;
                case 3:
                    System.out.println("¡Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Not valid option. Try again.");
            }
        }
    }
}



