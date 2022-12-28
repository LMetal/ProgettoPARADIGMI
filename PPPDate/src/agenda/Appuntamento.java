package agenda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appuntamento {
    private Date data_ora_inizio;
    private Date data_ora_fine;
    private int durata;
    private String nomePersona;
    private String luogoAppuntamento;
    private final SimpleDateFormat format;


    public Appuntamento(String data, String ora, int durata_minuti, String nome_persona, String luogo) throws AppuntamentoException {
        validaCampi(data, ora, durata_minuti, nome_persona, luogo);

        format = new SimpleDateFormat("dd-MM-yyyy-kk-mm");
        this.durata = durata_minuti;

        try {
            data_ora_inizio = format.parse(data + "-" + ora);
            data_ora_fine = format.parse(data + "-" + add_offset(ora));
            //System.out.println(data_ora_inizio + " -> " + data_ora_fine);
        } catch (ParseException e) {
            throw new AppuntamentoException("Errore formattazzione parametri");
        }

        this.nomePersona = nome_persona;
        this.luogoAppuntamento = luogo;
    }


    private void validaCampi(String data, String ora, int durata_minuti, String nome_persona, String luogo) throws AppuntamentoException {
        if(!data.matches("^\\d{2}-\\d{2}-\\d{4}$")) throw new AppuntamentoException("Formato data invalido");
        if(!ora.matches("^\\d{2}-\\d{2}$")) throw new AppuntamentoException("Formato ora invalido");
        if(!nome_persona.matches("^[A-Z][a-z]+$")) throw new AppuntamentoException("Formato nome invalido");
    }


    private String add_offset(String ora) {
        int oraOff = Integer.parseInt(ora.substring(0, 2));
        int minOff = Integer.parseInt(ora.substring(3));


        minOff += durata;

        oraOff += (minOff / 60);
        minOff = minOff % 60;


        return Integer.toString(oraOff).concat("-"+Integer.toString(minOff));
    }


    public Date inzio() {
        return data_ora_inizio;
    }

    public Date fine() {
        return data_ora_fine;
    }
}
