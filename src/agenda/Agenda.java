package agenda;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe che memorizza oggetti di tipo Appuntamento, ha un nome.
 * Gli appuntamenti memorizzati non possono essere sovrapposti temporalmente
 *
 * @author leonardo_galliera
 */
public class Agenda {
    private final ArrayList<Appuntamento> appuntamenti;
    private final String nomeAgenda;
    private final static String PATH = "agende";

    /**
     * Costruttore di Agenda dato un nome come argomento
     *
     * @param nome Nome stringa
     */
    public Agenda(String nome) {
        this.nomeAgenda = nome; // nome deve essere unico
        appuntamenti = new ArrayList<>();
    }

    /**
     * @author leonardo_galliera
     */
    class IteratoreAgenda implements Iterator<Appuntamento>{
        int indice;

        IteratoreAgenda(){
            indice = 0;
        }

        /**
         * Ritorna true se esiste un appuntamento dopo quello corrente, altrimenti false
         *
         * @return true se esiste un appuntamento dopo quello corrente, altrimenti false
         */
        @Override
        public boolean hasNext() {
            return indice < appuntamenti.size();
        }

        /**
         * Ritorna l'appuntamento successivo a ì quello corrente
         *
         * @return l'appuntamento successivo a ì quello corrente
         */
        @Override
        public Appuntamento next() {
            return appuntamenti.get(indice++);
        }

        /**
         * Metodo non implementato e non supportato
         */
        @Override
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    private static File openDir() throws IOException {
        File directory = new File(PATH);
        if(!directory.exists()){
            System.out.println("Nuova directory creata in " + System.getProperty("user.dir"));
            if(!directory.mkdirs()) throw new IOException();
        }
        return directory;
    }

    /**
     * Get numero appuntamenti in agenda
     *
     * @return numero appuntamenti in agenda
     */
    public int numApp() {
        return appuntamenti.size();
    }

    /**
     * Get nome agenda
     *
     * @return nome agenda
     */
    public String getName() {
        return nomeAgenda;
    }

    /**
     * Aggiunge un appuntamento passato come parametro all'agenda se non si sovrappone ad appuntamenti gia' presenti
     *
     * @param newApp appuntamento da aggiungere all'agenda
     * @throws AgendaException se l'appuntamento da aggiungere si sovrappone ad almeno un appuntamento gia' presente
     */
    public void add(Appuntamento newApp) throws AgendaException {
        validaSovrapposizioni(newApp);
        appuntamenti.add(newApp);
        appuntamenti.sort((a1, a2) -> a1.compareTo(a2));
    }

    /**
     * Crea un nuovo appuntamento con i dati in argomento
     * Se creato, lo aggiunge all'agenda se non si sovrappone ad appuntamenti gia' presenti
     *
     * @param data identifica il giorno d'inizio dell'appuntamento
     * @param ora identifica l'ora d'inizio dell'appuntamento
     * @param durata identifica la durata dell'appuntamento
     * @param nome nome della persona
     * @param luogo luogo dell'appuntamento
     * @throws AppuntamentoException se i parametri non sono conformi al formato
     * @throws AgendaException se l'appuntamento da aggiungere si sovrappone ad almeno un appuntamento gia' presente
     */
    public void add(String data, String ora, int durata, String nome, String luogo) throws AppuntamentoException, AgendaException {
        add(new Appuntamento(data, ora, durata, nome, luogo));
    }

    private void validaSovrapposizioni(Appuntamento newApp) throws AgendaException {
        if(appuntamenti.stream().anyMatch(app -> app.isSovrapposto(newApp))) throw new AgendaException("Presente sovrapposizione");
    }

    private void tryAdd(Appuntamento newApp, Appuntamento oldApp) throws AgendaException {
        try {
            add(newApp);
        } catch (AgendaException e) {
            appuntamenti.add(oldApp);
            throw new AgendaException("Presente sovrapposizione\nAppuntamento non modificato");
        }
    }

    /**
     * Dato una agenda come argomento, ritorna true se ha nome uguale al
     * nome di questa agenda, altrimenti false
     *
     * @param agendaToCompare agenda di cui comparare il nome
     * @return true se l'agenda da argomento ha nome uguale al nome di questa agenda, altrimenti false
     */
    public boolean compareNome(Agenda agendaToCompare){
        return this.nomeAgenda.equals(agendaToCompare.nomeAgenda);
    }

    /**
     * Dato l'indice che identifica un appuntamento e una nuova data,
     * viene modificata la data di quell'appuntamento se la modifica non crea sovrapposizioni.
     * In caso contrario l'appuntamento non viene modificato
     *
     * @param index indice che definisce l'appuntamento da modificare
     * @param newDate identifica la nuova data dall'appuntamento
     * @throws AppuntamentoException se newDate non &egrave; conforme al formato
     * @throws AgendaException se l'appuntamento modificato si sovrappone a un altro appuntamento
     */
    public void modificaData(int index, String newDate) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(index);
        Appuntamento newApp = new Appuntamento(newDate, old.getOra(), old.getDurata(), old.getNome(), old.getLuogo());
        appuntamenti.remove(old);

        tryAdd(newApp, old);
    }

