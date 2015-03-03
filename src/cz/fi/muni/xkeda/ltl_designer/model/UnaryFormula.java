/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.xkeda.ltl_designer.model;

/**
 *
 * @author adekcz
 */
public class UnaryFormula extends Formula {

	private UnaryConjunction conjuction;
	private Formula subformula;

	//TODO potentionally do unary enum
	public UnaryFormula(UnaryConjunction conjuction, Formula subformula) {
		this.conjuction = conjuction;
		this.subformula = subformula;
	}

	public UnaryConjunction getConjuction() {
		return conjuction;
	}

	public void setConjuction(UnaryConjunction conjuction) {
		this.conjuction = conjuction;
	}

	public Formula getSubformula() {
		return subformula;
	}

	public void setSubformula(Formula subformula) {
		this.subformula = subformula;
	}

	@Override
	public String toString() {
		switch (conjuction) {
			case UNION:
			case REPEAT_INFINITELY:
			case CLOSURE:
				return "(" + subformula + conjuction.toString()+")";
			case NEGATION:
				return "(" + conjuction.toString() + subformula+")";
			default:
				return subformula.toString();
		}
	}

}
