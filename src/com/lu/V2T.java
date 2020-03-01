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

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.EncodingAttributes;

public class V2T{
	private static String path = null;
	private static int ftp, fw, fh;
	private static double fps;

	private V2T() {
	}

	public static void v2t() {
		v2p();
	}

	private static List<File> v2p() {
		new Del();
		org.bytedeco.javacv.Frame fr = null;
		List<File> fs = new ArrayList<>();
		try {
			File f=new File(getFilePath());
			FFmpegFrameGrabber ffg = new FFmpegFrameGrabber(f);
			getAudio();
			String filePath = ".\\output\\text_row\\";
			String fileTargetName = "get";
			ffg.start();
			fps = ffg.getVideoFrameRate();
			ftp = ffg.getLengthInFrames();
			for (int i = 0; i < ftp; i++) {
				if (i == 1)
					wrtLog();
				fr = ffg.grabImage();
				doExecuteFrame(fr, filePath, fileTargetName, i, fs);
				if (i == (ftp - 2))
					break;
				p2t(filePath + fileTargetName + "_" + i + "." + "jpg", i);
				new File(filePath + fileTargetName + "_" + i + "." + "jpg").delete();
				System.out.println("���ڴ���\t"+(i+1)+"֡\t/\t"+(ftp-2)+"֡");
			}
			
			
			ffg.stop();
			ffg.close();
			new File(".\\output\\msc\\Audinfo.mp3").delete();
			Compress.comp();
		} catch (Exception e) {
			System.exit(0);
		}
		return fs;
	}

	private static String getFilePath() {
		int result = 0;
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
			String baset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_/\\'-&$:().";
			for (int i = 0; i < path.length(); i++) {
				if (baset.indexOf(path.charAt(i)) == -1) {
					JOptionPane.showMessageDialog(fc, "·���а����Ƿ��ַ�", "�Ƿ�·��", JOptionPane.ERROR_MESSAGE);
					return getFilePath();
				}
			}
			return path;
		}
		return null;
	}

	public static String getFilePath2() {
		int result = 0;
		JFileChooser fc = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		FileFilter ff = new FileNameExtensionFilter("�����ļ�(*.cvp)", "cvp");
		fc.setFileFilter(ff);
		fc.setCurrentDirectory(fsv.getHomeDirectory());
		fc.setDialogTitle("��ѡ�񹤳��ļ�...");
		fc.setApproveButtonText("ȷ��");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		result = fc.showOpenDialog(fc);
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
				output.createNewFile();
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

	private static void p2t(final String path, int indet) throws IOException {// pathΪ�ļ���ַ��indetΪ�ļ���������������ļ���

		/*
		 * �����㷨�ο���https://jingyan.baidu.com/album/295430f1946b4e0c7e00503c.html?picindex
		 * =2
		 */
		final String base = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'. ";// �����ַ����ӳ̶ȶ��峣���ַ���base
		final BufferedImage im = ImageIO.read(new File(path));// ����javax.imageio.ImageIO.read��������BufferedImage���͵�im����ȡͼƬ
		File ff = new File(".\\output\\text_row\\" + indet + ".txt");
		if (!ff.exists())
			ff.createNewFile();
		// ��������ļ�
		BufferedWriter bfr = new BufferedWriter(new FileWriter(".\\output\\text_row\\" + indet + ".txt"));
		// �������������
		for (int y = 0; y < im.getHeight(); y += (int) (im.getHeight() / 40)) {
			StringBuffer sbf = new StringBuffer();
			// ����StringBuffer���洢ÿ������ַ���ÿ���һ�����
			for (int x = 0; x < im.getWidth(); x += (int) (im.getWidth() / 40)) {
				final int pixel = im.getRGB(x, y);// ȡ���������ڵ����ɫ
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
		ff.setReadOnly();
	}

	private static void getAudio() {
		Runtime r = Runtime.getRuntime();
		try {
			Process p = r.exec(".\\source\\ffmpeg -i " + path + " .\\output\\msc\\Audinfo.mp3");
			p.waitFor();
			p.getOutputStream().close();
			p.getInputStream().close();
			p.getErrorStream().close();
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("pcm_alaw");
			audio.setBitRate(new Integer(64000));
			audio.setChannels(new Integer(1));
			audio.setSamplingRate(new Integer(8000));
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setFormat("wav");
			attrs.setAudioAttributes(audio);
			Encoder encoder = new Encoder();
			encoder.encode(new File(".\\output\\msc\\Audinfo.mp3"), new File(".\\output\\msc\\Audinfo.wav"), attrs);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static void wrtLog() {
		try {
			File f = new File("output\\text_row\\0.txt");
			BufferedReader bfr = new BufferedReader(new FileReader(f));
			long j = bfr.readLine().length();
			long i = bfr.lines().count();
			fw = (int) j;
			fh = (int) i;
			bfr.close();
			String info = (ftp - 2) + "," + fps + "," + fw + "," + fh;
			File config = new File("output\\config\\config.log");
			if (!config.exists())
				config.createNewFile();
			BufferedWriter bfw = new BufferedWriter(new FileWriter(config));
			bfw.write(info);
			bfw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
