package test;

import agenda.Agenda;
import agenda.AgendaException;
import agenda.Appuntamento;
import agenda.AppuntamentoException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestAgenda {
    @Test
    void testCostruttore(){
        Agenda a = new Agenda("a1");
        assertEquals(0, a.numApp());
        assertEquals("a1", a.getName());
    }

    /*@Test
    void testElimina() throws AppuntamentoException, AgendaException {
        Agenda a1 = new Agenda("1");
        Agenda a2 = new Agenda("2");

        a1.add("01-01-2000", "21-22", 60, "Pippo", "");
        a1.add("01-01-2000", "23-22", 60, "Pippo", "");

        a1.elimina();
    }*/

    @Test
    void testGetName(){
        Agenda a1 = new Agenda("famiglia");
        Agenda a2 = new Agenda("lavoro");
        Agenda a3 = new Agenda("studio");

        assertEquals("famiglia", a1.getName());
        assertEquals("lavoro", a2.getName());
        assertEquals("studio", a3.getName());

    }

    @Test
    void testAdd() throws AppuntamentoException, AgendaException {
        Agenda a1 = new Agenda("a1");
        a1.add("22-11-2022", "22-00", 120, "Leo", "ufficio");
        assertEquals(1, a1.numApp());

        Agenda a2 = new Agenda("a2");
        a2.add("22-11-2022", "22-10", 10, "Leo", "ufficio");
        a2.add("23-12-2022", "22-30", 10, "Leo", "ufficio");
        assertEquals(2, a2.numApp());
        a2.add("24-12-2022", "22-00", 60, "Leo", "ufficio");
        assertEquals(3, a2.numApp());
        assertEquals(1, a1.numApp());

    }
    @Test
    void testAddSovrapposizione() {
        Agenda a1 = new Agenda("a1");

        // 1         |-----------|                          OK
        // 2                        |------------|          OK
        // 3   |---------|                       .          SOVRAPPOSIZIONE
        // 4                  |-----------|      .          SOVRAPPOSIZIONE
        // 5                                     |------|   OK
        // 6                                   |-----|      SOVRAPPOSIZIONE

        AgendaException ex = assertThrows(AgendaException.class, () -> {
            a1.add("01-01-2022", "10-20", 60, "Leo", "ufficio"); // 1
            a1.add("01-01-2022", "11-30", 60, "Leo", "ufficio"); // 2
            a1.add("01-01-2022", "09-00", 90, "Leo", "ufficio"); // 3
        });
        assertEquals("Presente sovrapposizione", ex.getMessage());
        assertEquals(2, a1.numApp());

        ex = assertThrows(AgendaException.class, () -> {
            a1.add("01-01-2022", "11-00", 60, "Leo", "ufficio"); // 4
        });
        assertEquals("Presente sovrapposizione", ex.getMessage());
        assertEquals(2, a1.numApp());

        ex = assertThrows(AgendaException.class, () -> {
            a1.add("01-01-2022", "12-30", 60, "Leo", "ufficio");  // 5
            a1.add("01-01-2022", "12-25", 120, "Leo", "ufficio"); // 6
        });
        assertEquals("Presente sovrapposizione", ex.getMessage());
        assertEquals(3, a1.numApp());
    }

    @Test
    void modificaData() throws AppuntamentoException, AgendaException {
        Agenda a1 = new Agenda("");
        a1.add("01-01-2022", "10-20", 60, "Leo", "");

        AgendaException ex = assertThrows(AgendaException.class, () -> {
            a1.add("01-01-2022", "10-00", 60, "Leo", "");
        });
        assertEquals("Presente sovrapposizione", ex.getMessage());

        a1.modificaData(0, "02-01-2022");
        a1.add("01-01-2022", "10-00", 60, "Leo", "");

        assertEquals(2, a1.numApp());
    }

    @Test
    void modificaOra() throws AppuntamentoException, AgendaException {
        Agenda a1 = new Agenda("");
        a1.add("01-01-2022", "10-20", 60, "Leo", "");

        AgendaException exAg = assertThrows(AgendaException.class, () -> {
            a1.add("01-01-2022", "10-00", 60, "Leo", "");
        });
        assertEquals("Presente sovrapposizione", exAg.getMessage());

        a1.modificaOra(0, "09-00");
        a1.add("01-01-2022", "10-00", 60, "Leo", "");

        assertEquals(2, a1.numApp());

        Agenda a2 = new Agenda("");
        a2.add("01-01-2022", "10-20", 60, "Leo", "");
        a2.modificaData(0, "01-10-2023");
        assertEquals(1, a2.numApp());

        AppuntamentoException exApp = assertThrows(AppuntamentoException.class, () -> {
            a2.modificaData(0, "22-22");
        });
        assertEquals("Formato data invalido <dd-mm-aaaa>", exApp.getMessage());

        assertEquals(1, a2.numApp());

    }

    @Test
    void testIterator() throws AppuntamentoException, AgendaException {
        Agenda a1 = new Agenda("");
        a1.add("01-01-2022", "10-20", 60, "Leo", "ufficio");
        Appuntamento app1 = new Appuntamento("01-01-2022", "10-20", 60, "Leo", "ufficio");

        a1.add("01-01-2022", "11-30", 60, "Leo", "ufficio");
        Appuntamento app2 = new Appuntamento("01-01-2022", "11-30", 60, "Leo", "ufficio");

        Iterator<Appuntamento> iter = a1.iterator();

        assertEquals(app1.toString(), iter.next().toString());
        assertEquals(app2.toString(), iter.next().toString());
        assertFalse(iter.hasNext());

        Agenda a2 = new Agenda("");
        assertFalse(a2.iterator().hasNext());
    }

    @Test
    void testElimina() throws AppuntamentoException, AgendaException {
        Agenda a1 = new Agenda("");
        a1.add("01-01-2022", "10-20", 60, "Leo", "");
        a1.add("01-01-2022", "11-30", 60, "Leo", "ufficio");

        a1.elimina(0);
        assertEquals(1, a1.numApp());
        a1.add("01-01-2022", "10-00", 60, "Leo", "");
        assertEquals(2, a1.numApp());
    }
}
