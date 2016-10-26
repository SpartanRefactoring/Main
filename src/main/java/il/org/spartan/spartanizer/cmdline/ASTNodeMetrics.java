package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/**
 * A Class that contains all the metrics for an {@link ASTNode}
 * @author Matteo Orru'
 */

@SuppressWarnings("unused")
public class ASTNodeMetrics {
  
  private ASTNode n;

  private int length;
  private int tokens;
  private int nodes;
  private int body;
  private int statements;
  private int tide;
  private int essence;
    
  public ASTNodeMetrics(ASTNode n) {
    super();
    this.n = n;
  }  
  
  public void computeMetrics(){
    this.length = n.getLength();
    this.tokens = metrics.tokens(n + "");
    this.nodes = count.nodes(n);
    this.body = metrics.bodySize(n);
    this.statements = extract.statements(az.methodDeclaration(n).getBody()).size();
    this.tide = clean(n + "").length();
    this.essence = Essence.of(n + "").length();
  }

  /**
   * @return the n
   */
  public ASTNode getN() {
    return n;
  }

  /**
   * @return the length
   */
  public int getLength() {
    return length;
  }

  /**
   * @return the tokens
   */
  public int getTokens() {
    return tokens;
  }

  /**
   * @return the nodes
   */
  public int getNodes() {
    return nodes;
  }

  /**
   * @return the body
   */
  public int getBody() {
    return body;
  }

  /**
   * @return the statements
   */
  public int getStatements() {
    return statements;
  }

  /**
   * @return the tide
   */
  public int getTide() {
    return tide;
  }

  /**
   * @return the essence
   */
  public int getEssence() {
    return essence;
  }
  
}
