package Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Game implements Serializable {
    private static final long serialVersionUID = 710L;

    private ArrayList<PlayerStateGame> results;
    private byte hands;
    private int time = 0;
    private LocalDate date = null;
    private boolean isEditable = true;
    private boolean setByPass = false;

    public Game(ArrayList<PlayerStateGame> results, LocalDate date, byte hands, int time, boolean isEditable, boolean setByPass) {
        this.results = results;
        this.hands = hands;
        this.time = time;
        this.date = date;
        this.isEditable = isEditable;
        this.setByPass = setByPass;
    }

    public ArrayList<PlayerStateGame> getResults() {
        return results;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public void setResults(ArrayList<PlayerStateGame> results) {
        this.results = results;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public byte getHands() {
        return hands;
    }

    public void setHands(byte hands) {
        this.hands = hands;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String toStringTime() {
        int hours = time/60;
        int mins = time - (hours*60);

        return hours + "h " + mins + "m";
    }

    public boolean isSetByPass() {
        return setByPass;
    }

    public void setByPass(boolean setByPass) {
        this.setByPass = setByPass;
    }

    public String getPlayers() {
        StringBuilder w = new StringBuilder();

        for (int i = 0; i < results.size(); i++) {
            if (i != 0) {
                w.append(", ");
            }

            w.append(results.get(i).getUsername());
        }

        return w.toString();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (PlayerStateGame p : results) {
            s.append(p.toString());
        }

        return s.toString();
    }
}
