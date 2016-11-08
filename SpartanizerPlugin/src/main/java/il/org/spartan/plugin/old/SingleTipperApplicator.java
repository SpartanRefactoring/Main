package il.org.spartan.plugin.old;

import static il.org.spartan.plugin.old.eclipse.*;

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

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

public final class SingleTipperApplicator {
  private static ASTRewrite createRewrite(//
      final IProgressMonitor pm, //
      final CompilationUnit u, //
      final IMarker m, //
      final Type t, //
      final Tipper<?> w) {
    assert pm != null : "Tell whoever calls me to use " + NullProgressMonitor.class.getCanonicalName() + " instead of " + null;
    pm.beginTask("Creating rewrite operation...", 1);
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    fillRewrite($, u, m, t, w);
    pm.done();
    return $;
  }
  private static ASTRewrite createRewrite(//
      final IProgressMonitor pm, //
      final IMarker m, //
      final Type t, //
      final Tipper<?> w, //
      final IFile f) {
    return createRewrite(pm, f != null ? (CompilationUnit) makeAST.COMPILATION_UNIT.from(f) : (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm),
        m, t, w);
  }
  private static Tipper<?> fillRewrite(final ASTRewrite $, //
      final CompilationUnit u, //
      final IMarker m, //
      final Type t, //
      final Tipper<?> w) {
    Toolbox.refresh();
    final TipperApplyVisitor v = new TipperApplyVisitor($, m, t, u, w);
    if (w == null)
      u.accept(v);
    else
      v.applyLocal(w, u);
    return v.tipper;
  }
  public void go(final IProgressMonitor pm, final IMarker m, final Type t) throws IllegalArgumentException, CoreException {
    if (Type.PROJECT.equals(t)) {
      goProject(pm, m);
      return;
    }
    final ICompilationUnit u = makeAST.iCompilationUnit(m);
    final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
    final Tipper<?> w = fillRewrite(null, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, Type.SEARCH_TIPPER, null);
    if (w == null)
      return;
    pm.beginTask("Applying " + w.description() + " tip to " + u.getElementName(), IProgressMonitor.UNKNOWN);
    textChange.setTextType("java");
    textChange.setEdit(createRewrite(newSubMonitor(pm), m, t, null, null).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    if (Type.FILE.equals(t))
      eclipse.announce("Done applying " + w.description() + " tip to " + u.getElementName());
    pm.done();
  }
  public void goProject(final IProgressMonitor pm, final IMarker m) throws IllegalArgumentException {
    final ICompilationUnit cu = eclipse.currentCompilationUnit();
    if (cu == null)
      return;
    assert cu != null;
    final List<ICompilationUnit> todo = eclipse.facade.compilationUnits();
    assert todo != null;
    pm.beginTask("Spartanizing project", todo.size());
    final IJavaProject jp = cu.getJavaProject();
    // XXX Roth: find a better way to get tipper from marker
    final Tipper<?> w = fillRewrite(null, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, Type.PROJECT, null);
    if (w == null) {
      pm.done();
      return;
    }
    for (int i = 0; i < LaconizeProject.MAX_PASSES; ++i) {
      final IWorkbench wb = PlatformUI.getWorkbench();
      final IProgressService ps = wb.getProgressService();
      final AtomicInteger pn = new AtomicInteger(i + 1);
      final AtomicBoolean canelled = new AtomicBoolean(false);
      try {
        ps.run(true, true, px -> {
          px.beginTask("Applying " + w.description() + " to " + jp.getElementName() + " ; pass #" + pn.get(), todo.size());
          int n = 0;
          final List<ICompilationUnit> exhausted = new ArrayList<>();
          for (final ICompilationUnit u : todo) {
            if (px.isCanceled()) {
              canelled.set(true);
              break;
            }
            final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
            textChange.setTextType("java");
            try {
              textChange.setEdit(createRewrite(newSubMonitor(pm), m, Type.PROJECT, w, (IFile) u.getResource()).rewriteAST());
            } catch (JavaModelException | IllegalArgumentException x) {
              monitor.logEvaluationError(this, x);
              exhausted.add(u);
            }
            if (textChange.getEdit().getLength() == 0)
              exhausted.add(u);
            else
              try {
                textChange.perform(pm);
              } catch (final CoreException e) {
                monitor.logEvaluationError(this, e);
              }
            px.worked(1);
            px.subTask(u.getElementName() + " " + ++n + "/" + todo.size());
          }
          todo.removeAll(exhausted);
          px.done();
        });
      } catch (final InvocationTargetException e) {
        monitor.logEvaluationError(this, e);
      } catch (final InterruptedException x) {
        monitor.logCancellationRequest(this, x);
      }
      if (todo.isEmpty() || canelled.get())
        break;
    }
    pm.done();
    eclipse.announce("Done applying " + w.description() + " tip to " + jp.getElementName());
  }

  public enum Type {
    DECLARATION, FILE, PROJECT, SEARCH_TIPPER
  }

  static class TipperApplyVisitor extends DispatchingVisitor {
    final IMarker marker;
    final ASTRewrite rewrite;
    final Type type;
    final CompilationUnit compilationUnit;
    Tipper<?> tipper;
    /** A boolean flag indicating end of traversal. Set true after required
     * operation has been made. */
    boolean doneTraversing;

    public TipperApplyVisitor(final ASTRewrite rewrite, final IMarker marker, final Type type, final CompilationUnit compilationUnit) {
      this.rewrite = rewrite;
      this.marker = marker;
      this.type = type;
      this.compilationUnit = compilationUnit;
      tipper = null;
      doneTraversing = false;
    }
    public TipperApplyVisitor(final ASTRewrite rewrite, final IMarker marker, final Type type, final CompilationUnit compilationUnit,
        final Tipper<?> tipper) {
      this.rewrite = rewrite;
      this.marker = marker;
      this.type = type;
      this.compilationUnit = compilationUnit;
      this.tipper = tipper;
    }
    protected final void apply(final Tipper<?> w, final ASTNode n) {
      tipper = w;
      switch (type) {
        case DECLARATION:
          applyDeclaration(w, n);
          break;
        case FILE:
          applyFile(w, n);
          break;
        case PROJECT:
        default:
          break;
      }
    }
    protected void applyDeclaration(final Tipper<?> w, final ASTNode n) {
      applyLocal(w, searchAncestors.forClass(BodyDeclaration.class).inclusiveFrom(n));
    }
    protected void applyFile(final Tipper<?> w, final ASTNode n) {
      applyLocal(w, searchAncestors.forClass(BodyDeclaration.class).inclusiveLastFrom(n));
    }
    protected void applyLocal(@SuppressWarnings("rawtypes") final Tipper w, final ASTNode b) {
      b.accept(new DispatchingVisitor() {
        @Override protected <N extends ASTNode> boolean go(final N n) {
          if (disabling.on(n) || !w.myAbstractOperandsClass().isInstance(n))
            return true;
          Toolbox.defaultInstance();
          @SuppressWarnings("unchecked") final Tipper<N> x = Toolbox.findTipper(n, w);
          if (x != null) {
            final Tip make = x.tip(n, exclude);
            if (make != null) {
              if (LogManager.isActive())
                LogManager.getLogWriter().printRow(compilationUnit.getJavaElement().getElementName(), make.description, make.lineNumber + "");
              make.go(rewrite, null);
            }
          }
          return true;
        }
        @Override protected void initialization(final ASTNode ¢) {
          disabling.scan(¢);
        }
      });
    }
    @Override protected <N extends ASTNode> boolean go(final N n) {
      if (doneTraversing)
        return false;
      if (eclipse.facade.isNodeOutsideMarker(n, marker))
        return true;
      final Tipper<N> t = Toolbox.defaultInstance().firstTipper(n);
      if (t != null)
        apply(t, n);
      doneTraversing = true;
      return false;
    }
  }
}
