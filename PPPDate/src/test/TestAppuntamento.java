package test;

import agenda.Agenda;
import agenda.Appuntamento;
import agenda.AppuntamentoException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


public class TestAppuntamento {
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-kk-mm");

    @Test
    void testConstruttoreValida(){
        AppuntamentoException ex = assertThrows(AppuntamentoException.class, () -> {
            Appuntamento a1 = new Appuntamento("11-12-2022", "09-30", 120, "Pippo", "Casa");
            Appuntamento a2 = new Appuntamento("11-12-20", "09-30", 120, "Pippo", "Casa");
        });
        assertEquals("Formato data invalido <dd-mm-aaaa>", ex.getMessage());

        ex = assertThrows(AppuntamentoException.class, () -> {
            Appuntamento a1 = new Appuntamento("11-12-2022", "09-3", 120, "Pippo", "Casa");
        });
        assertEquals("Formato ora invalido <hh-mm> 24h format", ex.getMessage());

        ex = assertThrows(AppuntamentoException.class, () -> {
            Appuntamento a1 = new Appuntamento("11-12-2022", "09-30", 120, "Pippo4", "Casa");
        });
        assertEquals("Formato nome invalido", ex.getMessage());
    }

    @Test
    void testInizio() throws ParseException, AppuntamentoException {
        Date d1 = format.parse("01-01-2000-01-01");
        Appuntamento a1 = new Appuntamento("01-01-2000","01-01", 10, "Leo", "ufficio");
        assertEquals(d1, a1.inzio());

        Date d2 = format.parse("31-11-1999-23-15");
        Appuntamento a2 = new Appuntamento("31-11-1999","23-15", 120, "Leo", "ufficio");
        assertEquals(d2, a2.inzio());

        Date d3 = format.parse("28-02-2010-11-21");
        Appuntamento a3 = new Appuntamento("28-02-2010","11-21", 10, "Leo", "ufficio");
        assertEquals(d3, a3.inzio());
    }

    @Test
    void testFine() throws ParseException, AppuntamentoException {
        Appuntamento a1 = new Appuntamento("01-01-2000","01-01", 10, "Leo", "ufficio");
        Date d1 = format.parse("01-01-2000-01-11");
        assertEquals(d1, a1.fine());

        Appuntamento a2 = new Appuntamento("31-12-1999","23-15", 120, "Leo", "ufficio");
        Date d2 = format.parse("01-01-2000-01-15");
        assertEquals(d2, a2.fine());

        Appuntamento a3 = new Appuntamento("28-02-2010","23-21", 60, "Leo", "ufficio");
        Date d3 = format.parse("01-03-2010-00-21");
        assertEquals(d3, a3.fine());
    }

    @Test
    void appToString() throws AppuntamentoException {
        Appuntamento ap1 = new Appuntamento("01-01-2000","02-02", 10, "Leo", "ufficio");
        assertEquals("01-01-2000 02-02 10 Leo ufficio", ap1.toString());

        Appuntamento ap2 = new Appuntamento("25-12-2022","00-00", 120, "Leo", "casa");
        assertEquals("25-12-2022 00-00 120 Leo casa", ap2.toString());
    }

    @Test
    void testGetter() throws AppuntamentoException {
        Appuntamento ap1 = new Appuntamento("01-01-2000","02-02", 10, "Leo", "ufficio");
        assertEquals("01-01-2000", ap1.getData());
        assertEquals("02-02", ap1.getOra());
        assertEquals(10, ap1.getDurata());
        assertEquals("Leo", ap1.getNome());
        assertEquals("ufficio", ap1.getLuogo());


        Appuntamento ap2 = new Appuntamento("25-12-2022","00-00", 120, "Pippo", "casa");
        assertEquals("25-12-2022", ap2.getData());
        assertEquals("00-00", ap2.getOra());
        assertEquals(120, ap2.getDurata());
        assertEquals("Pippo", ap2.getNome());
        assertEquals("casa", ap2.getLuogo());

    }
}
