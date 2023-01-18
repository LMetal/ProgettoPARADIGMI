package test;

import agenda.Agenda;
import agenda.AgendaException;
import agenda.Appuntamento;
import agenda.AppuntamentoException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestAgenda {
    @Test
    void testCostruttore(){
        Agenda a = new Agenda("a1");
        assertEquals(0, a.numApp());
        assertEquals("a1", a.getName());
    }

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
    void testModificaData() throws AppuntamentoException, AgendaException {
        Agenda a0 = new Agenda("");

        a0.add("02-01-2023", "10-20", 60, "Leo", "casa");

        AppuntamentoException exApp = assertThrows(AppuntamentoException.class,() -> a0.modificaData(0, "22-22-202"));
        assertEquals("Formato data invalido <dd-mm-aaaa>", exApp.getMessage());

        //appuntamento non modificato
        assertEquals(1, a0.numApp());
        assertEquals(new Appuntamento("02-01-2023", "10-20", 60, "Leo", "casa").toString(), a0.iterator().next().toString());


        Agenda a1 = new Agenda("");

        a1.add("02-01-2022", "10-20", 60, "Leo", "casa");
        a1.modificaData(0, "01-01-2022");
        Iterator<Appuntamento> iter1 = a1.iterator();

        Appuntamento check = new Appuntamento("01-01-2022", "10-20", 60, "Leo", "casa");

        assertEquals(check.toString(), iter1.next().toString());
        assertEquals(1, a1.numApp());


        a1.add("02-01-2022", "10-20", 60, "Leo", "casa");
        a1.modificaData(1, "02-11-2022");
        Iterator<Appuntamento> iter2 = a1.iterator();
        iter2.next();

        check = new Appuntamento("02-11-2022", "10-20", 60, "Leo", "casa");

        assertEquals(check.toString(), iter2.next().toString());
        assertEquals(2, a1.numApp());


        AgendaException ex = assertThrows(AgendaException.class, () -> a1.modificaData(1, "01-01-2022"));
        assertEquals("Presente sovrapposizione\nAppuntamento non modificato", ex.getMessage());

        assertEquals(2, a1.numApp());

    }

    @Test
    void testModificaOra() throws AppuntamentoException, AgendaException {
        Agenda a0 = new Agenda("");

        a0.add("02-01-2023", "10-20", 60, "Leo", "casa");

        AppuntamentoException exApp = assertThrows(AppuntamentoException.class,() -> a0.modificaOra(0, "22-12-2022"));

        assertEquals("Formato ora invalido <hh-mm> 24h format", exApp.getMessage());
        assertEquals(1, a0.numApp());
        assertEquals(new Appuntamento("02-01-2023", "10-20", 60, "Leo", "casa").toString(), a0.iterator().next().toString());

        Agenda a1 = new Agenda("");

        a1.add("01-01-2022", "10-20", 60, "Leo", "casa");
        a1.modificaOra(0, "09-20");
        Iterator<Appuntamento> iter1 = a1.iterator();

        Appuntamento check = new Appuntamento("01-01-2022", "09-20", 60, "Leo", "casa");

        assertEquals(check.toString(), iter1.next().toString());
        assertEquals(1, a1.numApp());


        a1.add("01-01-2022", "10-20", 60, "Leo", "casa");
        a1.modificaOra(1, "22-30");
        Iterator<Appuntamento> iter2 = a1.iterator();
        iter2.next();

        check = new Appuntamento("01-01-2022", "22-30", 60, "Leo", "casa");

        assertEquals(check.toString(), iter2.next().toString());
        assertEquals(2, a1.numApp());


        AgendaException ex = assertThrows(AgendaException.class, () -> a1.modificaOra(1, "09-40"));

        assertEquals("Presente sovrapposizione\nAppuntamento non modificato", ex.getMessage());

        assertEquals(2, a1.numApp());


    }

    @Test
    void testModificaDurata() throws AppuntamentoException, AgendaException {
        Agenda a0 = new Agenda("");

        a0.add("02-01-2023", "10-20", 60, "Leo", "casa");

        a0.modificaDurata(0, 30);
        assertEquals(30, a0.iterator().next().getDurata());
        assertEquals(1, a0.numApp());

        a0.modificaDurata(0, 120);
        assertEquals(120, a0.iterator().next().getDurata());
        assertEquals(1, a0.numApp());

        a0.add("02-01-2023", "09-20", 60, "Leo", "casa");
        assertEquals(2, a0.numApp());
        AgendaException ex = assertThrows(AgendaException.class, () -> {
            a0.modificaDurata(0, 120);
        });
        assertEquals("Presente sovrapposizione\nAppuntamento non modificato", ex.getMessage());
        assertEquals(2, a0.numApp());

    }

    @Test
    void testModificaNome() throws AppuntamentoException, AgendaException {
        Agenda a0 = new Agenda("");
        a0.add("02-01-2023", "10-20", 60, "Alan Parson", "London");
        a0.add("02-01-2023", "22-20", 60, "Sting", "London");

        a0.modificaNome(0, "Jack Black");
        assertEquals("Jack Black", a0.iterator().next().getNome());

        a0.modificaNome(1, "Slash");
        Iterator<Appuntamento> iter = a0.iterator();
        iter.next();
        assertEquals("Slash", iter.next().getNome());

    }

    @Test
    void testIterator() throws AppuntamentoException, AgendaException {
        Agenda a1 = new Agenda("");
        Appuntamento app1 = new Appuntamento("01-01-2022", "10-20", 60, "Leo", "ufficio");
        a1.add(app1);

        Appuntamento app2 = new Appuntamento("01-01-2022", "11-30", 60, "Leo", "ufficio");
        a1.add(app2);

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
        a1.add("01-01-2022", "10-20", 60, "Leo", "casa");
        a1.add("02-01-2022", "11-30", 60, "Leo", "ufficio");

        a1.elimina(0);

        Iterator<Appuntamento> iter1 = a1.iterator();
        assertEquals(1, a1.numApp());
        assertEquals("02-01-2022", iter1.next().getData());

        Agenda a2 = new Agenda("");
        a2.add("01-01-2022", "10-20", 60, "Leo", "casa");
        a2.add("02-01-2022", "11-30", 60, "Leo", "ufficio");

        a2.elimina(1);

        Iterator<Appuntamento> iter2 = a2.iterator();
        assertEquals(1, a2.numApp());
        assertEquals("01-01-2022", iter2.next().getData());

    }

}
