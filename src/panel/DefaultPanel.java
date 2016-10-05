package panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cal.Calculate;
import cal.Fun;
import dialog.AddFunDialog;
import dialog.DelFunDialog;

public class DefaultPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	// 显示表达式
	private JTextArea showArea;
	// 显示计算结果
	private JTextField result;
	// 显示用户自定义的函数
	private JTextArea funArea;
	// 加入函数按钮
	private JButton addFunction;
	// 删除函数按钮
	private JButton delFunction;
	
	public DefaultPanel() {
		init();
	}

	// 设置 DefaultPanel 显示
	private void init() {
		showArea = new JTextArea(4, 34);
		showArea.setLineWrap(true);
		JScrollPane jsp1 = new JScrollPane(showArea);
		result = new JTextField();
		result.setHorizontalAlignment(JTextField.RIGHT);
		JPanel jp1 = new JPanel();
		jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));
		jp1.add(jsp1);
		jp1.add(result);
		add(jp1);
		funArea = new JTextArea(6, 20);
		funArea.setEditable(false);
		JScrollPane jsp2 = new JScrollPane(funArea);
		addFunction = new JButton("添加函数");
		delFunction = new JButton("删除函数");
		JPanel jp2 = new JPanel();
		jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS));
		jp2.add(addFunction);
		jp2.add(delFunction);
		JPanel jp3 = new JPanel();
		jp3.setLayout(new BoxLayout(jp3, BoxLayout.LINE_AXIS));
		jp3.add(jsp2);
		jp3.add(jp2);
		add(jp3);
	}

	// 为各个组件添加监听，实现功能
	public void launch() {
		registerAddFun();
		registerDelFun();
		registerExpArea();
	}

	private void registerExpArea() {
		// 添加鼠标监听
		showArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 双击重置输入
				if (e.getClickCount() == 2) {
					showArea.setText("");
				}
			}

		});

		// 添加键盘监听
		showArea.addKeyListener(new KeyAdapter() {
			@Override
			// 输入回车时，表达式输入完成，计算表达式的值
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// 得到表达式
					String expression = showArea.getText();
					// 开启线程计算结果
					Calculate calculate = new Calculate(result, expression);
					Thread thread = new Thread(calculate);
					thread.start();
				}
			}
		});

	}

	private void registerAddFun() {
		// button，点击时添加函数
		addFunction.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AddFunDialog add = new AddFunDialog(funArea);
				add.launch();
			}
		});
	}

	private void registerDelFun() {
		delFunction.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new DelFunDialog();
			}
		});

	}

}