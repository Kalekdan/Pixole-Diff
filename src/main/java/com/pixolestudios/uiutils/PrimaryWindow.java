package main.java.com.pixolestudios.uiutils;

import main.java.com.pixolestudios.pdiff.Diff;
import main.java.com.pixolestudios.plogger.PLog;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrimaryWindow extends JFrame {
    private final String MAIN_WINDOW_TITLE = "Pixole Diff Tool";
    private final int WIDTH = 300;
    private final int HEIGHT = 200;

    private JButton btnAddImg1;
    private JButton btnAddImg2;
    private JButton btnDiff;

    private JFileChooser fileChooser;

    private String img1Path = "";
    private String img2Path = "";

    public PrimaryWindow() {
        setupWindow();
        setupWindowContents();
        setLayout(new FlowLayout());
    }

    private void setupWindow() {
        PLog.debug("Creating main window: " + MAIN_WINDOW_TITLE, "ui");
        setTitle(MAIN_WINDOW_TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupWindowContents() {
        btnAddImg1 = new JButton("Load image 1");
        btnAddImg1.setBackground(Color.orange);
        btnAddImg1.setOpaque(true);
        btnAddImg1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLoadImageAction(1);
                prepareToDiff();
            }
        });
        add(btnAddImg1);

        btnAddImg2 = new JButton("Load image 2");
        btnAddImg2.setBackground(Color.orange);
        btnAddImg2.setOpaque(true);
        btnAddImg2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLoadImageAction(2);
                prepareToDiff();
            }
        });
        add(btnAddImg2);

        btnDiff = new JButton("Calculate diff");
        btnDiff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDiffAction();
            }
        });
        btnDiff.setEnabled(false);
        add(btnDiff);
    }

    private void performDiffAction() {
        Diff diff = new Diff(img1Path, img2Path);
        diff.CalculateDiff(false, true);
    }

    // If both images are loaded - enable diff button
    private void prepareToDiff() {
        if (!img1Path.isEmpty() && !img2Path.isEmpty()) {
            btnDiff.setEnabled(true);
        } else {
            btnDiff.setEnabled(false);
        }
    }

    private void doLoadImageAction(int oneOrTwo) {
        fileChooser = new JFileChooser();
        int r = fileChooser.showOpenDialog(null);
        // if the user selects a file
        if (r == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (oneOrTwo == 1) {
                img1Path = path;
                btnAddImg1.setBackground(Color.GREEN);
                btnAddImg1.setOpaque(true);
            } else {
                img2Path = path;
                btnAddImg2.setBackground(Color.GREEN);
                btnAddImg2.setOpaque(true);
            }
            PLog.info("Loading image " + oneOrTwo + " from " + path, "load");
        } else {
            PLog.debug("Loading image " + oneOrTwo + " cancelled", "load");
        }
    }
}
