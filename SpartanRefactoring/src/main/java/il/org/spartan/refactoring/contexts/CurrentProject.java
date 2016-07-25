package il.org.spartan.refactoring.contexts;


import static il.org.spartan.lazy.Cookbook.from;

import il.org.spartan.*;

import java.util.*;

import org.eclipse.jdt.core.*;

import static il.org.spartan.refactoring.suggestions.DialogBoxes.*;

/** Provides list of all {@link ICompilationUnit} in this Workbench
 * @author Yossi Gil
 * @since 2016` */
public class CurrentProject extends CurrentCompilationUnit.Context {
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
    if (context.compilationUnit() == null)
      return announce("Cannot find current compilation unit ");
    work();
    if (context.javaProject() == null)
      return announce("Cannot find project of " + context.compilationUnit());
    work();
    if (packageFragmentRoots() == null)
      return announce("Cannot find roots of " + context.javaProject());
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
  final Cell<List<ICompilationUnit>> compilationUnits = from(context.compilationUnit).make(//
      () -> {
        begin("Collecting all project's compilation units...", 1);
        final List<ICompilationUnit> $ = idiomatic.<List<ICompilationUnit>>katching(() -> collect());
        end();
        return $;
      });

  /** Direct access to the underlying cell */
  final Cell<IPackageFragmentRoot[]> packageFragmentRoots = from(context.javaProject).make(//
      ()-> idiomatic.<IPackageFragmentRoot[]>  katching(() -> context.javaProject().getPackageFragmentRoots()));
}
