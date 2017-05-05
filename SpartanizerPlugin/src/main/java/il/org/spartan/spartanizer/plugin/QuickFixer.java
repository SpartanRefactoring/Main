package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.spartanizer.plugin.NewGUIApplicator.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ui.*;

import fluent.ly.*;
import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.tipping.*;

/** A quickfix generator for spartanization refactoring. Revision: final marker
 * resolutions.
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Ori Roth
 * @since 2013/07/01 */
// XXX OriRoth can we eliminate some of these many fields? --yg
// I want lazy solutions creation, see my solution.
@SuppressWarnings("unused")
public final class QuickFixer implements IMarkerResolutionGenerator {
  private final IMarkerResolution[] solutions = new IMarkerResolution[] { //
      quickFix("Apply Ctrl+5", λ -> NewGUIApplicator.plain().restrictTo(Tipper.materialize(λ)).selection(Selection.Util.by(λ)).go()), //
      // applyPreview, //
      // laconizeFile, //
      quickFix("Spartanize function", λ -> applicator(λ).selection(Selection.Util.expand(λ, MethodDeclaration.class)).go()), //
      quickFix("Spartanize class", λ -> applicator(λ).selection(Selection.Util.expand(λ, AbstractTypeDeclaration.class)).go()), //
      // singleTipperFunction, //
      quickFix("Apply to compilation unit",
          λ -> applicator(λ).restrictTo(Tipper.materialize(λ)).selection(Selection.Util.getCurrentCompilationUnit(λ)).go()), //
      quickFix("Apply to entire project",
          λ -> SpartanizationHandler.applicator().restrictTo(Tipper.materialize(λ)).manyPasses().selection(Selection.Util.getAllCompilationUnit(λ))
              .go()), //
      quickFix("Disable tip", DisableTipper::disable), //
      fixers.disableFunctionFix(), //
      fixers.disableClassFix(), //
      // disableFile, //
      fixers.disableClassAnnotationFix() //
  };

  @Override public IMarkerResolution[] getResolutions(final IMarker __) {
    return Arrays.copyOf(solutions, solutions.length);
  }
  private static NewGUIApplicator applicator(final IMarker λ) {
    return defaultApplicator()//
        .manyPasses();
  }
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
  static GUITraversal getSpartanizer(final IMarker $) {
    return new GUITraversal().setMarker($);
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
            note.bug(this, ¢);
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
            note.bug(this, ¢);
          }
        }
      };
    }
  }
}