package cal;

import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculate {

	LinkedList<Float> numbers;
	LinkedList<String> operate;
	Stack<Float> result = new Stack<>();
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
			// 2. 将所有负数替换为0-x
			replaceNegative(expression);
			// 2.计算表达式
			expression = count(expression);
			// 3.返回结果
			return expression;
		}

		return new String("表达式有误！");
	}

	private void replaceNegative(String expression) {

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

		String sTemp = null;
		Float nTemp = 0f;

		int i = 0;

		while (!operate.isEmpty()) {
			sTemp = operate.peek();
			nTemp = numbers.peek();
			if (sTemp.equals("(") && i == 0) {
				ope.push(operate.pop());
				sTemp = operate.peek();
				result.push(numbers.pop());
				nTemp = numbers.peek();
			}

			if (sTemp.equals("(") || ope.isEmpty()) {
				ope.push(operate.pop());
				result.push(numbers.pop());
			} else if (sTemp.equals(")")) {
				while (!ope.peek().equals("(")) {
					nTemp = count(result.pop(), result.pop(), ope.pop());
					result.push(nTemp);
				}
				// nTemp = count(result.pop(), result.pop(), ope.pop());
				// result.push(nTemp);
				ope.pop();
				operate.pop();
			} else if (sTemp.equals("*")) {
				if (ope.peek().equals("/") || ope.peek().equals("*")) {
					nTemp = count(result.pop(), result.pop(), ope.pop());
					result.push(nTemp);
				} else {
					ope.push(operate.pop());
					result.push(numbers.pop());
				}
			} else if (sTemp.equals("/")) {
				if (ope.peek().equals("/") || ope.peek().equals("*")) {
					nTemp = count(result.pop(), result.pop(), ope.pop());
					result.push(nTemp);
				} else {
					ope.push(operate.pop());
					result.push(numbers.pop());
				}
			} else if (sTemp.equals("+")) {
				if (ope.peek().equals("(")) {
					ope.push(operate.pop());
					result.push(numbers.pop());
				} else {
					nTemp = count(result.pop(), result.pop(), ope.pop());
					result.push(nTemp);
				}
			} else if (sTemp.equals("-")) {
				if (ope.peek().equals("(")) {
					ope.push(operate.pop());
					result.push(numbers.pop());
				} else {
					nTemp = count(result.pop(), result.pop(), ope.pop());
					result.push(nTemp);
				}
			} else if (sTemp.equals("#")) {
				operate.clear();
				nTemp = count(result.pop(), result.pop(), ope.pop());	
				result.push(nTemp);
			}
		}
		
		return Float.toString(result.pop());
	}

	private Float count(Float op2, Float op1, String temp) {
		Float float1 = 0f;
		switch (temp) {
		case "+":
			float1 = op1 + op2;
			break;
		case "-":
			float1 = op1 - op2;
			break;
		case "*":
			float1 = op1 * op2;
			break;
		case "/":
			float1 = op1 / op2;
			break;
		default:
			break;
		}
		return float1;
	}

	private void fillNumber(String expression) {
		expression = expression.replace("+", " ");
		expression = expression.replace("-", " ");
		expression = expression.replace("*", " ");
		expression = expression.replace("/", " ");
		expression = expression.replace("(", " ");
		expression = expression.replace(")", " ");
		String[] strings = expression.split(" ");
		Float f = null;
		for (String string : strings) {
			if (!string.trim().equals("")) {
				f = Float.valueOf(string);
				numbers.add(f);
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

	// private void replaceFun(String expression) {
	// // TODO Auto-generated method stub
	//
	// }

	public Calculate() {
		numbers = new LinkedList<>();
		operate = new LinkedList<>();
	}

}
