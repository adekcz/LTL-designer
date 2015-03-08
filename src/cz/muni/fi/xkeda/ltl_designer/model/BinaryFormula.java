/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.model;

/**
 *
 * @author adekcz
 */
public class BinaryFormula extends Formula{
	BinaryConjuction conjuction;
	Formula leftSubformula;
	Formula rightSubformula;

	public BinaryFormula(BinaryConjuction conjuction, Formula leftSubformula, Formula rightSubformula) {
		this.conjuction = conjuction;
		this.leftSubformula = leftSubformula;
		this.rightSubformula = rightSubformula;
	}

	public BinaryConjuction getConjuction() {
		return conjuction;
	}

	public void setConjuction(BinaryConjuction conjuction) {
		this.conjuction = conjuction;
	}

	public Formula getLeftSubformula() {
		return leftSubformula;
	}

	public void setLeftSubformula(Formula leftSubformula) {
		this.leftSubformula = leftSubformula;
	}

	public Formula getRightSubformula() {
		return rightSubformula;
	}

	public void setRightSubformula(Formula rightSubformula) {
		this.rightSubformula = rightSubformula;
	}


	@Override
	public String toString() {
		return "(" + leftSubformula  + conjuction.toString() +rightSubformula +")";
	}

	
}
