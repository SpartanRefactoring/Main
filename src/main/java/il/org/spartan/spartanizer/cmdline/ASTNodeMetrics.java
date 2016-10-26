package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** A Class that contains all the metrics for an {@link ASTNode}
 * @author Matteo Orru' */
@SuppressWarnings("unused") public class ASTNodeMetrics {
  private final ASTNode n;
  private static int length;
  private static int tokens;
  private static int nodes;
  private static int body;
  private static int statements;
  private static int tide;
  private static int essence;

  public ASTNodeMetrics(final ASTNode n) {
    super();
    this.n = n;
  }

  public void computeMetrics() {
    length = n.getLength();
    tokens = metrics.tokens(n + "");
    nodes = count.nodes(n);
    body = metrics.bodySize(n);
    statements = extract.statements(az.methodDeclaration(n).getBody()).size();
    tide = clean(n + "").length();
    essence = Essence.of(n + "").length();
  }

  /** @return the n */
  public ASTNode n() {
    return n;
  }

  /** @return the length */
  public static int length() {
    return length;
  }

  /** @return the tokens */
  public static int tokens() {
    return tokens;
  }

  /** @return the nodes */
  public static int nodes() {
    return nodes;
  }

  /** @return the body */
  public static int body() {
    return body;
  }

  /** @return the statements */
  public static int statements() {
    return statements;
  }

  /** @return the tide */
  public static int tide() {
    return tide;
  }

  /** @return the essence */
  public static int essence() {
    return essence;
  }
}
