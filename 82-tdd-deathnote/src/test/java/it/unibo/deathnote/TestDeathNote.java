package it.unibo.deathnote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import it.unibo.deathnote.impl.DeathNoteImp;
import it.unibo.deathnote.api.DeathNote;

class TestDeathNote {
    private static final int MIN_EXCEPTION_LENG = "errore".length();
    private static final int NUM_RULES = 13;
    private static final String CAUSE_OF_DEATH_HEART_ATTACK = "heart attack";
    private static final String CAUSE_OF_DEATH = "cancer";
    private static final String NAME = "mario rossi";
    private static final String ANOTHER_NAME = "paolo paolini";
    private static final String DETAILS = "ran for too long";
    private static final String ANOTHER_DETAIL = "run for very too long";
    private DeathNote victim;

    @BeforeEach
    void setup() {
        this.victim = new DeathNoteImp();
    }

    @Test
    void test1() {

        final int upperLimit = 14;
        final int lowerLimit = 0;

        try {
            this.victim.getRule(-1);
            this.victim.getRule(lowerLimit);
            this.victim.getRule(upperLimit);
            fail("it should return an error but it doesn't");
        } catch (final IllegalArgumentException e) {
            assertTrue(MIN_EXCEPTION_LENG <= e.getMessage().length());
            assertInstanceOf(Exception.class, e);
            assertFalse(e.getMessage().isBlank());

        }
    }

    @Test
    void test2() {
        for (int i = 1; i < NUM_RULES; i++) {
            final String rule = this.victim.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isEmpty());
            assertFalse(rule.isBlank());
        }
    }

    @Test
    void test3() {
        assertFalse(this.victim.isNameWritten(NAME));
        this.victim.writeName(NAME);
        assertTrue(this.victim.isNameWritten(NAME));
        // capire come fare a vedere se c'è qualcun'altro del death book
        assertThrows(NullPointerException.class, () -> this.victim.writeName(null));

    }

    @Test
    void test4() throws InterruptedException { 
        final String anothercause = "karting accident";
        try {
            this.victim.writeDeathCause(CAUSE_OF_DEATH);
            this.victim.writeName(NAME);
            fail("there should be an exception");
        } catch (final IllegalStateException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().length() >= MIN_EXCEPTION_LENG);
            assertFalse(e.getMessage().isBlank());
        }
        this.victim.writeName(NAME);
        assertEquals(this.victim.getDeathCause(NAME), CAUSE_OF_DEATH_HEART_ATTACK);
        this.victim.writeName(ANOTHER_NAME);
        this.victim.writeDeathCause(anothercause);
        assertTrue(this.victim.isNameWritten(ANOTHER_NAME));
        assertEquals(anothercause, this.victim.getDeathCause(ANOTHER_NAME));
        Thread.sleep(100);
        this.victim.writeDeathCause(anothercause);
        assertEquals(anothercause, this.victim.getDeathCause(ANOTHER_NAME));
    }

    @Test
    void test5() throws InterruptedException {
        final int timewaiting = 6100;
        final String lastDetails;
        assertFalse(this.victim.isNameWritten(NAME));
        try {
            this.victim.writeDetails(DETAILS);
        } catch (final IllegalStateException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().length() >= MIN_EXCEPTION_LENG);
            assertFalse(e.getMessage().isBlank());
        }
        this.victim.writeName(NAME);
        assertEquals(this.victim.getDeathDetails(NAME), "");
        this.victim.writeDeathCause(CAUSE_OF_DEATH);
        assertTrue(this.victim.writeDetails(DETAILS));
        assertEquals(this.victim.getDeathDetails(NAME), DETAILS);
        this.victim.writeName(ANOTHER_NAME);
        lastDetails = this.victim.getDeathDetails(ANOTHER_NAME);
        Thread.sleep(timewaiting);
        this.victim.writeDetails(ANOTHER_DETAIL);
        assertEquals(lastDetails, this.victim.getDeathDetails(ANOTHER_NAME));
    }
}
