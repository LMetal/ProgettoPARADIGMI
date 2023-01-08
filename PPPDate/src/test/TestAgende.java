package test;

import agenda.Agenda;
import agenda.Agende;
import agenda.AgendeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class TestAgende {
    @BeforeEach
    void setup(){
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
}
