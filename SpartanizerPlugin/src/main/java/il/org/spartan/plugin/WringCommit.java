package il.org.spartan.plugin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

import static il.org.spartan.plugin.eclipse.*;

import java.util.*;

public class WringCommit {

  public static void go(final IProgressMonitor pm, final IMarker m, final Type t) throws IllegalArgumentException, CoreException {
    if (Type.PROJECT.equals(t)) {
      goProject(pm, m);
      return;
    }
    pm.beginTask("Toggling spartanization...", 2);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(newSubMonitor(pm), m, t).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    pm.done();
  }
  
  public static void goProject(final IProgressMonitor pm, final IMarker m) throws IllegalArgumentException, CoreException {
    final List<ICompilationUnit> us = eclipse.compilationUnits();
    pm.beginTask("Spartanizing project", us.size());
    int n = 0;
    for (final ICompilationUnit u : us) {
      final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
      textChange.setTextType("java");
      textChange.setEdit(createRewrite(newSubMonitor(pm), m, Type.FILE).rewriteAST());
      if (textChange.getEdit().getLength() != 0)
        textChange.perform(pm);
      pm.worked(1);
      pm.subTask(u.getElementName() + " " + ++n + "/" + us.size());
    }
    pm.done();
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
    Toolbox.refresh();
    u.accept((new WringCommitVisitor($, m, t, u)));
  }

  public enum Type {
    DECLARATION, FILE, PROJECT
  }
  
  static class WringCommitVisitor extends Trimmer.DispatchingVisitor {
    IMarker m;
    ASTRewrite r;
    Type t;
    CompilationUnit u;
    boolean b;
    
    public WringCommitVisitor(ASTRewrite r, IMarker m, Type t, CompilationUnit u) {
      this.r = r;
      this.m = m;
      this.t = t;
      this.u = u;
      b = false;
    }
    @Override protected <N extends ASTNode> boolean go(final N n) {
      if (b)
        return false;
      if (eclipse.isNodeOutsideMarker(n, m))
        return true;
      final Wring<N> w = Toolbox.defaultInstance().find(n);
      if (w != null)
        commit(w, n);
      b = true;
      return false;
    }
    
    protected void commit(@SuppressWarnings("rawtypes") Wring w, ASTNode n) {
      switch (t) {
        case DECLARATION:
          commitDeclaration(w, n);
          break;
        case FILE:
          commitFile(w, n);
          break;
        default:
          break;
      }
    }
    
    protected void commitDeclaration(@SuppressWarnings("rawtypes") Wring w, ASTNode n) {
      commitLocal(w, ToggleSpartanization.getDeclaringDeclaration(n));
    }
    
    protected void commitFile(@SuppressWarnings("rawtypes") Wring w, ASTNode n) {
      commitLocal(w, ToggleSpartanization.getDeclaringFile(n));
    }
    
    protected void commitLocal(@SuppressWarnings("rawtypes") Wring w, final ASTNode d) {
      final DisabledChecker dc = new DisabledChecker(u);
      d.accept(new Trimmer.DispatchingVisitor() {
        @Override protected <N extends ASTNode> boolean go(N n) {
          if (dc.check(n))
            return true;
          @SuppressWarnings("unchecked") final Wring<N> x = Toolbox.defaultInstance().findWring(n, w);
          if (x != null) {
            final Rewrite make = x.make(n, exclude);
            if (make != null) {
              if (LogManager.isActive())
                // LogManager.initialize();
                LogManager.getLogWriter().printRow(u.getJavaElement().getElementName(), make.description, make.lineNumber + "");
              make.go(r, null);
            }
          }
          return true;
        }
      });
    }
  }
}
