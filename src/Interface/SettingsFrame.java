package Interface;

import Data.Settings;
import FileManager.AutoSaveFile;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class SettingsFrame extends JFrame {
    private final String text = "Set autosave timer: ";
    private Timer autoSave;
    private JLabel timerInfo;
    private JSlider delay;
    //private JCheckBox disableTimer;
    private Settings settings;
    private UserController ui;

    public SettingsFrame(UserController ui) {
        super("Settings");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());
        this.ui = ui;
        this.settings = ui.getSettings();

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(timerInfo = new JLabel(text + settings.getRefreshSaveRate()/1000 + " seconds"), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        add(delay = new JSlider(JSlider.HORIZONTAL), constraints);
        delay.setMinimum(30000);
        delay.setMaximum(300000);
        delay.setValue(settings.getRefreshSaveRate());
        delay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //if (!disableTimer.isSelected()) {
                    int range = delay.getMaximum() - delay.getMinimum();
                    double valueAt = e.getPoint().x / (double) delay.getWidth();
                    delay.setValue((int) (delay.getMinimum() + valueAt*range));
                    settings.setRefreshSaveRate(delay.getValue());

                    validate();
                    saveSettings();
                //}
            }
        });

        /*
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(disableTimer = new JCheckBox("Disable?"));
        */

        autoSave = new Timer(settings.getRefreshSaveRate(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AutoSaveFile(settings.getOpenedFile(), ui.getPlayers());
            }
        });

        delay.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                autoSave.setDelay(((JSlider) changeEvent.getSource()).getValue());
                if (!ui.hasBeenSaved()) {
                    autoSave.start();
                }
                settings.setRefreshSaveRate(((JSlider) changeEvent.getSource()).getValue());
                timerInfo.setText(text + delay.getValue()/1000 + " seconds");

                saveSettings();
                validate();
            }
        });

        /*
        disableTimer.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (((JCheckBox) changeEvent.getSource()).isSelected()) {
                    autoSave.stop();
                    delay.setEnabled(false);
                    settings.setRefreshSaveRate(300000);
                } else {
                    settings.setRefreshSaveRate(delay.getValue());
                    autoSave.setDelay(delay.getValue());
                    delay.setEnabled(true);
                }

                saveSettings();
                validate();
            }
        });

        if (settings.getRefreshSaveRate() == 300000) {
            disableTimer.setSelected(true);
            delay.setEnabled(false);
        }
        */

        setMinimumSize(new Dimension(300, 100));
        setLocationRelativeTo(ui);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(false);
    }

    public void startTimer() {
        //if (settings.getRefreshSaveRate() != 300000) {
            autoSave.start();
        //}
    }
    public void stopTimer() {
        autoSave.stop();
    }

    private void saveSettings() {
        ui.setSettings(settings);
    }
}
