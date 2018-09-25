package FileManager;

import Data.Settings;
import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SettingsButton extends AbstractAction {
    private UserController ui;

    public SettingsButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, "Settings");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        ui.getSettingsFrame().setVisible(true);
    }
}