    /**
     * Dato l'indice che identifica un appuntamento e una nuova ora,
     * viene modificato l'orario di quell'appuntamento se la modifica non crea sovrapposizioni.
     * In caso contrario l'appuntamento non viene modificato
     *
     * @param index indice che definisce l'appuntamento da modificare
     * @param newOra identifica la nuova ora dall'appuntamento
     * @throws AppuntamentoException se newOra non &egrave; conforme al formato
     * @throws AgendaException se l'appuntamento modificato si sovrappone a un altro appuntamento
     */
    public void modificaOra(int index, String newOra) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(index);
        Appuntamento newApp = new Appuntamento(old.getData(), newOra, old.getDurata(), old.getNome(), old.getLuogo());
        elimina(index);

        tryAdd(newApp, old);
    }

    /**
     * Dato l'indice che identifica un appuntamento e una nuova data,
     * viene modificata la data di quell'appuntamento se la modifica non crea sovrapposizioni.
     * In caso contrario l'appuntamento non viene modificato
     *
     * @param index indice che definisce l'appuntamento da modificare
     * @param newDurata identifica la nuova data dall'appuntamento
     * @throws AppuntamentoException se newDate non &egrave; conforme al formato
     * @throws AgendaException se l'appuntamento modificato si sovrappone a un altro appuntamento
     */
    public void modificaDurata(int index, int newDurata) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(index);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), newDurata, old.getNome(), old.getLuogo());
        elimina(index
        );

        tryAdd(newApp, old);
    }

    /**
     * Dato l'indice che identifica un appuntamento e un nuovo nome per la persona,
     * viene modificato il nome di quell'appuntamento se il nuovo nome &egrave; conforme al formato.
     * In caso contrario l'appuntamento non viene modificato
     *
     * @param index indice che definisce l'appuntamento da modificare
     * @param newNome nuovo nome della persona dall'appuntamento
     * @throws AppuntamentoException se newDate non &egrave; conforme al formato
     * @throws AgendaException se l'appuntamento modificato si sovrappone a un altro appuntamento
     */
    public void modificaNome(int index, String newNome) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(index);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), old.getDurata(), newNome, old.getLuogo());
        elimina(index);

        tryAdd(newApp, old);
    }

    /**
     * Dato l'indice che identifica un appuntamento e un luogo,
     * viene modificato il luogo di quell'appuntamento se il nuovo luogo &egrave; conforme al formato.
     * In caso contrario l'appuntamento non viene modificato
     *
     * @param index indice che definisce l'appuntamento da modificare
     * @param newLuogo nuovo luogo dall'appuntamento
     * @throws AppuntamentoException se newDate non &egrave; conforme al formato
     * @throws AgendaException se l'appuntamento modificato si sovrappone a un altro appuntamento
     */
    public void modificaLuogo(int index, String newLuogo) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(index);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), old.getDurata(), old.getNome(), newLuogo);
        elimina(index);

        tryAdd(newApp, old);
    }

    /**
     * Ritorna un iterator degli appuntamenti in agenda, remove non &egrave; implementato
     *
     * @return iterator degli appuntamenti in agenda
     */
    public Iterator<Appuntamento> iterator() {
        return new IteratoreAgenda();
    }

    /**
     * Rimuove un appuntamento identificato da index dall'agenda,
     * lancia un'eccezione se l'indice non identifica un appuntamento in agenda
     *
     * @param index indice che identifica un appuntamento in agenda
     * @throws AgendaException se index non identifica un appuntamento in agenda
     */
    public void elimina(int index) throws AgendaException {
        if(index >= appuntamenti.size() || index<0) throw new AgendaException("Indice out of bound");
        appuntamenti.remove(index);
    }

    /**
     * Scrive gli appuntamenti dell'agenda su un file col nome dell'agenda con la formattazione definita da toStringFile() della classe agenda
     * Sovrascrive eventuali file di scrittura della stessa agenda
     *
     * @throws IOException se &egrave; avvenuto un errore inaspettato di apertura del file
     */
    public void scriviAgenda() throws IOException {
        File path = openDir();
        PrintWriter out = new PrintWriter(path + File.separator + this.nomeAgenda);
        for(Appuntamento a: appuntamenti) {
            out.write(a.toStringFile());
            out.write("\n");
        }
        out.close();
    }

    /**
     * Legge gli appuntamenti dal file col nome dell'agenda e li inserisce in agenda.
     * Effettua il check del formato e correttezza ti tutti i campi e verifica l'assenza di sovrapposizioni
     *
     * @throws AppuntamentoException se almeno un appuntamento letto da file non &egrave; conforme al formato
     * @throws AgendaException se almeno un appuntamento letto da file si sovrappone a un altro
     * @throws IOException se &egrave; avvenuto un errore inaspettato di apertura del file
     */
    public void leggiAgenda() throws AppuntamentoException, AgendaException, IOException {
        String line;
        File path = openDir();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path + File.separator + this.nomeAgenda));
            while ((line = in.readLine()) != null) {
                add(new Appuntamento(line));
            }
        } catch (FileNotFoundException e) {
            throw new AgendaException("Agenda non trovata");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

