package Data;

import javax.swing.*;
import java.io.Serializable;

public class HistoryData implements Serializable {
  private JComponent component;
  private String label;

  public HistoryData(JComponent component, String label) {
    this.component = component;
    this.label = label;
  }

  public JComponent getComponent() {
    return component;
  }

  public void setComponent(JComponent component) {
    this.component = component;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}
