package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TabbedListButton extends AbstractAction {
    private UserController ui;

    public TabbedListButton(UserController ui) {
        this.ui = ui;

        putValue(Action.SHORT_DESCRIPTION, "Test");
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(TabbedListButton.class.getResource("WebComponent24.gif")));
        putValue(Action.SMALL_ICON, new ImageIcon(TabbedListButton.class.getResource("WebComponent16.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
