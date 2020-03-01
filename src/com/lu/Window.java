package com.lu;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	public Window() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				new Del();
			}
		});

		this.setBounds(600, 400, 400, 200);
		this.setTitle("选择模式...");
		this.setLayout(new GridLayout(3, 1, 0, 0));

		JButton b1 = new JButton("创建字符画文件");
		JButton b2 = new JButton("打开字符画文件");
		JButton b3 = new JButton("退    出");

		this.setVisible(true);
		b1.setVisible(true);
		b2.setVisible(true);
		b3.setVisible(true);

		this.add(b1);
		this.add(b2);
		this.add(b3);

		b1.setFont(new Font("黑体", 1, 30));
		b2.setFont(new Font("黑体", 1, 30));
		b3.setFont(new Font("黑体", 1, 30));

		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				V2T.v2t();
				b1.setEnabled(false);
				b2.setEnabled(false);
			}
		});
		
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Uncompress();
				b1.setEnabled(false);
				b2.setEnabled(false);
			}
		});
		
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (File i : new File(".\\output\\msc").listFiles())
					i.delete();
				System.exit(0);
			}
		});
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
class Del{
	public Del() {
		if(new File("output\\msc").listFiles()!=null)
			for (File i : new File("output\\msc").listFiles())
				i.delete();
		if(new File("output\\text_row").listFiles()!=null)
			for (File i : new File("output\\text_row").listFiles())
				i.delete();
		if(new File("output\\config").listFiles()!=null)
			for (File i : new File("output\\config").listFiles())
				i.delete();
	}
}