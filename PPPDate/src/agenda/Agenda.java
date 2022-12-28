package agenda;

import java.text.ParseException;
import java.util.ArrayList;

public class Agenda {
    private ArrayList<Appuntamento> appuntamenti;
    private String nomeAgenda;

    public Agenda(String nome) {
        this.nomeAgenda = nome;
        appuntamenti = new ArrayList<Appuntamento>();
    }

    public int numApp() {
        return appuntamenti.size();
    }

    public String getName() {
        return nomeAgenda;
    }

    public void add(String data, String ora, int durata, String nome, String luogo) throws AppuntamentoException, AgendaException {
        Appuntamento newApp = new Appuntamento(data, ora, durata, nome, luogo);
        validaSovrapposizioni(newApp);
        appuntamenti.add(newApp);
    }

    private void validaSovrapposizioni(Appuntamento newApp) throws AgendaException {
        for(Appuntamento a: appuntamenti){
            validaSovrapposizione(newApp, a);
        }
    }

    private void validaSovrapposizione(Appuntamento a1, Appuntamento a2) throws AgendaException {
        // a1         |---------|
        // a2   |-----------|


        if(a1.inzio().compareTo(a2.fine()) <= 0 && a1.fine().compareTo(a2.fine()) >= 0) throw new AgendaException("Presente sovrapposizione");

        if(a2.inzio().compareTo(a1.fine()) <= 0 && a2.fine().compareTo(a1.fine()) >= 0) throw new AgendaException("Presente sovrapposizione");
    }
}
