package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** A Class that contains all the metrics for an {@link ASTNode}
 * @author Matteo Orru' */
public class ASTNodeMetrics {
  private final ASTNode node;
  private int length;
  private int tokens;
  private int nodes;
  private int body;
  private int statements;
  private int tide;
  private int essence;

  public ASTNodeMetrics(final ASTNode n) {
    this.node = n;
  }

  public void computeMetrics() {
    length = node.getLength();
    tokens = metrics.tokens(node + "");
    nodes = count.nodes(node);
    body = metrics.bodySize(node);
    final MethodDeclaration methodDeclaration = az.methodDeclaration(node);
    statements = methodDeclaration == null ? -1 : extract.statements(methodDeclaration.getBody()).size();
    // extract.statements(az.
    // methodDeclaration(node)
    // .getBody())
    // .size();
    tide = clean(node + "").length();
    essence = Essence.of(node + "").length();
  }

  /** @return the node */
  public ASTNode n() {
    return node;
  }

  /** @return the length */
  public int length() {
    return length;
  }

  /** @return the tokens */
  public int tokens() {
    return tokens;
  }

  /** @return the nodes */
  public int nodes() {
    return nodes;
  }

  /** @return the body */
  public int body() {
    return body;
  }

  /** @return the statements */
  public int statements() {
    return statements;
  }

  /** @return the tide */
  public int tide() {
    return tide;
  }

  /** @return the essence */
  public int essence() {
    return essence;
  }
}