package com.lu;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;

import static java.awt.Font.*;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	public Window() {
		String[] string_arr = {".\\output",".\\output\\msc", ".\\output\\text_row", ".\\output\\config"};
		File f;
		for (String e : string_arr) if (!(f = new File(e)).exists()) System.out.println(f.mkdir());

		Runtime.getRuntime().addShutdownHook(new Thread(Del::new));

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

		b1.setFont(new Font("黑体", BOLD, 30));
		b2.setFont(new Font("黑体", BOLD, 30));
		b3.setFont(new Font("黑体", BOLD, 30));

		b1.addActionListener(e -> V2T.v2t());

		b2.addActionListener(e -> new Uncompress());
		
		b3.addActionListener(e -> {
			new Del();
			System.exit(0);
		});
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
class Del{
	public Del() {
		boolean inr = false;
		if(new File("output\\msc").listFiles()!=null)
			for (File i : Objects.requireNonNull(new File("output\\msc").listFiles()))
				inr=i.delete();
		if(new File("output\\text_row").listFiles()!=null)
			for (File i : Objects.requireNonNull(new File("output\\text_row").listFiles()))
				inr=i.delete();
		if(new File("output\\config").listFiles()!=null)
			for (File i : Objects.requireNonNull(new File("output\\config").listFiles()))
				inr=i.delete();
		System.out.println(inr);
	}
}