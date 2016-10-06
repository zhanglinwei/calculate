package dialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cal.Fun;
import util.PropertiesUtil;

public class AddFunDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 350;
	private static final int HEIGHT = 200;
	private JTextField expJTF, funNameJTF, argsJTF;
	private JPanel centerPanel;
	private JButton addButton;
	private String funName, funExp, funArgs;
	
	private JTextArea showFun;
	
	
	private Fun fun;
	
	public AddFunDialog() {
		funName = "";
		funExp = "";
		funArgs = "";
		init();
	}

	public AddFunDialog(JTextArea funArea) {
		this();
		showFun = funArea;
	}

	public Fun getFun() {
		return fun;
	}

	public void launch() {
		registerAll();
	}

	private void registerAll() {
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				funName = funNameJTF.getText();
				funExp = expJTF.getText();
				funArgs = argsJTF.getText();
				fun = new Fun(funName, funExp, funArgs);
				PropertiesUtil.add(fun);
				PropertiesUtil.showFunctions(showFun);
				dispose();
			}
		});
	}

	private void init() {
		setTitle("添加函数");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		centerPanel = new JPanel();
		expJTF = new JTextField();
		funNameJTF = new JTextField();
		argsJTF = new JTextField();
		addButton = new JButton("确定");
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(funNameJTF);
		centerPanel.add(argsJTF);
		centerPanel.add(expJTF);
		centerPanel.add(addButton);
		add(centerPanel);
		setVisible(true);
	}

}
