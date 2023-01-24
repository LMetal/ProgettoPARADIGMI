package interfacciaUtente;

import agenda.*;
import jbook.util.Input;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class Interfaccia {
    private static final String PATH = "agende";

    public static void main(String[] argv) throws IOException {
        openDir();

        while(true){
            System.out.println("""
                    \nChe operazione si vuole svolgere ?
                    1. Visualizzazione Agende
                    2. Creazione Agenda
                    3. Eliminazione Agenda
                    4. Modifica e Visualizzazione Agenda
                    5. Scrivi Agenda su File
                    6. Leggi Agenda da File
                    0. EXIT
            """);
            
            try{
                switch (Input.readInt("\nScelta: ")){
                    case 1:
                        visualizzaAgende();
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
                    case 5:
                        scriviSuFile();
                        break;
                    case 6:
                        leggiFile();
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


    // UTILITY METHODS

    private static File openDir() throws IOException {
        File directory = new File(PATH);
        if(!directory.exists()){
            System.out.println("Nuova directory creata in " + System.getProperty("user.dir"));
            if(!directory.mkdirs()) throw new IOException();
        }
        return directory;
    }

    private static Agenda sceltaAgenda(String messaggio){
        printAgende();
        System.out.print(messaggio);
        try{
            return Agende.getAgenda(Input.readInt());
        }
        catch (AgendeException e) {
            System.out.println("ERRORE -> " + e.getMessage() + "\n\n");
        }
        return null;
    }

    private static void printAgende(){
        System.out.println("\tNOME\tNUM_APPUNTAMENTI");
        for(int i=0; i<Agende.numAgende(); i++){
            try {
                System.out.println(i + ".\t" + Agende.getAgenda(i).getName() + "\t" + Agende.getAgenda(i).numApp());
            } catch (AgendeException e) {
                System.out.println("ERRORE ->" + e.getMessage());
            }
        }
    }


    // COMMAND LINE METHODS

    private static void visualizzaAgende() {
        System.out.println("\nNumero agende: " + Agende.numAgende());
        if(Agende.numAgende() <= 0) return;

        printAgende();
    }

    private static void creaAgenda() {
        System.out.println(">>> CREAZIONE AGENDA\n\n");
        System.out.print("Che nome ? ");
        try {
            Agende.createAgenda(Input.readString());
        } catch (AgendeException e) {
            System.out.println("ERRORE -> " + e.getMessage());
            System.out.println("Agenda non creata\n");
        }
    }

    private static void eliminaAgenda() {
        System.out.println(">>> ELIMINAZIONE AGENDA\n");
        Agende.elimina(sceltaAgenda("Che agenda eliminare ? "));

    }

    private static void scriviSuFile() throws IOException {
        System.out.println(">>> SCRITTURA AGENDA\n");

        Objects.requireNonNull(sceltaAgenda("Che agenda scrivere su file? ")).scriviAgenda();
    }

    private static void leggiFile() throws IOException {
        File directory = openDir();
        String[] nomi = directory.list();

        if (nomi != null) {
            System.out.println("Agende presenti: ");
            for ( String nome : nomi ) {
                System.out.println(nome);
            }
        }

        System.out.print("Che agenda leggere da file? :");

        try {
            Agende.readAgenda(Input.readString());
        } catch (AgendeException | AppuntamentoException | AgendaException e) {
            System.out.println("ERRORE -> " + e.getMessage() + "\n\n");
            System.out.println("Agenda non importata");
        }
    }



    private static void operazioniAgenda() {
        System.out.println(">>> OPERAZIONI SU AGENDA\n");
        Agenda agenda = sceltaAgenda("Su che agenda operare ? ");

        if(agenda == null){
            System.out.println("Errore selezione agenda");
            return;
        }

        int d;
        while(true){
            System.out.println("""
                    \nChe operazione si vuole svolgere ?
                    1. Visualizzazione Agenda
                    2. Aggiunta Appuntamento
                    3. Eliminazione Appuntamento
                    4. Modifica Appuntamento
                    0. GO BACK
            """);

            try{
                d = Input.readInt("\nScelta: ");
                switch (d){
                    case 0:
                        return;
                    case 1:
                        visualizzaAppuntamenti(agenda, true);
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

    private static void visualizzaAppuntamenti(Agenda a, boolean wait){
        Iterator<Appuntamento> iter = a.iterator();
        int app = 0;
        System.out.println("APPUNTAMENTI IN AGENDA: " + a.numApp());
        if(a.numApp() <= 0) return;

        System.out.println("\tDATA\t\tORA\t\tDURATA\tNOME\tLUOGO");
        while(iter.hasNext()){
            System.out.println(app + ".\t" + iter.next().toString());
            app++;
        }

        if(wait){
            System.out.println("Premere un INVIO per continuare");
            Input.readString();
        }
    }

    private static void visualizzaAppuntamento(Agenda a, int indice) throws AgendaException {
        Iterator<Appuntamento> iter = a.iterator();
        int i = 0;

        while (iter.hasNext()){
            if(indice == i){
                System.out.println(iter.next().toString());
                return;
            }
            i++;
            iter.next();
        }
        throw new IndexOutOfBoundsException();
    }

    private static void aggiunta(Agenda a) {
        /*System.out.println("\n\n>>>\tAGGIUNTA NUOVO APPUNTAMENTO\n");
        System.out.print("In che data? <gg-mm-yyyy> : ");
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
        }
    }

    private static void eliminazione(Agenda a) {
        System.out.println("\n\n>>>\tRIMOZIONE APPUNTAMENTO\n");
        visualizzaAppuntamenti(a, false);
        System.out.print("Che elemento eliminare ? ");
        try {
            a.elimina(Input.readInt());
        } catch (AgendaException e) {
            System.out.println("ERRORE -> Appuntamento non aggiunto");
            System.out.println(e.getMessage());
        }
    }

    private static void modifica(Agenda a) {
        System.out.println("\n>>>MODIFICA");

        visualizzaAppuntamenti(a, false);
        System.out.print("\nChe appuntamento si vuole modificare ? ");
        int indice = Input.readInt();

        try {
            visualizzaAppuntamento(a, indice);
        } catch (AgendaException e) {
            System.out.println("ERRORE -> Appuntamento non trovato");
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("""
                \nChe cosa si vuole modificare ?\s
                 1. Data Agenda
                 2. Ora Appuntamento
                 3. Durata Appuntamento
                 4. Nome Persona Appuntamento
                 5. Luogo Appuntamento
                 0. EXIT
                """);


        System.out.print("\nSCELTA: ");

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
}
