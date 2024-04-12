/*
@authors: Tymur Kulivar y Claudiu Iovanescu
@date: 26/11/2023 23:59:59
@summary: Juego de la ruleta de la suerte en la terminal 
*/

package ruletaSuerte;
import java.util.Scanner;
import java.util.Random;
 
public class Ruleta {
    static Scanner sc = new Scanner(System.in);
    
	// Definiendo variables globales necesarias
    static String panel = "";
    static String tip = "";
    static String panelHidden = "";
    static String lettersUsed = "";
    static boolean guessed = false;
    

    public static void main(String[] args) throws InterruptedException {
        final int numPlayers = getNumPlayers(3); // Limite de 3 jugadores

		// Definiendo el dinero de los jugadores
        int player1Total = 0;
        int player2Total = 0;
        int player3Total = 0;
        int winnerPanel = 0;

        boolean stop = false;
        panel = "";
        tip = "";
        panelHidden = "";
        lettersUsed = "";
        guessed = false;
        
		// Bucle del juego
        do { 
            int player1Panel = 0;
            int player2Panel = 0;
            int player3Panel = 0;
            panel = newPanel();
            tip = newTip();
            panelHidden = toStars(panel);
            
			// Bucle de un panel
            do { 
                for (int i = 1; i <= numPlayers; i++) {
                    switch (i){
                        case 1:
                            player1Panel = play(player1Panel, 1);
                            break;
                        case 2:
                            player2Panel = play(player2Panel, 2);
                            break;
                        case 3:
                            player3Panel = play(player3Panel, 3);
                            break;
                    }
                    
                    if (guessed == true){
                        winnerPanel = i;
                        i=numPlayers;
                    }
                    else {
                        for (int j = 1; j <= numPlayers; j++) {
                            switch (j){
                                case 1:
                                    System.out.printf("Jugador 1: %d   ", player1Panel);
                                    break;
                                case 2:
                                    System.out.printf("Jugador 2: %d   ", player2Panel);
                                    break;
                                case 3:
                                    System.out.printf("Jugador 3: %d   ", player3Panel);
                                    break;
                            }
                        }
                        System.out.println();
                    }
                }
            } while (guessed == false);
            
            switch (winnerPanel) {
                case 1:
                    System.out.printf("El jugador 1 ha ganado y ha obtenido %d euros%n",player1Panel);
                    player1Total += player1Panel;
                    break;
                case 2:
                    System.out.printf("El jugador 2 ha ganado y ha obtenido %d euros%n",player2Panel);
                    player2Total += player2Panel;
                    break;
                case 3:
                    System.out.printf("El jugador 3 ha ganado y ha obtenido %d euros%n",player3Panel);
                    player3Total += player3Panel;
                    break;
            }

            System.out.println("¿Quiere crear nuevo panel? \n1.Si\n2.No ");
            char option = getOption();
            if (option == '1') {
                restartPanel();
            }
            else if (option == '2') {
                stop = true;
            }

        } while(stop == false);

        results(numPlayers, player1Total, player2Total, player3Total);
    }
    
    static int play(int playerMoney, int index) throws InterruptedException{
        char letter;
        System.out.printf("%nHola, jugador %d%n",index);
        do{
            System.out.printf("%s (%s)%n%n",panelHidden,tip);
            System.out.println("¿Quiere resolver el panel, o solo decir una letra?\n1.Panel\n2.Letra");
            char option = getOption();
            if (option == '1') {
                guessed = guessPanel();
                if (guessed == false) {
                	System.out.println("No es la frase correcta");
                }
                break;
            }
            else {
                System.out.println("Vamos a girar la ruleta");
                int ruletaResult = random();
                if (ruletaResult == 5) {
                    System.out.println("Has perido todo el dinero en este panel");
                    playerMoney=0;
                    break;
                }
                System.out.printf("Las letras usadas son: %s%n", lettersUsed.equals("") ? "No hay" : lettersUsed);
                
                letter = getLetter();
                panelHidden = toStars(letter);
                
                if (numberOfOcurrencies(letter, panel)!=0) {
                    System.out.printf("Has ganado %d en este panel%n", (playerMoney+ruletaResult*numberOfOcurrencies(letter, panel)));
                    System.out.printf("Continuamos con el jugador %d%n", index);
                    playerMoney+=ruletaResult*numberOfOcurrencies(letter, panel);
                }
                else {
                    System.out.printf("Has perdido el turno, en el panel no hay letra %c%n", letter);
                }
                if (panelHidden.equals(panel)) {
                    guessed = true;
                    break;
                }
            }
            
        }while(numberOfOcurrencies(letter, panel)!=0);
        return playerMoney;
    }
    

