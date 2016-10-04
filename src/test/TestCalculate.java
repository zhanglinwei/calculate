package test;

import static org.junit.Assert.*;

import org.junit.Test;

import Exception.EmptyInputException;
import cal.Calculate;

public class TestCalculate {
	
	Calculate calculate = new Calculate();

	@Test
	public void testCalculate() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalculateJTextFieldString() {
		fail("Not yet implemented");
	}

	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

	@Test
	public void testFormatExp() throws EmptyInputException {
		String string = "1+2*3@";
		calculate.formatExp(string);
		
	}

	@Test
	public void testCalculate1() {
		fail("Not yet implemented");
	}

}
