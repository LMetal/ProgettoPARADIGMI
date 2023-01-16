package agenda;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Appuntamento implements Comparable {
    private final LocalDateTime data_ora_inizio;
    private final LocalDateTime data_ora_fine;
    private final int durata;
    private final String nomePersona;
    private final String luogoAppuntamento;
    static private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");


    public Appuntamento(String data, String ora, int durata_minuti, String nome_persona, String luogo) throws AppuntamentoException {
        validaCampi(data, ora, nome_persona, luogo);

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

    public Appuntamento(String fileLine) throws AppuntamentoException {
        String[] dati = fileLine.split(",");

        if(dati.length != 5) throw new AppuntamentoException("Formato appuntamento invalido");

        validaCampi(dati[0], dati[1], dati[3], dati[4]);


        this.durata = Integer.parseInt(dati[2]);

        try {
            data_ora_inizio = LocalDateTime.parse(dati[0] + "-" + dati[1], format);
            data_ora_fine = LocalDateTime.parse(dati[0] + "-" + dati[1], format);
        } catch (DateTimeParseException e) {
            throw new AppuntamentoException("Errore formattazzione parametri da file");
        }

        this.nomePersona = dati[3];
        this.luogoAppuntamento = dati[4];
    }


    private void validaCampi(String data, String ora, String nome_persona, String luogo) throws AppuntamentoException {
        if(!data.matches("^\\d{2}-\\d{2}-\\d{4}$")) throw new AppuntamentoException("Formato data invalido <dd-mm-aaaa>");      //puoi fare megio
        if(!ora.matches("^\\d{2}-\\d{2}$")) throw new AppuntamentoException("Formato ora invalido <hh-mm> 24h format");         //puoi fare megio
        if(!nome_persona.matches("^\\D+$")) throw new AppuntamentoException("Formato nome invalido"); //^[A-Z][a-z]+$
        if(!luogo.matches("^\\D+$")) throw new AppuntamentoException("Formato luogo invalido"); //^[A-Z][a-z]+$

    }

    private String add_offset(String minuti_ore) {
        int minuti = Integer.parseInt(minuti_ore.substring(3));
        int ore = Integer.parseInt(minuti_ore.substring(0,2));

        minuti += durata;

        return Integer.toString(ore).concat("-" + minuti);
    }

    public LocalDateTime inzio() {
        return data_ora_inizio;
    }

    public LocalDateTime fine() {
        return data_ora_fine;
    }

    public String toString(){
        return getData() + "\t" + getOra() + "\t" + durata + "\t" + nomePersona + "\t" + luogoAppuntamento;
    }

    public String getData() {
        return format.format(data_ora_inizio).substring(0, 10);
    }

    public String getOra() {
        return format.format(data_ora_inizio).substring(11, 16);
    }

    public int getDurata() {
        return durata;
    }

    public String getNome() {
        return nomePersona;
    }

    public String getLuogo() {
        return luogoAppuntamento;
    }

    @Override
	public boolean equals(Object a) {
        Appuntamento newApp = (Appuntamento) a;
        if(this.inzio().isBefore(newApp.fine()) && this.fine().isAfter(newApp.fine()) || this.inzio().compareTo(newApp.inzio()) == 0) return true;

        if(newApp.inzio().isBefore(this.fine()) && newApp.fine().isAfter(this.fine()) || this.fine().compareTo(newApp.fine()) == 0) return true;

        return false;
	}

	public String toStringFile() {
		return toString().replace('\t', ',');
	}


    @Override
    public int compareTo(Object o) {
        Appuntamento a = (Appuntamento) o;
        return this.inzio().compareTo(a.inzio());
    }
}
