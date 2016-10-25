package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** Selection useful to deal with projects using the command line
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSelection extends AbstractSelection<CommandLineSelection> {
  private List<WrappedCompilationUnit> compilationUnits;

  public CommandLineSelection(final List<WrappedCompilationUnit> compilationUnits, final String name) {
//    this.compilationUnits 
    this.inner = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.name = name;
  }

  public List<CompilationUnit> getCompilationUnits() {
    final List<CompilationUnit> $ = new ArrayList<>();
    for (final WrappedCompilationUnit ¢ : inner)
      $.add(¢.compilationUnit);
    return $;
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
    private static String inputPath = "."; // default input path

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
      // final List<WrappedCompilationUnit> cuList = new ArrayList<>();
      // for (final File ¢ : new FilesGenerator(".java").from("."))
      // cuList.add(WrappedCompilationUnit.of((CompilationUnit)
      // makeAST.COMPILATION_UNIT.from(¢)));
      // return new CommandLineSelection(cuList, "default");
      return getFromPath(inputPath);
    }

    /** @return */
    public static AbstractSelection<CommandLineSelection> get(final String from) {
      return getFromPath(from);
    }

    public static AbstractSelection<CommandLineSelection> getFromPath(final String path) {
      final List<WrappedCompilationUnit> cuList = new ArrayList<>();
      for (final File ¢ : new FilesGenerator(".java").from(path))
        cuList.add(WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢)));
      return new CommandLineSelection(cuList, "selection");
    }

    public static List<CompilationUnit> getAllCompilationUnit(String from) {
      List<CompilationUnit> $ = new ArrayList<>();
      for (final File ¢ : new FilesGenerator(".java").from(from)) {
        System.out.println("¢: " + ¢.getAbsolutePath()); // TODO Matteo: remove this line
        System.out.println("Free memory (bytes): " + Unit.BYTES.format(Runtime.getRuntime().freeMemory()));
        CompilationUnit cu;
        if (!system.isTestFile(¢))
          try {
            cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(FileUtils.read(¢));
//            System.out.println("cu: " + cu); // TODO Matteo: remove this line
            $.add(cu);
          } catch (IOException x) {
            monitor.log(x);
            x.printStackTrace();
          }
       }
      System.out.println("$.size(): " + $.size()); // TODO Matteo: remove this line
      return $;
    }
  }

  /** @param inputPath
   * @return */
  public void createSelectionFromProjectDir(final String inputPath) {
    final List<WrappedCompilationUnit> cuList = new ArrayList<>();
    System.err.println("Loading selection ...");
    for (final File ¢ : new FilesGenerator(".java").from(inputPath))
      // System.out.println("Free memory (bytes): " +
      // Unit.BYTES.format(Runtime.getRuntime().freeMemory()));
      cuList.add(WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢)));
//    compilationUnits = cuList;
    inner = cuList;
    System.err.println("Loading selection: done!");
  }

  public CommandLineSelection buildAll() {
    for (final WrappedCompilationUnit ¢ : compilationUnits)
      ¢.build();
    return this;
  }
  // public CU build() {
  // if (compilationUnit == null)
  // compilationUnit = makeAST.COMPILATION_UNIT.from("");
  // return this;
  // }

  public static AbstractSelection<?> of(List<CompilationUnit> ¢) {
    System.out.println("inside CommandLineSelecion.of --> ¢.size(): " + ¢.size()); // TODO Matteo: remove this line
    CommandLineSelection commandLineSelection = new CommandLineSelection(WrappedCompilationUnit.ov(¢), "cuList");
    System.out.println("commandLineSelection.size(): " + commandLineSelection.size()); // TODO Matteo: remove this line
    return commandLineSelection;
  }

  @SuppressWarnings("unused") private static Object getName(List<CompilationUnit> ¢) {
    return null;
  }
}
