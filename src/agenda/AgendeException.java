package agenda;

/**
 * Eccezione agende
 * Possibili messaggi: Nome gia' in uso, Numero agenda inesistente, Agenda già presente
 *
 * @see java.lang.Throwable
 * @see Agende
 */
public class AgendeException extends Throwable {
    /**
     * Crea una classe eccezione che estende Throwable
     *
     * @param messaggio messaggio di errore
     */
    public AgendeException(String messaggio) {
        super(messaggio);
    }
}
