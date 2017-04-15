package il.org.spartan.spartanizer.engine;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** A function object representing a sequence of operations on an
 * {@link ASTRewrite} object.
 * @author Yossi Gil
 * @since 2015-08-28 */
public abstract class Tip {
  @UnderConstruction("yogi -- 10/04/2017") public static boolean overlap(final Range r1, final Range r2) {
    if (r1 == null || r2 == null)
      return false;
    if (r1 == r2)
      return true;
    if (r1.from < r2.from)
      return r1.to > r2.from;
    if (r2.from < r1.from)
      return r2.to > r1.from;
    assert r1.from == r2.from;
    return true;
  }

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
   * @param description a textual description of the changes described by
   *        thisinstance
   * @param spartanizationRange the node on which change is to be carried out
   * @param highlight the node on which change is to be marked
   * @param ns additional nodes, defining the scope of this action. */
  public <N1 extends ASTNode, N2 extends ASTNode> Tip(final String description, //
      final Class<Tipper<N1>> tipperClass, //
      final N2 highlight, final ASTNode... ns) {
    this(description, tipperClass, highlight);
    spanning(ns);
  }

  public Tip spanning(final ASTNode... ¢) {
    span = range(span, ¢);
    return this;
  }

  public int getSpartanizationCharEnd() {
    return span.to;
  }

  public int getSpartanizationCharStart() {
    return span.from;
  }

  /** Convert the rewrite into changes on an {@link ASTRewrite}
   * @param r where to place the changes
   * @param g to be associated with these changes @ */
  public abstract void go(ASTRewrite r, TextEditGroup g);

  @Override public String toString() {
    return "Tip[h=" + highlight + //
        ", s=" + span + //
        ", d=" + description + //
        ", n=" + lineNumber + //
        ", c=" + namer.lastComponent(tipperClass + "") + "]";
  }

  public void intoMarker(final IMarker $) {
    try {
      $.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
      $.setAttribute(Builder.SPARTANIZATION_TIPPER_KEY, tipperClass);
      $.setAttribute(IMarker.MESSAGE, Builder.prefix() + description);
      $.setAttribute(IMarker.CHAR_START, highlight.from);
      $.setAttribute(IMarker.CHAR_END, highlight.to);
      $.setAttribute(Builder.SPARTANIZATION_CHAR_START, getSpartanizationCharStart());
      $.setAttribute(Builder.SPARTANIZATION_CHAR_END, getSpartanizationCharEnd());
      $.setAttribute(IMarker.TRANSIENT, false);
      $.setAttribute(IMarker.LINE_NUMBER, lineNumber);
    } catch (final CoreException ¢) {
      note.bug(¢);
    }
  }

  /** A textual description of the action to be performed */
  public final String description;
  /** What text range to highlight in the marker of this instance */
  public final Range highlight;
  /** The line number of the first character to be rewritten */
  public int lineNumber;
  /** Which text range would the application of this instance touches */
  public Range span;
  @SuppressWarnings("rawtypes")
  /** The tipper class that supplied that tip */
  public final Class<? extends Tipper> tipperClass;
}
