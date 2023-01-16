package agenda;

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


    public static Agenda getAgenda(int i) {
        return agende.get(i);
    }
}
