/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.model;

/**
 * <table>
 * <tr><th>Logic</th></tr>
 * <tr><th>~</th><td>negation</td></tr>
 * <tr><th>|</th><td>or</td></tr>
 * <tr><th>&</th><td>and</td></tr>
 * <tr><th>=></th><td>implication</td></tr>
 * <tr><th><=></th><td>equivalence</td></tr>
 * <tr><th>LTL:</th></tr>
 * <tr><th>!</th><td>repeat infinite times</td></tr>
 * <tr><th>As in regular expressions:</th></tr>
 * <tr><th>+</th><td>union</td></tr>
 * <tr><th>*</th><td>closure</td></tr>
 * <tr><th>As in graphical representation</th></tr>
 * <tr><th>T</th><td>true ≡ all states</td></tr>
 * <tr><th>F</th><td>false ≡ no state</td></tr>
 * </table>
 * 
 * Examples of textual representation
 * <table>
 * <tr><th>LTL</th><th>textual representation</th></tr>
 * <tr><th>Xf</th><td>TfT!</td></tr>
 * <tr><th>Ff</th><td>T*fT!</td></tr>
 * <tr><th>Gf</th><td>f!</td></tr>
 * <tr><th>fUg</th><td>f*gT!</td></tr>
 * <tr><th>fRg</th><td>((~f&g)! + (~f&g)*(f&g)T!)</td></tr>
 * <tr><th>G(h->F(fUg))</th><td>(h=>(T*(f*gT!)T!))!</td></tr>
 * </table>
 * 
 * Path patterns (CTL unnecessary)
 * <table>
 * <tr><th>p</th><td>TfT!</td></tr>
 * <tr><th>[p]</th><td>T*fT!</td></tr>
 * <tr><th>&lt;p&gt;</th><td>T*fT!</td></tr>
 * </table>
 * @author adekcz
 */
public abstract class Formula
{



  @Override
  public abstract String toString();

  
  // /**
  // * appends this to formula that is given
  // * @param formula
  // * @return 
  // */
  // public abstract Formula appendTo(Formula formula);
  
}

