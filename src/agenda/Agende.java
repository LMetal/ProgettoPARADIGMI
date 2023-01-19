package agenda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author leonardo_galliera
 *
 * Classe statica che memorizza classi di tipo agenda
 */
public class Agende {
    private static final ArrayList<Agenda> agende = new ArrayList<>();

    /**
     * Ritorna il numero di agende memorizzate
     *
     * @return numero di agende memorizzate
     */
    public static int numAgende() {
        return agende.size();
    }

    /**
     * Dato in nome di una nuova agenda come parametro, crea una nuova agenda vuota
     * e la aggiunge alle agende memorizzate se ha nome unico.
     * In caso contrario l'agenda non viene memorizzata e viene lanciata un'eccezione
     *
     * @param nome nome nuova agenda
     * @throws AgendeException se il nome &egrave; gia' in uso
     */
    public static void createAgenda(String nome) throws AgendeException {
        if(isUnico(nome)){
            agende.add(new Agenda(nome));
        } else {
            throw new AgendeException("Nome gia' in uso");
        }
    }

    private static boolean isUnico(String nome) {
        for(Agenda a: agende){
            if(a.getName().equals(nome)) return false;
        }
        return true;
    }

    /**
     * Data un'agenda come parametro, viene eliminata da quelle memorizzate
     *
     * @param a agenda da eliminare
     */
    public static void elimina(Agenda a) {
        agende.remove(a);
    }

    /**
     * Dato un indice che identifica una agenda, ritorna quell'agenda
     *
     * @param index indice che identifica una agenda memorizzata
     * @return l'agenda identificata da index
     * @throws AgendeException se index non identifica un'agenda
     */
    public static Agenda getAgenda(int index) throws AgendeException {

        if(index < 0 || index> agende.size()) throw new AgendeException("Numero agenda inesistente");

        return agende.get(index);
    }

    /**
     * Dato il nome di un'agenda come parametro, legge quell'agenda da file e,
     * se non sono state lanciate eccezioni, la aggiunge tra quelle memorizzate
     *
     * @param name nome agenda da leggere da file
     * @throws AppuntamentoException se almeno un appuntamento letto da file non &egrave; conforme al formato
     * @throws AgendaException se almeno un appuntamento letto da file si sovrappone a un altro appuntamento
     * @throws AgendeException se l'agenda identificata da name &egrave; gia' memorizzata
     * @throws IOException se &egrave; avvenuto un errore inaspettato di apertura del file
     */
    public static void readAgenda(String name) throws AppuntamentoException, AgendaException, AgendeException, IOException {
        Agenda a = new Agenda(name);
        if(!isUnico(name)) try {
            throw new AgendeException("Agenda già presente");
        } catch (AgendeException e) {
            agende.remove(a);
            throw e;
        }

        a.leggiAgenda();

        agende.add(a);

    }


}
