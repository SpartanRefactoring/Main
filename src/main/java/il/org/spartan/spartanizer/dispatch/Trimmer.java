package il.org.spartan.spartanizer.dispatch;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wringing.*;

/** @author Yossi Gil
 * @since 2015/07/10 */
public class Trimmer extends GUI$Applicator {
  /** Disable spartanization markers, used to indicate that no spartanization
   * should be made to node */
  public static final String disablers[] = { "[[SuppressWarningsSpartan]]", //
  };
  /** Enable spartanization identifier, overriding a disabler */
  public static final String enablers[] = { "[[EnableWarningsSpartan]]", //
  };
  static final String disabledPropertyId = "Trimmer_disabled_id";

  /** A recursive scan for disabled nodes. Adds disabled property to disabled
   * nodes and their sub trees.
   * <p>
   * Algorithm:
   * <ol>
   * <li>Visit all nodes that contain an annotation.
   * <li>If a node has a disabler, disable all nodes below it using
   * {@link hop#descendants(ASTNode)}
   * <li>Disabling is done by setting a node property, and is carried out
   * <li>If a node which was previously disabled contains an enabler, enable all
   * all its descendants.
   * <li>If a node which was previously enabled, contains a disabler, disable
   * all nodes below it, and carry on.
   * <li>Obviously, the visit needs to be pre-order, i.e., visiting the parent
   * before the children.
   * </ol>
   * The disabling information is used later by the suggestion/fixing
   * mechanisms, which should know little about this class.
   * @param n an {@link ASTNode}
   * @author Ori Roth
   * @since 2016/05/13 */
  public static void disabledScan(final ASTNode n) {
    n.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(@SuppressWarnings("hiding") final N n) {
        if (!(n instanceof BodyDeclaration) || !isDisabledByIdentifier((BodyDeclaration) n))
          return true;
        disable((BodyDeclaration) n);
        return false;
      }
    });
  }

  /** @param n an {@link ASTNode}
   * @return true iff the node is spartanization disabled */
  public static boolean isDisabled(final ASTNode n) {
    return NodeData.has(n, disabledPropertyId);
  }

  public static boolean prune(final Suggestion r, final List<Suggestion> rs) {
    if (r != null) {
      r.pruneIncluders(rs);
      rs.add(r);
    }
    return true;
  }

  /** The recursive disabling process. Returns to {@link Trimmer#disabledScan}
   * upon reaching an enabler.
   * @param d disabled {@link BodyDeclaration} */
  static void disable(final BodyDeclaration d) {
    d.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        if (n instanceof BodyDeclaration && isEnabledByIdentifier((BodyDeclaration) n)) {
          disabledScan(n);
          return false;
        }
        NodeData.set(n, disabledPropertyId);
        return true;
      }
    });
  }

  static boolean hasJavaDocIdentifier(final BodyDeclaration d, final String[] ids) {
    if (d == null || d.getJavadoc() == null)
      return false;
    final String s = d.getJavadoc().toString();
    for (final String i : ids)
      if (s.contains(i))
        return true;
    return false;
  }

  static boolean isDisabledByIdentifier(final BodyDeclaration d) {
    return hasJavaDocIdentifier(d, disablers);
  }

  static boolean isEnabledByIdentifier(final BodyDeclaration d) {
    return !hasJavaDocIdentifier(d, disablers) && hasJavaDocIdentifier(d, enablers);
  }

  public final Toolbox toolbox;

  /** Instantiates this class */
  public Trimmer() {
    this(Toolbox.defaultInstance());
  }

  public Trimmer(final Toolbox toolbox) {
    super("Apply");
    this.toolbox = toolbox;
  }

  @Override public void consolidateSuggestions(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    Toolbox.refresh();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        progressMonitor.worked(1);
        TrimmerLog.visitation(n);
        if (!inRange(m, n) || isDisabled(n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        if (w == null)
          return true;
        final Suggestion s = w.suggest(n, exclude);
        TrimmerLog.suggestion(w, n);
        if (s != null) {
          if (LogManager.isActive())
            LogManager.getLogWriter().printRow(u.getJavaElement().getElementName(), s.description, s.lineNumber + "");
          TrimmerLog.application(r, s);
        }
        return true;
      }

      @Override protected void initialization(final ASTNode n) {
        disabledScan(n);
      }
    });
  }

  public String fixed(final String from) {
    final Document $ = new Document(from);
    for (;;) {
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get());
      final ASTRewrite r = createRewrite(u);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException x) {
        LoggingManner.logEvaluationError(this, x);
        throw new AssertionError(x);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }

  @Override protected ASTVisitor makeSuggestionsCollector(final List<Suggestion> $) {
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        progressMonitor.worked(1);
        if (isDisabled(n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        if (w != null)
          progressMonitor.worked(5);
        return w == null || w.cantSuggest(n) || prune(w.suggest(n, exclude), $);
      }

      @Override protected void initialization(final ASTNode n) {
        disabledScan(n);
      }
    };
  }

  public abstract class With {
    public Trimmer trimmer() {
      return Trimmer.this;
    }
  }
}
