package Interface;

import Data.Game;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;

public class ProgramTable extends AbstractTableModel {
    private String[] columns;
    private ArrayList<Object[]> data;

    private UserController controller;

    ProgramTable(UserController controller, Game[] exec) {
        this.controller = controller;

        columns = new String[] {
                controller.getSettings().getResourceBundle().getString("matchDate"),
                controller.getSettings().getResourceBundle().getString("hands"),
                controller.getSettings().getResourceBundle().getString("duration"),
                controller.getSettings().getResourceBundle().getString("listPlayer")
        };

        data = new ArrayList<>(exec.length);

        for (Game p : exec) {
            data.add(new Object[] {
                    p.getDate(),
                    p.getHands(),
                    setUpStringTime(p.getTime()),
                    p.getPlayers()
            });
        }
    }

    void addProgram(Game exec) {
        data.add(new Object[] {
                exec.getDate(),
                exec.getHands(),
                setUpStringTime(exec.getTime()),
                exec.getPlayers()
        });

        fireChanges();
    }

    public ArrayList<Game> getProgram(int index) {
        return new ArrayList<>(Arrays.asList((Game[]) data.get(index)));
    }

    public void editProgram(int index, Game exec) {
        data.set(index, new Object[] {
                exec.getDate(),
                exec.getHands(),
                setUpStringTime(exec.getTime()),
                exec.getPlayers()
        });

        fireChanges();
    }

    public void removeProgram(int index) {
        if (!data.isEmpty() && index != -1) {
            data.remove(index);
        }

        fireChanges();
    }

    public int getTime(int index) {
        return controller.getData().getGame().get(index).getTime();
    }

    String setUpStringTime(int time) {
        int hours = time/60;
        int mins = time - (hours*60);

        return hours + "h " + mins + "m";
    }

    void fireChanges() {
        fireTableDataChanged();

        for (int i = 0; i < controller.getPUI().size(); i++) {
            controller.getPUI().get(i).repaintTable();
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
}
