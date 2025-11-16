package it.unibo.deathnote.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import it.unibo.deathnote.api.DeathNote;

/**
 * A DeathNote is a special book intended to be used by Shinigamis (Death gods) to kill humans,
 * thereby extending their own lives. Each DeathNote has a set of rules that must be followed
 * in order to use it properly.
 */
public class DeathNoteImp implements DeathNote {

    private static final int MININDEX = 1;
    private static final int MAXINDEX = 14;
    private static final double TIMED_1 = 40;
    private static final double TIMED_2 = 6040;
    private static final String CAUSE0 = "heart attack";
    private final Map<String, List<String>> map = new HashMap<>();
    private String currentName = "";
    private final Victim currentVictim = new Victim();

    /**
     * Returns the rule with the given number.
     *
     * @param ruleNumber the number of the rule to return. The first rule has number one
     * @return the rule with the given number
     * @throws IllegalArgumentException if the given rule number is smaller than 1 or larger
     *     than the number of rules
     */
    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber > MAXINDEX || ruleNumber < MININDEX) {
            throw new IllegalArgumentException("invalid rule number");
        }
        return this.RULES.get(ruleNumber);
    }

    /**
     * The human whose name is written in this DeathNote will die.
     *
     * @param name the name of the human to kill
     * @throws NullPointerException if the given name is null.
     */
    @Override
    public void writeName(final String name) {
        if (name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("invalid name");
        }
        Objects.requireNonNull(name, "invalid name");
        if (!isNameWritten(name)) {
            map.put(name, new ArrayList<>(List.of(CAUSE0, "")));
            currentVictim.setTimeWrite(System.currentTimeMillis());
            currentName = name;
        }
    }

    /**
     * If the cause of death is written within the next 40 milliseconds of writing the person's
     * name, it will happen.
     *
     * @param cause the cause of the human's death
     * @return true if the cause was written within 40 milliseconds, false otherwise
     * @throws IllegalStateException if there is no name written in this DeathNote,
     *     or the cause is null
     */
    @Override
    public boolean writeDeathCause(final String cause) {

        if (!isNameWritten(currentName)) {
            throw new IllegalStateException("there is no name written in the deathnote");
        }

        if (-currentVictim.getTimeWrite() + System.currentTimeMillis() <= TIMED_1) {
            final List<String> tmp = map.get(currentName);
            tmp.removeFirst();
            tmp.addFirst(cause);
            map.replace(currentName, tmp);
            currentVictim.setTimeCause(System.currentTimeMillis());
            return true;
        }

        return false;
    }

    /**
     * After writing the cause of death, details of the death should be written in the next
     * 6 seconds and 40 milliseconds.
     *
     * @param details the details of the human's death
     * @return true if the details were written within 6 seconds and 40 milliseconds, false otherwise
     * @throws IllegalStateException if there is no name written in this DeathNote,
     *     or the details are null
     */
    @Override
    public boolean writeDetails(final String details) {

        if (!isNameWritten(currentName) || Objects.isNull(details)) {
            throw new IllegalStateException("there is no name written in the deathnote");
        }
        if (System.currentTimeMillis() - currentVictim.getTimeCause() <= TIMED_2) {
            final List<String> tmp = map.get(currentName);
            tmp.remove(tmp.getLast());
            tmp.add(details);

            map.replace(currentName, tmp);
            return true;
        }
        return false;
    }

    /**
     * Provides the cause of death of the person with the given name.
     *
     * @param name the name of the person whose death cause to return
     * @return the death cause of the person with the given name.
     *     If the cause of death is not specified, the method will return "heart attack".
     * @throws IllegalArgumentException if the provided name is not written in this DeathNote
     */
    @Override
    public String getDeathCause(final String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("the provided name is not written in this DeathNote");
        }

        return map.get(name).getFirst();
    }

    /**
     * Provides the details of the death of the person with the given name.
     *
     * @param name the name of the person whose death cause to return
     * @return the death details of the person with the given name,
     *     or an empty string if no details have been provided.
     * @throws IllegalArgumentException if the provided name is not written in this DeathNote.
     */
    @Override
    public String getDeathDetails(final String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("the provided name is not written in this DeathNote");
        }
        return map.get(name).getLast();
    }

    /**
     * Checks if the given name is written in this DeathNote.
     *
     * @param name the name of the person
     * @return true if the given name is written in this DeathNote, false otherwise
     */
    @Override
    public boolean isNameWritten(final String name) {
        return map.containsKey(name);
    }

    /**
     * Inner class representing a victim.
     */
    private final class Victim {

        private double timeWritten;
        private double timeCause;

        void setTimeWrite(final double t) {
            this.timeWritten = t;
        }

        void setTimeCause(final double t) {
            this.timeCause = t;
        }

        double getTimeCause() {
            return this.timeCause;
        }

        double getTimeWrite() {
            return this.timeWritten;
        }

    }
}
