package agenda;

import jbook.util.Input;

import java.util.Iterator;


public class Interfaccia {
    public static void main(String[] argv) {
        Agenda a = new Agenda("");
        int d;
        while(true){
            System.out.println("\nChe operazione si vuole svolgere ? ");
            System.out.println("1. Visualizzazione Agenda");
            System.out.println("2. Aggiunta Appuntamento");
            System.out.println("3. Eliminazione Appuntamento");
            System.out.println("4. Modifica Appuntamento");
            System.out.println("0. EXIT");
            System.out.print("\nScelta: ");
            d = Input.readInt();

            if(d>=0 && d<=3){
                switch (d){
                    case 0:
                        return;
                    case 1:
                        visualizza(a, true);
                        break;
                    case 2:
                        aggiunta(a);
                        break;
                    case 3:
                        eliminazione(a);
                        break;
                    case 4:
                        modifica(a);
                        break;

                }
            } else{
                System.out.println("NON VALIDO");
            }
        }
    }

    private static void modifica(Agenda a) {
        System.out.println("\n>>>MODIFICA");

        visualizza(a, false);
        System.out.println("\nChe appuntamento si vuole modificare ? ");
        int indice = Input.readInt();

        System.out.println("\nChe cosa si vuole modificare ? ");
        System.out.println("1. Data Agenda");
        System.out.println("2. Ora Appuntamento");
        System.out.println("3. Durata Appuntamento");
        int m = Input.readInt();

        switch (m) {
            case 1 -> {
                System.out.println("Inserire nuova data: ");
                try {
                    a.modificaData(indice, Input.readString());
                } catch (AppuntamentoException | AgendaException e) {
                    System.out.println("ERRORE -> Appuntamento non modificato");
                    System.out.println(e.getMessage());
                }
            }

            case 2 -> {
                System.out.println("Inserire nuova ora: ");
                try {
                    a.modificaOra(indice, Input.readString());
                } catch (AppuntamentoException | AgendaException e) {
                    System.out.println("ERRORE -> Appuntamento non modificato");
                    System.out.println(e.getMessage());
                }
            }

            case 3 -> {
                System.out.println("Inserire nuova durata: ");
                try {
                    a.modificaDurata(indice, Input.readInt());
                } catch (AppuntamentoException | AgendaException e) {
                    System.out.println("ERRORE -> Appuntamento non modificato");
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static void eliminazione(Agenda a) {
        System.out.println("\n\n>>>\tRIMOZIONE APPUNTAMENTO\n");
        visualizza(a, false);
        System.out.println("Che elemento eliminare ? ");
        try {
            a.elimina(Input.readInt());
        } catch (AgendaException e) {
            System.out.println("ERRORE -> Appuntamento non aggiunto");
            System.out.println(e.getMessage());
        }
    }

    private static void aggiunta(Agenda a) {
        System.out.println("\n\n>>>\tAGGIUNTA NUOVO APPUNTAMENTO\n");
        System.out.println("In che data? <gg-mm-aaaa> ");
        String data = Input.readString();
        System.out.println("A che ora? <oo-mm> ");
        String ora = Input.readString();
        System.out.println("Che durata? <min> ");
        int durata = Input.readInt();
        System.out.println("Nome persona? ");
        String nome = Input.readString();
        System.out.println("Dove? ");
        String luogo = Input.readString();

        try {
            a.add(data, ora, durata, nome, luogo);
        } catch (AppuntamentoException | AgendaException e) {
            System.out.println("ERRORE -> Appuntamento non aggiunto");
            System.out.println(e.getMessage());
        }
    }

    static void visualizza(Agenda a, boolean wait){
        Iterator<Appuntamento> iter = a.iterator();
        int app = 0;
        System.out.println("APPUNTAMENTI IN AGENDA: " + a.numApp());
        while(iter.hasNext()){
            System.out.println(app + ".\t" + iter.next());
            app++;
        }

        if(wait){
            System.out.println("Premere un INVIO per continuare");
            Input.readString();
        }

    }
}
