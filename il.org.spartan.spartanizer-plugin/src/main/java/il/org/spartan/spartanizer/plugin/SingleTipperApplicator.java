package il.org.spartan.spartanizer.plugin;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.utils.*;

/** Apply a single tipper to a specific code scope (declaration/file/project).
 * @author Ori Roth <ori.rothh@gmail.com>
 * @since Oct 16, 2016
 * @deprecated old omplementation, needs revision */
@Deprecated
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
    return createRewrite(pm, (CompilationUnit) (f != null ? makeAST.COMPILATION_UNIT.from(f) : makeAST.COMPILATION_UNIT.from(m, pm)), m, t, w);
  }
  private static Tipper<?> fillRewrite(final ASTRewrite $, //
      final CompilationUnit u, //
      final IMarker m, //
      final Type t, //
      final Tipper<?> w) {
    final TipperApplyVisitor v = new TipperApplyVisitor($, m, t, u, w);
    if (w == null)
      u.accept(v);
    else
      v.applyLocal(w, u);
    return v.tipper;
  }
  @SuppressWarnings("deprecation") public void go(final IProgressMonitor pm, final IMarker m, final Type t)
      throws IllegalArgumentException, CoreException {
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
    textChange.setEdit(createRewrite(Eclipse.newSubMonitor(pm), m, t, null, null).rewriteAST());
    if (textChange.getEdit().getLength() != 0)
      textChange.perform(pm);
    // if (Type.FILE.equals(t))
    // eclipse.announce("Done applying " + w.description() + " tip to " +
    // u.getElementName());
    pm.done();
  }
  @SuppressWarnings({ "boxing", "deprecation" }) public void goProject(final IProgressMonitor pm, final IMarker m) throws IllegalArgumentException {
    final ICompilationUnit cu = Selection.Util.getCurrentCompilationUnit().inner.get(0).descriptor;
    if (cu == null)
      return;
    final List<WrappedCompilationUnit> todo = Selection.Util.getAllCompilationUnits().inner;
    assert todo != null;
    pm.beginTask("Spartanizing project", todo.size());
    final IJavaProject jp = cu.getJavaProject();
    // XXX Roth: find a better way to get tipper from marker
    final Tipper<?> w = fillRewrite(null, (CompilationUnit) makeAST.COMPILATION_UNIT.from(m, pm), m, Type.PROJECT, null);
    if (w == null) {
      pm.done();
      return;
    }
    for (final Integer i : range.from(0).to(NewGUIApplicator.PASSES_MANY)) {
      final IProgressService ps = PlatformUI.getWorkbench().getProgressService();
      final Int pn = new Int(i + 1);
      final Bool canelled = new Bool();
      try {
        ps.run(true, true, px -> {
          px.beginTask("Applying " + w.description() + " to " + jp.getElementName() + " ; pass #" + pn.get(), todo.size());
          int n = 0;
          final Collection<WrappedCompilationUnit> exhausted = an.empty.list();
          for (final WrappedCompilationUnit wu : todo) {
            final ICompilationUnit u = wu.descriptor;
            if (px.isCanceled()) {
              canelled.set();
              break;
            }
            final TextFileChange textChange = new TextFileChange(u.getElementName(), (IFile) u.getResource());
            textChange.setTextType("java");
            try {
              textChange.setEdit(createRewrite(Eclipse.newSubMonitor(pm), m, Type.PROJECT, w, (IFile) u.getResource()).rewriteAST());
            } catch (JavaModelException | IllegalArgumentException ¢) {
              note.bug(this, ¢);
              exhausted.add(wu);
            }
            if (textChange.getEdit().getLength() == 0)
              exhausted.add(wu);
            else
              try {
                textChange.perform(pm);
              } catch (final CoreException ¢) {
                note.bug(this, ¢);
              }
            px.worked(1);
            px.subTask(u.getElementName() + the.nth(++n, todo.size()));
          }
          todo.removeAll(exhausted);
          px.done();
        });
      } catch (final InvocationTargetException ¢) {
        note.bug(this, ¢);
      } catch (final InterruptedException ¢) {
        note.cancel(this, ¢);
      }
      if (todo.isEmpty() || canelled.get())
        break;
    }
    pm.done();
    // eclipse.announce("Done applying " + w.description() + " tip to " +
    // jp.getElementName());
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

    TipperApplyVisitor(final ASTRewrite rewrite, final IMarker marker, final Type type, final CompilationUnit compilationUnit) {
      this.rewrite = rewrite;
      this.marker = marker;
      this.type = type;
      this.compilationUnit = compilationUnit;
      tipper = null;
      doneTraversing = false;
    }
    TipperApplyVisitor(final ASTRewrite rewrite, final IMarker marker, final Type type, final CompilationUnit compilationUnit,
        final Tipper<?> tipper) {
      this.rewrite = rewrite;
      this.marker = marker;
      this.type = type;
      this.compilationUnit = compilationUnit;
      this.tipper = tipper;
    }
    protected final void apply(final Tipper<?> w, final ASTNode n) {
      tipper = w;
      if (type == Type.DECLARATION)
        applyDeclaration(w, n);
      else if (type == Type.FILE)
        applyFile(w, n);
    }
    protected void applyDeclaration(final Tipper<?> w, final ASTNode n) {
      applyLocal(w, yieldAncestors.untilClass(BodyDeclaration.class).inclusiveFrom(n));
    }
    protected void applyFile(final Tipper<?> w, final ASTNode n) {
      applyLocal(w, yieldAncestors.untilClass(BodyDeclaration.class).inclusiveLastFrom(n));
    }
    protected void applyLocal(final Tipper<? extends ASTNode> t, final ASTNode root) {
      final Class<? extends ASTNode> c = t.getAbstractOperandClass();
      root.accept(new DispatchingVisitor() {
        @Override protected <N extends ASTNode> boolean go(final N ¢) {
          return disabling.on(¢) || !c.isInstance(¢) || foo(t, ¢);
        }
        @Override protected void initialization(final ASTNode ¢) {
          disabling.scan(¢);
        }
      });
    }
    @Override protected <N extends ASTNode> boolean go(final N n) {
      if (doneTraversing)
        return false;
      if (Ranger.disjoint(n, marker))
        return true;
      final Tipper<N> t = Configurations.all().firstTipper(n);
      if (t != null)
        apply(t, n);
      doneTraversing = true;
      return false;
    }
    <N extends ASTNode> boolean foo(final Tipper<? extends ASTNode> t, final N n) {
      @SuppressWarnings("unchecked") final Tipper<N> tipper1 = (Tipper<N>) t;
      if (!tipper1.check(n))
        return true;
      final Tip tip = tipper1.tip(n);
      if (tip != null)
        tip.go(rewrite, null);
      return true;
    }
  }
}
