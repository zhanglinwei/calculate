package test;

import org.junit.Test;

import cal.Calculate;

public class TestCalculate {

	Calculate calculate = new Calculate();
	
	@Test
	public void testReplaceNegative() {
		String string = "1+-2-2*(-3*4)";
		calculate.replaceNegative(string);
	}

}
