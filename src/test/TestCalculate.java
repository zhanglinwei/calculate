package test;

import java.math.BigDecimal;

import javax.print.DocFlavor.STRING;

import org.junit.Test;
import org.junit.validator.PublicClassValidator;

import cal.Calculate;

public class TestCalculate {

	Calculate cal = new Calculate();

	@Test
	public void testFormatExp() {
		String exp = "1.6--1--2--3*-4*-5*-6";
		exp = cal.formatExp(exp);
		
		
		
		exp = exp.replaceAll("\\+", " ");
		exp = exp.replaceAll("\\-", " ");
		exp = exp.replaceAll("\\*", " ");
		exp = exp.replaceAll("\\/", " ");
		exp = exp.replaceAll("\\(", " ");
		exp = exp.replaceAll("\\)", " ");
		
		String[] strings = exp.split(" ");
		
		System.out.println(exp);

		for (int i = 0; i < strings.length; i++) {
			System.out.println(strings[i]);
		}
		
		
		BigDecimal op1 = new BigDecimal("21.6");
		BigDecimal op2 = new BigDecimal("0.18");
		
	}
	
	
}
