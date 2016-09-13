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
    textChange.setEdit(createRewrite(newSubMonitor(pm), m, t, null, null).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    pm.done();
  }
  
  public static void goProject(final IProgressMonitor pm, final IMarker m) throws IllegalArgumentException, CoreException {
    final ICompilationUnit cu = eclipse.currentCompilationUnit();
    final List<ICompilationUnit> us = eclipse.compilationUnits();
    pm.beginTask("Spartanizing project", us.size());
    int n = 0;
    Wring w = fillRewrite(null, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, Type.PROJECT, null);
    for (final ICompilationUnit u : us) {
      final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
      textChange.setTextType("java");
      textChange.setEdit(createRewrite(newSubMonitor(pm), m, Type.PROJECT, w, (IFile) u.getResource()).rewriteAST());
      if (textChange.getEdit().getLength() != 0)
        textChange.perform(pm);
      pm.worked(1);
      pm.subTask(u.getElementName() + " " + ++n + "/" + us.size());
    }
    pm.done();
  }
  
  private static ASTRewrite createRewrite(final IProgressMonitor pm, final CompilationUnit u, final IMarker m, final Type t, Wring w) {
    assert pm != null : "Tell whoever calls me to use " + NullProgressMonitor.class.getCanonicalName() + " instead of " + null;
    pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m, t, w);
    pm.done();
    return $;
  }
  
  private static ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m, final Type t, Wring w, IFile f) {
    if (f == null)
      return createRewrite(pm, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, t, w);
    return createRewrite(pm, (CompilationUnit) makeAST.COMPILATION_UNIT.from(f), m, t, w);
  }

  private static Wring fillRewrite(final ASTRewrite $, final CompilationUnit u, final IMarker m, final Type t, Wring w) {
    Toolbox.refresh();
    WringCommitVisitor v = new WringCommitVisitor($, m, t, u, w);
    if (w == null)
      u.accept(v);
    else
      v.commitLocal(w, u);
    return v.wring;
  }

  public enum Type {
    DECLARATION, FILE, PROJECT
  }
  
  static class WringCommitVisitor extends Trimmer.DispatchingVisitor {
    IMarker m;
    ASTRewrite r;
    Type t;
    CompilationUnit u;
    Wring wring;
    boolean b;
    
    public WringCommitVisitor(ASTRewrite r, IMarker m, Type t, CompilationUnit u) {
      this.r = r;
      this.m = m;
      this.t = t;
      this.u = u;
      b = false;
    }
    
    public WringCommitVisitor(ASTRewrite r, IMarker m, Type t, CompilationUnit u, Wring w) {
      this.r = r;
      this.m = m;
      this.t = t;
      this.u = u;
      this.wring = w;
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
      wring = w;
      switch (t) {
        case DECLARATION:
          commitDeclaration(w, n);
          break;
        case FILE:
          commitFile(w, n);
          break;
        case PROJECT:
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
    
    protected void commitLocal(@SuppressWarnings("rawtypes") Wring w, final ASTNode n) {
      final DisabledChecker dc = new DisabledChecker(u);
      n.accept(new Trimmer.DispatchingVisitor() {
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
