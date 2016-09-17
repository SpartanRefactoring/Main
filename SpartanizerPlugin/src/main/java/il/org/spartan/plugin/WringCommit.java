package il.org.spartan.plugin;

import static il.org.spartan.plugin.eclipse.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

public class WringCommit {
  public static void go(final IProgressMonitor pm, final IMarker m, final Type t) throws IllegalArgumentException, CoreException {
    if (Type.PROJECT.equals(t)) {
      goProject(pm, m);
      return;
    }
    pm.beginTask("Toggling spartanization...", IProgressMonitor.UNKNOWN);
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(newSubMonitor(pm), m, t, null, null).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    pm.done();
  }

  public static void goProject(final IProgressMonitor pm, final IMarker m) throws IllegalArgumentException {
    final ICompilationUnit cu = eclipse.currentCompilationUnit();
    assert cu != null;
    final List<ICompilationUnit> us = eclipse.compilationUnits();
    assert us != null;
    pm.beginTask("Spartanizing project", us.size());
    final IJavaProject jp = cu.getJavaProject();
    final Wring w = fillRewrite(null, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, Type.PROJECT, null);
    assert w != null;
    for (int i = 0; i < SpartanizeAll.MAX_PASSES; ++i) {
      final IWorkbench wb = PlatformUI.getWorkbench();
      final IProgressService ps = wb.getProgressService();
      final AtomicInteger pn = new AtomicInteger(i + 1);
      try {
        // TODO: ORIORIRORIORORI NO BUSY CURSOR
        ps.busyCursorWhile(px -> {
          px.beginTask("Applying " + w.getClass().getSimpleName() + " to " + jp.getElementName() + " ; pass #" + pn.get(), us.size());
          int n = 0;
          final List<ICompilationUnit> es = new LinkedList<>();
          for (final ICompilationUnit u : us) {
            final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
            textChange.setTextType("java");
            try {
              textChange.setEdit(createRewrite(newSubMonitor(pm), m, Type.PROJECT, w, (IFile) u.getResource()).rewriteAST());
            } catch (JavaModelException | IllegalArgumentException x) {
              Plugin.log(x);
            }
            if (textChange.getEdit().getLength() == 0)
              es.add(u);
            else
              try {
                textChange.perform(pm);
              } catch (final CoreException e) {
                Plugin.log(e);
              }
            px.worked(1);
            px.subTask(u.getElementName() + " " + ++n + "/" + us.size());
          }
          us.removeAll(es);
          px.done();
        });
      } catch (InvocationTargetException | InterruptedException e) {
        Plugin.log(e);
      }
    }
    pm.done();
  }

  private static ASTRewrite createRewrite(final IProgressMonitor pm, final CompilationUnit u, final IMarker m, final Type t, final Wring w) {
    assert pm != null : "Tell whoever calls me to use " + NullProgressMonitor.class.getCanonicalName() + " instead of " + null;
    pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m, t, w);
    pm.done();
    return $;
  }

  private static ASTRewrite createRewrite(final IProgressMonitor pm, final IMarker m, final Type t, final Wring w, final IFile f) {
    return createRewrite(pm, f != null ? (CompilationUnit) makeAST.COMPILATION_UNIT.from(f) : (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm),
        m, t, w);
  }

  private static Wring<?> fillRewrite(final ASTRewrite $, final CompilationUnit u, final IMarker m, final Type t, final Wring w) {
    Toolbox.refresh();
    final WringCommitVisitor v = new WringCommitVisitor($, m, t, u, w);
    if (w == null)
      u.accept(v);
    else
      v.commitLocal(w, u);
    return v.wring;
  }

  public enum Type {
    DECLARATION, FILE, PROJECT
  }

  static class WringCommitVisitor extends DispatchingVisitor {
    final IMarker marker;
    final ASTRewrite rewrite;
    final Type type;
    final CompilationUnit compilationUnit;
    Wring<?> wring;
    // TODO: Ori, you cannot have a boolean undocumented like this
    boolean b;

    public WringCommitVisitor(final ASTRewrite rewrite, final IMarker marker, final Type type, final CompilationUnit compilationUnit) {
      this.rewrite = rewrite;
      this.marker = marker;
      this.type = type;
      this.compilationUnit = compilationUnit;
      wring = null;
      b = false;
    }

    public WringCommitVisitor(final ASTRewrite rewrite, final IMarker marker, final Type type, final CompilationUnit compilationUnit,
        final Wring<?> wring) {
      this.rewrite = rewrite;
      this.marker = marker;
      this.type = type;
      this.compilationUnit = compilationUnit;
      this.wring = wring;
      b = false;
    }

    protected void commit(final Wring<?> w, final ASTNode n) {
      wring = w;
      switch (type) {
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

    protected void commitDeclaration(final Wring<?> w, final ASTNode n) {
      commitLocal(w, ToggleSpartanization.getDeclaringDeclaration(n));
    }

    protected void commitFile(final Wring<?> w, final ASTNode n) {
      commitLocal(w, ToggleSpartanization.getDeclaringFile(n));
    }

    protected void commitLocal(final Wring w, final ASTNode n) {
      Toolbox.refresh();
      n.accept(new DispatchingVisitor() {
        @Override protected <N extends ASTNode> boolean go(final N n) {
          @SuppressWarnings("unchecked") final Wring<N> x = Toolbox.defaultInstance().findWring(n, w);
          if (x != null) {
            final Suggestion make = x.suggest(n, exclude);
            if (make != null) {
              if (LogManager.isActive())
                // LogManager.initialize();
                LogManager.getLogWriter().printRow(compilationUnit.getJavaElement().getElementName(), make.description, make.lineNumber + "");
              make.go(rewrite, null);
            }
          }
          return true;
        }
      });
    }

    @Override protected <N extends ASTNode> boolean go(final N n) {
      if (b)
        return false;
      if (eclipse.isNodeOutsideMarker(n, marker))
        return true;
      final Wring<N> w = Toolbox.defaultInstance().find(n);
      if (w != null)
        commit(w, n);
      b = true;
      return false;
    }
  }
}
