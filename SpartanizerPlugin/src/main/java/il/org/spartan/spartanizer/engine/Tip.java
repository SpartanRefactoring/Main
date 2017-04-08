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
public abstract class Tip {
  /** What text range to highlight in the marker of this instance */
  public final Range highlight;
  /** Which text range would the application of this instance touche */
  public Range span;

  private static Range range(final Range r, final ASTNode... ns) {
    Range $ = r;
    for (final ASTNode ¢ : ns)
      $ = $.merge(range(¢));
    return $;
  }

  static Range range(final ASTNode ¢) {
    final int $ = ¢.getStartPosition();
    return new Range($, $ + ¢.getLength());
  }

  public <N1 extends ASTNode, N2 extends ASTNode> Tip(final String description, //
      final Class<? extends Tipper<N1>> tipperClass, //
      final N2 highlight) {
    this.description = description;
    this.tipperClass = tipperClass;
    span = range(this.highlight = range(highlight)); // Ensure two distinct
    final CompilationUnit compilationUnit = yieldAncestors.untilClass(CompilationUnit.class).from(highlight);
    lineNumber = compilationUnit == null ? -1 : compilationUnit.getLineNumber(highlight.getStartPosition());
  }

  /** Instantiates this class
   * @param description a textual description of the changes described by this
   *        instance
   * @param spartanizationRange the node on which change is to be carried out
   * @param highlight the node on which change is to be marked
   * @param ns additional nodes, defining the scope of this action. */
  public <N1 extends ASTNode, N2 extends ASTNode> Tip(final String description, //
      final Class<Tipper<N1>> tipperClass, //
      final N2 highlight, final ASTNode... ns) {
    this(description, tipperClass, highlight);
    extend(ns);
  }

  /** Convert the rewrite into changes on an {@link ASTRewrite}
   * @param r where to place the changes
   * @param g to be associated with these changes @ */
  public abstract void go(ASTRewrite r, TextEditGroup g);

  public Tip extend(final ASTNode... ¢) {
    span = range(span, ¢);
    return this;
  }

  public int getSpartanizationCharStart() {
    return span.from;
  }

  public int getSpartanizationCharEnd() {
    return span.to;
  }

  /** A textual description of the action to be performed **/
  public final String description;
  /** The line number of the first character to be rewritten **/
  public int lineNumber;
  @SuppressWarnings("rawtypes")
  /** The tipper class that supplied that tip */
  public final Class<? extends Tipper> tipperClass;
}
