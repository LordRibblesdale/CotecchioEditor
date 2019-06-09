package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CalendarButton extends AbstractAction {
    private UserController ui;

    public CalendarButton(UserController ui) {
        this.ui = ui;

        putValue(Action.SHORT_DESCRIPTION, "Test");
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(CalendarButton.class.getResource("WebComponent24.gif")));
        putValue(Action.SMALL_ICON, new ImageIcon(CalendarButton.class.getResource("WebComponent16.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
