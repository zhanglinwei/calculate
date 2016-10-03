package cal;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Formatter.BigDecimalLayoutForm;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculate {

	LinkedList<String> numbers;
	LinkedList<String> operate;
	Stack<String> result = new Stack<>();
	Stack<String> ope = new Stack<>();

	/**
	 * 计算表达式的值
	 * 
	 * @param string
	 */
	public String calculate(String expression) {

		if (!expression.equals("") && expression != null) {
			// 1.扫描表达式，若有函数，替换对应表达式
			// replaceFun(expression);

			// 2.预处理表达式
			expression = formatExp(expression);
			// 3.计算表达式
			expression = count(expression);
			// 3.返回结果
			return expression;
		}

		return new String("表达式有误！");
	}

	private String count(String expression) {
		// 将运算符装入 operate
		fillOperater(expression);
		// 将数据装入 numbers
		fillNumber(expression);
		// 计算表达式的值
		expression = count();
		return expression;
	}

	private String count() {


		String temp = "0";
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
				temp = count(result.pop(), "0", ope.pop());
			} else {
				temp = count(result.pop(), result.pop(), ope.pop());
			}
			result.push(temp);
		}

		return result.pop();

	}

	private String count(String str2, String str1, String temp) {
		BigDecimal res = new BigDecimal("0");
		BigDecimal op1 = new BigDecimal(str1);
		BigDecimal op2 = new BigDecimal(str2);
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
		return res.toString();
	}

	private void fillNumber(String expression) {
		
		Pattern pattern = null;
		Matcher matcher = null;
		
		pattern = Pattern.compile("\\.");
		matcher = pattern.matcher(expression);
		while (matcher.find()) {
			expression = matcher.replaceAll("b");
		}
		pattern = Pattern.compile("\\W");
		matcher = pattern.matcher(expression);
		while (matcher.find()) {
			expression = matcher.replaceAll("a");
		}
		pattern = Pattern.compile("b");
		matcher = pattern.matcher(expression);
		while (matcher.find()) {
			expression = matcher.replaceAll("\\.");
		}
//		expression = expression.replaceAll("+", " ");
//		expression = expression.replaceAll("-", " ");
//		expression = expression.replaceAll("*", " ");
//		expression = expression.replaceAll("/", " ");
//		expression = expression.replaceAll("(", " ");
//		expression = expression.replaceAll(")", " ");
		String[] strings = expression.split("a");
		for (String string : strings) {
			if (!string.trim().equals("")) {
				numbers.add(string);
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

	public String formatExp(String expression) {

		Pattern pattern = null;
		Matcher matcher = null;
		StringBuffer sBuffer = null;

		sBuffer = new StringBuffer(expression);
		int k = 0;
		
		if (sBuffer.charAt(0) == '-') {
			pattern = Pattern.compile("\\-[0-9]+([.]{1}[0-9]+){0,1}");
			matcher = pattern.matcher(sBuffer);
			matcher.find();
			int i = matcher.start();
			int j = matcher.end() + 2;
			sBuffer = sBuffer.insert(i, "(0", 0, 2);
			sBuffer = sBuffer.insert(j, ")", 0, 1);
			matcher = pattern.matcher(sBuffer);
		}
		
		// +-
		pattern = Pattern.compile("\\+\\-[0-9]+([.]{1}[0-9]+){0,1}");
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
			
		}

		// --
		pattern = Pattern.compile("\\-\\-[0-9]+([.]{1}[0-9]+){0,1}");
		matcher = pattern.matcher(sBuffer);
		k = 0;
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

		// *-
		pattern = Pattern.compile("\\*\\-[0-9]+([.]{1}[0-9]+){0,1}");
		matcher = pattern.matcher(sBuffer);
		k = 0;
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

		// /-
		pattern = Pattern.compile("\\/\\-[0-9]+([.]{1}[0-9]+){0,1}");
		matcher = pattern.matcher(sBuffer);
		k = 0;
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
		// (-
		pattern = Pattern.compile("\\(\\-[0-9]+([.]{1}[0-9]+){0,1}");
		matcher = pattern.matcher(sBuffer);
		k = 0;
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

		return sBuffer.toString();
	}

	private void replaceFun(String expression) {
		// TODO Auto-generated method stub

	}

	public Calculate() {
		numbers = new LinkedList<>();
		operate = new LinkedList<>();
	}
	

}
