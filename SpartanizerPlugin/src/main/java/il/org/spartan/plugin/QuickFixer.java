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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A quickfix generator for spartanization refactoring. Revision: final marker
 * resolutions.
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Ori Roth
 * @since 2013/07/01 */
@SuppressWarnings("unused")
public final class QuickFixer implements IMarkerResolutionGenerator {
  @NotNull
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
      λ -> new GUIBatchLaconizer().defaultSettings().defaultRunAction(getSpartanizer(λ)).passes(1).selection(Selection.Util.by(λ)).go());
  /** Apply spartanization to marked code with a preview. */
  private final IMarkerResolution applyPreview = quickFix("Apply after preview", ¢ -> {
    final AbstractGUIApplicator g = getSpartanizer(¢);
    final Applicator a = new GUIBatchLaconizer().defaultSettings().passes(1).selection(Selection.Util.by(¢));
    a.setRunAction(u -> {
      try {
        new RefactoringWizardOpenOperation(new Wizard(g)).run(Display.getCurrent().getActiveShell(), "Laconization: " + g);
      } catch (@NotNull final InterruptedException ¢¢) {
        monitor.logCancellationRequest(this, ¢¢);
      }
      return Integer.valueOf(0);
    });
    g.setMarker(¢);
    a.go();
  });
  /** Spartanize current file. */
  private final IMarkerResolution laconizeFile = quickFix("Laconize file",
      λ -> defaultApplicator().defaultRunAction(getSpartanizer(λ)).defaultPassesMany().selection(Selection.Util.getCurrentCompilationUnit(λ)).go());
  /** Spartanize current function. */
  private final IMarkerResolution laconizeFunction = quickFix("Laconize function", λ -> defaultApplicator().defaultRunAction(getSpartanizer(λ))
      .defaultPassesMany().selection(Selection.Util.expand(λ, MethodDeclaration.class)).go());
  /** Spartanize current class. */
  private final IMarkerResolution laconizeClass = quickFix("Laconize class", λ -> defaultApplicator().defaultRunAction(getSpartanizer(λ))
      .defaultPassesMany().selection(Selection.Util.expand(λ, AbstractTypeDeclaration.class)).go());
  /** Apply tipper to current function. */
  private final IMarkerResolution singleTipperFunction = quickFix("Apply to enclosing function", λ -> defaultApplicator()
      .defaultRunAction(SingleTipper.getApplicator(λ)).defaultPassesMany().selection(Selection.Util.expand(λ, MethodDeclaration.class)).go());
  /** Apply tipper to current file. */
  private final IMarkerResolution singleTipperFile = quickFix("Apply to compilation unit", λ -> defaultApplicator()
      .defaultRunAction(SingleTipper.getApplicator(λ)).defaultPassesMany().selection(Selection.Util.getCurrentCompilationUnit(λ)).go());
  /** Apply tipper to entire project. */
  private final IMarkerResolution singleTipperProject = quickFix("Apply to entire project", λ -> SpartanizationHandler.applicator()
      .defaultRunAction(SingleTipper.getApplicator(λ)).defaultPassesMany().selection(Selection.Util.getAllCompilationUnit(λ)).go());
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
  @NotNull
  private static IMarkerResolution quickFix(@NotNull final String name, @NotNull final Consumer<IMarker> solution) {
    return new IMarkerResolution() {
      @Override public void run(final IMarker ¢) {
        solution.accept(¢);
      }

      @NotNull
      @Override public String getLabel() {
        return name;
      }
    };
  }

  static AbstractGUIApplicator getSpartanizer(@NotNull final IMarker $) {
    try {
      return Tips.get((String) $.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
    } catch (@NotNull final CoreException ¢) {
      monitor.log(¢);
    }
    return null;
  }

  /** Single tipper applicator implementation using modified {@link Trimmer}
   * @author Ori Roth
   * @since 2016 */
  private static class SingleTipper<N extends ASTNode> extends Trimmer {
    @NotNull
    final Tipper<N> tipper;

    SingleTipper(@NotNull final Tipper<N> tipper) {
      this.tipper = tipper;
      name = "Applying " + tipper.myName();
    }

    @Override protected boolean check(@NotNull final ASTNode ¢) {
      return tipper != null && Toolbox.defaultInstance().get(¢.getNodeType()).contains(tipper);
    }

    @Nullable
    @Override @SuppressWarnings("unchecked") protected Tipper<N> getTipper(@NotNull final ASTNode ¢) {
      assert check(¢);
      return !tipper.canTip((N) ¢) ? null : tipper;
    }

    @SuppressWarnings("unchecked") public static SingleTipper<?> getApplicator(@NotNull final IMarker $) {
      try {
        assert $.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY) != null;
        return $.getResource() == null ? null : getSingleTipper((Class<? extends Tipper<?>>) $.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY));
      } catch (@NotNull final CoreException ¢) {
        monitor.log(¢);
      }
      return null;
    }

    private static <X extends ASTNode, T extends Tipper<X>> SingleTipper<X> getSingleTipper(@NotNull final Class<T> $) {
      try {
        return new SingleTipper<>($.newInstance());
      } catch (@NotNull InstantiationException | IllegalAccessException ¢) {
        monitor.log(¢);
      }
      return null;
    }
  }

  interface fixers {
    String APPLY_TO_FILE = "Apply to compilation unit";
    String APPLY_TO_FUNCTION = "Apply to enclosing function";
    String APPLY_TO_PROJECT = "Apply to entire project";

    @NotNull
    static IMarkerResolution apply(final SingleTipperApplicator.Type t, @NotNull final String label) {
      return new IMarkerResolution() {
        @NotNull
        @Override public String getLabel() {
          return label;
        }

        @Override public void run(final IMarker m) {
          try {
            new SingleTipperApplicator().go(nullProgressMonitor, m, t);
          } catch (@NotNull IllegalArgumentException | CoreException ¢) {
            monitor.logEvaluationError(this, ¢);
          }
        }
      };
    }

    @NotNull
    static IMarkerResolution applyFile() {
      return apply(SingleTipperApplicator.Type.FILE, APPLY_TO_FILE);
    }

    @NotNull
    static IMarkerResolution applyFunction() {
      return apply(SingleTipperApplicator.Type.DECLARATION, APPLY_TO_FUNCTION);
    }

    @NotNull
    static IMarkerResolution applyProject() {
      return apply(SingleTipperApplicator.Type.PROJECT, APPLY_TO_PROJECT);
    }

    @NotNull
    static IMarkerResolution disableClassFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.CLASS, "Suppress spartanization tips on class");
    }

    @NotNull
    static IMarkerResolution disableFileFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.FILE, "Suppress spartanization tips on out most class");
    }

    @NotNull
    static IMarkerResolution disableFunctionFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.FUNCTION, "Suppress spartanization tips on function");
    }

    @NotNull
    static IMarkerResolution toggle(final SuppressWarningsLaconicOnOff.Type t, @NotNull final String label) {
      return new IMarkerResolution() {
        @NotNull
        @Override public String getLabel() {
          return label;
        }

        @Override public void run(final IMarker m) {
          try {
            SuppressWarningsLaconicOnOff.deactivate(nullProgressMonitor, m, t);
          } catch (@NotNull IllegalArgumentException | CoreException ¢) {
            monitor.logEvaluationError(this, ¢);
          }
        }
      };
    }
  }
}