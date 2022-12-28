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
        assertEquals("Formato data invalido", ex.getMessage());

        ex = assertThrows(AppuntamentoException.class, () -> {
            Appuntamento a1 = new Appuntamento("11-12-2022", "09-3", 120, "Pippo", "Casa");
        });
        assertEquals("Formato ora invalido", ex.getMessage());

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
        Date d1 = format.parse("01-01-2000-01-11");
        Appuntamento a1 = new Appuntamento("01-01-2000","01-01", 10, "Leo", "ufficio");
        assertEquals(d1, a1.fine());

        Date d2 = format.parse("01-01-2000-01-15");
        Appuntamento a2 = new Appuntamento("31-12-1999","23-15", 120, "Leo", "ufficio");
        assertEquals(d2, a2.fine());

        Date d3 = format.parse("01-03-2010-00-21");
        Appuntamento a3 = new Appuntamento("28-02-2010","23-21", 60, "Leo", "ufficio");
        assertEquals(d3, a3.fine());
    }
}
