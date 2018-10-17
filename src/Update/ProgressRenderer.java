package Update;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ProgressRenderer extends JProgressBar implements TableCellRenderer {
   public ProgressRenderer() {
      super(0, 100);
      setStringPainted(true);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      setValue(((int) ((Float) value).floatValue()));
      return this;
   }
}
