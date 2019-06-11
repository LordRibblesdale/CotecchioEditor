package Interface;

import Data.Game;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ProgramTable extends AbstractTableModel {
    private String[] columns;
    private ArrayList<Object[]> data;

    private UserController controller;

    public ProgramTable(UserController controller) {
        this.controller = controller;

        columns = new String[] {
                controller.getSettings().getResourceBundle().getString("matchDate"),
                controller.getSettings().getResourceBundle().getString("playersNumber"),
                controller.getSettings().getResourceBundle().getString("winnerPlayer")
        };

        data = new ArrayList<>(1);
    }

    public ProgramTable(UserController controller, Game[] exec) {
        this.controller = controller;   //TODO is this useful?

        columns = new String[] {
                controller.getSettings().getResourceBundle().getString("matchDate"),
                controller.getSettings().getResourceBundle().getString("playersNumber"),
                controller.getSettings().getResourceBundle().getString("winnerPlayer")
        };

        data = new ArrayList<>(exec.length);

        for (Game p : exec) {
            data.add(new Object[] {
                    p.getDate(),
                    p.getResults().size(),
                    p.getWinner()
            });
        }
    }

    public void addProgram(Game exec) {
        data.add(new Object[] {
                exec.getDate(),
                exec.getResults().size(),
                exec.getWinner()
        });
    }

    public Object[] getProgram(int index) {
        return data.get(index);
    }

    public void editProgram(int index, Game exec) {
        data.set(index, new Object[] {
                exec.getDate(),
                exec.getResults().size(),
                exec.getWinner()
        });
    }

    public void removeProgram(int index) {
        if (!data.isEmpty() && index != -1) {
            data.remove(index);
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
