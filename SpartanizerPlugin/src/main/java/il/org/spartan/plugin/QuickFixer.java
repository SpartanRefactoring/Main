package il.org.spartan.plugin;

import static il.org.spartan.plugin.GUIBatchLaconizer.*;

import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** A quickfix generator for spartanization refactoring. Revision: final marker
 * resolutions.
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Ori Roth
 * @since 2013/07/01 */
@SuppressWarnings("unused") public final class QuickFixer implements IMarkerResolutionGenerator {
  @Override public IMarkerResolution[] getResolutions(final IMarker __) {
    return new IMarkerResolution[] { //
        apply, //
        // applyPreview, //
        // laconizeFile, //
        laconizeFunction, //
        laconizeClass, //
        // singleTipperFunction, //
        singleTipperFile, //
        singleTipperProject, //
        disableFunction, //
        disableClass, //
        // disableFile //
    };
  }

  /** Apply spartanization to marked code. */
  private final IMarkerResolution apply = quickFix("Apply",
      ¢ -> new GUIBatchLaconizer().defaultSettings().defaultRunAction(getSpartanizer(¢)).passes(1).selection(Selection.Util.by(¢)).go());
  /** Apply spartanization to marked code with a preview. */
  private final IMarkerResolution applyPreview = quickFix("Apply after preview", ¢ -> {
    final AbstractGUIApplicator g = getSpartanizer(¢);
    final Applicator a = new GUIBatchLaconizer().defaultSettings().passes(1).selection(Selection.Util.by(¢));
    a.setRunAction(u -> {
      try {
        new RefactoringWizardOpenOperation(new Wizard(g)).run(Display.getCurrent().getActiveShell(), "Laconization: " + g);
      } catch (final InterruptedException x) {
        monitor.logCancellationRequest(this, x);
      }
      return Integer.valueOf(0);
    });
    g.setMarker(¢);
    a.go();
  });
  /** Spartanize current file. */
  private final IMarkerResolution laconizeFile = quickFix("Laconize file",
      ¢ -> defaultApplicator().defaultRunAction(getSpartanizer(¢)).defaultPassesMany().selection(Selection.Util.getCurrentCompilationUnit(¢)).go());
  /** Spartanize current function. */
  private final IMarkerResolution laconizeFunction = quickFix("Laconize function", ¢ -> defaultApplicator().defaultRunAction(getSpartanizer(¢))
      .defaultPassesMany().selection(Selection.Util.expand(¢, MethodDeclaration.class)).go());
  /** Spartanize current class. */
  private final IMarkerResolution laconizeClass = quickFix("Laconize class", ¢ -> defaultApplicator().defaultRunAction(getSpartanizer(¢))
      .defaultPassesMany().selection(Selection.Util.expand(¢, AbstractTypeDeclaration.class)).go());
  /** Apply tipper to current function. */
  private final IMarkerResolution singleTipperFunction = quickFix("Apply to enclosing function", ¢ -> defaultApplicator()
      .defaultRunAction(SingleTipper.getApplicator(¢)).defaultPassesMany().selection(Selection.Util.expand(¢, MethodDeclaration.class)).go());
  /** Apply tipper to current file. */
  private final IMarkerResolution singleTipperFile = quickFix("Apply to compilation unit", ¢ -> defaultApplicator()
      .defaultRunAction(SingleTipper.getApplicator(¢)).defaultPassesMany().selection(Selection.Util.getCurrentCompilationUnit(¢)).go());
  /** Apply tipper to entire project. */
  private final IMarkerResolution singleTipperProject = quickFix("Apply to entire project", ¢ -> SpartanizationHandler.applicator()
      .defaultRunAction(SingleTipper.getApplicator(¢)).defaultPassesMany().selection(Selection.Util.getAllCompilationUnit(¢)).go());
  /** Disable spartanization in function. */
  private final IMarkerResolution disableFunction = fixers.disableFunctionFix();
  /** Disable spartanization in class. */
  private final IMarkerResolution disableClass = fixers.disableClassFix();
  /** Disable spartanization in file. */
  private final IMarkerResolution disableFile = fixers.disableFileFix();

