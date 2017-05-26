package il.org.spartan.spartanizer.plugin;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;

/** Couples together {@link CompilationUnit} and its {@link ICompilationUnit}.
 * @author Ori Roth
 * @since 2016 */
public class WrappedCompilationUnit {
  public ICompilationUnit descriptor;
  public CompilationUnit compilationUnit;
  public String fileName;
  public String filePath;
  public boolean useBinding;

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
  public WrappedCompilationUnit(final CompilationUnit cu, final String fileName, final String absolutePath) {
    this.fileName = fileName;
    filePath = absolutePath;
    compilationUnit = cu;
  }
  public WrappedCompilationUnit build() {
    if (compilationUnit == null)
      try {
        compilationUnit = (CompilationUnit) (!useBinding ? make.COMPILATION_UNIT.parser(descriptor)
            : make.COMPILATION_UNIT.parserWithBinding(descriptor)).createAST(nullProgressMonitor);
      } catch (final Throwable x) {
        note.bug(x);
      }
    return this;
  }
  public WrappedCompilationUnit buildWithBinding() {
    if (compilationUnit == null)
      compilationUnit = (CompilationUnit) make.COMPILATION_UNIT.parserWithBinding(descriptor).createAST(nullProgressMonitor);
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
  public static List<WrappedCompilationUnit> ov(final Collection<CompilationUnit> ¢) {
    return ¢.stream().map(WrappedCompilationUnit::new).collect(toList());
  }
  public static List<WrappedCompilationUnit> of(final Collection<ICompilationUnit> ¢) {
    return ¢.stream().map(WrappedCompilationUnit::new).collect(toList());
  }
  public static WrappedCompilationUnit of(final CompilationUnit from) {
    return new WrappedCompilationUnit(from);
  }
  public static WrappedCompilationUnit of(final CompilationUnit from, final String name, final String absolutePath) {
    return new WrappedCompilationUnit(from, name, absolutePath);
  }
  public String getFileName() {
    return fileName;
  }
  public String getFilePath() {
    return filePath;
  }
}
