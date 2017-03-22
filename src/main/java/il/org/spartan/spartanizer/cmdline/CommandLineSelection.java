package il.org.spartan.spartanizer.cmdline;

import static java.util.stream.Collectors.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Selection useful to deal with projects using the command line
 * @author Matteo Orru'
 * @since 2016 */
public final class CommandLineSelection extends AbstractSelection<CommandLineSelection> {
  private List<WrappedCompilationUnit> compilationUnits;

  private CommandLineSelection(@Nullable final List<WrappedCompilationUnit> compilationUnits, final String name) {
    inner = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.name = name;
  }

  public List<CompilationUnit> getCompilationUnits() {
    return inner.stream().map(λ -> λ.compilationUnit).collect(toList());
  }

  @Nullable
  public List<WrappedCompilationUnit> get() {
    return inner;
  }

  /** Factory method for empty selection
   * @return empty selection */
  @Nullable
  public static CommandLineSelection empty() {
    return new CommandLineSelection(null, null);
  }

  public enum Util {
    ;
    private static String presentSourcePath = "."; // default input path

    public static String getPresentSourcePath() {
      return presentSourcePath;
    }

    public static void setPresentSourcePath(final String presentSourcePath) {
      Util.presentSourcePath = presentSourcePath;
    }

    /** @return CommandLineSelection */
    @Nullable
    public static CommandLineSelection getAllCompilationUnits() {
      return getSelection();
    }

    /** @return CommandLineSelection */
    private static CommandLineSelection getSelection() {
      return null;
    }

    @NotNull
    public static AbstractSelection<CommandLineSelection> get() {
      return getFromPath(presentSourcePath);
    }

    @NotNull
    public static AbstractSelection<CommandLineSelection> get(final String from) {
      return getFromPath(from);
    }

    /** @param path
     * @author Matteo Orru'
     * @return */
    @NotNull
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
    @NotNull
    @SuppressWarnings("synthetic-access") public static AbstractSelection<CommandLineSelection> getWrappedCompilationUnitsSelection(
        final String path) {
      return new CommandLineSelection(az.stream(new FilesGenerator(".java").from(path)).filter(λ -> !Utils.isTestFile(λ))
          .map(λ -> WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(λ), λ.getName(), λ.getAbsolutePath()))
          .collect(toList()), "selection");
    }

    @NotNull
    public static Collection<CompilationUnit> getAllCompilationUnits(final String path) {
      @NotNull final Collection<CompilationUnit> $ = new ArrayList<>();
      for (@NotNull final File ¢ : new FilesGenerator(".java").from(path)) {
        System.out.println(¢.getName());
        // System.out.println("Free memory (bytes): " +
        // Unit.BYTES.format(Runtime.getRuntime().freeMemory()));
        if (!Utils.isTestFile(¢))
          getCompilationUnit(¢, $);
      }
      return $;
    }

    public static void getCompilationUnit(@NotNull final File f, @NotNull final Collection<CompilationUnit> $) {
      try {
        $.add((CompilationUnit) makeAST.COMPILATION_UNIT.from(FileUtils.read(f)));
      } catch (@NotNull final IOException ¢) {
        monitor.infoIOException(¢);
      }
    }
  }

  public void createSelectionFromProjectDir(final String presentSourcePath) {
    System.err.println("Loading selection ...");
    // compilationUnits = cuList;
    inner = new ArrayList<>(az.stream(new FilesGenerator(".java").from(presentSourcePath))
        .map(λ -> WrappedCompilationUnit.of(az.compilationUnit(makeAST.COMPILATION_UNIT.from(λ)))).collect(toList()));
    System.err.println("Loading selection: done!");
  }

  @NotNull
  public CommandLineSelection buildAll() {
    compilationUnits.forEach(WrappedCompilationUnit::build);
    return this;
  }

  @NotNull
  public static AbstractSelection<?> of(@NotNull final Collection<CompilationUnit> ¢) {
    return new CommandLineSelection(WrappedCompilationUnit.ov(¢), "cuList");
  }
}