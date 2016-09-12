package il.org.spartan.plugin;

import static il.org.spartan.plugin.eclipse.*;

import java.util.*;
import java.util.regex.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.*;

public class ToggleSpartanization {
  abstract static class DeclarationVisitor extends ASTVisitor {
    abstract <N extends ASTNode> boolean go(final N n);

    @Override public final boolean visit(final Assignment ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final Block ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final CastExpression ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final ConditionalExpression x) {
      return go(x);
    }

    @Override public final boolean visit(final EnumDeclaration ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final FieldDeclaration ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final IfStatement ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final InfixExpression ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final MethodDeclaration ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final MethodInvocation ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final NormalAnnotation ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final PostfixExpression ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final PrefixExpression ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final ReturnStatement ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final SingleVariableDeclaration d) {
      return go(d);
    }

    @Override public final boolean visit(final SuperConstructorInvocation ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final TypeDeclaration ¢) {
      return go(¢);
    }

    @Override public final boolean visit(final VariableDeclarationFragment ¢) {
      return go(¢);
    }
  }

  public enum Type {
    DECLARATION, CLASS, FILE
  }

  static final String disabler = DisabledChecker.disablers[0];

  private static ASTRewrite createRewrite(final IProgressMonitor pm, final CompilationUnit u, final IMarker m, final Type t) {
    if (pm != null)
      pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m, t);
    if (pm != null)
      pm.done();
    return $;
  }

  private static ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m, final Type t) {
    return createRewrite(pm, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, t);
  }

  public static void diactivate(final IProgressMonitor pm, final IMarker m, final Type t) throws IllegalArgumentException, CoreException {
    pm.beginTask("Toggling spartanization...", 2);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(newSubMonitor(pm), m, t).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    pm.done();
  }

  static void disable(final ASTRewrite $, final BodyDeclaration d) {
    final Javadoc j = d.getJavadoc();
    String s = enablersRemoved(j);
    if (getDisablers(s).isEmpty())
      s = s.replaceFirst("\\*\\/$", (s.matches("(?s).*\n\\s*\\*\\/$") ? "" : "\n ") + "* " + disabler + "\n */");
    if (j != null)
      $.replace(j, $.createStringPlaceholder(s, ASTNode.JAVADOC), null);
    else
      $.replace(d, $.createStringPlaceholder(s + "\n" + (d + "").trim(), d.getNodeType()), null);
  }

  static boolean disabledByAncestor(final ASTNode n) {
    for (ASTNode p = n.getParent(); p != null; p = p.getParent())
      if (p instanceof BodyDeclaration && ((BodyDeclaration) p).getJavadoc() != null) {
        final String s = ((BodyDeclaration) p).getJavadoc() + "";
        for (final String e : DisabledChecker.enablers)
          if (s.contains(e))
            return false;
        for (final String d : DisabledChecker.disablers)
          if (s.contains(d))
            return true;
      }
    return false;
  }

  static String enablersRemoved(final Javadoc j) {
    String $ = j == null ? "/***/" : (j + "").trim();
    final Set<String> es = getEnablers($);
    for (final String e : es) {
      final String qe = Pattern.quote(e);
      $ = $.replaceAll("(\n(\\s|\\*)*" + qe + ")|" + qe, "");
    }
    return $;
  }

  private static void fillRewrite(final ASTRewrite $, final CompilationUnit u, final IMarker m, final Type t) {
    u.accept(new DeclarationVisitor() {
      boolean b = false;

      @Override <N extends ASTNode> boolean go(final N n) {
        if (b || isNodeOutsideMarker(n, m))
          return true;
        ASTNode c;
        switch (t) {
          case DECLARATION:
            c = getDeclaringDeclaration(n);
            break;
          case CLASS:
            c = getDeclaringClass(n);
            break;
          case FILE:
            c = getDeclaringFile(n);
            break;
          default:
            c = null;
        }
        if (c == null)
          return false;
        final BodyDeclaration d = (BodyDeclaration) c;
        final boolean da = disabledByAncestor(d);
        if (!da) {
          recursiveUnEnable($, d);
          disable($, d);
        } else if (!Type.CLASS.equals(t) && !Type.FILE.equals(t))
          unEnable($, d);
        else
          recursiveUnEnable($, d);
        b = true;
        return false;
      }
    });
  }

  static ASTNode getDeclaringClass(final ASTNode n) {
    ASTNode $ = n;
    // TODO: Ori: Use one of our library class for this, we have done this so
    // many times.
    for (; $ != null && !($ instanceof AbstractTypeDeclaration); $ = $.getParent())
      ;
    return $;
  }

  static ASTNode getDeclaringDeclaration(final ASTNode n) {
    ASTNode $ = n;
    // Ori: Use a builtin class for this, we have done this so many times.
    for (; $ != null && !($ instanceof BodyDeclaration); $ = $.getParent())
      ;
    return $;
  }

  static ASTNode getDeclaringFile(final ASTNode n) {
    ASTNode $ = getDeclaringDeclaration(n);
    if ($ == null)
      return $;
    for (ASTNode p = $.getParent(); p != null; p = p.getParent())
      if (p instanceof BodyDeclaration)
        $ = p;
    return $;
  }

  static Set<String> getDisablers(final String s) {
    return getKeywords(s, DisabledChecker.disablers);
  }

  static Set<String> getEnablers(final String s) {
    return getKeywords(s, DisabledChecker.enablers);
  }

  static Set<String> getKeywords(final String c, final String[] kws) {
    final Set<String> $ = new HashSet<>();
    for (final String kw : kws)
      if (c.contains(kw))
        $.add(kw);
    return $;
  }

  static void recursiveUnEnable(final ASTRewrite $, final BodyDeclaration d) {
    d.accept(new DeclarationVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        if (!(n instanceof BodyDeclaration))
          return true;
        unEnable($, (BodyDeclaration) n);
        return true;
      }
    });
  }

  static void unEnable(final ASTRewrite $, final BodyDeclaration d) {
    final Javadoc j = d.getJavadoc();
    if (j != null)
      $.replace(j, $.createStringPlaceholder(enablersRemoved(j), ASTNode.JAVADOC), null);
  }
}
