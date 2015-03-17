/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.model;

/**
 *
 * @author adekcz
 */
public enum BinaryConjuction {
	 OR, AND, IMPLICATION, EQUIVALENCE,; 

	@Override
	public String toString() {
	 	switch(this){
			case OR: return "|";
			case AND: return "&";
			case IMPLICATION: return "=>";
			case EQUIVALENCE: return "<=>";
		    default: throw new IllegalArgumentException();
		}
	}

	 
}
