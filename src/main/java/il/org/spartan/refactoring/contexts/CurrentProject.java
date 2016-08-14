package il.org.spartan.refactoring.contexts;


import static il.org.spartan.refactoring.suggestions.DialogBoxes.*;

import java.util.*;

import org.eclipse.jdt.core.*;

import static il.org.spartan.lazy.Cookbook.*;

import il.org.spartan.*;

/** Provides list of all {@link ICompilationUnit} in this Workbench
 * @author Yossi Gil
 * @since 2016` */
public class CurrentProject extends CurrentCompilationUnit.Environment {
  /** instantiates this class
   * @param current the containing context */
  public CurrentProject(CurrentCompilationUnit current) {
    current.super();
  }
  /** @return contents of the underlying cell; may trigger computation */
  public List<ICompilationUnit> compilationUnits() {
    return compilationUnits.get();
  }
  List<ICompilationUnit> collect() throws JavaModelException {
    work();
    if (parent.compilationUnit() == null)
      return announce("Cannot find current compilation unit ");
    work();
    if (parent.javaProject() == null)
      return announce("Cannot find project of " +parent.compilationUnit());
    work();
    if (packageFragmentRoots() == null)
      return announce("Cannot find roots of " +parent.javaProject());
    final List<ICompilationUnit> $ = new ArrayList<>();
    for (final IPackageFragmentRoot r : packageFragmentRoots()) {
      work();
      if (r.getKind() != IPackageFragmentRoot.K_SOURCE)
        continue;
      work();
      for (final IJavaElement e : r.getChildren()) {
        work();
        if (e.getElementType() != IJavaElement.PACKAGE_FRAGMENT)
          continue;
        work();
        $.addAll(Arrays.asList(((IPackageFragment) e).getCompilationUnits()));
        work();
      }
      work();
    }
    work();
    return $;
  }

  /** @return contents of the underlying cell; may trigger computation */
  final IPackageFragmentRoot[] packageFragmentRoots() {
    return packageFragmentRoots.get();
  }
  /** Direct access to the underlying cell */
  final Property<List<ICompilationUnit>> compilationUnits = function(parent.compilationUnit).make(//
      () -> {
        begin("Collecting all project's compilation units...", 1);
        final List<ICompilationUnit> $ = idiomatic.<List<ICompilationUnit>>katching(() -> collect());
        end();
        return $;
      });

  /** Direct access to the underlying cell */
  final Property<IPackageFragmentRoot[]> packageFragmentRoots = from(parent.javaProject).make(//
      ()-> idiomatic.<IPackageFragmentRoot[]>  katching(() -> context.javaProject().getPackageFragmentRoots()));
}
