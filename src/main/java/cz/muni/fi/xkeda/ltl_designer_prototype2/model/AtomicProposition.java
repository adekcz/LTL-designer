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
public class AtomicProposition extends Formula {
	private final String text;

	public AtomicProposition(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
	
}
