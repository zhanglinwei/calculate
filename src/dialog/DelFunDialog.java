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

import util.PropertiesUtil;

public class DelFunDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 200;
	private static final int HEIGHT = 100;

	private JTextField funName;
	private JButton delButton;

	private JTextArea showFun;

	public DelFunDialog() {
		init();
	}

	public DelFunDialog(JTextArea area) {
		this();
		showFun = area;
	}

	private void init() {
		setTitle("添加函数");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		funName = new JTextField();
		delButton = new JButton("删除");
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.add(funName);
		jPanel.add(delButton);
		add(jPanel);
		setVisible(true);
	}

	public void launch() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		delButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String name = funName.getText();
				PropertiesUtil.delete(name);
				PropertiesUtil.showFunctions(showFun);
				dispose();
			}

		});
	}

}
