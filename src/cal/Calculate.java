package cal;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import Exception.ErrorInputException;

public class Calculate implements Runnable {

	private String expression;
	private JTextField jTextField;

	private LinkedList<BigDecimal> numbers;
	private LinkedList<String> operate;
	private Stack<BigDecimal> result = new Stack<>();
	private Stack<String> ope = new Stack<>();

	public Calculate() {
		numbers = new LinkedList<>();
		operate = new LinkedList<>();
	}

	public Calculate(JTextField jTextField, String expression) {
		this();
		this.jTextField = jTextField;
		this.expression = expression;
	}

	@Override
	public void run() {
		// 处理表达式
		try {
			formatExp(expression);
		} catch (ErrorInputException e) {
			Thread.interrupted();
		}

		// 计算表达式的值
		BigDecimal result = calculate(expression);

		// 将结果显示在jTextFiled中
		jTextField.setText(result.toString());
	}

	/*
	 * 处理表达式，使之可以用于计算
	 */
	public void formatExp(String expression) throws ErrorInputException {

		// 1.检查是否还有非法输入。
		// 若有，则 thorw ErrorInputException
		// 非法输入：null、空字符串、空格、非法字符（除 ( ) A-Z a-z 0-9 . + - * / 外的字
		// 符为非法字符）
		checkErrorInput(expression);

		// 2.判断表达式是否有错
		// 含有连个连续符号,且不为(_、_-、_+
		// 小数，多个小数点

		// 3.替换函数
		replaceFun(expression);

		// 4.处理负数
		dealNegive(expression);

	}

	/*
	 * 将表达式中的负数变为运算表达式 例如：-2 -> (0-2)
	 */
	private void dealNegive(String expression) {
		Pattern pattern = null;
		StringBuffer sBuffer = new StringBuffer(expression);
		// +-
		pattern = Pattern.compile("\\+\\-[0-9]+([.]{1}[0-9]+){0,1}");
		sBuffer = addBrackets(pattern, sBuffer);
		// --
		pattern = Pattern.compile("\\-\\-[0-9]+([.]{1}[0-9]+){0,1}");
		sBuffer = addBrackets(pattern, sBuffer);
		// *-
		pattern = Pattern.compile("\\*\\-[0-9]+([.]{1}[0-9]+){0,1}");
		sBuffer = addBrackets(pattern, sBuffer);
		// /-
		pattern = Pattern.compile("\\/\\-[0-9]+([.]{1}[0-9]+){0,1}");
		sBuffer = addBrackets(pattern, sBuffer);
		// (-
		pattern = Pattern.compile("\\(\\-[0-9]+([.]{1}[0-9]+){0,1}");
		sBuffer = addBrackets(pattern, sBuffer);
	}

	private void checkErrorInput(String expression) throws ErrorInputException {
		// 非法输入：null、空字符串、空格、非法字符
		// 除 ( ) A-Z a-z 0-9 . + - * / 外的字符为非法字符）
		if (expression == null) {
			throw new ErrorInputException("输入为NULL");
		}
		if (expression.equals("")) {
			throw new ErrorInputException("输入为空");
		}
		if (expression.equals(" ")) {
			throw new ErrorInputException("输入为空格");
		}
	}

	private StringBuffer addBrackets(Pattern pattern, StringBuffer sBuffer) {
		Matcher matcher;
		int k = 0;
		matcher = pattern.matcher(sBuffer);
		while (matcher.find(k)) {
			int i = matcher.start() + 1;
			int j = matcher.end() + 2;
			k = j + 1;
			sBuffer = sBuffer.insert(i, "(0", 0, 2);
			sBuffer = sBuffer.insert(j, ")", 0, 1);
			if (sBuffer.charAt(i - 1) >= '0' && sBuffer.charAt(i - 1) <= '9') {
				sBuffer = sBuffer.insert(i, "-", 0, 1);
			}
			matcher = pattern.matcher(sBuffer);
		}
		return sBuffer;
	}

	private void replaceFun(String expression) {
		// TODO Auto-generated method stub

	}

	/*
	 * 计算表达式的值
	 */
	public BigDecimal calculate(String expression) {
		// 1. 将运算符装入 operate
		fillOperater(expression);
		// 2. 将数据装入 numbers
		fillNumber(expression);
		// 3. 计算表达式的值
		BigDecimal result = countAll();
		return result;
	}

	private BigDecimal countAll() {

		BigDecimal temp = new BigDecimal(0);
		result.push(numbers.pop());

		while (!operate.peek().equals("#")) {
			if (ope.isEmpty()) {
				while (operate.peek().equals("(")) {
					ope.push(operate.pop());
				}
				ope.push(operate.pop());
				if (!numbers.isEmpty()) {
					result.push(numbers.pop());
				}
			} else if (operate.peek().equals("(")) {
				while (operate.peek().equals("(")) {
					ope.push(operate.pop());
				}
				ope.push(operate.pop());
				if (!numbers.isEmpty()) {
					result.push(numbers.pop());
				}
			} else if (operate.peek().equals("*") || operate.peek().equals("/")) {
				if (ope.peek().equals("*") || ope.peek().equals("/")) {
					temp = count(result.pop(), result.pop(), ope.pop());
					result.push(temp);
				} else {
					ope.push(operate.pop());
					if (!numbers.isEmpty()) {
						result.push(numbers.pop());
					}
				}
			} else if (operate.peek().equals("+") || operate.peek().equals("-")) {
				if (!ope.peek().equals("(")) {
					temp = count(result.pop(), result.pop(), ope.pop());
					result.push(temp);
				} else {
					ope.push(operate.pop());
					if (!numbers.isEmpty()) {
						result.push(numbers.pop());
					}
				}
			} else if (operate.peek().equals(")")) {
				while (!ope.peek().equals("(")) {

					temp = count(result.pop(), result.pop(), ope.pop());
					result.push(temp);
				}
				ope.pop();
				operate.pop();
			}
		}

		while (!ope.isEmpty()) {
			if (result.size() == 1) {
				temp = count(result.pop(), new BigDecimal(0), ope.pop());
			} else {
				temp = count(result.pop(), result.pop(), ope.pop());
			}
			result.push(temp);
		}

		return result.pop();

	}

	private BigDecimal count(BigDecimal op2, BigDecimal op1, String temp) {
		BigDecimal res = new BigDecimal(0);
		switch (temp) {
		case "+":
			res = op1.add(op2);
			break;
		case "-":
			res = op1.subtract(op2);
			break;
		case "*":
			res = op1.multiply(op2);
			break;
		case "/":
			res = op1.divide(op2);
			break;
		default:
			break;
		}
		return res;
	}

	private void fillNumber(String expression) {
		expression = expression.replaceAll("\\+", " ");
		expression = expression.replaceAll("\\-", " ");
		expression = expression.replaceAll("\\*", " ");
		expression = expression.replaceAll("\\/", " ");
		expression = expression.replaceAll("\\(", " ");
		expression = expression.replaceAll("\\)", " ");
		String[] strings = expression.split(" ");
		for (String string : strings) {
			if (!string.trim().equals("")) {
				numbers.add(new BigDecimal(string));
			}
		}
	}

	private void fillOperater(String expression) {
		Pattern p = Pattern.compile("\\D");
		Matcher m = p.matcher(expression);
		while (m.find()) {
			if (m.group().equals(".")) {
				continue;
			}
			operate.add(m.group());
		}
		operate.add("#");
	}

}
