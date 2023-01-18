package agenda;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Agenda {
    private final ArrayList<Appuntamento> appuntamenti;
    private final String nomeAgenda;
    private final static String PATH = "agende";

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

    public void add(Appuntamento newApp) throws AgendaException {
        validaSovrapposizioni(newApp);
        appuntamenti.add(newApp);
        appuntamenti.sort((a1, a2) -> a1.compareTo(a2));
    }

    public void add(String data, String ora, int durata, String nome, String luogo) throws AppuntamentoException, AgendaException {
        add(new Appuntamento(data, ora, durata, nome, luogo));
    }

    private void validaSovrapposizioni(Appuntamento newApp) throws AgendaException {
        if(appuntamenti.contains(newApp)) throw new AgendaException("Presente sovrapposizione");
    }

    private void tryAdd(Appuntamento newApp, Appuntamento oldApp) throws AgendaException {
        try {
            add(newApp);
        } catch (AgendaException e) {
            appuntamenti.add(oldApp);
            throw new AgendaException("Presente sovrapposizione\nAppuntamento non modificato");
        }
    }

    public void modificaData(int i, String newData) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(i);
        Appuntamento newApp = new Appuntamento(newData, old.getOra(), old.getDurata(), old.getNome(), old.getLuogo());
        appuntamenti.remove(old);

        tryAdd(newApp, old);
    }

    public void modificaOra(int i, String newOra) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(i);
        Appuntamento newApp = new Appuntamento(old.getData(), newOra, old.getDurata(), old.getNome(), old.getLuogo());
        elimina(i);

        tryAdd(newApp, old);
    }


    public void modificaDurata(int numAppuntamento, int newDurata) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(numAppuntamento);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), newDurata, old.getNome(), old.getLuogo());
        elimina(numAppuntamento);

        tryAdd(newApp, old);
    }

    public void modificaNome(int numAppuntamento, String newNome) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(numAppuntamento);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), old.getDurata(), newNome, old.getLuogo());
        elimina(numAppuntamento);

        tryAdd(newApp, old);
    }

    public void modificaLuogo(int numAppuntamento, String newLuogo) throws AppuntamentoException, AgendaException {
        Appuntamento old = appuntamenti.get(numAppuntamento);
        Appuntamento newApp = new Appuntamento(old.getData(), old.getOra(), old.getDurata(), old.getNome(), newLuogo);
        elimina(numAppuntamento);

        tryAdd(newApp, old);
    }

    public Iterator<Appuntamento> iterator() {
        return new IteratoreAgenda();
    }

    public void elimina(int i) throws AgendaException {
        if(i >= appuntamenti.size() || i<0) throw new AgendaException("Indice out of bound");
        appuntamenti.remove(i);
    }

	public void scriviAgenda() throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File(PATH + File.separator + this.nomeAgenda));
		for(Appuntamento a: appuntamenti) {
			out.write(a.toStringFile());
            out.write("\n");
		}
		out.close();
	}

    public void leggiAgenda() throws AppuntamentoException, AgendaException {
        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(PATH + File.separator + this.nomeAgenda));
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

