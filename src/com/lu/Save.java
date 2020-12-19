package com.lu;


import java.io.File;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class Save {
    private Save() {
    }

    public static void saves() throws Exception {
        int result;
        JFileChooser fc = new JFileChooser();
        File a;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        fc.setDialogTitle("保存工程文件至指定目录(最好是空文件夹)");
        fc.setApproveButtonText("确定");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        result = fc.showSaveDialog(fc);
        if (JFileChooser.APPROVE_OPTION == result) {
            if (!(a = fc.getSelectedFile()).exists()) {
                a.mkdir();
            }
            if ((a=new File(a.getAbsolutePath() + "\\output")).exists()) {
                a.mkdir();
            }
            System.out.println(a.getAbsolutePath());
            Files.copy(new File("Runner.exe").toPath(),new File(fc.getSelectedFile().getPath()+"\\"+"Runner.exe").toPath());
            for (File f:new File("output\\").listFiles()
                 ) {

                f.renameTo(new File(fc.getSelectedFile().getPath()+"\\output\\"+f.getName()));
            }

        }
    }

}
