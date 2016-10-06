package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JTextArea;

import cal.Fun;

public class PropertiesUtil {

	public static Map<String, Fun> funs = new TreeMap<>();

	public static void add(Fun fun) {
		funs.put(fun.getFunName(), fun);
	}

	public static void delete(String name) {
		if (funs.containsKey(name)) {
			funs.remove(name);
		}

	}

	public static void store() {
		FileOutputStream argsOutputStream = null;
		FileOutputStream expOutputStream = null;
		Properties name = new Properties();
		Properties exp = new Properties();
		try {
			argsOutputStream = new FileOutputStream("args.properties");
			expOutputStream = new FileOutputStream("exp.properties");

			Iterator<String> it = funs.keySet().iterator();
			while (it.hasNext()) {
				String string = (String) it.next();
				Fun fun = funs.get(string);
				exp.setProperty(fun.getFunName(), fun.getFunExp());
				name.setProperty(fun.getFunName(), fun.getArgs());
			}

			name.store(argsOutputStream, null);
			exp.store(expOutputStream, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (argsOutputStream != null) {
				try {
					argsOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (expOutputStream != null) {
				try {
					expOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void read() {
		FileInputStream argsInput = null;
		FileInputStream expInput = null;
		Properties args = new Properties();
		Properties exp = new Properties();
		try {
			argsInput = new FileInputStream("args.properties");
			expInput = new FileInputStream("exp.properties");
			args.load(argsInput);
			exp.load(expInput);
			Set<String> funsName = args.stringPropertyNames();
			for (String string : funsName) {
				add(new Fun(string, exp.getProperty(string), args.getProperty(string)));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (argsInput != null) {
				try {
					argsInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (expInput != null) {
				try {
					expInput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void showFunctions(JTextArea funArea) {
		funArea.setText("");
		if (!funs.isEmpty()) {
			Iterator<String> it = funs.keySet().iterator();
			while (it.hasNext()) {
				String string = (String) it.next();
				Fun fun = funs.get(string);
				funArea.append(fun.getExpression());
			}
		}
	}
}
