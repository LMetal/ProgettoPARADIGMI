package test;

import agenda.Agenda;
import agenda.AgendaException;
import agenda.Appuntamento;
import agenda.AppuntamentoException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

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
        // 3   |---------|                                  SOVRAPPOSIZIONE
        // 4                  |-----------|                 SOVRAPPOSIZIONE
        // 5                                   |-----|      SOVRAPPOSIZIONE

        AgendaException ex = assertThrows(AgendaException.class, () -> {
            a1.add("01-01-2022", "10-20", 60, "Leo", "ufficio"); // 1
            a1.add("01-01-2022", "11-30", 60, "Leo", "ufficio"); // 2
            a1.add("01-01-2022", "09-00", 90, "Leo", "ufficio"); // 3
        });
        assertEquals("Presente sovrapposizione", ex.getMessage());

        ex = assertThrows(AgendaException.class, () -> {
            a1.add("01-01-2022", "11-00", 60, "Leo", "ufficio"); // 4
        });
        assertEquals("Presente sovrapposizione", ex.getMessage());

        ex = assertThrows(AgendaException.class, () -> {
            a1.add("01-01-2022", "12-25", 120, "Leo", "ufficio"); // 4
        });
        assertEquals("Presente sovrapposizione", ex.getMessage());
    }
}
