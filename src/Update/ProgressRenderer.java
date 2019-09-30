package Update;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ProgressRenderer extends ProgressMonitor implements TableCellRenderer {
   ProgressRenderer(Component mainWindow, String title, String message) {
      super(mainWindow, message, title ,0, 100);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      return null;
   }
}