  /** Factory method for {@link IMarkerResolution}s.
   * @param name resolution's name
   * @param solution resolution's solution
   * @return marker resolution */
  private static IMarkerResolution quickFix(final String name, final Consumer<IMarker> solution) {
    return new IMarkerResolution() {
      @Override public void run(final IMarker ¢) {
        solution.accept(¢);
      }
      @Override public String getLabel() {
        return name;
      }
    };
  }
  static AbstractGUIApplicator getSpartanizer(final IMarker m) {
    try {
      return Tips.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
    } catch (final CoreException x) {
      monitor.log(x);
    }
    return null;
  }

  /** Single tipper applicator implementation using modified {@link Trimmer}
   * @author Ori Roth
   * @since 2016 */
  private static class SingleTipper<N extends ASTNode> extends Trimmer {
    final Tipper<N> tipper;

    public SingleTipper(final Tipper<N> tipper) {
      this.tipper = tipper;
      name = "Applying " + tipper.myName();
    }
    @Override protected boolean check(final ASTNode ¢) {
      return tipper != null && Toolbox.defaultInstance().get(¢.getNodeType()).contains(tipper);
    }
    @SuppressWarnings("unchecked") @Override protected Tipper<N> getTipper(final ASTNode ¢) {
      assert check(¢);
      return !tipper.canTip((N) ¢) ? null : tipper;
    }
    @SuppressWarnings("unchecked") public static SingleTipper<?> getApplicator(final IMarker ¢) {
      try {
        assert ¢.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY) != null;
        return ¢.getResource() == null ? null : getSingleTipper((Class<? extends Tipper<?>>) ¢.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY));
      } catch (final CoreException x) {
        monitor.log(x);
      }
      return null;
    }
    private static <X extends ASTNode, T extends Tipper<X>> SingleTipper<X> getSingleTipper(final Class<T> t) {
      try {
        return new SingleTipper<>(t.newInstance());
      } catch (InstantiationException | IllegalAccessException x) {
        monitor.log(x);
      }
      return null;
    }
  }

  interface fixers {
    String APPLY_TO_FILE = "Apply to compilation unit";
    String APPLY_TO_FUNCTION = "Apply to enclosing function";
    String APPLY_TO_PROJECT = "Apply to entire project";

    static IMarkerResolution apply(final SingleTipperApplicator.Type t, final String label) {
      return new IMarkerResolution() {
        @Override public String getLabel() {
          return label;
        }
        @Override public void run(final IMarker m) {
          try {
            new SingleTipperApplicator().go(nullProgressMonitor, m, t);
          } catch (IllegalArgumentException | CoreException e) {
            monitor.logEvaluationError(this, e);
          }
        }
      };
    }
    static IMarkerResolution applyFile() {
      return apply(SingleTipperApplicator.Type.FILE, APPLY_TO_FILE);
    }
    static IMarkerResolution applyFunction() {
      return apply(SingleTipperApplicator.Type.DECLARATION, APPLY_TO_FUNCTION);
    }
    static IMarkerResolution applyProject() {
      return apply(SingleTipperApplicator.Type.PROJECT, APPLY_TO_PROJECT);
    }
    static IMarkerResolution disableClassFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.CLASS, "Suppress laconize tips on class");
    }
    static IMarkerResolution disableFileFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.FILE, "Suppress laconize tips on out most class");
    }
    static IMarkerResolution disableFunctionFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.FUNCTION, "Suppress laconize tips on function");
    }
    static IMarkerResolution toggle(final SuppressWarningsLaconicOnOff.Type t, final String label) {
      return new IMarkerResolution() {
        @Override public String getLabel() {
          return label;
        }
        @Override public void run(final IMarker m) {
          try {
            SuppressWarningsLaconicOnOff.deactivate(nullProgressMonitor, m, t);
          } catch (IllegalArgumentException | CoreException x) {
            monitor.logEvaluationError(this, x);
          }
        }
      };
    }
  }
}