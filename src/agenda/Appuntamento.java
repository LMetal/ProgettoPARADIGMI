package agenda;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * @author leonardo_galliera
 *
 */
public class Appuntamento implements Comparable {
    private final LocalDateTime data_ora_inizio;
    private final LocalDateTime data_ora_fine;
    private final int durata;
    private final String nomePersona;
    private final String luogoAppuntamento;
    static private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");


    /**
     * Costruttore con check di validita' dei dati
     * Formato richiesto data dd-MM-yyyy, ora HH-mm, durata_minuti intero positivo,
     * nome_persona stringa senza numeri
     *
     * @param data identifica una data
     * @param ora identifica un'ora
     * @param durata_minuti specifica la durata dell'appuntamento
     * @param nome_persona identifica il nome della persona all'appuntamento
     * @param luogo identifica in luogo dell'appuntamento
     * @throws AppuntamentoException se i dati non sono conformi al formato
     */
    public Appuntamento(String data, String ora, int durata_minuti, String nome_persona, String luogo) throws AppuntamentoException {
        validaCampi(data, ora, durata_minuti, nome_persona);

        this.durata = durata_minuti;

        try {
            data_ora_inizio = LocalDateTime.parse(data + "-" + ora, format);
            data_ora_fine = data_ora_inizio.plusMinutes(durata_minuti);
        } catch (DateTimeParseException e) {
            throw new AppuntamentoException("Formattazione errata");
        }

        this.nomePersona = nome_persona;
        this.luogoAppuntamento = luogo;
    }

    /**
     * Costruttore con check di validita' dei dati
     * a partire da una stringa con formato dd-MM-yyy,HH-mm,"durata in minuti","nome persona","luogo"
     *
     * @param fileLine stringa da file che descrive i campi dell'appuntamento
     * @throws AppuntamentoException se i dati non sono conformi al formato
     */
    public Appuntamento(String fileLine) throws AppuntamentoException { //RIGUARDA
        String[] dati = fileLine.split(",");

        if(dati.length != 5) throw new AppuntamentoException("Formato appuntamento da file invalido");

        validaCampi(dati[0], dati[1], Integer.parseInt(dati[2]), dati[3]);    //aggiungi check durata positiva


        this.durata = Integer.parseInt(dati[2]);

        try {
            data_ora_inizio = LocalDateTime.parse(dati[0] + "-" + dati[1], format);
            data_ora_fine = LocalDateTime.parse(dati[0] + "-" + dati[1], format).plusMinutes(Integer.parseInt(dati[2]));
        } catch (DateTimeParseException e) {
            throw new AppuntamentoException("Formato appuntamento da file invalido");
        }

        this.nomePersona = dati[3];
        this.luogoAppuntamento = dati[4];
    }


    /*
     * Valida la correttezza dei parametri:
     * data deve essere in formato dd-MM-yyyy
     * ora deve essere in formato HH-mm
     * durata deve essere un intero maggiore di 0
     * nome_persona deve essere composto non da numeri
     * luogo deve essere composto non da numeri
     *
     * @param data stringa che identifica una data
     * @param ora stringa che identifica un'ora
     * @param durata intero che specifica la durata dell'appuntamento
     * @param nome_persona stringa che identifica il nome della persona all'appuntamento
     * @param luogo stringa che identifica in luogo dell'appuntamento
     * @throws AppuntamentoException lancia l'eccezione se almeno uno dei parametri non rispetta il formato
     */
    private void validaCampi(String data, String ora, int durata, String nome_persona) throws AppuntamentoException {
        if(!data.matches("^\\d{2}-\\d{2}-\\d{4}$")) throw new AppuntamentoException("Formato data invalido <dd-mm-yyyy>");      //puoi fare meglio
        if(!ora.matches("^\\d{2}-\\d{2}$")) throw new AppuntamentoException("Formato ora invalido <hh-mm> 24h format");         //puoi fare meglio
        if(durata <= 0) throw new AppuntamentoException("Formato durata invalida <int maggiore di 0>");
        if(!nome_persona.matches("^\\D+$")) throw new AppuntamentoException("Formato nome invalido"); //^[A-Z][a-z]+$

    }

