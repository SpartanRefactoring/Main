package il.org.spartan.spartanizer.engine;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A function object representing a sequence of operations on an
 * {@link ASTRewrite} object.
 * @author Yossi Gil
 * @since 2015-08-28 */
public abstract class Tip extends Range {
  /** A factory function that converts a sequence of ASTNodes into a
   * {@link Range}
   * @param n arbitrary
   * @param ns */
  static Range range(final ASTNode n, final ASTNode... ns) {
    return range(singleNodeRange(n), ns);
  }

  static Range range(final Range r, final ASTNode... ns) {
    Range $ = r;
    for (final ASTNode ¢ : ns)
      $ = $.merge(singleNodeRange(¢));
    return $;
  }

  static Range singleNodeRange(final ASTNode n) {
    final int from = n.getStartPosition();
    return new Range(from, from + n.getLength());
  }

  /** A textual description of the action to be performed **/
  public final String description;
  /** The line number of the first character to be rewritten **/
  public int lineNumber = -1;
  /** The tipper class that supplied that tip */
  @SuppressWarnings("rawtypes") public final Class<? extends Tipper> tipperClass;

  /** Instantiates this class
   * @param description a textual description of the changes described by this
   *        instance
   * @param n the node on which change is to be carried out
   * @param ns additional nodes, defining the scope of this action. */
  public Tip(final String description, final ASTNode n, @SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass,
      final ASTNode... ns) {
    this(description, range(n, ns), tipperClass);
    lineNumber = searchAncestors.forClass(CompilationUnit.class).from(n).getLineNumber(from);
  }

  Tip(final String description, final Range other, @SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
    super(other);
    this.description = description;
    this.tipperClass = tipperClass;
  }

  /** Convert the rewrite into changes on an {@link ASTRewrite}
   * @param r where to place the changes
   * @param g to be associated with these changes @ */
  public abstract void go(ASTRewrite r, TextEditGroup g);
}
