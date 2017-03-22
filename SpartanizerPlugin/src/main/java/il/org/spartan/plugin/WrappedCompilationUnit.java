package il.org.spartan.plugin;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Couples together {@link CompilationUnit} and its {@link ICompilationUnit}.
 * @author Ori Roth
 * @since 2016 */
public class WrappedCompilationUnit {
  public ICompilationUnit descriptor;
  @Nullable
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

  @NotNull
  public WrappedCompilationUnit build() {
    if (compilationUnit == null)
      try {
        compilationUnit = (CompilationUnit) (!useBinding ? make.COMPILATION_UNIT.parser(descriptor)
            : make.COMPILATION_UNIT.parserWithBinding(descriptor)).createAST(nullProgressMonitor);
      } catch (@NotNull final Throwable x) {
        monitor.log(x);
      }
    return this;
  }

  @NotNull
  public WrappedCompilationUnit buildWithBinding() {
    if (compilationUnit == null)
      compilationUnit = (CompilationUnit) make.COMPILATION_UNIT.parserWithBinding(descriptor).createAST(nullProgressMonitor);
    return this;
  }

  @NotNull
  public WrappedCompilationUnit dispose() {
    compilationUnit = null;
    return this;
  }

  @Nullable
  public String name() {
    return descriptor == null ? null : descriptor.getElementName();
  }

  /** Factory method
   * @param ¢ JD
   * @return an instance created by the parameter */
  @NotNull
  public static WrappedCompilationUnit of(final ICompilationUnit ¢) {
    return new WrappedCompilationUnit(¢);
  }

  /** Factory method that takes a list of CompilationUnit
   * @author matteo
   * @param ¢ JD
   * @return an instance created by the parameter */
  public static List<WrappedCompilationUnit> ov(@NotNull final Collection<CompilationUnit> ¢) {
    return ¢.stream().map(WrappedCompilationUnit::new).collect(toList());
  }

  public static List<WrappedCompilationUnit> of(@NotNull final Collection<ICompilationUnit> ¢) {
    return ¢.stream().map(WrappedCompilationUnit::new).collect(toList());
  }

  @NotNull
  public static WrappedCompilationUnit of(final CompilationUnit from) {
    return new WrappedCompilationUnit(from);
  }

  @NotNull
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
