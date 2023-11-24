package encyptiondecryptionproject;

import javax.tools.OptionChecker;
import java.util.Scanner;

public class menu {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        Functions F = new Functions(); // Carga funciones de la clase EncryptionDecryption

        while (true) {
            F.Texto("yellow", "══════════════════════════════", true);
            F.Texto("yellow", "         Option Menu          ", true);
            F.Texto("yellow", "══════════════════════════════", true);
            F.Texto("yellow", "║ 1. File Encrypt              ", true);
            F.Texto("yellow", "║ 2. File Decrypt              ", true);
            F.Texto("yellow", "║ 3. Leave                     ", true);
            F.Texto("yellow", "║ Option : ", false);

            int opcion = F.numero();
            switch (opcion) {
                case 1:
                    F.encrypt_menu(); // Llama al método encrypt_menu
                    break;
                case 2:
                    F.decrypt_menu(); // Llama al método decrypt_menu

                    break;
                case 3:
                    F.Texto("yellow", "║ ", false);
                    F.Texto("cyan", Functions.ANSI_BOLD+"¡Goodbye!", true);
                    sc.close();
                    return;
                default:
                    F.Texto("yellow", "║ ", false);
                    F.Texto("red", Functions.ANSI_BOLD +"¡Not valid option. Try again.!", true);
            }
        }
    }
}



