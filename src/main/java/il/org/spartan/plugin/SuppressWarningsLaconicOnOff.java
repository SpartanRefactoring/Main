package il.org.spartan.plugin;

import static il.org.spartan.plugin.old.eclipse.*;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;

import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;

/** A utility class used to add enablers/disablers to code automatically, with
 * AST scan based recursive algorithms. The automatic disabling mechanism is
 * offered in the marker quick fix menu, see {@link QuickFixer}. The change is
 * textual, implemented as a JavaDoc comment that can be read by
 * {@link DisabledChecker}.
 * @author Ori Roth */
public enum SuppressWarningsLaconicOnOff {
  ;
  /** Commit textual change of a certain {@link Type}: adding a disabler comment
   * to marked code with a progress monitor.
   * @param pm progress monitor for the operation
   * @param m marked code to be disabled
   * @param tipper deactivation {@link Type} */
  public static void deactivate(final IProgressMonitor pm, final IMarker m, final Type t) throws IllegalArgumentException, CoreException {
    pm.beginTask("Toggling spartanization...", 2);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(newSubMonitor(pm), m, t).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    pm.done();
  }

  /** Textually disable a {@link BodyDeclaration}, while recursively removing
   * enablers from sub tree.
   * @param $ a rewrite to fill
   * @param d a {@link BodyDeclaration} to disable */
  static void disable(final ASTRewrite $, final BodyDeclaration d) {
    final Javadoc j = d.getJavadoc();
    String s = enablersRemoved(j);
    if (getDisablers(s).isEmpty())
      s = s.replaceFirst("\\*/$", (s.matches("(?s).*\n\\s*\\*/$") ? "" : "\n ") + "* " + disabling.disabler + "\n */");
    if (j != null)
      $.replace(j, $.createStringPlaceholder(s, ASTNode.JAVADOC), null);
    else
      $.replace(d, $.createGroupNode(new ASTNode[] { $.createStringPlaceholder(s + fixNL(d), ASTNode.JAVADOC), $.createCopyTarget(d) }), null);
  }

  private static String fixNL(final BodyDeclaration ¢) {
    return ¢ instanceof EnumDeclaration || ¢ instanceof MethodDeclaration || ¢ instanceof EnumConstantDeclaration ? "\n" : "";
  }

  /** @param n an {@link ASTNode}
   * @return <code><b>true</b></code> <em>iff</em> the node is disabled by an
   *         ancestor {@link BodyDeclaration}, containing a disabler in its
   *         JavaDoc. */
  static boolean disabledByAncestor(final ASTNode n) {
    for (ASTNode p = n.getParent(); p != null; p = p.getParent())
      if (p instanceof BodyDeclaration && ((BodyDeclaration) p).getJavadoc() != null) {
        final String s = ((BodyDeclaration) p).getJavadoc() + "";
        for (final String e : disabling.enablers) // NANO
          if (s.contains(e))
            return false;
        for (final String d : disabling.disablers) // NANO
          if (s.contains(d))
            return true;
      }
    return false;
  }

  /** @param j a {@link JavaDoc}
   * @return comment's text, without eneblers identifiers. */
  static String enablersRemoved(final Javadoc j) {
    String $ = j == null ? "/***/" : (j + "").trim();
    for (final String e : getEnablers($)) {
      final String qe = Pattern.quote(e);
      $ = $.replaceAll("(\n(\\s|\\*)*" + qe + ")|" + qe, "");
    }
    return $;
  }

  static Collection<String> getDisablers(final String ¢) {
    return getKeywords(¢, disabling.disablers);
  }

  static Iterable<String> getEnablers(final String ¢) {
    return getKeywords(¢, disabling.enablers);
  }

  static Set<String> getKeywords(final String c, final String[] kws) {
    return Stream.of(kws).filter(c::contains).collect(Collectors.toSet());
  }

  static void recursiveUnEnable(final ASTRewrite $, final BodyDeclaration d) {
    d.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof BodyDeclaration)
          unEnable($, (BodyDeclaration) ¢);
      }
    });
  }

  static void unEnable(final ASTRewrite $, final BodyDeclaration d) {
    unEnable($, d.getJavadoc());
  }

  private static ASTRewrite createRewrite(final IProgressMonitor pm, final CompilationUnit u, final IMarker m, final Type t) {
    assert pm != null : "Tell whoever calls me to use " + NullProgressMonitor.class.getCanonicalName() + " instead of " + null;
    pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m, t);
    pm.done();
    return $;
  }

  private static ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m, final Type t) {
    return createRewrite(pm, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, t);
  }

  private static void fillRewrite(final ASTRewrite $, final CompilationUnit u, final IMarker m, final Type t) {
    u.accept(new ASTVisitor() {
      boolean b;

      @Override public void preVisit(final ASTNode n) {
        if (b || eclipse.facade.isNodeOutsideMarker(n, m))
          return;
        BodyDeclaration d;
        switch (t) {
          case CLASS:
            d = (BodyDeclaration) yieldAncestors.untilClass(AbstractTypeDeclaration.class).inclusiveFrom(n);
            break;
          case FILE:
            d = (BodyDeclaration) yieldAncestors.untilClass(BodyDeclaration.class).inclusiveLastFrom(n);
            break;
          case FUNCTION:
            d = (BodyDeclaration) yieldAncestors.untilClass(BodyDeclaration.class).inclusiveFrom(n);
            break;
          default:
            return;
        }
        recursiveUnEnable($, d);
        if (!disabledByAncestor(d))
          disable($, d);
        b = true;
      }
    });
  }

  private static void unEnable(final ASTRewrite $, final Javadoc j) {
    if (j != null)
      $.replace(j, $.createStringPlaceholder(enablersRemoved(j), ASTNode.JAVADOC), null);
  }

  public enum Type {
    FUNCTION, CLASS, FILE
  }
}
