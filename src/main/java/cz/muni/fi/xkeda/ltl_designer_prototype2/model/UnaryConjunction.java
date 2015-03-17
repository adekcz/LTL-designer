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
public enum UnaryConjunction {
	NEGATION, UNION, CLOSURE, REPEAT_INFINITELY;

	@Override
	public String toString() {
		switch(this){
			case NEGATION: return "~";
			case UNION: return "+";
			case CLOSURE: return "*";
			case REPEAT_INFINITELY: return "!";
		    default: throw new IllegalArgumentException();
		}
	}
}
