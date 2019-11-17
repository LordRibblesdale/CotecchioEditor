package Interface;

import FileManager.SaveFile;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TabbedListButton extends AbstractAction {
    private UserController ui;

    TabbedListButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("editButton"));
        putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("editDescription"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(TabbedListButton.class.getResource("ComposeMail24.gif")));
        putValue(Action.SMALL_ICON, new ImageIcon(TabbedListButton.class.getResource("ComposeMail16.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!ui.hasBeenSaved()) {
            int result = JOptionPane.showConfirmDialog(ui.getFrame(),
                    ui.getSettings().getResourceBundle().getString("saveBeforeClosing"),
                    ui.getSettings().getResourceBundle().getString("exitConfirmation"),
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                new SaveFile(ui).actionPerformed(null);
                //ui.setUpData();
                ui.askForNextPage("EDIT");
            }
        } else {
            //ui.setUpData();
            ui.askForNextPage("EDIT");
        }
    }
}
