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

        // Replace the bottomPanel + SOUTH add with a split pane:
        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPanel, scrollPaneBottom);
        verticalSplit.setResizeWeight(0.80); // top gets ~80% initially
        verticalSplit.setOneTouchExpandable(true); // adds quick expand/collapse buttons
        verticalSplit.setContinuousLayout(true); // smoother dragging
        verticalSplit.setBorder(null); // optional: cleaner look

        frame.add(panel, BorderLayout.NORTH);
        frame.add(verticalSplit, BorderLayout.CENTER);

        // If you want an initial divider position after the frame shows:
        SwingUtilities.invokeLater(() -> verticalSplit.setDividerLocation(0.75));

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
        SwingUtilities.invokeLater(() -> {
            textAreaQb.setText(data);
            textAreaQb.setCaretPosition(0);
            textAreaQb.revalidate();
            textAreaQb.repaint();
        });
    }

    public void displayImportData(String data) {
        SwingUtilities.invokeLater(() -> {
            textAreaImport.setText(data);
            textAreaImport.setCaretPosition(0);
            textAreaImport.revalidate();
            textAreaImport.repaint();
        });
    }

    public void displayExportData(String data) {
        textAreaExport.setText(data);
        SwingUtilities.invokeLater(() -> {
            textAreaExport.setText(data);
            textAreaExport.setCaretPosition(0);
            textAreaExport.revalidate();
            textAreaExport.repaint();
        });
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