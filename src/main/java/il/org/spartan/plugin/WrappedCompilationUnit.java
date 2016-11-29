package il.org.spartan.plugin;

import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.engine.*;

/** Couples together {@link CompilationUnit} and its {@link ICompilationUnit}.
 * @author Ori Roth
 * @since 2016 */
public class WrappedCompilationUnit {
  public ICompilationUnit descriptor;
  public CompilationUnit compilationUnit;

  /** Instantiates this class
   * @param compilationUnit JD */
  public WrappedCompilationUnit(final ICompilationUnit compilationUnit) {
    descriptor = compilationUnit;
  }

  /** Instantiates this class with a Compilation Unit (useful for command line
   * applicator
   * @author Matteo Orru'
   * @param compilationUnit JD */
  public WrappedCompilationUnit(final CompilationUnit cu) {
    compilationUnit = cu;
  }

  public WrappedCompilationUnit build() {
    if (compilationUnit == null)
      compilationUnit = (CompilationUnit) Make.COMPILATION_UNIT.parser(descriptor).createAST(nullProgressMonitor);
    return this;
  }

  public WrappedCompilationUnit dispose() {
    compilationUnit = null;
    return this;
  }

  public String name() {
    return descriptor == null ? null : descriptor.getElementName();
  }

  /** Factory method
   * @param ¢ JD
   * @return an instance created by the parameter */
  public static WrappedCompilationUnit of(final ICompilationUnit ¢) {
    return new WrappedCompilationUnit(¢);
  }

  /** Factory method that takes a list of CompilationUnit
   * @author matteo
   * @param ¢ JD
   * @return an instance created by the parameter */
  /**  */
  public static List<WrappedCompilationUnit> ov(final List<CompilationUnit> ¢) {
    return ¢.stream().map(WrappedCompilationUnit::new).collect(Collectors.toList());
  }

  /**  */
  public static List<WrappedCompilationUnit> of(final List<ICompilationUnit> ¢) {
    return ¢.stream().map(WrappedCompilationUnit::new).collect(Collectors.toList());
  }

  /** @param from
   * @return */
  public static WrappedCompilationUnit of(final CompilationUnit from) {
    return new WrappedCompilationUnit(from);
  }
}
