package agenda;

/**
 * Eccezione agenda
 * Possibili messaggi: Presente sovrapposizione, Index out of bound
 *
 * @see java.lang.Throwable
 * @see Agenda
 */
public class AgendaException extends Throwable {
    /**
     * Crea una classe eccezione che estende Throwable
     *
     * @param messaggio messaggio di errore
     */
    public AgendaException(String messaggio) {
        super(messaggio);
    }
}
