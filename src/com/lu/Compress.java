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
        FileFilter ff = new FileNameExtensionFilter("CVP文件", "cvp");
        fc.setFileFilter(ff);
        fc.setCurrentDirectory(fsv.getHomeDirectory());
        fc.setDialogTitle("保存工程文件至...");
        fc.setApproveButtonText("确定");
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
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            ProgressMonitorInputStream pmi = new ProgressMonitorInputStream(null, "打包中...",
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
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
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
