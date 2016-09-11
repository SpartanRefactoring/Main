package il.org.spartan.plugin;

import static il.org.spartan.plugin.eclipse.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.*;

public class ToggleSpartanization {
  public enum Type {
    DECLARATION, CLASS, FILE
  }
  static final String disabler = DisabledChecker.disablers[0];
  public static void diactivate(IProgressMonitor pm, IMarker m, Type t) throws IllegalArgumentException, CoreException {
    pm.beginTask("Toggling spartanization...", 2);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(newSubMonitor(pm), m, t).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    pm.done();
  }
  
  private static ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m, Type t) {
    return createRewrite(pm, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, t);
  }

  private static ASTRewrite createRewrite(IProgressMonitor pm, CompilationUnit u, IMarker m, Type t) {
    if (pm != null)
      pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m, t);
    if (pm != null)
      pm.done();
    return $;
  }

  private static void fillRewrite(ASTRewrite $, CompilationUnit u, IMarker m, Type t) {
    u.accept(new ASTVisitor() {
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

      <N extends ASTNode> boolean go(final N n) {
        if (isNodeOutsideMarker(n, m))
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
        Javadoc j = d.getJavadoc();
        String s;
        if (j == null)
          s = "/***/";
        else
          s = j.toString().trim();
        Set<String> es = getEnablers(s);
        for (String e : es)
          s.replaceAll(e, "");
        if (s.matches("(?s).*\n\\s*\\*\\/$"))
          s = s.replaceFirst("\\*\\/$", "* " + disabler + "\n */");
        else
          s = s.replaceFirst("\\*\\/$", "\n * " + disabler + "\n */");
        if (j != null)
          $.replace(j, $.createStringPlaceholder(s, ASTNode.JAVADOC), null);
        else
          $.replace(d, $.createStringPlaceholder(s + "\n" + d.toString().trim(), d.getNodeType()), null);
        return false;
      }
    });
  }
  
  static ASTNode getDeclaringDeclaration(ASTNode n) {
    ASTNode $ = n;
    for (; $ != null && !($ instanceof BodyDeclaration) ; $ = $.getParent());
    return $;
  }
  
  static ASTNode getDeclaringClass(ASTNode n) {
    ASTNode $ = n;
    for (; $ != null && !($ instanceof AbstractTypeDeclaration) ; $ = $.getParent());
    return $;
  }
  
  static ASTNode getDeclaringFile(ASTNode n) {
    ASTNode $ = getDeclaringDeclaration(n);
    if ($ == null)
      return $;
    for (ASTNode p = $.getParent(); p != null ; p = p.getParent())
      if (p instanceof BodyDeclaration)
        $ = p;
    return $;
  }
  
  static Set<String> getEnablers(String s) {
    return getKeywords(s, DisabledChecker.enablers);
  }
  
  static Set<String> getDisablers(String s) {
    return getKeywords(s, DisabledChecker.disablers);
  }
  
  static Set<String> getKeywords(String c, String[] kws) {
    Set<String> $ = new HashSet<>();
    for (String kw : kws)
      if (c.contains(kw))
        $.add(kw);
    return $;
  }
}
