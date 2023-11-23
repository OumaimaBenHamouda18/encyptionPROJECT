package encyptiondecryptionproject;

import javax.tools.OptionChecker;
import java.util.Scanner;

public class menu {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        Functions F = new Functions(); // Carga funciones de la clase EncryptionDecryption

        while (true) {
            System.out.println(F.Colores("white") + "Option menu:" + F.Colores(null));
            System.out.println(F.Colores("blue") + "1. File Encrypt");
            System.out.println(F.Colores("blue") + "2. File Decrypt");
            System.out.println(F.Colores("red") + "3. Leave");
            System.out.print(F.Colores("white") + "Select an option: " + F.Colores(null));


            int opcion = F.numero();
            switch (opcion) {
                case 1:

                    F.encrypt_menu(); // Llama al método encrypt_menu
                    break;
                case 2:
                    F.decrypt_menu(); // Llama al método decrypt_menu
                    break;
                case 3:
                    System.out.println(F.Colores("cyan") + "¡Goodbye!");
                    sc.close();
                    return;
                default:
                System.out.println(F.Colores("red") + "Not valid option. Try again." + F.Colores(null));

            }
        }
    }
}



