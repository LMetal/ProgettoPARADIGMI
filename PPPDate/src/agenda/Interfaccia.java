package agenda;

import jbook.util.Input;

import java.util.Iterator;


public class Interfaccia {
    public static void main(String[] argv) {
        while(true){
            System.out.println("\nChe operazione si vuole svolgere ? ");
            System.out.println("1. Visualizzazione Agende");
            System.out.println("2. Creazione Agenda");
            System.out.println("3. Eliminazione Agenda");
            System.out.println("4. Modifica Agenda");
            System.out.println("0. Exit");
            try{
                switch (Input.readInt("\nScelta: ")){
                    case 1:
                        visualizzaAgende(true);
                        break;
                    case 2:
                        creaAgenda();
                        break;
                    case 3:
                        eliminaAgenda();
                        break;
                    case 4:
                        operazioniAgenda();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("ERRORE -> Scelta non valida");
                }
            }
            catch (Exception e) {
                System.out.println("ERRORE -> Formato scelta non valido\n\n");
            }
        }
    }

    private static Agenda sceltaAgenda(String messaggio){
        visualizzaAgende(false);
        System.out.println(messaggio);
        try{
            return Agende.getAgenda(Input.readInt());
        }
        catch (Exception e) {
            System.out.println("ERRORE -> Formato scelta non valido\n\n");
        }
        return null;
    }

    private static void eliminaAgenda() {
        System.out.println(">>> ELIMINAZIONE AGENDA\n\n");
        Agende.elimina(sceltaAgenda("Che agenda eliminare ? "));

    }

    private static void creaAgenda() {
        System.out.println(">>> CREAZIONE AGENDA\n\n");
        System.out.print("Che nome ? ");
        try {
            Agende.createAgenda(Input.readString());
        } catch (AgendeException e) {
            System.out.println("ERRORE -> " + e.getMessage());
            System.out.println("Agenda non modificata\n\n");
        }
    }

    private static void visualizzaAgende(boolean wait) {
        System.out.println("\nNumero agende: " + Agende.numAgende());
        System.out.println("\tNOME\tNUM_APPUNTAMENTI");
        for(int i=0; i<Agende.numAgende(); i++){
            System.out.println(i + ".\t" + Agende.getAgenda(i).getName() + "\t" + Agende.getAgenda(i).numApp());
        }
        if(wait){
            System.out.println("Premere un INVIO per continuare");
            Input.readString();
        }
    }

    private static void operazioniAgenda() {
        System.out.println(">>> OPERAZIONI SU AGENDA");
        metodiAgenda.main(sceltaAgenda("Su che agenda operare ? "));
    }
}

class metodiAgenda{
    public static void main(Agenda agenda){
        int d;
        while(true){
            System.out.println("\nChe operazione si vuole svolgere ? ");
            System.out.println("1. Visualizzazione Agenda");
            System.out.println("2. Aggiunta Appuntamento");
            System.out.println("3. Eliminazione Appuntamento");
            System.out.println("4. Modifica Appuntamento");
            System.out.println("0. EXIT");

            try{
                d = Input.readInt("\nScelta: ");
                switch (d){
                    case 0:
                        return;
                    case 1:
                        visualizza(agenda, true);
                        break;
                    case 2:
                        aggiunta(agenda);
                        break;
                    case 3:
                        eliminazione(agenda);
                        break;
                    case 4:
                        modifica(agenda);
                        break;
                    default:
                        System.out.println("ERRORE -> Scelta non valida\n\n");
                        break;
                }

            } catch (Exception e) {
                System.out.println("ERRORE -> Formato scelta non valido\n\n");
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
        System.out.println("4. Nome Persona Appuntamento");
        System.out.println("5. Luogo Appuntamento");
        System.out.println("0. EXIT");

        visualizza(a, indice);

        System.out.print("SCELTA: ");

        try {
                switch (Input.readInt()) {
                    case 1 -> {
                        System.out.print("Inserire nuova data: ");
                        a.modificaData(indice, Input.readString());
                    }

                    case 2 -> {
                        System.out.print("Inserire nuova ora: ");
                        a.modificaOra(indice, Input.readString());
                    }

                    case 3 -> {
                        System.out.print("Inserire nuova durata: ");
                        a.modificaDurata(indice, Input.readInt());
                    }

                    case 4 -> {
                        System.out.print("Inserire nuovo nome: ");
                        a.modificaNome(indice, Input.readString());
                    }

                    case 5 -> {
                        System.out.print("Inserire nuovo luogo: ");
                        a.modificaLuogo(indice, Input.readString());
                    }

                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("ERRORE -> Scelta non valida\n");
            }
        }
        catch (Exception e){
            System.out.println("ERRORE -> Formato scelta non valido\n\n");
        } catch (AppuntamentoException | AgendaException e) {
            System.out.println("ERRORE -> " + e.getMessage());
            System.out.println("Appuntamento non modificato\n\n");
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
        /*System.out.println("\n\n>>>\tAGGIUNTA NUOVO APPUNTAMENTO\n");
        System.out.print("In che data? <gg-mm-aaaa> : ");
        String data = Input.readString();
        System.out.print("A che ora? <hh-mm> : ");
        String ora = Input.readString();
        System.out.print("Che durata? <min> : ");
        int durata = Input.readInt();
        System.out.print("Nome persona? : ");
        String nome = Input.readString();
        System.out.print("Dove? : ");
        String luogo = Input.readString();*/


        try {
            a.add("01-01-2022", "10-20", 120, "Pippo", "luogo");
        } catch (AppuntamentoException | AgendaException e) {
            System.out.println("ERRORE -> Appuntamento non aggiunto");
            System.out.println(e.getMessage());
        }
    }

    static void visualizza(Agenda a, boolean wait){
        Iterator<Appuntamento> iter = a.iterator();
        int app = 0;
        System.out.println("APPUNTAMENTI IN AGENDA: " + a.numApp());
        System.out.println("\tDATA\t\tORA\t\tDURATA\tNOME\tLUOGO");
        while(iter.hasNext()){
            System.out.println(app + ".\t" + iter.next());
            app++;
        }

        if(wait){
            System.out.println("Premere un INVIO per continuare");
            Input.readString();
        }
    }

    private static void visualizza(Agenda a, int indice) {
        Iterator<Appuntamento> iter = a.iterator();

        while (iter.hasNext()){
            if(indice == 0){
                System.out.println(iter.next().toString());
                return;
            }
            indice--;
            iter.next();
        }
    }
}