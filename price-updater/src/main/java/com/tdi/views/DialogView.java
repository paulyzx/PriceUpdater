package com.tdi.views;

import javax.swing.*;

import com.tdi.models.config.Config;

import java.io.File;
import java.util.Properties;

public class DialogView {
    private JFrame frame;
    private Config config;

    public DialogView(Properties properties) {
        this.config = new Config(properties);
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
    }

    public String showInputDialog(String message, String title) {
        return (String) JOptionPane.showInputDialog(frame, message, title,
                JOptionPane.QUESTION_MESSAGE, null, config.Options,
                null);
    }

    public void showMessageDialog(String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private File showFileChooser(File file) {
        System.out.println("Showing file chooser for: " + file.getAbsolutePath());
        JFileChooser fileChooser = new JFileChooser(file);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int returnValue = fileChooser.showOpenDialog(fileChooser);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public File showLoadCsvFileChooser() {
        return showFileChooser(new File(System.getProperty("user.dir"),
                config.QbImportFilePath));
    }

    public File showVendorFileChooser() {
        return showFileChooser(new File(System.getProperty("user.dir"),
                config.VendorImportFilePath));
    }

    public File showExportFileChooser() {
        JFileChooser fileChooser = new JFileChooser(
                new File(System.getProperty("user.dir"), config.ExportFilePath));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
