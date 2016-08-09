package il.org.spartan.refactoring.application;

import static il.org.spartan.external.External.Introspector.*;
import java.io.*;
import java.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.*;
import il.org.spartan.external.*;
import il.org.spartan.files.*;
import il.org.spartan.refactoring.handlers.*;
import il.org.spartan.utils.*;

/** Command line version of this plug-in
 * @author Yossi Gil
 * @since 2015/10/10 */
public class Xiphos {
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
    public void addRoundStat(final int i) {
      roundStats.add(Integer.valueOf(i));
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
    public int getRoundStat(final int r) {
      try {
        return roundStats.get(r).intValue();
      } catch (final IndexOutOfBoundsException e) {
        e.printStackTrace();
        return 0;
      }
    }
  }
  static int countLines(final File f) throws IOException {
    try (LineNumberReader lr = new LineNumberReader(new FileReader(f))) {
      lr.skip(Long.MAX_VALUE);
      return lr.getLineNumber();
    }
  }
  static int countLines(final String fileName) throws IOException {
    return countLines(new File(fileName));
  }
  static void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.delete(true, null);
    } catch (final JavaModelException e) {
      e.printStackTrace();
    } catch (final NullPointerException e) {
      // Ignore
    }
  }
  static String getPackageNameFromSource(final String source) {
    final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    p.setSource(source.toCharArray());
    final Wrapper<String> $ = new Wrapper<>("");
    p.createAST(null).accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration node) {
        $.set(node.getName().toString());
        return false;
      }
    });
    return $.get();
  }
  /** main function, to which command line arguments are passed.
   * @param args command line arguments */
  public static void main(final String[] args) {
    new Xiphos(args).go();
  }
  static void printHelpPrompt() {
    System.out.println("Spartan Refactoring plugin command line");
    System.out.println("Usage: eclipse -application il.org.spartan.refactoring.application -nosplash [OPTIONS] PATH");
    System.out.println("Executes the Spartan Refactoring Eclipse plug-in from the command line on all the Java source files "
        + "within the given PATH. Files are spartanized in place by default.");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -N       Do not overwrite existing files (writes the Spartanized output to a new file in the same directory)");
    System.out.println("  -C<num>  Maximum number of Spartanizaion rounds for each file (default: 20)");
    System.out.println("  -E       Display statistics for each file separately");
    System.out.println("  -V       Be verbose");
    System.out.println("");
    System.out.println("Print statistics:");
    System.out.println("  -l       Show the number of lines before and after Spartanization");
    System.out.println("  -r       Show the number of Spartanizaion made in each round");
  }
  @External(alias = "r") int rounds = 20;
  @External(alias = "v") boolean verbose = false;
  boolean optIndividualStatistics = false;
  boolean optStatsLines = false;
  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;
  boolean optDoNotOverwrite = false;
  boolean optStatsChanges = false;
  private final List<String> remaining;
  private Xiphos(final String[] args) {
    remaining = extract(args, this);
  }
  String determineOutputFilename(final String path) {
    return !optDoNotOverwrite ? path : path.substring(0, path.lastIndexOf('.')) + "_new.java";
  }
  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }
  private void go() {
    final List<FileStats> fileStats = new ArrayList<>();
    try {
      prepareTempIJavaProject();
    } catch (final CoreException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
    int done = 0, failed = 0;
    for (final File f : new FilesGenerator(".java", ".JAVA").from(remaining)) {
      ICompilationUnit u = null;
      try {
        u = openCompilationUnit(f);
        final FileStats s = new FileStats(f);
        for (int i = 0; i < rounds; ++i) {
          final int n = CleanupHandler.countSuggestions(u);
          if (n == 0)
            break;
          s.addRoundStat(n);
          ApplySpartanizationHandler.apply(u);
        }
        FileUtils.writeToFile(determineOutputFilename(f.getAbsolutePath()), u.getSource());
        if (verbose)
          System.out.println("Spartanized file " + f.getAbsolutePath());
        s.countLinesAfter();
        fileStats.add(s);
        ++done;
      } catch (final JavaModelException | IOException e) {
        System.err.println(f + ": " + e.getMessage());
        ++failed;
      } catch (final Exception e) {
        System.err.println("An unexpected error has occurred on file " + f + ": " + e.getMessage());
        e.printStackTrace();
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
  }
  ICompilationUnit openCompilationUnit(final File f) throws IOException, JavaModelException {
    final String source = FileUtils.read(f);
    setPackage(getPackageNameFromSource(source));
    return pack.createCompilationUnit(f.getName(), source, false, null);
  }
  void prepareTempIJavaProject() throws CoreException {
    final IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTemp");
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
  private void printChangeStatistics(final List<FileStats> ss) {
    System.out.println("\nTotal changes made: ");
    if (optIndividualStatistics)
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.fileName());
        for (int i = 0; i < rounds; ++i)
          System.out.println("    Round #" + (i + 1) + ": " + (i < 9 ? " " : "") + f.getRoundStat(i));
      }
    else
      for (int i = 0; i < rounds; ++i) {
        int roundSum = 0;
        for (final FileStats f : ss)
          roundSum += f.getRoundStat(i);
        System.out.println("    Round #" + (i + 1) + ": " + (i < 9 ? " " : "") + roundSum);
      }
  }
  void printLineStatistics(final List<FileStats> ss) {
    System.out.println("\nLine differences:");
    if (optIndividualStatistics)
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.fileName());
        System.out.println("    Lines before: " + f.getLinesBefore());
        System.out.println("    Lines after: " + f.getLinesAfter());
      }
    else {
      int totalBefore = 0, totalAfter = 0;
      for (final FileStats f : ss) {
        totalBefore += f.getLinesBefore();
        totalAfter += f.getLinesAfter();
      }
      System.out.println("  Lines before: " + totalBefore);
      System.out.println("  Lines after: " + totalAfter);
    }
  }
  void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
}
