package client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import cal.Calculate;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 247;

	// 显示表达式和计算结果
	private JTextArea show;
	// 显示用户自定义的函数
	private JTextArea fun;
	// 键入用户自定义函数
	private JButton button;

	// 运行计算器，添加监听，实现计算器功能
	public void launch() {
		// 初始化计算器
		init();
		// 添加鼠标监听
		show.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				show.setText("");
			}
			
		});
		// 添加键盘监听
		show.addKeyListener(new KeyAdapter() {
			@Override
			// 输入回车时，表达式输入完成，计算表达式的值
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// 计算结果，转为字符串
					Calculate calculate = new Calculate();
					String expression = show.getText();
					String result = calculate.calculate(expression);
					// show显示结果
					show.setText(result);
				}
			}
		});
		// 函数显示面板，无法编辑
		fun.setEditable(false);
		// button，点击时添加函数
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addFun();
			}
		});
	}

	private void addFun() {
		// TODO Auto-generated method stub
		
	}

	// 初始化计算器,完成计算器的显示
	private void init() {
		setTitle("黑叔叔的计算器");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		show = new JTextArea("请在此处输入表达式，回车结束输入，计算结果！");
		fun = new JTextArea("此处显示函数");
		show.setLineWrap(true);
		fun.setLineWrap(true);
		JPanel contentPanel = new JPanel();
		add(contentPanel);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		JScrollPane jsp1 = new JScrollPane(show);
		jsp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel low = new JPanel();
		contentPanel.add(jsp1);
		contentPanel.add(low);
		low.setLayout(new BoxLayout(low, BoxLayout.X_AXIS));
		JScrollPane jsp2 = new JScrollPane(fun);
		jsp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		button = new JButton("添加函数");
		low.add(jsp2);
		low.add(button);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}


	public static void main(String[] args) {
		Client client = new Client();
		client.launch();
	}


}
