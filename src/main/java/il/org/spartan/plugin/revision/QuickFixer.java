package il.org.spartan.plugin.revision;

import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.plugin.*;

/** A quickfix generator for spartanization refactoring. Revision: final marker
 * resolutions, configuration on the spot.
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Ori Roth
 * @since 2013/07/01 */
public final class QuickFixer implements IMarkerResolutionGenerator {
  @Override public IMarkerResolution[] getResolutions(final IMarker m) {
    try {
      final GUI$Applicator $ = Tips.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
      assert $ != null;
      return new IMarkerResolution[] { //
          apply, //
          applyPreview, //
          laconizeFile, //
          new LaconizeSelection.Enclosure(MethodDeclaration.class, "Laconize function"),
          new LaconizeSelection.Enclosure(TypeDeclaration.class, "Laconize class"), //
          SingleTipper.InDeclaration.instance(), //
          SingleTipper.InFile.instance(), //
          SingleTipper.InProject.instance(), //
          fixers.disableFunctionFix(), //
          fixers.disableClassFix(), //
          fixers.disableFileFix() };//
    } catch (final CoreException x) {
      monitor.logEvaluationError(this, x);
      return new IMarkerResolution[] {};
    }
  }

  /** Apply spartanization to current marker. */
  private final IMarkerResolution apply = quickFix("Apply",
      ¢ -> EventApplicator.defaultApplicator().defaultRunAction(getSpartanizer(¢)).passes(1).selection(Selection.Util.by(¢)).go());
  /** Apply spartanization to current marker after refactoring preview. */
  private final IMarkerResolution applyPreview = quickFix("Apply after preview", ¢ -> {
    final GUI$Applicator g = getSpartanizer(¢);
    final Applicator<?> a = EventApplicator.defaultApplicator().passes(1).selection(Selection.Util.by(¢));
    a.runAction(u -> {
      try {
        new RefactoringWizardOpenOperation(new Wizard(g)).run(Display.getCurrent().getActiveShell(), "Laconization: " + g);
      } catch (final InterruptedException x) {
        monitor.logCancellationRequest(this, x);
      }
      return Boolean.FALSE;
    });
    g.setMarker(¢);
    a.go();
  });
  /** Fully spartanize current compilation unit. */
  private final IMarkerResolution laconizeFile = quickFix("Laconize file", ¢ -> EventApplicator.defaultApplicator().defaultPassesMany()
      .defaultRunAction(getSpartanizer(¢)).selection(Selection.Util.getCurrentCompilationUnit(¢)).go());

  /** Factory method for {@link IMarkerResolution}.
   * @param name resolution name
   * @param fix resolution fix
   * @return resolution */
  private static IMarkerResolution quickFix(final String name, final Consumer<IMarker> fix) {
    return new IMarkerResolution() {
      @Override public void run(IMarker ¢) {
        fix.accept(¢);
      }

      @Override public String getLabel() {
        return name;
      }
    };
  }

  static GUI$Applicator getSpartanizer(IMarker m) {
    try {
      return Tips.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
    } catch (CoreException x) {
      monitor.log(x);
    }
    return null;
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