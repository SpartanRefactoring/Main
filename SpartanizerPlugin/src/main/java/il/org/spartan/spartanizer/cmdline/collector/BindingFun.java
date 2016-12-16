package il.org.spartan.spartanizer.cmdline.collector;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** An {@link IApplication} extension entry point, allowing execution of this
 * plug-in from the command line.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19 */
public final class BindingFun implements IApplication {
  /** Count the number of lines in a {@link File} f
   * @param ¢ File
   * @return
   * @throws IOException */
  static int countLines(final File ¢) throws IOException {
    try (LineNumberReader $ = new LineNumberReader(new FileReader(¢))) {
      $.skip(Long.MAX_VALUE);
      return $.getLineNumber();
    }
  }

  /** Count the number of lines in File named filename
   * @param fileName
   * @return
   * @throws IOException */
  static int countLines(final String fileName) throws IOException {
    return countLines(new File(fileName));
  }

  static MethodInvocation getMethodInvocation(final CompilationUnit u, final int lineNumber, final MethodInvocation i) {
    final Wrapper<MethodInvocation> $ = new Wrapper<>();
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodInvocation ¢) {
        if (u.getLineNumber(¢.getStartPosition()) == lineNumber)
          $.set(¢);
        return super.visit(¢);
      }
    });
    return $.get() == null ? i : $.get();
  }

  static String getPackageNameFromSource(final String source) {
    final ASTParser $ = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    $.setResolveBindings(true);
    $.setSource(source.toCharArray());
    return getPackageNameFromSource(new Wrapper<>(""), $.createAST(null));
  }

  private static String getPackageNameFromSource(final Wrapper<String> $, final ASTNode n) {
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration ¢) {
        $.set(¢.getName() + "");
        return false;
      }
    });
    return $.get();
  }

  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;
  boolean optDoNotOverwrite, optIndividualStatistics, optVerbose;
  boolean optStatsLines, optStatsChanges, printLog;
  int optRounds = 20;

  @Override public Object start(final IApplicationContext arg0) {
    ___.unused(arg0);
    final List<FileStats> fileStats = new ArrayList<>();
    try {
      prepareTempIJavaProject();
    } catch (final CoreException ¢) {
      System.err.println(¢.getMessage());
      return IApplication.EXIT_OK;
    }
    int done = 0, failed = 0;
    for (final File f : new FilesGenerator(".java", ".JAVA").from("C:\\Users\\sorimar\\git\\guava\\guava")) {
      ICompilationUnit u = null;
      try {
        u = openCompilationUnit(f);
        final FileStats s = new FileStats(f);
        for (int i = 0; i < optRounds; ++i) {
          final int n = new LaconizeProject().countTips();
          if (n == 0)
            break;
          s.addRoundStat(n);
          new Trimmer().apply(u);
        }
        FileUtils.writeToFile(determineOutputFilename(f.getAbsolutePath()), u.getSource());
        if (optVerbose)
          System.out.println("Spartanized file " + f.getAbsolutePath());
        s.countLinesAfter();
        fileStats.add(s);
        ++done;
      } catch (final JavaModelException | IOException ¢) {
        System.err.println(f + ": " + ¢.getMessage());
        ++failed;
      } catch (final Exception ¢) {
        System.err.println("An unexpected error has occurred on file " + f + ": " + ¢.getMessage());
        ¢.printStackTrace();
        ++failed;
      } finally {
        discardCompilationUnit(u);
      }
    }
    System.out.println(done + " files processed. " + (failed == 0 ? "" : failed + " failed."));
    if (optStatsChanges)
      printChangeStatistics(fileStats);
    if (optStatsLines)
      printLineStatistics(fileStats);
    return IApplication.EXIT_OK;
  }

  @Override public void stop() {
    ___.nothing();
  }

  String determineOutputFilename(final String path) {
    return !optDoNotOverwrite ? path : path.substring(0, path.lastIndexOf('.')) + "__new.java";
  }

  /** Discard compilation unit u
   * @param u */
  void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final NullPointerException | JavaModelException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
  }

  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException ¢) {
      ¢.printStackTrace();
    }
  }

  ICompilationUnit openCompilationUnit(final File ¢) throws IOException, JavaModelException {
    final String $ = FileUtils.read(¢);
    setPackage(getPackageNameFromSource($));
    return pack.createCompilationUnit(¢.getName(), $, false, null);
  }

  void prepareTempIJavaProject() throws CoreException {
    final IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("tempP");
    if (p.exists())
      p.delete(true, null);
    p.create(null);
    p.open(null);
    final IProjectDescription d = p.getDescription();
    d.setNatureIds(new String[] { JavaCore.NATURE_ID });
    p.setDescription(d, null);
    javaProject = JavaCore.create(p);
    final IFolder binFolder = p.getFolder("bin");
    final IFolder sourceFolder = p.getFolder("src");
    srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
    binFolder.create(false, true, null);
    sourceFolder.create(false, true, null);
    javaProject.setOutputLocation(binFolder.getFullPath(), null);
    final IClasspathEntry[] buildPath = new IClasspathEntry[1];
    buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
    javaProject.setRawClasspath(buildPath, null);
  }

  void printLineStatistics(final List<FileStats> ss) {
    System.out.println("\nLine differences:");
    if (optIndividualStatistics)
      for (final FileStats ¢ : ss) {
        System.out.println("\n  " + ¢.fileName());
        System.out.println("    Lines before: " + ¢.getLinesBefore());
        System.out.println("    Lines after: " + ¢.getLinesAfter());
      }
    else {
      int totalBefore = 0, totalAfter = 0;
      for (final FileStats ¢ : ss) {
        totalBefore += ¢.getLinesBefore();
        totalAfter += ¢.getLinesAfter();
      }
      System.out.println("  Lines before: " + totalBefore);
      System.out.println("  Lines after: " + totalAfter);
    }
  }

  void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }

  private void printChangeStatistics(final List<FileStats> ss) {
    System.out.println("\nTotal changes made: ");
    if (optIndividualStatistics)
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.fileName());
        for (int ¢ = 0; ¢ < optRounds; ++¢)
          System.out.println("    Round #" + ¢ + 1 + ": " + (¢ < 9 ? " " : "") + f.getRoundStat(¢));
      }
    else
      for (int i = 0; i < optRounds; ++i) {
        int roundSum = 0;
        for (final FileStats ¢ : ss)
          roundSum += ¢.getRoundStat(i);
        System.out.println("    Round #" + i + 1 + ": " + (i < 9 ? " " : "") + roundSum);
      }
  }

  /** Data structure designed to hold and compute information about a single
   * file, in order to produce statistics when completed execution */
  private class FileStats {
    final File file;
    final int linesBefore;
    int linesAfter;
    final List<Integer> roundStats = new ArrayList<>();

    public FileStats(final File file) throws IOException {
      linesBefore = countLines(this.file = file);
    }

    public void addRoundStat(final int ¢) {
      roundStats.add(Integer.valueOf(¢));
    }

    public void countLinesAfter() throws IOException {
      linesAfter = countLines(determineOutputFilename(file.getAbsolutePath()));
    }

    public String fileName() {
      return file.getName();
    }

    public int getLinesAfter() {
      return linesAfter;
    }

    public int getLinesBefore() {
      return linesBefore;
    }

    public int getRoundStat(final int $) {
      try {
        return roundStats.get($).intValue();
      } catch (final IndexOutOfBoundsException ¢) {
        ¢.printStackTrace();
        return 0;
      }
    }
  }
}