    /**
     * Ritorna una LocalDateTime che indica l'inizio dell'appuntamento
     *
     * @return inizio appuntamento in tipo LocalDateTime
     */
    public LocalDateTime inzio() {
        return data_ora_inizio;
    }

    /**
     * Ritorna una LocalDateTime che indica la fine dell'appuntamento
     *
     * @return fine appuntamento in tipo LocalDateTime
     */
    public LocalDateTime fine() {
        return data_ora_fine;
    }

    /**
     * Ritorna una stringa che descrive l'appuntamento formattata per la stampa su terminale
     *
     * @return informazioni appuntamento tipo String
     */
    public String toString(){
        return getData() + "\t" + getOra() + "\t" + durata + "\t\t" + nomePersona + "\t" + luogoAppuntamento;
    }

    /**
     * Get data inizio appuntamento
     *
     * @return LocalDateTime inizio appuntamento
     */
    public String getData() {
        return format.format(data_ora_inizio).substring(0, 10);
    }

    /**
     * Get ora inizio appuntamento
     *
     * @return ora inizio appuntamento
     */
    public String getOra() {
        return format.format(data_ora_inizio).substring(11, 16);
    }

    /**
     * Get durata appuntamento
     *
     * @return durata appuntamento
     */
    public int getDurata() {
        return durata;
    }

    /**
     * Get nome persona appuntamento
     *
     * @return nome persona appuntamento
     */
    public String getNome() {
        return nomePersona;
    }

    /**
     * Get luogo appuntamento
     *
     * @return luogo appuntamento
     */
    public String getLuogo() {
        return luogoAppuntamento;
    }

    /**
     * Ritorna una stringa che descrive l'appuntamento formattata per la scrittura su file
     *
     * @return informazioni appuntamento tipo String
     */
	public String toStringFile() {
		return toString().replace('\t', ',').replace(",,", ",");
	}

    /**
     * Dato un appuntamento come argomento, ritorna true se e' presente una sovrapposizione temporale, altrimenti false
     *
     * @param app appuntamento da comparare
     * @return true se è presente sovrapposizione, false se non è presente sovrapposizione
     */
    public boolean isSovrapposto(Appuntamento app){
        if(this.inzio().isBefore(app.fine()) && this.fine().isAfter(app.fine()) || this.inzio().compareTo(app.inzio()) == 0) return true;

        if(app.inzio().isBefore(this.fine()) && app.fine().isAfter(this.fine()) || this.fine().compareTo(app.fine()) == 0) return true;

        return false;
    }

    /**
     * Dato un appuntamento come argomento, ritorna true se l'inizio di a coincide con l'inizio dell'appuntamento
     * e se la fine di a coincide con la fine dell'appuntamento
     *
     * @param a appuntamento da comparare
     * @return true se i due appuntamenti sono uguali, altrimenti false
     */
    @Override
    public boolean equals(Object a) {
        Appuntamento app = (Appuntamento) a;

        return this.data_ora_inizio.compareTo(app.data_ora_inizio) == 0 && this.data_ora_fine.compareTo(app.data_ora_fine) == 0;
    }

    /**
     * Dato un oggetto appuntamento come argomento, ritorna rispettivamente:
     * ZERO se il suo inizio coincide con l'inizio di questo appuntamento,
     * un valore maggiore di ZERO se il suo inizio &egrave; dopo l'inizio di questo appuntamento,
     * un valore minore di ZERO se il suo inizio &egrave; prima l'inizio di questo appuntamento
     *
     * @param o oggetto appuntamento da comparare a questo oggetto
     * @return ZERO se l'inizio dell'appuntamento argomento &egrave; temporalmente uguale all'inizio di questo,
     * un valore maggiore di ZERO se l'inizio dell'appuntamento argomento &egrave; temporalmente dopo l'inizio di questo,
     * un valore minore di ZERO se l'inizio dell'appuntamento argomento &egrave; temporalmente prima l'inizio di questo
     */
    @Override
    public int compareTo(Object o) {
        Appuntamento a = (Appuntamento) o;
        return this.inzio().compareTo(a.inzio());
    }
}
