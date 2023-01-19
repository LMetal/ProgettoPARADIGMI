package test;

import agenda.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAgende {
    @BeforeEach
    void setup() throws AgendeException {
        for(int i=Agende.numAgende(); i>0; i--){
            Agende.elimina(Agende.getAgenda(i-1));
        }
    }

    @Test
    void testNumAgende() throws AgendeException {
        assertEquals(0, Agende.numAgende());
        Agende.createAgenda("pippo");
        assertEquals(1, Agende.numAgende());
        Agende.createAgenda("2");
        assertEquals(2, Agende.numAgende());
    }

    @Test
    void testSetNome() throws AgendeException {
        Agende.createAgenda("pippo");
        assertEquals("pippo", Agende.getAgenda(0).getName());
        Agende.createAgenda("topolino");
        assertEquals("topolino", Agende.getAgenda(1).getName());
    }

    @Test
    void testNomeUnico(){
        AgendeException ex = assertThrows(AgendeException.class, () -> {
            Agende.createAgenda("lavoro");
            Agende.createAgenda("studio");
            Agende.createAgenda("lavoro");
        });
        assertEquals("Nome gia' in uso", ex.getMessage());
    }

    @Test
    void testElimina() throws AgendeException {
        Agende.createAgenda("lavoro");
        Agende.createAgenda("amici");

        assertEquals(2, Agende.numAgende());
        Agende.elimina(Agende.getAgenda(0));
        assertEquals(1, Agende.numAgende());
    }

    @Test
    void testScriviLeggiFile() throws IOException, AppuntamentoException, AgendaException, AgendeException {
        Agende.createAgenda("foo");
        Appuntamento app1 = new Appuntamento("01-01-2022", "10-20",60, "Leo", "casa");
        Appuntamento app2 = new Appuntamento("02-01-2022", "11-30", 60, "Leo", "ufficio");

        Agenda a1 = Agende.getAgenda(0);

        a1.add(app1);
        a1.add(app2);

        Agende.getAgenda(0).scriviAgenda();

        a1.add("13-01-2022", "11-30", 60, "Leo", "ufficio");

        AgendeException ex = assertThrows(AgendeException.class, () -> Agende.readAgenda("foo"));
        assertEquals("Agenda già presente", ex.getMessage());
        assertEquals(3, a1.numApp());

        Agende.elimina(a1);

        AgendaException ex2 = assertThrows(AgendaException.class, () -> Agende.readAgenda("foo3"));
        assertEquals("Agenda non trovata", ex2.getMessage());

        Agende.readAgenda("foo");
        Agenda a2 = Agende.getAgenda(0);
        assertEquals(2, a2.numApp());

        Iterator<Appuntamento> iter = a2.iterator();
        assertEquals(iter.next().toString(), app1.toString());
        assertEquals(iter.next().toString(), app2.toString());
    }
}
