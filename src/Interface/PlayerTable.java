package Interface;

import Data.Player;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;

public class PlayerTable extends AbstractTableModel {
    private String[] columns;
    private ArrayList<Object[]> data;

    private UserController controller;

    public PlayerTable(UserController controller, ArrayList<Player> players) {
        this.controller = controller;

        Collections.sort(players);

        columns = new String[] {
                controller.getSettings().getResourceBundle().getString("tableName"),
                controller.getSettings().getResourceBundle().getString("tableUserName"),
                controller.getSettings().getResourceBundle().getString("tableAverage"),
        };

        data = new ArrayList<>(players.size());

        for (Player p : players) {
            data.add(new Object[] {
                    p.getName(),
                    p.getUsername(),
                    p.getScore() / (float) p.getTotalPlays()
            });
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
