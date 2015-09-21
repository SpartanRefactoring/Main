package org.spartan.refactoring.application;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.spartan.refactoring.handlers.ApplySpartanizationHandler;
import org.spartan.refactoring.handlers.CleanupHandler;
import org.spartan.utils.FileUtils;
import org.spartan.utils.Wrapper;

/**
 * An {@link IApplication} extension entry point, allowing execution of this
 * plug-in from a terminal
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19
 */
@SuppressWarnings("static-method") public class Application implements IApplication {
  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;
  boolean optDoNotOverwrite = false, optIndividualStatistics = false, optVerbose = false;
  boolean optStatsLines = false, optStatsChanges = false;
  int optRounds = 20;
  String optPath;
  @Override public Object start(final IApplicationContext arg0) throws Exception {
    if (parseArguments(Arrays.asList((String[]) arg0.getArguments().get(IApplicationContext.APPLICATION_ARGS))))
      return IApplication.EXIT_OK;
    try {
      final List<String> javaFiles = FileUtils.findAllJavaFiles(optPath);
      final List<FileStats> fileStats = new ArrayList<>();
      prepareTempIJavaProject();
      for (final String f : javaFiles) {
        final ICompilationUnit u = openCompilationUnit(f);
        final FileStats s = new FileStats(f);
        for (int i = 0; i < optRounds; ++i) {
          final int n = CleanupHandler.countSuggestions(u);
          if (n == 0)
            break;
          s.addRoundStat(n);
          ApplySpartanizationHandler.applySafeSpartanizationsTo(u);
        }
        FileUtils.writeToFile(determineOutputFilename(f), u.getSource());
        s.countLinesAfter();
        fileStats.add(s);
      }
      System.out.println("Files processed: " + javaFiles.size());
      if (optStatsChanges)
        printChangeStatistics(fileStats);
      if (optStatsLines)
        printLineStatistics(fileStats);
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      discardTempIProject();
    }
    return IApplication.EXIT_OK;
  }
  @Override public void stop() {
    // Unused
  }
  void printHelpPrompt() {
    System.out.println("Spartan Refactoring plugin command line");
    System.out.println("Usage: eclipse -application org.spartan.refactoring.application -nosplash [OPTIONS] PATH");
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
  void printLineStatistics(final List<FileStats> ss) {
    System.out.println("\nLine differences:");
    if (optIndividualStatistics)
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.getFileName());
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
  private void printChangeStatistics(final List<FileStats> ss) {
    System.out.println("\nTotal changes made: ");
    if (optIndividualStatistics)
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.getFileName());
        for (int i = 0; i < optRounds; ++i)
          System.out.println("    Round #" + (i + 1) + ": " + (i < 9 ? " " : "") + f.getRoundStat(i));
      }
    else
      for (int i = 0; i < optRounds; ++i) {
        int roundSum = 0;
        for (final FileStats f : ss)
          roundSum += f.getRoundStat(i);
        System.out.println("    Round #" + (i + 1) + ": " + (i < 9 ? " " : "") + roundSum);
      }
  }
  String determineOutputFilename(final String path) {
    return !optDoNotOverwrite ? path : path.substring(0, path.lastIndexOf('.')) + "_new.java";
  }
  boolean parseArguments(final List<String> args) {
    if (args == null || args.size() == 0) {
      printHelpPrompt();
      return true;
    }
    for (final String a : args) {
      if (a.equals("-N"))
        optDoNotOverwrite = true;
      if (a.equals("-E"))
        optIndividualStatistics = true;
      try {
        if (a.startsWith("-C"))
          optRounds = Integer.parseUnsignedInt(a.substring(2));
      } catch (final NumberFormatException e) {
        /* Ignore */ }
      if (a.equals("-V"))
        optVerbose = true;
      if (a.equals("-l"))
        optStatsLines = true;
      if (a.equals("-r"))
        optStatsChanges = true;
      if (!a.startsWith("-"))
        optPath = a;
    }
    return optPath == null;
  }
  void prepareTempIJavaProject() {
    try {
      final IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTemp");
      final boolean isNew = !proj.exists();
      if (isNew)
        proj.create(null);
      proj.open(null);
      if (isNew) {
        final IProjectDescription desc = proj.getDescription();
        desc.setNatureIds(new String[] { JavaCore.NATURE_ID });
        proj.setDescription(desc, null);
      }
      javaProject = JavaCore.create(proj);
      final IFolder binFolder = proj.getFolder("bin");
      final IFolder sourceFolder = proj.getFolder("src");
      srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
      if (isNew) {
        binFolder.create(false, true, null);
        sourceFolder.create(false, true, null);
      }
      javaProject.setOutputLocation(binFolder.getFullPath(), null);
      final IClasspathEntry[] buildPath = new IClasspathEntry[1];
      buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
      javaProject.setRawClasspath(buildPath, null);
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }
  void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
  ICompilationUnit openCompilationUnit(final String path) throws IOException, JavaModelException {
    final String source = FileUtils.readFromFile(path);
    final String packageName = getPackageNameFromSource(source);
    setPackage(packageName);
    return pack.createCompilationUnit(new File(path).getName(), source, false, null);
  }
  String getPackageNameFromSource(final String source) {
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
  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }

  /**
   * Data structure designed to hold and compute information about a single
   * file, in order to produce statistics when completed execution
   */
  private class FileStats {
    String fileName, path;
    int linesBefore, linesAfter;
    List<Integer> roundStats;
    public FileStats(final String path) {
      this.path = path;
      fileName = new File(path).getName();
      linesBefore = countLines(path);
      roundStats = new ArrayList<>();
    }
    public void countLinesAfter() {
      linesAfter = countLines(determineOutputFilename(path));
    }
    public void addRoundStat(final int i) {
      roundStats.add(i);
    }
    @SuppressWarnings("boxing") public int getRoundStat(final int r) {
      try {
        return roundStats.get(r);
      } catch (final IndexOutOfBoundsException e) {
        return 0;
      }
    }
    public String getFileName() {
      return fileName;
    }
    public int getLinesBefore() {
      return linesBefore;
    }
    public int getLinesAfter() {
      return linesAfter;
    }
    protected int countLines(final String p) {
      try (LineNumberReader lr = new LineNumberReader(new FileReader(new File(p)))) {
        lr.skip(Long.MAX_VALUE);
        return lr.getLineNumber();
      } catch (final IOException e) {
        e.printStackTrace();
        return -1;
      }
    }
  }
}
