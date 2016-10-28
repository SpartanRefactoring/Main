package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** A Class that contains all the metrics for an {@link ASTNode}
 * @author Matteo Orru' */
@SuppressWarnings("unused") public class ASTNodeMetrics {
  private final ASTNode n;
  private int length;
  private int tokens;
  private int nodes;
  private int body;
  private int statements;
  private int tide;
  private int essence;

  public ASTNodeMetrics(final ASTNode n) {
    this.n = n;
  }

  public void computeMetrics() {
    length = n.getLength();
    tokens = metrics.tokens(n + "");
    nodes = count.nodes(n);
    body = metrics.bodySize(n);
    final MethodDeclaration methodDeclaration = az.methodDeclaration(n);
    statements = methodDeclaration == null ? -1 : extract.statements(methodDeclaration.getBody()).size();
    // extract.statements(az.
    // methodDeclaration(n)
    // .getBody())
    // .size();
    tide = clean(n + "").length();
    essence = Essence.of(n + "").length();
  }

  /** @return the n */
  public ASTNode n() {
    return n;
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