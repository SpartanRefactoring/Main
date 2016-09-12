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
      boolean b = false;
      @Override public void preVisit(ASTNode n) {
        if (b || isNodeOutsideMarker(n, m))
          return;
        BodyDeclaration d;
        switch (t) {
          case DECLARATION:
            d = getDeclaringDeclaration(n);
            break;
          case CLASS:
            d = getDeclaringClass(n);
            break;
          case FILE:
            d = getDeclaringFile(n);
            break;
          default:
            return;
        }
        boolean da = disabledByAncestor(d);
        if (!da) {
          recursiveUnEnable($, d);
          disable($, d);
        } else if (Type.CLASS.equals(t) || Type.FILE.equals(t))
          recursiveUnEnable($, d);
        else
          unEnable($, d);
        b = true;
      }
    });
  }
  
  static String enablersRemoved(final Javadoc j) {
    String s;
    if (j == null)
      s = "/***/";
    else
      s = j.toString().trim();
    Set<String> es = getEnablers(s);
    for (String e : es) {
      String qe = Pattern.quote(e);
      s = s.replaceAll("(\n(\\s|\\*)*" + qe + ")|" + qe, "");
    }
    return s;
  }
  
  static void unEnable(final ASTRewrite $, final BodyDeclaration d) {
    final Javadoc j = d.getJavadoc();
    if (j != null)
      $.replace(j, $.createStringPlaceholder(enablersRemoved(j), ASTNode.JAVADOC), null);
  }
  
  static void recursiveUnEnable(final ASTRewrite $, final BodyDeclaration d) {
    d.accept(new ASTVisitor() {
      @Override public void preVisit(ASTNode n) {
        if (!(n instanceof BodyDeclaration))
          return;
        unEnable($, (BodyDeclaration) n);
      }
    });
  }
  
  static void disable(final ASTRewrite $, final BodyDeclaration d) {
    Javadoc j = d.getJavadoc();
    String s = enablersRemoved(j);
    if (getDisablers(s).isEmpty())
      if (s.matches("(?s).*\n\\s*\\*\\/$"))
        s = s.replaceFirst("\\*\\/$", "* " + disabler + "\n */");
      else
        s = s.replaceFirst("\\*\\/$", "\n * " + disabler + "\n */");
    if (j != null)
      $.replace(j, $.createStringPlaceholder(s, ASTNode.JAVADOC), null);
    else
      $.replace(d, $.createStringPlaceholder(s + "\n" + d.toString().trim(), d.getNodeType()), null);
  }
  
  static BodyDeclaration getDeclaringDeclaration(ASTNode n) {
    ASTNode $ = n;
    for (; $ != null && !($ instanceof BodyDeclaration) ; $ = $.getParent());
    return (BodyDeclaration) $;
  }
  
  static BodyDeclaration getDeclaringClass(ASTNode n) {
    ASTNode $ = n;
    for (; $ != null && !($ instanceof AbstractTypeDeclaration) ; $ = $.getParent());
    return (BodyDeclaration) $;
  }
  
  static BodyDeclaration getDeclaringFile(ASTNode n) {
    ASTNode $ = getDeclaringDeclaration(n);
    if ($ == null)
      return null;
    for (ASTNode p = $.getParent(); p != null ; p = p.getParent())
      if (p instanceof BodyDeclaration)
        $ = p;
    return (BodyDeclaration) $;
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
  
  static boolean disabledByAncestor(ASTNode n) {
    for (ASTNode p = n.getParent() ; p != null ; p = p.getParent())
      if (p instanceof BodyDeclaration && ((BodyDeclaration) p).getJavadoc() != null) {
        String s = ((BodyDeclaration) p).getJavadoc().toString();
        for (String e : DisabledChecker.enablers)
          if (s.contains(e))
            return false;
        for (String d : DisabledChecker.disablers)
          if (s.contains(d))
            return true;
      }
    return false;
  }
}
