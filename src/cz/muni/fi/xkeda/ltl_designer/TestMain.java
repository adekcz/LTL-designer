/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer;

import cz.muni.fi.xkeda.ltl_designer.model.AtomicProposition;
import cz.muni.fi.xkeda.ltl_designer.model.BinaryConjuction;
import cz.muni.fi.xkeda.ltl_designer.model.BinaryFormula;
import cz.muni.fi.xkeda.ltl_designer.model.ChainedFormula;
import cz.muni.fi.xkeda.ltl_designer.model.Formula;
import cz.muni.fi.xkeda.ltl_designer.model.UnaryConjunction;
import cz.muni.fi.xkeda.ltl_designer.model.UnaryFormula;

/**
 *
 * @author adekcz
 */
public class TestMain {

	public static void main(String[] args){
		AtomicProposition holka = new AtomicProposition("h");
		AtomicProposition fialova = new AtomicProposition("f");
		AtomicProposition green = new AtomicProposition("g");
		AtomicProposition truth = new AtomicProposition("T");
		
		ChainedFormula chain1 = new ChainedFormula();
		ChainedFormula chain2 = new ChainedFormula();
		ChainedFormula chain3 = new ChainedFormula();
		chain1.addFormula(new UnaryFormula(UnaryConjunction.CLOSURE, fialova)).addFormula(new UnaryFormula(UnaryConjunction.REPEAT_INFINITELY, green));
		chain2.addFormula(chain1).addFormula(new UnaryFormula(UnaryConjunction.REPEAT_INFINITELY, truth));
		chain3.addFormula(new UnaryFormula(UnaryConjunction.CLOSURE, truth)).addFormula(chain2);
		BinaryFormula binaryFormula = new BinaryFormula(BinaryConjuction.IMPLICATION, holka, chain3);
		UnaryFormula unaryFormula = new UnaryFormula(UnaryConjunction.REPEAT_INFINITELY, binaryFormula);


		System.out.println(unaryFormula);


	}
	
}
