package il.org.spartan.spartanizer.cmdline;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import an.empty;
import fluent.ly.as;
import fluent.ly.note;
import il.org.spartan.collections.FilesGenerator;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.cmdline.library.FileHeuristics;
import il.org.spartan.spartanizer.plugin.AbstractSelection;
import il.org.spartan.spartanizer.plugin.WrappedCompilationUnit;
import il.org.spartan.utils.FileUtils;

/** Selection useful to deal with projects using the command line
 * @author Matteo Orru'
 * @since 2016 */
public final class CommandLineSelection extends AbstractSelection<CommandLineSelection> {
  private List<WrappedCompilationUnit> compilationUnits;

  private CommandLineSelection(final List<WrappedCompilationUnit> compilationUnits, final String name) {
    inner = compilationUnits == null ? empty.list() : compilationUnits;
    this.name = name;
  }
  public List<CompilationUnit> getCompilationUnits() {
    return inner.stream().map(λ -> λ.compilationUnit).collect(toList());
  }
  public List<WrappedCompilationUnit> get() {
    return inner;
  }
  /** Factory method for empty selection
   * @return empty selection */
  public static CommandLineSelection empty() {
    return new CommandLineSelection(null, null);
  }

  public enum Util {
    DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
    private static String presentSourcePath = "."; // default input path

    public static String getPresentSourcePath() {
      return presentSourcePath;
    }
    public static void setPresentSourcePath(final String presentSourcePath) {
      Util.presentSourcePath = presentSourcePath;
    }
    /** @return CommandLineSelection */
    public static CommandLineSelection getAllCompilationUnits() {
      return getSelection();
    }
    /** @return CommandLineSelection */
    private static CommandLineSelection getSelection() {
      return null;
    }
    public static AbstractSelection<CommandLineSelection> get() {
      return getFromPath(presentSourcePath);
    }
    public static AbstractSelection<CommandLineSelection> get(final String from) {
      return getFromPath(from);
    }
    /** @param path
     * @author Matteo Orru'
     */
    public static AbstractSelection<CommandLineSelection> getFromPath(final String path) {
      // final List<WrappedCompilationUnit> cuList = an.empty.list();
      // for (final File ¢ : new FilesGenerator(".java").from(path))
      // cuList.add(WrappedCompilationUnit.of((CompilationUnit)
      // makeAST.COMPILATION_UNIT.from(¢), ¢.getName(), ¢.getAbsolutePath()));
      // return new CommandLineSelection(cuList, "selection");
      return getWrappedCompilationUnitsSelection(path);
    }
    /** @param path
     * @author Matteo Orru'
     */
     public static AbstractSelection<CommandLineSelection> getWrappedCompilationUnitsSelection(
        final String path) {
      return new CommandLineSelection(az.stream(new FilesGenerator(".java").from(path)).filter(λ -> !FileHeuristics.isTestFile(λ))
          .map(λ -> WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(λ), λ.getName(), λ.getAbsolutePath()))
          .collect(toList()), "selection");
    }
    public static Collection<CompilationUnit> getAllCompilationUnits(final String path) {
      final Collection<CompilationUnit> $ = an.empty.list();
      for (final File ¢ : new FilesGenerator(".java").from(path)) {
        System.out.println(¢.getName());
        // System.out.println("Free memory (bytes): " +
        // Unit.BYTES.format(Runtime.getRuntime().freeMemory()));
        if (!FileHeuristics.isTestFile(¢))
          getCompilationUnit(¢, $);
      }
      return $;
    }
    public static void getCompilationUnit(final File f, final Collection<CompilationUnit> $) {
      try {
        $.add((CompilationUnit) makeAST.COMPILATION_UNIT.from(FileUtils.read(f)));
      } catch (final IOException ¢) {
        note.io(¢);
      }
    }
  }

  public void createSelectionFromProjectDir(final String presentSourcePath) {
    System.err.println("Loading selection ...");
    // compilationUnits = cuList;
    inner = as.list(az.stream(new FilesGenerator(".java").from(presentSourcePath))
        .map(λ -> WrappedCompilationUnit.of(az.compilationUnit(makeAST.COMPILATION_UNIT.from(λ)))).collect(toList()));
    System.err.println("Loading selection: done!");
  }
  public CommandLineSelection buildAll() {
    compilationUnits.forEach(WrappedCompilationUnit::build);
    return this;
  }
  public static AbstractSelection<?> of(final Collection<CompilationUnit> ¢) {
    return new CommandLineSelection(WrappedCompilationUnit.ov(¢), "cuList");
  }
}