    static String valueToString(int random){
        switch (random) {
            case 0,1,2,3,4,6:
                return (String.valueOf(random*25));
            
            case 5:
                return ("qbr");
            default:
                System.out.println("Error imposible, pero yo debo escribir default");
                return "";
        }
    }
    
    static int randomReturn(int input) {
        switch (input) {
            case 0,1,2,3,4,6:
                return (input*25);
            case 5:
                return 5;
            default:
                return 0;
        }
    }
    static int random() throws InterruptedException{
        Random r = new Random();
        int random = r.nextInt(7);
        for (int i = 1; i <= 10; i++, random++) {
            random = random == 7 ? 0 : random;
            int next = random == 6 ? 0 : random+1;
            int last = random == 0 ? 6 : random-1;
            int last2 = last == 0? 6 : last-1;
            ruletaPrint(next, random, last, last2);
        }
        //System.out.println(random);
        return randomReturn(random-1);
    }

	// Mostrar animacion ruleta
    static void ruletaPrint(int next, int random, int last, int last2) throws InterruptedException {
    	System.out.printf(
			"J7???7!:%n"+        
			":     ^7?7:%n"+     
			":  %3s   .?Y^%n"+   
			":      .:^  J7%n"+  
			":    .:^     ??%n"+ 
			":  .~^        Y!%n"+
			"^^!~.   %3s   ^P        Panel: " + panelHidden + " (" + tip + ")%n"+
			"P7~^^::...    .G%n"+
			"!7     ^~~::::!P        Letras usadas: " + lettersUsed + "%n"+
			" ~7           5!%n"+
			"  ~7   %3s   7?%n"+
			"   ~?      .?7%n"+ 
			"    ^?   :7?^%n"+   
			"     ~J~77^%n"+  
			"!!7777!^%n%n",   
			valueToString(last2), valueToString(last),  valueToString(random));
    	Thread.sleep(150);
    	
    	System.out.printf(
			"J7???7!:%n"+   
			":     ^7?7:%n"+
			"%3s  77   7?^%n"+      
			"    7^     7J:%n"+    
			"  .7: %3s   :Y^%n"+   
			" :7.         .5.%n"+  
			"~7__.::::~~~::Y7        Panel: " + panelHidden + " (" + tip + ")%n"+
			"P!^^^^        ~J%n"+  
			":^~^    %3s   77        Letras usadas: " + lettersUsed + "%n"+
			"   ^~~.      .Y.%n"+  
			"     ^~!.   :J^%n"+   
			" %3s    ~~!J?:%n"+    
			"        .!!^%n"+      
			"       ~77^%n"+  
			"!!7?!7!^%n%n",
            valueToString(last2), valueToString(last),  valueToString(random), valueToString(next));
    	Thread.sleep(150);
    	
    	System.out.printf(
        		"77?577!^%n"+ 
    			"  !7  ^!?7^%n"+
    			"  Y.     :7J:%n"+
    			" !7  %3s   .J7%n"+  
    			" J.      .:^ 57%n"+ 
    			"~!   .~!^     5^%n"+
    			"Y^.:^         ~Y        Panel: " + panelHidden + " (" + tip + ")%n"+
    			"G7~.    %3s   ^P%n"+
    			"J  ^~:.       !J        Letras usadas: " + lettersUsed + "%n"+
    			"!~    ^~:.    5:%n"+
    			".J       ^~:.P~%n"+
    			" 7^  %3s   ^J^%n"+
    			" :J       :?7%n"+ 
    			"  J^  .:77~%n"+  
    			"77??!:~^%n%n", 
    			valueToString(last), valueToString(random),  valueToString(next));
        	Thread.sleep(150);
    }
    
