package il.org.spartan.plugin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ui.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

/** A quickfix generator for spartanization refactoring
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @since 2013/07/01 */
public final class QuickFixer implements IMarkerResolutionGenerator {
  @Override public IMarkerResolution[] getResolutions(final IMarker m) {
    try {
      final GUI$Applicator $ = Tips.get((String) m.getAttribute(Builder.SPARTANIZATION_TYPE_KEY));
      assert $ != null;
      return new IMarkerResolution[] { //
          $.getFix(), //
          $.getFixWithPreview(), //
          new LaconizeCurrent(), //
          new LaconizeSelection.Enclosure(MethodDeclaration.class, "Laconize function"),
          new LaconizeSelection.Enclosure(TypeDeclaration.class, "Laconize class"), //
          fixers.applyFunction(), //
          fixers.applyFile(), //
          fixers.applyProject(), //
          fixers.disableFunctionFix(), //
          fixers.disableClassFix(), //
          fixers.disableFileFix() };//
    } catch (final CoreException x) {
      LoggingManner.logEvaluationError(this, x);
      return new IMarkerResolution[] {};
    }
  }

  interface fixers {
    String APPLY_TO_FILE = "Apply to compilation unit";
    String APPLY_TO_FUNCTION = "Apply to enclosing function";
    String APPLY_TO_PROJECT = "Apply to entire project";

    static IMarkerResolution apply(final TipperCommit.Type t, final String label) {
      return new IMarkerResolution() {
        @Override public String getLabel() {
          return label;
        }

        @Override public void run(final IMarker m) {
          try {
            new TipperCommit().go(nullProgressMonitor, m, t);
          } catch (IllegalArgumentException | CoreException e) {
            LoggingManner.logEvaluationError(this, e);
          }
        }
      };
    }

    static IMarkerResolution applyFile() {
      return apply(TipperCommit.Type.FILE, APPLY_TO_FILE);
    }

    static IMarkerResolution applyFunction() {
      return apply(TipperCommit.Type.DECLARATION, APPLY_TO_FUNCTION);
    }

    static IMarkerResolution applyProject() {
      return apply(TipperCommit.Type.PROJECT, APPLY_TO_PROJECT);
    }

    static IMarkerResolution disableClassFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.CLASS, "Disable spartanization for class");
    }

    static IMarkerResolution disableFileFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.FILE, "Disable spartanization for file");
    }

    static IMarkerResolution disableFunctionFix() {
      return toggle(SuppressWarningsLaconicOnOff.Type.FUNCTION, "Disable spartanization for scope");
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
            LoggingManner.logEvaluationError(this, x);
          }
        }
      };
    }
  }
}