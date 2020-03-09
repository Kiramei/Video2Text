package com.lu;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;

public class V2T {
    private static String path = null;
    private static int ftp;
    private static double fps;
    private static boolean p;

    private V2T() {
    }

    /**
     * @param jLabel       The sending message path.
     * @param jProgressBar The progress of the JProgressBar.
     */
    public static void v2t(JProgressBar jProgressBar, JLabel jLabel) {
        v2p(jProgressBar, jLabel);
    }

    //The second pass.
    private static void v2p(JProgressBar jProgressBar, JLabel jLabel) {
        new Del();
        File f = new File(Objects.requireNonNull(getFilePath()));
        new Thread(V2T::getAudio).start();
        List<File> fs = new ArrayList<>();
        new Thread(() -> {
            try {
                Frame fr;
                FFmpegFrameGrabber ffg = new FFmpegFrameGrabber(f);
                String filePath = ".\\output\\text_row\\";
                String fileTargetName = "get";
                ffg.start();
                fps = ffg.getVideoFrameRate();
                ftp = ffg.getLengthInFrames();
                for (int i = 0; i < ftp; i++) {
                    if (i == 1) {
                        wrtLog();
                    }
                    fr = ffg.grabImage();
                    doExecuteFrame(fr, filePath, fileTargetName, i, fs);
                    if (i == (ftp - 2)) {
                        break;
                    }
                    p2t(filePath + fileTargetName + "_" + i + "." + "jpg", i);
                    p = new File(filePath + fileTargetName + "_" + i + "." + "jpg").delete();
                    jProgressBar.setValue((int) ((double) i / ftp * 100));
                    jLabel.setText("���ڴ���:" + i + "/" + ftp + "(" + (int) ((double) i / ftp * 100) + "%)");
                }
                System.out.println(p);
                ffg.stop();
                ffg.close();
                Compress.comp();
                jProgressBar.setValue(0);
                jLabel.setText("");
            } catch (Exception e) {
                System.exit(0);
            }
        }).start();

    }

    private static String getFilePath() {
        int result;
        JFileChooser fc = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        FileFilter ff = new FileNameExtensionFilter("��Ƶ�ļ�(*.mov,*.mp4,*.avi)", "mp4", "avi", "mov");
        fc.setFileFilter(ff);
        fc.setCurrentDirectory(fsv.getHomeDirectory());
        fc.setDialogTitle("��ѡ����Ƶ�ļ�...");
        fc.setApproveButtonText("ȷ��");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        result = fc.showOpenDialog(fc);
        if (JFileChooser.APPROVE_OPTION == result) {
            path = fc.getSelectedFile().getPath();
            String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_/\\'-&$:(). ";
            for (int i = 0; i < path.length(); i++) {
                if (base.indexOf(path.charAt(i)) == -1) {
                    JOptionPane.showMessageDialog(fc, "·���а����Ƿ��ַ�", "�Ƿ�·��", JOptionPane.ERROR_MESSAGE);
                    return getFilePath();
                }
            }
            return path;
        }
        return null;
    }

    public static String getFilePath2() {
        JFileChooser fc = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        FileFilter ff = new FileNameExtensionFilter("�����ļ�(*.cvp)", "cvp");
        fc.setFileFilter(ff);
        fc.setCurrentDirectory(fsv.getHomeDirectory());
        fc.setDialogTitle("��ѡ�񹤳��ļ�...");
        fc.setApproveButtonText("ȷ��");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fc.showOpenDialog(fc);
        if (JFileChooser.APPROVE_OPTION == result) {
            path = fc.getSelectedFile().getPath();
            return path;
        }
        return null;
    }

    private static void doExecuteFrame(Frame frame, String targetFilePath, String targetFileName, int index,
                                       List<File> files) {
        if (frame == null || frame.image == null) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "jpg";
        String fileName = targetFilePath + targetFileName + "_" + index + "." + imageMat;
        BufferedImage bi = converter.getBufferedImage(frame);
        File output = new File(fileName);
        if (!output.exists())
            try {
                p = output.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        files.add(output);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static void p2t(final String path, int inset) throws IOException {// pathΪ�ļ���ַ��insetΪ�ļ���������������ļ���

        /*
         * �����㷨�ο���https://jingyan.baidu.com/album/295430f1946b4e0c7e00503c.html?pic index=2
         */
        final String base = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'. ";// �����ַ����ӳ̶ȶ��峣���ַ���base
        final BufferedImage im = ImageIO.read(new File(path));// ����javax.imageio.ImageIO.read��������BufferedImage���͵�im����ȡͼƬ
        File ff = new File(".\\output\\text_row\\" + inset + ".txt");
        if (!ff.exists())
            p = ff.createNewFile();
        // ��������ļ�
        BufferedWriter bfr = new BufferedWriter(new FileWriter(".\\output\\text_row\\" + inset + ".txt"));
        // �������������
        double cross = (double) im.getHeight() / 45.0;
        double row = (double) im.getWidth() / ((double) im.getWidth() / (double) im.getHeight() * 90.0);
        System.out.println(row);
        for (double y = 0; y < im.getHeight(); y += cross) {
            StringBuilder sbf = new StringBuilder();
            // ����StringBuffer���洢ÿ������ַ���ÿ���һ�����
            for (double x = 0; x < im.getWidth(); x += row) {
                final int pixel = im.getRGB((int) x, (int) y);// ȡ���������ڵ����ɫ
                final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                final float grey = (0.299f * r + 0.578f * g + 0.114f * b);
                // ȡ�����صĻҶ�ֵ
                final int index = Math.round(grey * (base.length() + 1) / 255);// �õ��Ҷ�ֵ��ƥ���base�ַ��������������ַ�
                sbf.append(index >= base.length() ? " " : String.valueOf(base.charAt(index)));// �������жϲ��ӵ�sbf��
            }
            bfr.write(sbf.toString());// �����յĵ����ַ�������������
            bfr.newLine();// ���м������
        }
        bfr.close();// �ر������
        p = ff.setReadOnly();
    }

    private static void getAudio() {
        Runtime r = Runtime.getRuntime();
        try {
            Process p = r.exec(".\\lib\\ffmpeg -i " + path + " -f wav -ar 16000 .\\output\\msc\\Audinfo.wav");
            p.waitFor();
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void wrtLog() {
        try {
            File f = new File("output\\text_row\\0.txt");
            BufferedReader bfr = new BufferedReader(new FileReader(f));
            long j = bfr.readLine().length();
            long i = bfr.lines().count();
            int fw = (int) j;
            int fh = (int) i;
            bfr.close();
            String info = (ftp - 2) + "," + fps + "," + fw + "," + fh;
            File config = new File("output\\config\\config.log");
            if (!config.exists())
                p = config.createNewFile();
            BufferedWriter bfw = new BufferedWriter(new FileWriter(config));
            bfw.write(info);
            bfw.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
