package test;

import java.math.BigDecimal;

import org.junit.Test;

import cal.Calculate;

public class TestCalculate {

	Calculate cal = new Calculate();

	@Test
	public void testFormatExp() {
		String exp = "1--1--2--3*-4*-5*-6";
		exp = "1+2-3*-4/-4+-4--4";
		exp = cal.formatExp(exp);
		System.out.println(exp);
		
		BigDecimal op1 = new BigDecimal("21.6");
		BigDecimal op2 = new BigDecimal("0.18");
		
	}

}
