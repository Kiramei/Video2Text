package com.lu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ProgressMonitorInputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Compress {
    private static boolean p;

    private Compress() {
    }

    public static void comp() throws Exception {
        int result;
        JFileChooser fc = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        FileFilter ff = new FileNameExtensionFilter("CVP�ļ�", "cvp");
        fc.setFileFilter(ff);
        fc.setCurrentDirectory(fsv.getHomeDirectory());
        fc.setDialogTitle("���湤���ļ���...");
        fc.setApproveButtonText("ȷ��");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        result = fc.showSaveDialog(fc);
        if (JFileChooser.APPROVE_OPTION == result) {
            Compress.toZip(new FileOutputStream("output.cvp"));
            p = new File("output.cvp").renameTo(new File(fc.getSelectedFile().getPath() + ".cvp"));
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
            throws Exception {
        byte[] buf = new byte[2 * 1024];
        if (!sourceFile.isDirectory()) {
            // ��zip����������һ��zipʵ�壬��������nameΪzipʵ����ļ�������
            zos.putNextEntry(new ZipEntry(name));
            // copy�ļ���zip�������
            int len;
            ProgressMonitorInputStream pmi = new ProgressMonitorInputStream(null, "�����...",
                    new FileInputStream(sourceFile));
            while ((len = pmi.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            pmi.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // ��Ҫ����ԭ�����ļ��ṹʱ,��Ҫ�Կ��ļ��н��д���
                if (KeepDirStructure) {
                    // ���ļ��еĴ���
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // û���ļ�������Ҫ�ļ���copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // �ж��Ƿ���Ҫ����ԭ�����ļ��ṹ
                    if (KeepDirStructure) {
                        // ע�⣺file.getName()ǰ����Ҫ���ϸ��ļ��е����ּ�һб��,
                        // ��Ȼ���ѹ�����оͲ��ܱ���ԭ�����ļ��ṹ,���������ļ����ܵ�ѹ������Ŀ¼����
                        compress(file, zos, name + "/" + file.getName(), true);
                    } else {
                        compress(file, zos, file.getName(), false);
                    }
                }
            }
        }
    }

    private static void toZip(OutputStream out) throws RuntimeException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File("output");
            compress(sourceFile, zos, sourceFile.getName(), true);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean getP() {
        return p;
    }
}
