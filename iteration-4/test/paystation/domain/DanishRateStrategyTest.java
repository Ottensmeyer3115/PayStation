package paystation.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DanishRateStrategyTest {
	
	private RateStrategy rs;

	@Before
	public void setUp() throws Exception {
		rs = new DanishRateStrategy();
	}
	
	/* Test 7 minutes parking */
	@Test
	public void shouldGive7MinFor1Krone() {
		// First 7 minutes
	    assertEquals( 7 /*minutes*/, rs.calculateTime(1) ); 
	}
	
	/* Test 14 minutes parking */
	@Test
	public void shouldGive14MinFor2Krone() {
		// First 14 minutes
		assertEquals( 14 /*minutes*/, rs.calculateTime(2) );
	}
	
	/* Test 35 minutes parking */
	@Test
	public void shouldGive35MinFor5Krone() {
		// First 35 minutes
		assertEquals( 35 /*minutes*/, rs.calculateTime(5) );
	}
	
	/* Test 70 minutes parking */
	@Test
	public void shouldGive70MinFor10Krone() {
		// First 70 minutes
		assertEquals( 70 /*minutes*/, rs.calculateTime(10) );
	}
	
	/* Test 140 minutes parking */
	@Test
	public void shouldGive140MinFor20Krone() {
		// First 140 minutes
		assertEquals( 140 /*minutes*/, rs.calculateTime(20) );
	}

}
