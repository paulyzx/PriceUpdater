package com.tdi.views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.PrintStream;

public class AppView {
    private JFrame frame;
    private JButton loadButton;
    private JButton getVendorButton;
    private JButton createExportCsvButton;
    private JButton saveCsvButton;
    private JTextArea textAreaQb;
    private JTextArea textAreaImport;
    private JTextArea textAreaExport;
    private JTextArea textAreaLog;

    public AppView() {
        createUI();
    }

    private void createUI() {
        frame = new JFrame("CSV Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 600);
        frame.setLayout(new BorderLayout());
        ((JComponent) frame.getContentPane())
                .setBorder(BorderFactory
                        .createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel(new GridLayout(1, 3));
        JPanel centerPanel = new JPanel(new GridLayout(1, 3));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel bottomPanel = new JPanel(new GridLayout(1, 1));
        bottomPanel.setPreferredSize(new Dimension(0, 100));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loadButton = new JButton("Load CSV");
        loadButton.setSize(100, 25);
        getVendorButton = new JButton("Get Vendor");
        getVendorButton.setSize(100, 25);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        createExportCsvButton = new JButton("Create Export CSV");
        createExportCsvButton.setSize(50, 25);
        saveCsvButton = new JButton("Save CSV");
        saveCsvButton.setSize(50, 25);

        textAreaQb = new JTextArea();
        textAreaQb.setEditable(false);

        textAreaImport = new JTextArea();
        textAreaImport.setEditable(false);

        textAreaExport = new JTextArea();
        textAreaExport.setEditable(false);

        textAreaLog = new JTextArea();
        textAreaLog.setEditable(false);

        panel.add(loadButton);
        panel.add(getVendorButton);
        buttonPanel.add(createExportCsvButton);
        buttonPanel.add(saveCsvButton);
        panel.add(buttonPanel);

        JScrollPane scrollPaneLeft = new JScrollPane(textAreaQb);
        scrollPaneLeft.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(scrollPaneLeft);

        JScrollPane scrollPaneCenter = new JScrollPane(textAreaImport);
        scrollPaneCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(scrollPaneCenter);

        JScrollPane scrollPaneRight = new JScrollPane(textAreaExport);
        scrollPaneRight.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(scrollPaneRight);

        JScrollPane scrollPaneBottom = new JScrollPane(textAreaLog);
        scrollPaneBottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(scrollPaneBottom);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        redirectSystemStreams();
    }

    public void addButtonListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    public void addPerkinsListener(ActionListener listener) {
        getVendorButton.addActionListener(listener);
    }

    public void addCreateExportCsvListener(ActionListener listener) {
        createExportCsvButton.addActionListener(listener);
    }

    public void addSaveCsvListener(ActionListener listener) {
        saveCsvButton.addActionListener(listener);
    }

    public void displayData(String data) {
        textAreaQb.setText(data);
    }

    public void displayImportData(String data) {
        textAreaImport.setText(data);
    }

    public void displayExportData(String data) {
        textAreaExport.setText(data);
    }

    private void redirectSystemStreams() {
        try {
            TextAreaOutputStream out = new TextAreaOutputStream(textAreaLog);
            System.setOut(new PrintStream(out, true));
            System.setErr(new PrintStream(out, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}