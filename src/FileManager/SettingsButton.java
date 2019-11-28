package FileManager;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SettingsButton extends AbstractAction {
    private UserController ui;

    public SettingsButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("settings"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(SettingsButton.class.getResource("Preferences.png")));
        putValue(Action.SMALL_ICON, new ImageIcon(SettingsButton.class.getResource("Preferences.png")));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        ui.getSettingsFrame().setVisible(true);
    }
}
