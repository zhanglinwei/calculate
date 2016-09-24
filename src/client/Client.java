package client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 247;

	private JTextArea jTextArea;
	private JTextField jTextField;

	// 运行计算器
	public void launch() {
		init();
	}

	// 初始化计算器
	private void init() {
		setTitle("黑叔叔的计算器");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		jTextArea = new JTextArea("请在此处输入表达式，回车结束输入，计算结果！", 10, 20);
		jTextField = new JTextField("此处显示结果！！");
		JPanel jPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
		boxLayout.minimumLayoutSize(jPanel);
		jPanel.setLayout(boxLayout);
		jTextArea.setLineWrap(true);
		JScrollPane jsp = new JScrollPane(jTextArea);
		jPanel.add(jsp);
		jPanel.add(jTextField);
		add(jPanel);
		jTextArea.addKeyListener(new KeyMonitor());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				calculate();
			}
		}

	}

	public static void main(String[] args) {
		Client client = new Client();
		client.launch();
	}

	// 计算表达式的值
	private void calculate() {
	}

}
