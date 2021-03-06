package Data;

import javax.swing.ImageIcon;
import java.io.Serializable;

public class HistoryData implements Serializable {
  private ImageIcon image;
  private String text;
  private String label;
  private boolean isText = true;

  public HistoryData(Object component, String label) {
    if (component instanceof ImageIcon) {
      this.image = (ImageIcon) component;
      isText = false;
    } else if (component instanceof String) {
      this.text = (String) component;
    }

    this.label = label;
  }

  public Object getComponent() {
    if (isText) {
      return text;
    } else {
      return image;
    }
  }

  public void setComponent(Object component) {
    if (component instanceof ImageIcon) {
      this.image = (ImageIcon) component;
      isText = false;
    } else if (component instanceof String) {
      this.text = (String) component;
      isText = true;
    }
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}
