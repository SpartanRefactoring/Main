package il.org.spartan.refactoring.ast;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;


import il.org.spartan.refactoring.engine.*;


/** Use {@link il.org.spartan.refactoring.engine.Recurser Recurser} to measure
 * things over an AST
 * @author Dor Ma'ayan
 * @since 2016-09-06 */
public interface metrics {
  /** @param n JD
   * @return The total number of charcters in the compressed printout */
  static int length(final ASTNode n) {
    return 0;
  }

  /** @param n JD
   * @return The total number of idetifiers that where mentioned in the AST */
  static int identifiers(final ASTNode n) {
    return 0;
  }

  
  /**
   * @param n JD
   * @return The total number of nodes in the AST
   */
  @SuppressWarnings("boxing")
  static int nodes(ASTNode n){
    if(n==null)
      return 0;
    final Recurser<Integer> recurse = new Recurser<>(n, 0);
    final Function<Recurser<Integer>, Integer> counter = (x) -> (1 + x.getCurrent());
    return recurse.preVisit(counter);
  }
  
  /**
   * @param n JD
   * @return The total number of internal nodes in the AST
   */
  @SuppressWarnings("boxing") static int internals(ASTNode n){
    if(n==null)
      return 0;
    final Recurser<Integer> recurse = new Recurser<>(n, 0);
    final Function<Recurser<Integer>, Integer> counter = (x) -> { //
      if(!Recurser.getChildren(x.getRoot()).isEmpty()) //
          return x.getCurrent()+1;
      return x.getCurrent();
    };
    return recurse.preVisit(counter);
  }
  
  /**
   * @param n JD
   * @return The total number of leaves in the AST
   */
  static int leaves(ASTNode n){
    return nodes(n)-internals(n);
  }

  /** @param n JD
   * @return The total number of distinct identifiers in the AST */
  static int flowerishness(final ASTNode n) {
    return 0;
  }

  /** @param n JD
   * @return The total number of distinct kind of nodes in the AST */
  static int dexterity(final ASTNode n) {
    return 0;
  }
}
