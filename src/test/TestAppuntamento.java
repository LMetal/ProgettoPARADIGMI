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
        assertEquals("Formato data invalido <dd-mm-yyyy>", ex.getMessage());

        ex = assertThrows(AppuntamentoException.class, () -> new Appuntamento("11-12-2022", "09-3", 120, "Pippo", "Casa"));
        assertEquals("Formato ora invalido <hh-mm> 24h format", ex.getMessage());

        ex = assertThrows(AppuntamentoException.class, () -> new Appuntamento("11-12-2022", "09-30", -60, "Pippo", "Casa"));
        assertEquals("Formato durata invalida <int maggiore di 0>", ex.getMessage());

        ex = assertThrows(AppuntamentoException.class, () -> new Appuntamento("11-12-2022", "09-30", 120, "Pippo4", "Casa"));
        assertEquals("Formato nome invalido", ex.getMessage());
    }

    @Test
    void testInizio() throws AppuntamentoException {
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
    void testFine() throws AppuntamentoException {
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
        assertEquals("01-01-2000\t02-02\t10\t\tLeo\tufficio", ap1.toString());

        Appuntamento ap2 = new Appuntamento("25-12-2022","00-00", 120, "Leo", "casa");
        assertEquals("25-12-2022\t00-00\t120\t\tLeo\tcasa", ap2.toString());
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

    @Test
    void testIsSovrapposto() throws AppuntamentoException {
        //
        // 1                    |----------|
        // 2  |--------------|  .
        // 3                    .        |--------|
        // 4            |-------|
        //

        Appuntamento ap1 = new Appuntamento("01-01-2000","02-10", 60, "Leo", "ufficio");
        Appuntamento ap2 = new Appuntamento("01-01-2000","00-00", 120, "Leo", "casa");
        Appuntamento ap3 = new Appuntamento("01-01-2000","03-00", 10, "Leo", "ufficio");
        Appuntamento ap4 = new Appuntamento("01-01-2000","01-10", 60, "Leo", "ufficio");

        assertFalse(ap1.isSovrapposto(ap2));    //  1 - 2
        assertFalse(ap2.isSovrapposto(ap1));    //  2 - 1

        assertFalse(ap4.isSovrapposto(ap1));    //  4 - 1
        assertFalse(ap1.isSovrapposto(ap4));    //  1 - 4

        assertTrue(ap1.isSovrapposto(ap3));     //  1 - 3

        assertTrue(ap3.isSovrapposto(ap1));     //  3 - 1
    }

    @Test
    void testCompareTo() throws AppuntamentoException {
        //
        // 1                    |----------|
        // 2  |--------------|  .
        // 3                    .        |--------|
        // 4            |-------|
        //

        Appuntamento ap1 = new Appuntamento("02-01-2000","02-10", 60, "Leo", "ufficio");
        Appuntamento ap2 = new Appuntamento("02-01-2000","00-00", 120, "Leo", "casa");
        Appuntamento ap3 = new Appuntamento("02-01-2000","03-00", 10, "Leo", "ufficio");
        Appuntamento ap4 = new Appuntamento("02-01-2000","01-10", 60, "Leo", "ufficio");

        assertEquals(ap1.inzio().compareTo(ap2.inzio()), ap1.compareTo(ap2));
        assertEquals(ap2.inzio().compareTo(ap1.inzio()), ap2.compareTo(ap1));

        assertEquals(ap1.inzio().compareTo(ap2.inzio()), ap4.compareTo(ap2));

        assertEquals(ap3.inzio().compareTo(ap2.inzio()), ap3.compareTo(ap2));
    }

    @Test
    void testEquals() throws AppuntamentoException {
        //
        // 1        |----------|
        // 2        |----------|
        // 3   |---------------|
        // 4                   |-----------|
        // 5        |----|
        // 6        |------------------|
        //

        Appuntamento ap1 = new Appuntamento("01-01-2000","02-10", 60, "Leo", "ufficio");
        Appuntamento ap2 = new Appuntamento("01-01-2000","02-10", 60, "pippo", "ufficio");
        Appuntamento ap3 = new Appuntamento("01-01-2000","01-10", 120, "Leo", "ufficio");
        Appuntamento ap4 = new Appuntamento("01-01-2000","03-10", 120, "Leo", "ufficio");
        Appuntamento ap5 = new Appuntamento("01-01-2000","02-10", 30, "Leo", "ufficio");
        Appuntamento ap6 = new Appuntamento("01-01-2000","02-10", 100, "Leo", "ufficio");

        assertEquals(ap1, ap2);     //assertEquals applica ap1.equals(ap2)
        assertEquals(ap2, ap1);

        assertNotEquals(ap1, ap3);
        assertNotEquals(ap1, ap4);
        assertNotEquals(ap1, ap5);
        assertNotEquals(ap1, ap6);

    }

    @Test
    void testStringToFromFile() throws AppuntamentoException {
        Appuntamento ap1 = new Appuntamento("01-01-2000","02-10", 60, "Leo", "ufficio");
        Appuntamento ap2 = new Appuntamento("01-01-2023","13-20", 120, "Rick", "casa");

        String ap1Str = "01-01-2000,02-10,60,Leo,ufficio";
        String ap2Str = "01-01-2023,13-20,120,Rick,casa";

        assertEquals(ap1Str, ap1.toStringFile());
        assertEquals(ap2Str, ap2.toStringFile());

        assertEquals(ap1, new Appuntamento(ap1Str));
        assertEquals(ap2, new Appuntamento(ap2Str));

    }
}