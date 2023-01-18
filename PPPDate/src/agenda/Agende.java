package agenda;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Agende {
    private static final ArrayList<Agenda> agende = new ArrayList<>();

    public static int numAgende() {
        return agende.size();
    }

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

    public static void elimina(Agenda a) {
        agende.remove(a);
    }

    public static Agenda getAgenda(int i) throws AgendeException {

        if(i> agende.size()) throw new AgendeException("Numero agenda inesistente");
        Agenda a = agende.get(i);

        return a;
    }

    public static void readAgenda(String name) throws AppuntamentoException, AgendaException, AgendeException {
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
