package agenda;

/**
 * Eccezione appuntamento <br>
 * Possibili messaggi:
 * Formattazione errata,
 * Formato appuntamento da file invalido,
 * Formato data invalido &lt;dd-mm-yyyy&gt;,
 * Formato ora invalido &lt;hh-mm&gt; 24h format,
 * Formato durata invalida &lt;int maggiore di 0&gt;,
 * Formato nome invalido
 *
 * @see java.lang.Throwable
 * @see Appuntamento
 */
public class AppuntamentoException extends Throwable {
    /**
     * Crea una classe eccezione che estende Throwable
     *
     * @param messaggio messaggio di errore
     */
    public AppuntamentoException(String messaggio) {
        super(messaggio);
    }
}
