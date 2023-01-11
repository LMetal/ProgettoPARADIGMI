package agenda;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class Agenda {
    private final ArrayList<Appuntamento> appuntamenti;
    private final String nomeAgenda;

    public Agenda(String nome) {
        this.nomeAgenda = nome; // nome deve essere unico
        appuntamenti = new ArrayList<>();
    }

    class IteratoreAgenda implements Iterator<Appuntamento>{
        int indice;

        IteratoreAgenda(){
            indice = 0;
        }

        @Override
        public boolean hasNext() {
            return indice < appuntamenti.size();
        }

        @Override
        public Appuntamento next() {
            return appuntamenti.get(indice++);
        }
    }

    public int numApp() {
        return appuntamenti.size();
    }

    public String getName() {
        return nomeAgenda;
    }


    private void add(Appuntamento newApp) throws AgendaException {
        validaSovrapposizioni(newApp);
        appuntamenti.add(newApp);
        appuntamenti.sort(new Comparator<>() {
            @Override
            public int compare(Appuntamento a1, Appuntamento a2) {
                return a1.inzio().compareTo(a2.inzio());
            }
        });
    }

    public void add(String data, String ora, int durata, String nome, String luogo) throws AppuntamentoException, AgendaException {
        add(new Appuntamento(data, ora, durata, nome, luogo));
    }

    private void validaSovrapposizioni(Appuntamento newApp) throws AgendaException {
        for(Appuntamento a: appuntamenti){
            a.compare(newApp);
        }
    }

    public void modificaData(int i, String newData) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(i);
        Appuntamento newApp = new Appuntamento(newData, old.getOra(), old.getDurata(), old.getNome(), old.getLuogo());
        appuntamenti.remove(old);

        try {
            add(newApp);
        } catch (AgendaException e) {
            appuntamenti.add(old);
            throw new AgendaException("Presente sovrapposizione\nAppuntamento non modificato");
        }
    }

    public void modificaOra(int i, String newOra) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(i);
        Appuntamento newApp = new Appuntamento(old.getData(), newOra, old.getDurata(), old.getNome(), old.getLuogo());
        elimina(i);

        try {
            add(newApp);
        } catch (AgendaException e) {
            add(old);
            throw new AgendaException("Presente sovrapposizione\nAppuntamento non modificato");
        }
    }


    public void modificaDurata(int numAppuntamento, int newDurata) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(numAppuntamento);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), newDurata, old.getNome(), old.getLuogo());
        elimina(numAppuntamento);

        try {
            add(newApp);
        } catch (AgendaException e) {
            add(old);
            throw new AgendaException("Presente sovrapposizione\nAppuntamento non modificato");
        }
    }

    public void modificaNome(int numAppuntamento, String newNome) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(numAppuntamento);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), old.getDurata(), newNome, old.getLuogo());
        elimina(numAppuntamento);

        try {
            add(newApp);
        } catch (AgendaException e) {
            add(old);
            throw new AgendaException("Errore Imprevisto\nAppuntamento non modificato");
        }
    }

    public void modificaLuogo(int numAppuntamento, String newLuogo) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(numAppuntamento);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), old.getDurata(), old.getNome(), newLuogo);
        elimina(numAppuntamento);

        try {
            add(newApp);
        } catch (AgendaException e) {
            add(old);
            throw new AgendaException("Errore Imprevisto\nAppuntamento non modificato");
        }
    }

    public Iterator<Appuntamento> iterator() {
        return new IteratoreAgenda();
    }

    public void elimina(int i) throws AgendaException {
        if(i >= appuntamenti.size() || i<0) throw new AgendaException("Indice out of bound");
        appuntamenti.remove(i);
    }

	public void scriviAgenda() throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File("outFile"));
		for(Appuntamento a: appuntamenti) {
			out.write(a.toStringFile());
			out.flush();
			
		}
		out.close();
		
	}
}

