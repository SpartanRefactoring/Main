package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

/** An {@link IApplication} extension entry point, allowing execution of this
 * plug-in from the command line.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19 */
public final class Application implements IApplication {
  /** Count the number of lines in a {@link File} f
   * @param ¢ File
   * @return
   * @throws IOException */
  static int countLines(final File ¢) throws IOException {
    try (LineNumberReader ret = new LineNumberReader(new FileReader(¢))) {
      ret.skip(Long.MAX_VALUE);
      return ret.getLineNumber();
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
    final Wrapper<MethodInvocation> ret = new Wrapper<>();
    u.accept(new ASTVisitor(true) {
      @Override public boolean visit(final MethodInvocation ¢) {
        if (u.getLineNumber(¢.getStartPosition()) == lineNumber)
          ret.set(¢);
        return super.visit(¢);
      }
    });
    return ret.get() == null ? i : ret.get();
  }
  private static String getPackageNameFromSource(final String source) {
    final ASTParser ret = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    ret.setSource(source.toCharArray());
    return getPackageNameFromSource(new Wrapper<>(""), ret.createAST(null));
  }
  private static void printHelpPrompt() {
    System.out.println("Spartan Refactoring plugin command line");
    System.out.println("Usage: eclipse -application il.org.spartan.spartanizer.application -nosplash [OPTIONS] PATH");
    System.out.println("Executes the Spartan Refactoring Eclipse plug-in from the command line on all the Java source files "
        + "within the given PATH. Files are spartanized in place by default.");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -N       Do not overwrite existing files (writes the Spartanized output to a new file in the same directory)");
    System.out.println("  -C<num>  Maximum number of Spartanizaion rounds for each file (default: 20)");
    System.out.println("  -E       Display statistics for each file separately");
    System.out.println("  -V       Be verbose");
    System.out.println("  -L       printout logs");
    System.out.println("");
    System.out.println("Print statistics:");
    System.out.println("  -l       Show the number of lines before and after spartanization");
    System.out.println("  -r       Show the number of Spartanizaion made in each round");
    System.out.println("");
    System.out.println("Output:");
    System.out.println("  -logPath Output dir for logs");
    System.out.println("");
  }
  private static String getPackageNameFromSource(final Wrapper<String> $, final ASTNode n) {
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final PackageDeclaration ¢) {
        $.set(¢.getName() + "");
        return false;
      }
    });
    return $.get();
  }

  private IJavaProject javaProject;
  private IPackageFragmentRoot srcRoot;
  private IPackageFragment pack;
  private boolean optOverwrite = true;
  private boolean optIndividualStatistics;
  private boolean optVerbose;
  private boolean optStatsLines;
  private boolean optStatsChanges;
  private int optRounds = 30;
  private String optPath;

  @Override public Object start(final IApplicationContext arg0) {
    final Object ret = startInner(arg0);
    try (Scanner s = new Scanner(System.in)) {
      try {
        s.nextLine();
      } catch (final Exception ¢) {
        note.bug(¢);
      }
    }
    return ret;
  }
  @SuppressWarnings("OverlyComplexMethod") public Object startInner(final IApplicationContext arg0) {
    if (parseArguments(as.list((String[]) arg0.getArguments().get(IApplicationContext.APPLICATION_ARGS))))
      return IApplication.EXIT_OK;
    try {
      prepareTempIJavaProject();
    } catch (final CoreException ¢) {
      note.bug(¢);
      return IApplication.EXIT_OK;
    }
    int done = 0, failed = 0;
    final Collection<FileStats> fileStats = an.empty.list();
    for (final File f : new FilesGenerator(".java", ".JAVA").from(optPath)) {
      ICompilationUnit u = null;
      try {
        u = openCompilationUnit(f);
        fileStats.add(process(f, u));
        ++done;
      } catch (final JavaModelException ¢) {
        note.bug(this, ¢);
        ++failed;
      } catch (final IOException ¢) {
        note.io(¢);
        ++failed;
      } catch (final Exception ¢) {
        note.ignore(this, ¢);
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
  private FileStats process(final File f, final ICompilationUnit u) throws IOException, JavaModelException {
    final FileStats ret = new FileStats(f);
    final GUITraversal t = new GUITraversal();
    IntStream.range(0, optRounds).forEach(λ -> t.apply(u));
    FileUtils.writeToFile(determineOutputFilename(f.getAbsolutePath()), u.getSource());
    if (optVerbose)
      System.out.println("Spartanized file " + f.getAbsolutePath());
    ret.countLinesAfter();
    return ret;
  }
  @Override public void stop() {
    ___.nothing();
  }
  String determineOutputFilename(final String path) {
    return optOverwrite ? path : path.substring(0, path.lastIndexOf('.')) + "__new.java";
  }
  /** Discard compilation unit u
   * @param u */
  private void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final NullPointerException | JavaModelException ¢) {
      note.bug(this, ¢);
    }
  }
  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    }
  }
  private ICompilationUnit openCompilationUnit(final File ¢) throws IOException, JavaModelException {
    final String ret = FileUtils.read(¢);
    setPackage(getPackageNameFromSource(ret));
    return pack.createCompilationUnit(¢.getName(), ret, false, null);
  }
  private boolean parseArguments(final Collection<String> args) {
    if (args == null || args.isEmpty()) {
      printHelpPrompt();
      return true;
    }
    for (final String a : args) {
      if ("-N".equals(a))
        optOverwrite = false;
      if ("-E".equals(a))
        optIndividualStatistics = true;
      try {
        if (a.startsWith("-C"))
          optRounds = Integer.parseUnsignedInt(a.substring(2));
      } catch (final NumberFormatException ¢) {
        note.bug(¢);
      }
      if ("-V".equals(a))
        optVerbose = true;
      if ("-l".equals(a))
        optStatsLines = true;
      if ("-r".equals(a))
        optStatsChanges = true;
      if (!a.startsWith("-"))
        optPath = a;
    }
    return optPath == null;
  }
  private void prepareTempIJavaProject() throws CoreException {
    final IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTemp");
    if (p.exists())
      p.delete(true, null);
    p.create(null);
    p.open(null);
    final IProjectDescription d = p.getDescription();
    d.setNatureIds(new String[] { JavaCore.NATURE_ID });
    p.setDescription(d, null);
    javaProject = JavaCore.create(p);
    final IFolder binFolder = p.getFolder("bin"), sourceFolder = p.getFolder("src");
    srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
    binFolder.create(false, true, null);
    sourceFolder.create(false, true, null);
    javaProject.setOutputLocation(binFolder.getFullPath(), null);
    final IClasspathEntry[] buildPath = new IClasspathEntry[1];
    buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
    javaProject.setRawClasspath(buildPath, null);
  }
  private void printLineStatistics(final Iterable<FileStats> ss) {
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
  private void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
  @SuppressWarnings("boxing") private void printChangeStatistics(final Collection<FileStats> ss) {
    System.out.println("\nTotal changes made: ");
    if (!optIndividualStatistics)
      range.to(optRounds).forEach(i -> System.out
          .println("    Round #" + i + 1 + ": " + (i < 9 ? " " : "") + ss.stream().map(λ -> λ.getRoundStat(i)).reduce((x, y) -> x + y).get()));
    else
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.fileName());
        for (int ¢ = 0; ¢ < optRounds; ++¢)
          System.out.println("    Round #" + ¢ + 1 + ": " + (¢ < 9 ? " " : "") + f.getRoundStat(¢));
      }
  }

  /** Data structure designed to hold and compute information about a single
   * file, in order to produce statistics when completed execution */
  private class FileStats {
    final File file;
    final int linesBefore;
    int linesAfter;
    final List<Integer> roundStats = an.empty.list();

    FileStats(final File file) throws IOException {
      linesBefore = countLines(this.file = file);
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
    public int getRoundStat(final int ret) {
      try {
        return roundStats.get(ret).intValue();
      } catch (final IndexOutOfBoundsException ¢) {
        note.bug(¢);
        return 0;
      }
    }
  }
}
