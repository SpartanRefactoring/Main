package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.plugin.old.eclipse.*;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.traversal.*;

/** A utility class used to add enablers/disablers to code automatically, with
 * AST scan based recursive algorithms. The automatic disabling mechanism is
 * offered in the marker quick fix menu, see {@link QuickFixer}. The change is
 * textual, implemented as a JavaDoc comment that can be read by
 * {@link DisabledChecker}.
 * @author Ori Roth */
public enum SuppressWarningsOnOff {
  ByComment((r, d) -> {
    final Javadoc j = d.getJavadoc();
    String s = enablersRemoved(j);
    if (getDisablers(s).isEmpty())
      s = s.replaceFirst("\\*/$", (s.matches("(?s).*\n\\s*\\*/$") ? "" : "\n ") + "* " + disabling.ByComment.disabler + "\n */");
    if (j != null)
      r.replace(j, r.createStringPlaceholder(s, ASTNode.JAVADOC), null);
    else
      r.set(d, d.getJavadocProperty(), r.createGroupNode(new ASTNode[] { r.createStringPlaceholder(s, ASTNode.JAVADOC) }), null);
  }), ByAnnotation((r, d) -> {
    final StringLiteral s = d.getAST().newStringLiteral();
    s.setLiteralValue(Eclipse.user() + " -- " + Eclipse.date());
    final SingleMemberAnnotation a = d.getAST().newSingleMemberAnnotation();
    a.setTypeName(d.getAST().newName(disabling.ByAnnotation.disabler));
    a.setValue(s);
    r.getListRewrite(d, d.getModifiersProperty()).insertFirst(a, null);
  });
  /** Textually disable a {@link BodyDeclaration}, while recursively removing
   * enablers from sub tree.
   * @param r a rewrite to fill
   * @param d a {@link BodyDeclaration} to disable */
  final BiConsumer<ASTRewrite, BodyDeclaration> disable;

  SuppressWarningsOnOff(final BiConsumer<ASTRewrite, BodyDeclaration> disable) {
    this.disable = disable;
  }

  /** Commit textual change of a certain {@link Type}: adding a disabler comment
   * to marked code with a progress monitor.
   * @param pm progress monitor for the operation
   * @param m marked code to be disabled
   * @param tipper deactivation {@link Type} */
  public void deactivate(final IProgressMonitor pm, final IMarker m, final Type t) throws IllegalArgumentException, CoreException {
    pm.beginTask("Toggling spartanization...", 2);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(newSubMonitor(pm), m, t).rewriteAST());
    textChange.perform(pm);
    pm.done();
  }

  /** @param n an {@link ASTNode}
   * @return whether the node is disabled by an ancestor
   *         {@link BodyDeclaration}, containing a disabler in its JavaDoc. */
  static boolean disabledByAncestor(final ASTNode n) {
    if (n == null)
      return false;
    for (ASTNode p = n.getParent(); p != null; p = p.getParent())
      if (iz.bodyDeclaration(p)) {
        final BodyDeclaration d = (BodyDeclaration) p;
        if (d.getJavadoc() != null) {
          final String s = d.getJavadoc() + "";
          for (final String e : disabling.ByComment.enablers) // NANO
            if (s.contains(e))
              return false;
          for (final String ds : disabling.ByComment.disablers) // NANO
            if (s.contains(ds))
              return true;
        }
        if (disabling.hasAnnotation(d, disabling.ByAnnotation.disablers))
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
    return getKeywords(¢, disabling.ByComment.disablers);
  }

  static Iterable<String> getEnablers(final String ¢) {
    return getKeywords(¢, disabling.ByComment.enablers);
  }

  static Collection<String> getKeywords(final String c, final String[] kws) {
    return Stream.of(kws).filter(c::contains).collect(toSet());
  }

  static void recursiveUnEnable(final ASTRewrite $, final BodyDeclaration d) {
    d.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof BodyDeclaration)
          unEnable($, (BodyDeclaration) ¢);
      }
    });
  }

  static void unEnable(final ASTRewrite $, final BodyDeclaration d) {
    unEnable($, d.getJavadoc());
  }

  private ASTRewrite createRewrite(final IProgressMonitor pm, final CompilationUnit u, final IMarker m, final Type t) {
    assert pm != null : "Tell whoever calls me to use " + NullProgressMonitor.class.getCanonicalName() + " instead of " + null;
    pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m, t);
    pm.done();
    return $;
  }

  private ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m, final Type t) {
    return createRewrite(pm, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, t);
  }

  private void fillRewrite(final ASTRewrite $, final CompilationUnit u, final IMarker m, final Type t) {
    u.accept(new ASTVisitor(true) {
      boolean b;

      @Override public void preVisit(final ASTNode n) {
        if (b || Ranger.disjoint(n, m) || !Ranger.contained(n, m))
          return;
        final BodyDeclaration d;
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
        if (d == null)
          return;
        b = true; // may consider setting before null check
        recursiveUnEnable($, d);
        if (!disabledByAncestor(d))
          disable.accept($, d);
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
