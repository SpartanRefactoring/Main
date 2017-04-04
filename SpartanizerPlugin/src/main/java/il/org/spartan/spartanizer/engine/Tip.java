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

  static Range singleNodeRange(final ASTNode ¢) {
    final int $ = ¢.getStartPosition();
    return new Range($, $ + ¢.getLength());
  }

  public <N1 extends ASTNode, N2 extends ASTNode> Tip(final String description, //
      final Class<? extends Tipper<N1>> class1, //
      final N2 hightlight) {
    super(range(hightlight));
    this.description = description;
    tipperClass = class1;
    lineNumber = yieldAncestors.untilClass(CompilationUnit.class).from(hightlight).getLineNumber(from);
    spartanizationCharStart = hightlight.getStartPosition();
    spartanizationCharEnd = spartanizationCharStart + hightlight.getLength();
  }

  /** Instantiates this class
   * @param description a textual description of the changes described by this
   *        instance
   * @param spartanizationRange the node on which change is to be carried out
   * @param highlight the node on which change is to be marked
   * @param ns additional nodes, defining the scope of this action. */
  public <N1 extends ASTNode, N2 extends ASTNode> Tip(final String description, //
      final Class<Tipper<N1>> c, //
      final N2 highlight, final ASTNode... ns) {
    this(description, c, highlight);
    extend(ns);
  }

  /** Convert the rewrite into changes on an {@link ASTRewrite}
   * @param r where to place the changes
   * @param g to be associated with these changes @ */
  public abstract void go(ASTRewrite r, TextEditGroup g);

  public Tip extend(final ASTNode... ns) {
    final Range r = range(this, ns);
    spartanizationCharStart = r.from;
    spartanizationCharEnd = r.to;
    return this;
  }

  /** A textual description of the action to be performed **/
  public final String description;
  /** The line number of the first character to be rewritten **/
  public int lineNumber;
  /** Spartanization range char end. */
  public int spartanizationCharEnd;
  /** Spartanization range char start. */
  public int spartanizationCharStart;
  /** The tipper class that supplied that tip */
  @SuppressWarnings("rawtypes") public final Class<? extends Tipper> tipperClass;
}
