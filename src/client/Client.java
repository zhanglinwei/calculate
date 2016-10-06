package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import panel.DefaultPanel;
import util.PropertiesUtil;

/**
 * 客户端实体类
 * 设定客户端大小、显示风格
 * @author zhanglinwei
 *
 */
public class Client extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 247;

	private DefaultPanel defaultPanel;
	
	public Client() {
		defaultPanel = new DefaultPanel();
	}
	

	// 运行计算器，添加监听，实现计算器功能
	public void launch() {
		setTitle("计算器HSS");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				PropertiesUtil.store();
			}
		});
		add(defaultPanel);
		defaultPanel.launch();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}



	public static void main(String[] args) {
		Client client = new Client();
		client.launch();
	}


}