	// Obtener numero de jugadores (MAXIMO 3)
    static int getNumPlayers(int MAX_PLAYERS) {
        char number;
        do {
            System.out.printf("Introduce el numero de jugadores (hasta %d):%n", MAX_PLAYERS);
            number = sc.nextLine().charAt(0);
            System.out.print((number < '1' || number > (MAX_PLAYERS + '0'))? "El numero invalido: " : "");
        } while (number < '1' || number > (MAX_PLAYERS + '0')); //int a char, o Character.forDigit(i, RADIX);
        return (int)number-'0';  //char a int, sumamos ASCII '0'  + numero de posiciones;
    }

	// Crear nuevo panel 
    static String newPanel() {
        System.out.println("Escribe una frase secreta: ");
        String panel = sc.nextLine();
        return panel.toLowerCase();
    }
    
	// Crear nueva pista
    static String newTip() {
        System.out.println("Escribe la pista: ");
        String tip = sc.nextLine();
        return tip;
    }
    
	// Menu opciones al jugar un panel (resolver panel o letra)
    static char getOption() {
        char option;
        do {
            option = sc.nextLine().charAt(0);
            System.out.print((option!= '1' && option!= '2') ? "La opción invalida: " : "");
        }while(option!= '1' && option!= '2');
        return option ;
    }

	// Obtener input letra de jugador
    static char getLetter() {
        char letter;
        do {
            //System.out.print(lettersUsed.indexOf(letter) >= 0 ? "(Letra repetida) " : "");
            System.out.println("Elige la letra: ");
            letter = sc.nextLine().charAt(0);
            System.out.print(numberOfOcurrencies(letter, lettersUsed)!=0 ? "(Letra repetida) " : ""); //si no en panel- avisa
            System.out.print(Character.isLowerCase(letter)? "" : "(Letra invalida) "); //si no minuscula- avisa
        }while(!(numberOfOcurrencies(letter, lettersUsed) == 0 && Character.isLowerCase(letter))); //si no en panel / no minuscula
        lettersUsed += letter;
        return letter;
    } 
    
	// Convertir panel visible a oculto
    static String toStars(String panel) {
        for (char i: panel.toCharArray()) {
            if (i!=' ' && i!=',' && i!='-') {
                panel = panel.replace(i, '*');
            }
        }
        return panel;   
    }

	// Revelar letra del panel oculto
    static String toStars(char letter) {     
        for (int i = 0; i < panel.length(); i++) {
            if (panel.charAt(i) == letter) {
                panelHidden = panelHidden.substring(0,i) + letter + panelHidden.substring(i+1,panel.length());
            }
        }
        return panelHidden;
    }

	// Opcion si jugador quiere adivinar todo el panel
    static boolean guessPanel() {
        System.out.println("Prueba adivinar la frase ");
        String guess = sc.nextLine();
        if (guess.equals(panel)) 
            return true;
        
        else 
            return false;
    }

	// Contar nº de letras iguales en un String dado un char
    static int numberOfOcurrencies(char letter, String text) {
        int counter = 0;
        for (char i: text.toCharArray()) {
            if (i == letter) {
                counter++;}
        }
        return counter;
    }

	// Resetear panel
    static void restartPanel() {
        panel = "";
        tip = "";
        panelHidden = "";
        lettersUsed = "";
        guessed = false;
    }

	// Comprobar que jugador ha ganado 
    static int winner(int player1Total, int player2Total, int player3Total) {
        if (player1Total > player2Total && player1Total > player3Total) {
            return 1;
        }
        else if (player2Total > player1Total && player2Total > player3Total) {
            return 2;
        }
        else if (player3Total > player1Total && player3Total > player2Total) {
            return 3;
        }
        else {
            return 0;
        }
    }


	// Mostrar resultados
    static void results(int numPlayers, int player1Total, int player2Total, int player3Total) {
        int winner = winner(player1Total, player2Total, player3Total);
        System.out.println("Aqui estan los resultados del juego");
        for (int i = 1; i <= numPlayers; i++) {
            switch (i) {
                case 1:
                    System.out.printf("El jugador 1 ha %s con %d euros%n", winner == 1?"ganado":"perdido", player1Total);
                    break;
                case 2:
                    System.out.printf("El jugador 2 ha %s con %d euros%n", winner == 2?"ganado":"perdido", player2Total);
                    break;
                case 3:
                    System.out.printf("El jugador 3 ha %s con %d euros%n", winner == 3?"ganado":"perdido", player3Total);
                    break;
            }
        }
    }
}
