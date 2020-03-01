package com.lu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.swing.ProgressMonitorInputStream;

public class Uncompress {
	public Uncompress() {
		String rf = V2T.getFilePath2();
		if ("".equals(rf) || rf == null)
			return;
		File file = new File(rf);
		ZipInputStream zpin;
		try {
			ProgressMonitorInputStream df = new ProgressMonitorInputStream(null, "’˝‘⁄‘ÿ»Î...", new FileInputStream(file));
			ZipFile zpf = new ZipFile(file);
			zpin = new ZipInputStream(df);
			ZipEntry en;
			while (((en = zpin.getNextEntry()) != null) && !en.isDirectory()) {
				File tem = new File(en.getName());
				if (!tem.exists()) {
					tem.getParentFile().mkdir();
					OutputStream ops = new FileOutputStream(tem);
					InputStream in = zpf.getInputStream(en);

					int co = 0;
					while ((co = in.read()) != -1)
						ops.write(co);
					ops.close();
					in.close();
				}
				zpin.closeEntry();
			}
			df.close();
			zpin.close();
			zpf.close();
			Reader.reading();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
