/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adekcz
 */
public class ChainedFormula extends Formula{
	
	protected List<Formula> chain;


	/**
	 * 
	 * @param nextFormula 
	 * @return this formula, so you can chain add calls
	 */
	public  ChainedFormula addFormula(Formula nextFormula){
		chain.add(nextFormula);
		return this;
	}

	public List<Formula> getChain() {
		return chain;
	}

	public ChainedFormula() {
		chain = new ArrayList<>();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		chain.stream().forEach((formula) -> {
			sb.append(formula.toString());
		});
		sb.append(")");
		return sb.toString();
	}
}
