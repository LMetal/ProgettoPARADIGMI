package test;

import agenda.Appuntamento;
import agenda.AppuntamentoException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;


public class TestAppuntamento {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");

    @Test
    void testConstruttoreValida(){
        AppuntamentoException ex = assertThrows(AppuntamentoException.class, () -> {
            new Appuntamento("11-12-2022", "09-30", 120, "Pippo", "Casa");
            new Appuntamento("11-12-20", "09-30", 120, "Pippo", "Casa");
        });
        assertEquals("Formato data invalido <dd-mm-aaaa>", ex.getMessage());

        ex = assertThrows(AppuntamentoException.class, () -> new Appuntamento("11-12-2022", "09-3", 120, "Pippo", "Casa"));

        assertEquals("Formato ora invalido <hh-mm> 24h format", ex.getMessage());

        ex = assertThrows(AppuntamentoException.class, () -> new Appuntamento("11-12-2022", "09-30", 120, "Pippo4", "Casa"));

        assertEquals("Formato nome invalido", ex.getMessage());
    }

    @Test
    void testInizio() throws ParseException, AppuntamentoException {
        LocalDateTime d1 = LocalDateTime.parse("27-06-1888-22-00", format);
        Appuntamento a1 = new Appuntamento("27-06-1888","22-00", 55, "Jonas Kahnwald", "Nielsen's House");
        assertEquals(d1, a1.inzio());

        LocalDateTime d2 = LocalDateTime.parse("24-08-2020-09-00", format);
        Appuntamento a2 = new Appuntamento("24-08-2020","09-00", 120, "Claudia Tiedemann", "Nuclear Power Plant");
        assertEquals(d2, a2.inzio());

        LocalDateTime d3 = LocalDateTime.parse("10-11-1953-11-21", format);
        Appuntamento a3 = new Appuntamento("10-11-1953","11-21", 10, "Ulrich Nielsen", "Doppler's Bunker");
        assertEquals(d3, a3.inzio());
    }

    @Test
    void testFine() throws ParseException, AppuntamentoException {
        Appuntamento a1 = new Appuntamento("01-01-2000","01-01", 10, "Leo", "ufficio");
        LocalDateTime d1 = LocalDateTime.parse("01-01-2000-01-11", format);
        assertEquals(d1, a1.fine());

        Appuntamento a2 = new Appuntamento("31-12-1999","23-15", 120, "Leo", "ufficio");
        LocalDateTime d2 = LocalDateTime.parse("01-01-2000-01-15", format);
        assertEquals(d2, a2.fine());

        Appuntamento a3 = new Appuntamento("28-02-2010","23-21", 60, "Leo", "ufficio");
        LocalDateTime d3 = LocalDateTime.parse("01-03-2010-00-21", format);
        assertEquals(d3, a3.fine());
    }

    @Test
    void appToString() throws AppuntamentoException {
        Appuntamento ap1 = new Appuntamento("01-01-2000","02-02", 10, "Leo", "ufficio");
        assertEquals("01-01-2000\t02-02\t10\tLeo\tufficio", ap1.toString());

        Appuntamento ap2 = new Appuntamento("25-12-2022","00-00", 120, "Leo", "casa");
        assertEquals("25-12-2022\t00-00\t120\tLeo\tcasa", ap2.toString());
    }

    @Test
    void testGetter() throws AppuntamentoException {
        Appuntamento ap1 = new Appuntamento("01-01-2000","02-02", 10, "Jabba the Hutt", "ufficio");
        assertEquals("01-01-2000", ap1.getData());
        assertEquals("02-02", ap1.getOra());
        assertEquals(10, ap1.getDurata());
        assertEquals("Jabba the Hutt", ap1.getNome());
        assertEquals("ufficio", ap1.getLuogo());


        Appuntamento ap2 = new Appuntamento("25-12-2022","00-00", 120, "James T. Kirk", "casa");
        assertEquals("25-12-2022", ap2.getData());
        assertEquals("00-00", ap2.getOra());
        assertEquals(120, ap2.getDurata());
        assertEquals("James T. Kirk", ap2.getNome());
        assertEquals("casa", ap2.getLuogo());

    }
}