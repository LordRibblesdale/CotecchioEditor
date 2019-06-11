package Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CalendarButton extends AbstractAction {
    private UserController ui;

    public CalendarButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("calendarButton"));
        putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("calendarDescription"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(CalendarButton.class.getResource("History24.gif")));
        putValue(Action.SMALL_ICON, new ImageIcon(CalendarButton.class.getResource("History16.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.askForNextPage("CALENDAR");

        ui.validate();
    }
}
