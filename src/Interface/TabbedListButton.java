package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TabbedListButton extends AbstractAction {
    private UserController ui;

    public TabbedListButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("editButton"));
        putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("editDescription"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(TabbedListButton.class.getResource("ComposeMail24.gif")));
        putValue(Action.SMALL_ICON, new ImageIcon(TabbedListButton.class.getResource("ComposeMail16.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.askForNextPage("EDIT");
    }
}
