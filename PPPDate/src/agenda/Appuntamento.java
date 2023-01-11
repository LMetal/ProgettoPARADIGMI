package agenda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appuntamento {
    private final Date data_ora_inizio;
    private final Date data_ora_fine;
    private final int durata;
    private final String nomePersona;
    private final String luogoAppuntamento;
    private final SimpleDateFormat format;


    public Appuntamento(String data, String ora, int durata_minuti, String nome_persona, String luogo) throws AppuntamentoException {
        validaCampi(data, ora, nome_persona);

        format = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        this.durata = durata_minuti;

        try {
            data_ora_inizio = format.parse(data + "-" + ora);
            data_ora_fine = format.parse(data + "-" + add_offset(ora));
            //System.out.println(format.format(data_ora_inizio) + " -> " + data_ora_fine);
        } catch (ParseException e) {
            throw new AppuntamentoException("Errore formattazzione parametri");
        }

        this.nomePersona = nome_persona;
        this.luogoAppuntamento = luogo;
    }


    private void validaCampi(String data, String ora, String nome_persona) throws AppuntamentoException {
        if(!data.matches("^\\d{2}-\\d{2}-\\d{4}$")) throw new AppuntamentoException("Formato data invalido <dd-mm-aaaa>");      //puoi fare megio
        if(!ora.matches("^\\d{2}-\\d{2}$")) throw new AppuntamentoException("Formato ora invalido <hh-mm> 24h format");         //puoi fare megio
        if(!nome_persona.matches("^\\D+$")) throw new AppuntamentoException("Formato nome invalido"); //^[A-Z][a-z]+$
    }

    private String add_offset(String minuti_ore) {
        int minuti = Integer.parseInt(minuti_ore.substring(3));
        int ore = Integer.parseInt(minuti_ore.substring(0,2));

        minuti += durata;

        return Integer.toString(ore).concat("-" + minuti);
    }

    public Date inzio() {
        return data_ora_inizio;
    }

    public Date fine() {
        return data_ora_fine;
    }

    public String toString(){
        return getData() + "\t" + getOra() + "\t" + durata + "\t\t" + nomePersona + "\t" + luogoAppuntamento;
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

	public void compare(Appuntamento newApp) throws AgendaException {
		if(this.inzio().before(newApp.fine()) && this.fine().after(newApp.fine()) || this.inzio().compareTo(newApp.inzio()) == 0) throw new AgendaException("Presente sovrapposizione");
		
		if(newApp.inzio().before(this.fine()) && newApp.fine().after(this.fine()) || this.fine().compareTo(newApp.fine()) == 0) throw new AgendaException("Presente sovrapposizione");

	}


	public String toStringFile() {
		return getData() + "," + getOra() + "," + durata + "," + nomePersona + "," + luogoAppuntamento + "\n";
	}
}
