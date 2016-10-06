package cal;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import Exception.EmptyInputException;
import Exception.NoSuchFunctionException;
import util.PropertiesUtil;

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
		try {
			// 处理表达式
			expression = formatExp(expression);
			// 计算表达式的值
			BigDecimal result = calculate(expression);
			float f = result.floatValue();
			String string = String.valueOf(f);
			// 将结果显示在jTextFiled中
			jTextField.setText(string);
		} catch (NoSuchFunctionException e) {
			jTextField.setText(e.getMessage());
		} catch (EmptyInputException e) {
			jTextField.setText(e.getMessage());
		} catch (Exception e) {
			jTextField.setText("请检查表达式！表达式中可能含有特殊字符！");
		}

	}

	// 处理表达式，使之可以用于计算,只做简单的错误处理。
	// 保证将字符串转换为可计算的格式，若程序出错，则证明字符串有错误输入。
	// 捕获并抛出，交给用户去检查字符串的正误
	public String formatExp(String expression) throws EmptyInputException, NoSuchFunctionException {
		// 1.字符串预处理（保证不为空串或包含回车换行）
		// 若有，则 thorw ErrorInputException
		checkExp(expression);
		// 2.替换函数
		expression = replaceFun(expression);
		// 4.处理负数
		expression = dealNegive(expression);
		return expression;
	}

	// 将表达式中的负数变为运算表达式 例如：-2 -> (0-2)
	private String dealNegive(String expression) {
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

		return sBuffer.toString();
	}

	// 保证字符串不为NULL或空串，或包含回车换行
	private void checkExp(String expression) throws EmptyInputException {
		// 非法输入：null、空字符串、空格、非法字符
		// 除 ( ) A-Z a-z 0-9 . + - * / 外的字符为非法字符）
		if (expression == null) {
			throw new EmptyInputException("输入为NULL");
		}
		if (expression.equals("")) {
			throw new EmptyInputException("输入为空");
		}
		if (expression.equals(" ")) {
			throw new EmptyInputException("输入为空格");
		}
		if (expression.contains("\r\n") || expression.contains("\n")) {
			throw new EmptyInputException("表达式中不能保护回车、换行等特殊字符！");
		}

	}

	// 加括号
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

	private String replaceFun(String expression) throws NoSuchFunctionException {
		Fun fun = null;

		Pattern pattern = Pattern.compile("[a-zA-Z]+\\(.+\\)");
		Matcher matcher = pattern.matcher(expression);
		while (matcher.find()) {
			String temp = matcher.group();
			String funName = parseFunName(temp);
			String[] args = parseArgs(matcher.group());
			fun = getFun(funName);
			String funExp = "(";
			funExp += fun.transform(args);
			funExp += ")";
			temp = funName + "\\(.+\\)";
			expression = expression.replaceAll(temp, funExp);
			matcher = pattern.matcher(expression);
		}
		return expression;

	}

	private String[] parseArgs(String string) {
		int i = string.indexOf("(");
		string = string.substring(i + 1, string.length() - 1);
		String[] args = string.split(",");
		return args;
	}

	private String parseFunName(String string) {
		int i = string.indexOf("(");
		string = string.substring(0, i);
		return string;
	}

	private Fun getFun(String funName) throws NoSuchFunctionException {
		Map<String, Fun> funs = PropertiesUtil.funs;
		if (funs.containsKey(funName)) {
			return funs.get(funName);
		}
		throw new NoSuchFunctionException("此函数未定义！");
	}

	// 计算表达式的值
	public BigDecimal calculate(String expression) {
		// 1. 将运算符装入 operate
		fillOperater(expression);
		// 2. 将数据装入 numbers
		fillNumber(expression);
		// 3. 计算表达式的值
		BigDecimal result = countAll();
		return result;
	}

	// 将操作符与操作数队列全部计算完毕
	private BigDecimal countAll() {

		// 1. 先入栈一个操作数
		pushNumber();

		// 2. 比较栈顶、队列头操作符的优先级
		// 如果队列头高于栈顶，操作符入栈，操作数入栈
		// 否则，计算站内元素
		while (!operate.isEmpty()) {
			if (operate.peek().equals(")")) {
				while (!ope.peek().equals("(")) {
					BigDecimal temp = count(result.pop(), result.pop(), ope.pop());
					result.push(temp);
				}
				ope.pop();
				operate.pop();
			} else if (isOutHigh()) {
				pushNumber();
				pushOpe();
			} else {
				count();
			}
		}

		while (!ope.isEmpty()) {
			count();
		}

		return result.pop();

	}

	private void count() {
		BigDecimal temp = null;

		if (result.size() == 1) {
			temp = count(popNumber(), new BigDecimal(0), ope.pop());
		} else {
			temp = count(popNumber(), popNumber(), ope.pop());
		}

		if (temp != null) {
			result.push(temp);
		}
	}

	private boolean isOutHigh() {
		if (ope.isEmpty() || ope.peek().equals("(") || operate.peek().equals("(")) {
			return true;
		} else if (ope.peek().equals("*") || ope.peek().equals("/")) {
			if (operate.peek().equals("(")) {
				return true;
			}
		} else if (ope.peek().equals("+") || ope.peek().equals("-")) {
			if (operate.peek().equals("*") || operate.peek().equals("/")) {
				return true;
			}
		}
		return false;
	}

	// 操作符入栈
	private void pushOpe() {
		if (ope.isEmpty() || operate.peek().equals("(")) {
			while (operate.peek().equals("(")) {
				ope.push(operate.pop());
			}
		}
		ope.push(operate.pop());
	}

	// 操作数入栈
	private void pushNumber() {
		if (!numbers.isEmpty()) {
			result.push(numbers.pop());
		}
	}

	// 取出栈顶数字
	private BigDecimal popNumber() {
		return result.pop();
	}

	// 根据符号来计算结果
	private BigDecimal count(BigDecimal op2, BigDecimal op1, String symbol) {
		BigDecimal res = null;
		switch (symbol) {
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
		// operate.add("#");
	}

}
