package il.org.spartan.refactoring.ast;

import org.eclipse.jdt.core.dom.*;

/**
 * Use {@link il.org.spartan.refactoring.engine.Recurser Recurser} to measure things over an AST
 * @author Dor Ma'ayan
 * @since 2016-09-06
 */
public interface metrics {
  
  /**
   * @param n JD
   * @return The total number of charcters in the compressed printout
   */
  static int length(ASTNode n){
    return 0;
  }
  
  /**
   * @param n JD
   * @return The total number of idetifiers that where mentioned in the AST
   */
  static int identifiers(ASTNode n){
    return 0;
  }
  
  /**
   * @param n JD
   * @return The total number of nodes in the AST
   */
  static int nodes(ASTNode n){
    return 0;
  }
  
  /**
   * @param n JD
   * @return The total number of internal nodes in the AST
   */
  static int internals(ASTNode n){
    return 0;
  }
  
  /**
   * @param n JD
   * @return The total number of leaves in the AST
   */
  static int leaves(ASTNode n){
    return 0;
  }
  
  /**
   * @param n JD
   * @return The total number of distinct identifiers in the AST
   */
  static int flowerishness(ASTNode n){
    return 0;
  }
  
  /**
   * @param n JD
   * @return The total number of distinct kind of nodes in the AST
   */
  static int dexterity(ASTNode n){
    return 0;
  }
  
  
}
