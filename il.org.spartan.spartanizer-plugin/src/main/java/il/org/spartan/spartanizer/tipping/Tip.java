package il.org.spartan.spartanizer.tipping;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

/** A function object representing a sequence of operations on an
 * {@link ASTRewrite} object.
 * @author Yossi Gil
 * @since 2015-08-28 */
public abstract class Tip {
  private static Range range(final Range r, final ASTNode... ns) {
    Range $ = r;
    for (final ASTNode ¢ : ns)
      $ = $.merge(il.org.spartan.spartanizer.ast.navigate.Ranger.make(¢));
    return $;
  }
  public <N1 extends ASTNode, N2 extends ASTNode> Tip(//
      final String description, //
      final Class<? extends Tipper<N1>> tipperClass, //
      final N2 center) {
    this(description, tipperClass, containing.compilationUnit(center), il.org.spartan.spartanizer.ast.navigate.Ranger.make(center));
  }
  public <N extends ASTNode> Tip(final String description, final Class<? extends Tipper<N>> tipperClass, final CompilationUnit u,
      final Range highlight) {
    this.description = description;
    this.tipperClass = tipperClass;
    span = range(this.highlight = highlight); // Ensure two distinct ranges
    lineNumber = u == null ? -1 : u.getLineNumber(highlight.from);
  }
  /** Instantiates this class
   * @param description a textual description of the changes described by this
   *        instance
   * @param highlight the node on which change is to be marked
   * @param ns additional nodes, defining the scope of this misc. */
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
        ", c=" + cCamelCase.lastComponent(tipperClass + "") + "]";
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

  /** A textual description of the misc to be performed */
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
