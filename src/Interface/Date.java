package Interface;

import java.text.SimpleDateFormat;

class Date extends java.util.Date {
  Date() {
    super();
  }

  @Override
  public String toString() {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    return formatter.format(this);
  }
}
