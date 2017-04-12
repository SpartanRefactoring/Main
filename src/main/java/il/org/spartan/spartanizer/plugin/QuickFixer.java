package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.spartanizer.plugin.BatchApplicator.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A quickfix generator for spartanization refactoring. Revision: final marker
 * resolutions.
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Ori Roth
 * @since 2013/07/01 */
// TODO OriRoth can we eliminate some of these many fields? --yg
@SuppressWarnings("unused")
public final class QuickFixer implements IMarkerResolutionGenerator {
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
        disableTipper, //
        disableFunction, //
        disableClass, //
        // disableFile, //
        disableClassByAnnotation //
    };
  }

  private static BatchApplicator applicator(final IMarker λ) {
    return defaultApplicator()//
        .defaultRunAction(getSpartanizer(λ))//
        .defaultPassesMany();
  }

  /** Apply spartanization to marked code. */
  private final IMarkerResolution apply = quickFix("Apply",
      λ -> new BatchApplicator().defaultSettings().defaultRunAction(getSpartanizer(λ)).passes(1).selection(Selection.Util.by(λ)).go());
  /** Apply spartanization to marked code with a preview. */
  private final IMarkerResolution applyPreview = quickFix("Apply after preview", ¢ -> {
    final AbstractGUIApplicator g = getSpartanizer(¢);
    final Applicator a = new BatchApplicator().defaultSettings().passes(1).selection(Selection.Util.by(¢));
    a.setRunAction(u -> {
      try {
        new RefactoringWizardOpenOperation(new Wizard(g)).run(Display.getCurrent().getActiveShell(), "Spartanization: " + g);
      } catch (final InterruptedException ¢¢) {
        monitor.cancel(this, ¢¢);
      }
      return Integer.valueOf(0);
    });
    g.setMarker(¢);
    a.go();
  });
  /** Spartanize current file. */
  private final IMarkerResolution laconizeFile = quickFix("Spartanize file",
      λ -> applicator(λ).selection(Selection.Util.getCurrentCompilationUnit(λ)).go());
  /** Spartanize current function. */
  private final IMarkerResolution laconizeFunction = quickFix("Spartanize function",
      λ -> applicator(λ).selection(Selection.Util.expand(λ, MethodDeclaration.class)).go());
  /** Spartanize current class. */
  private final IMarkerResolution laconizeClass = quickFix("Spartanize class",
      λ -> applicator(λ).selection(Selection.Util.expand(λ, AbstractTypeDeclaration.class)).go());
  /** Apply tipper to current function. */
  private final IMarkerResolution singleTipperFunction = quickFix("Apply to enclosing function", λ -> applicator(λ)
      .defaultRunAction(SingleTipper.getApplicator(λ)).defaultPassesMany().selection(Selection.Util.expand(λ, MethodDeclaration.class)).go());
  /** Apply tipper to current file. */
  private final IMarkerResolution singleTipperFile = quickFix("Apply to compilation unit",
      λ -> applicator(λ).defaultRunAction(SingleTipper.getApplicator(λ)).selection(Selection.Util.getCurrentCompilationUnit(λ)).go());
  /** Apply tipper to entire project. */
  private final IMarkerResolution singleTipperProject = quickFix("Apply to entire project", λ -> SpartanizationHandler.applicator()
      .defaultRunAction(SingleTipper.getApplicator(λ)).defaultPassesMany().selection(Selection.Util.getAllCompilationUnit(λ)).go());
  /** Disable spartanization in function. */
  private final IMarkerResolution disableFunction = fixers.disableFunctionFix();
  /** Disable spartanization in class. */
  private final IMarkerResolution disableClass = fixers.disableClassFix();
  /** Disable spartanization in file. */
  private final IMarkerResolution disableFile = fixers.disableFileFix();
  /** Disables the tipper of this marker. */
  private final IMarkerResolution disableTipper = quickFix("Disable tip", DisableTipper::disable);
  /** Disable spartanization in class by annotation. */
  private final IMarkerResolution disableClassByAnnotation = fixers.disableClassAnnotationFix();

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

  static AbstractGUIApplicator getSpartanizer(final IMarker $) {
    try {
      return DefunctTips.get((String) $.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
    } catch (final CoreException ¢) {
      return monitor.bug(¢);
    }
  }

  /** Single tipper applicator implementation using modified {@link Trimmer}
   * @author Ori Roth
   * @since 2016 */
  private static class SingleTipper<N extends ASTNode> extends Trimmer {
    final Tipper<N> tipper;

    SingleTipper(final Tipper<N> tipper) {
      this.tipper = tipper;
      name = "Applying " + tipper.technicalName();
    }

    @Override @SuppressWarnings("unchecked") protected <N1 extends ASTNode> Tipper<N1> findTipper(final N1 ¢) {
      assert check(¢);
      return !tipper.check((N) ¢) ? null : (Tipper<N1>) tipper;
    }

    @Override protected boolean check(final ASTNode ¢) {
      return tipper != null && Configurations.all().get(¢.getNodeType()).contains(tipper);
    }

    @SuppressWarnings("unchecked") public static SingleTipper<?> getApplicator(final IMarker $) {
      try {
        assert $.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY) != null;
        return $.getResource() == null ? null : getSingleTipper((Class<? extends Tipper<?>>) $.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY));
      } catch (final CoreException ¢) {
        monitor.bug(¢);
      }
      return null;
    }

    private static <X extends ASTNode, T extends Tipper<X>> SingleTipper<X> getSingleTipper(final Class<T> $) {
      try {
        return new SingleTipper<>($.newInstance());
      } catch (InstantiationException | IllegalAccessException ¢) {
        monitor.bug(¢);
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
          } catch (IllegalArgumentException | CoreException ¢) {
            monitor.bug(this, ¢);
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
      return toggle(SuppressWarningsOnOff.ByComment, SuppressWarningsOnOff.Type.CLASS, "Suppress spartanization tips on class");
    }

    static IMarkerResolution disableFileFix() {
      return toggle(SuppressWarningsOnOff.ByComment, SuppressWarningsOnOff.Type.FILE, "Suppress spartanization tips on out most class");
    }

    static IMarkerResolution disableFunctionFix() {
      return toggle(SuppressWarningsOnOff.ByComment, SuppressWarningsOnOff.Type.FUNCTION, "Suppress spartanization tips on function");
    }

    static IMarkerResolution disableClassAnnotationFix() {
      return toggle(SuppressWarningsOnOff.ByAnnotation, SuppressWarningsOnOff.Type.CLASS, "Class under construction");
    }

    static IMarkerResolution toggle(final SuppressWarningsOnOff disabler, final SuppressWarningsOnOff.Type t, final String label) {
      return new IMarkerResolution() {
        @Override public String getLabel() {
          return label;
        }

        @Override public void run(final IMarker m) {
          try {
            disabler.deactivate(nullProgressMonitor, m, t);
          } catch (IllegalArgumentException | CoreException ¢) {
            monitor.bug(this, ¢);
          }
        }
      };
    }
  }
}