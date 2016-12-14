package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** Selection useful to deal with projects using the command line
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSelection extends AbstractSelection<CommandLineSelection> {
  private List<WrappedCompilationUnit> compilationUnits;

  public CommandLineSelection(final List<WrappedCompilationUnit> compilationUnits, final String name) {
    inner = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.name = name;
  }

  public List<CompilationUnit> getCompilationUnits() {
    return inner.stream().map(¢ -> ¢.compilationUnit).collect(Collectors.toList());
  }

  public List<WrappedCompilationUnit> get() {
    return inner;
  }

  /** Factory method for empty selection
   * @return empty selection */
  public static CommandLineSelection empty() {
    return new CommandLineSelection(null, null);
  }

  public static class Util {
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

    /** @return */
    public static AbstractSelection<CommandLineSelection> get() {
      return getFromPath(presentSourcePath);
    }

    /** @return */
    public static AbstractSelection<CommandLineSelection> get(final String from) {
      return getFromPath(from);
    }

    /** @param path
     * @author Matteo Orru'
     * @return */
    public static AbstractSelection<CommandLineSelection> getFromPath(final String path) {
      // final List<WrappedCompilationUnit> cuList = new ArrayList<>();
      // for (final File ¢ : new FilesGenerator(".java").from(path))
      // cuList.add(WrappedCompilationUnit.of((CompilationUnit)
      // makeAST.COMPILATION_UNIT.from(¢), ¢.getName(), ¢.getAbsolutePath()));
      // return new CommandLineSelection(cuList, "selection");
      return getWrappedCompilationUnitsSelection(path);
    }

    /** @param path
     * @author Matteo Orru'
     * @return */
    public static AbstractSelection<CommandLineSelection> getWrappedCompilationUnitsSelection(final String path) {
      final List<WrappedCompilationUnit> $ = new ArrayList<>();
      for (final File ¢ : new FilesGenerator(".java").from(path))
        if (!system.isTestFile(¢))
          $.add(WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢), ¢.getName(), ¢.getAbsolutePath()));
      return new CommandLineSelection($, "selection");
    }

    /** @param path
     * @return */
    public static List<CompilationUnit> getAllCompilationUnits(final String path) {
      final List<CompilationUnit> $ = new ArrayList<>();
      for (final File ¢ : new FilesGenerator(".java").from(path)) {
        System.out.println(¢.getName());
        // System.out.println("Free memory (bytes): " +
        // Unit.BYTES.format(Runtime.getRuntime().freeMemory()));
        if (!system.isTestFile(¢))
          getCompilationUnit(¢, $);
      }
      return $;
    }

    public static void getCompilationUnit(final File f, final List<CompilationUnit> $) {
      try {
        $.add((CompilationUnit) makeAST.COMPILATION_UNIT.from(FileUtils.read(f)));
      } catch (final IOException ¢) {
        monitor.log(¢);
        ¢.printStackTrace();
      }
    }
  }

  /** @param inputFolder
   * @return */
  public void createSelectionFromProjectDir(final String presentSourcePath) {
    final List<WrappedCompilationUnit> cuList = new ArrayList<>();
    System.err.println("Loading selection ...");
    for (final File ¢ : new FilesGenerator(".java").from(presentSourcePath))
      cuList.add(WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢)));
    // compilationUnits = cuList;
    inner = cuList;
    System.err.println("Loading selection: done!");
  }

  public CommandLineSelection buildAll() {
    compilationUnits.forEach(WrappedCompilationUnit::build);
    return this;
  }

  public static AbstractSelection<?> of(final List<CompilationUnit> ¢) {
    return new CommandLineSelection(WrappedCompilationUnit.ov(¢), "cuList");
  }
}