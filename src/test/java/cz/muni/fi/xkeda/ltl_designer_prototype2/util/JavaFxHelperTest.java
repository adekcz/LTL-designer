/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
/**
 *
 * @author adekcz
 */
public class JavaFxHelperTest {
	
	public JavaFxHelperTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}


	/**
	 * Test of countSubstringOccurencies method, of class JavaFxHelper.
	 */
	@org.junit.Test
	public void testCountSubstringOccurencies() {
		System.out.println("countSubstringOccurencies");
		String text = "asdf";
		String pattern = "";
		int expResult = 0;
		int result = JavaFxHelper.countSubstringOccurencies(text, pattern);
		assertEquals(expResult, result);
	}
	/**
	 * Test of countSubstringOccurencies method, of class JavaFxHelper.
	 */
	@org.junit.Test
	public void testCountSubstringOccurenciesSimple() {
		System.out.println("countSubstringOccurencies");
		String text = "aslfj XXX asdofij as XXX aosdfj XXX";
		String pattern = "XXX";
		int expResult = 3;
		int result = JavaFxHelper.countSubstringOccurencies(text, pattern);
		assertEquals(expResult, result);
	}
	/**
	 * Test of countSubstringOccurencies method, of class JavaFxHelper.
	 */
	@org.junit.Test
	public void testCountSubstringOccurenciesAmbiguous() {
		System.out.println("countSubstringOccurencies");
		String text = "asdfoij XXXX lasjdfoi XXX";
		String pattern = "XXX";
		int expResult = 2;
		int result = JavaFxHelper.countSubstringOccurencies(text, pattern);
		assertEquals(expResult, result);
	}
	
}
