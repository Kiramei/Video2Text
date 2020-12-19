package com.lu;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class Window extends JFrame {
    private static boolean p;

    public Window() throws UnsupportedLookAndFeelException {
        String string = ".\\output";
        File f;
        if (!(f = new File(string)).exists()) p = f.mkdir();
        Runtime.getRuntime().addShutdownHook(new Thread(Del::new));
        initComponents();
    }

    private void initComponents() throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        JButton button2 = new JButton();
        JButton button3 = new JButton();
        JLabel label1 = new JLabel();
        progressBar1 = new JProgressBar();
        info = new JLabel();

        //======== this ========
        setBackground(Color.white);
        setTitle("The Video2Text Tool");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- button2 ----
        button2.setText("Create");
        button2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        contentPane.add(button2);
        button2.setBounds(70, 55, 305, 50);

        //---- button3 ----
        button3.setText("Exit");
        button3.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        contentPane.add(button3);
        button3.setBounds(70, 115, 305, 50);

        //---- label1 ----
        label1.setText("Video2Text Tool");
        label1.setIcon(null);
        label1.setFont(new Font("\u5e7c\u5706", Font.BOLD, 32));
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(label1);
        label1.setBounds(85, 10, 275, 35);
        contentPane.add(progressBar1);
        progressBar1.setBounds(70, 180, 305, 25);
        contentPane.add(info);
        info.setBounds(70, 210, 305, 20);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        button2.addActionListener(actionEvent -> V2T.v2t(progressBar1, info, button2));
        button3.addActionListener(actionEvent -> {
            new Del();
            System.exit(0);
        });
        setSize(460, 280);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public static boolean getP() {
        return p;
    }

    private JProgressBar progressBar1;
    private JLabel info;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
