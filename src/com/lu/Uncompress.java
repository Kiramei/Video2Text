package com.lu;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.swing.*;

public class Uncompress {
	public Uncompress(JProgressBar jpg) {
		new Del();
		String rf = V2T.getFilePath2();
		if ("".equals(rf) || rf == null)
			return;
		File file = new File(rf);
		class Progressbar extends FilterInputStream{
			private int size=0;
			private int nread=0;

			protected Progressbar(InputStream in) {
				super(in);
				try {
					this.size = in.available();
				} catch (IOException e) {
					this.size=0;
				}
				jpg.setMaximum(this.size);
			}

			public int read() throws IOException {
				int c = this.in.read();
				if (c >= 0) {
					jpg.setValue(++this.nread);
				}
				return c;
			}
			public int read(byte[] b) throws IOException {
				int nr = this.in.read(b);
				if (nr > 0) {
					jpg.setValue(this.nread += nr);
				}
				return nr;
			}
			public int read(byte[] b, int off, int len) throws IOException {
				int nr = this.in.read(b, off, len);
				if (nr > 0) {
					jpg.setValue(this.nread += nr);
				}
				return nr;
			}
			public long skip(long n) throws IOException {
				long nr = this.in.skip(n);
				if (nr > 0L) {
					jpg.setValue(this.nread = (int)((long)this.nread + nr));
				}
				return nr;
			}
			public synchronized void reset() throws IOException {
				this.in.reset();
				this.nread = this.size - this.in.available();
				jpg.setValue(0);
			}
		}
		new Thread(() -> {
			try {
				ZipInputStream zen;
				FileInputStream fin=new FileInputStream(file);
				Progressbar df=new Progressbar(fin);
				ZipFile zpf = new ZipFile(file);
				zen = new ZipInputStream(df);
				ZipEntry en;
				boolean s = false;
				while (((en = zen.getNextEntry()) != null) && !en.isDirectory()) {
					File tem = new File(en.getName());
					if (!tem.exists()) {
						s=tem.getParentFile().mkdir();
						OutputStream ops = new FileOutputStream(tem);
						InputStream in = zpf.getInputStream(en);

						int co;
						while ((co = in.read()) != -1)
							ops.write(co);
						ops.close();
						in.close();
					}
					zen.closeEntry();
				}
				System.out.println(s);
				df.close();
				zen.close();
				zpf.close();
				jpg.setValue(0);
				Reader.reading();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}).start();

	}
}
