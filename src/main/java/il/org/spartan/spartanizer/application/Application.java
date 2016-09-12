package il.org.spartan.spartanizer.application;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.omg.Messaging.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

import static java.nio.file.StandardCopyOption.*;

/** An {@link IApplication} extension entry point, allowing execution of this
 * plug-in from the command line.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19 */
@SuppressWarnings({ "static-method", "unused" }) public class Application implements IApplication {
  static int countLines(final File f) throws IOException {
    try (LineNumberReader lr = new LineNumberReader(new FileReader(f))) {
      lr.skip(Long.MAX_VALUE);
      return lr.getLineNumber();
    }
  }

  static int countLines(final String fileName) throws IOException {
    return countLines(new File(fileName));
  }

  IJavaProject javaProject;
  IPackageFragmentRoot srcRoot;
  IPackageFragment pack;
  boolean optDoNotOverwrite = false, optIndividualStatistics = false, optVerbose = false;
  boolean optStatsLines = false, optStatsChanges = false, printLog = false;
  int optRounds = 20;
  String optPath;
  private boolean backupProjects = false;
  private boolean develop = false;

  @Override public Object start(final IApplicationContext arg0) {
    if (parseArguments(as.list((String[]) arg0.getArguments().get(IApplicationContext.APPLICATION_ARGS))))
      return IApplication.EXIT_OK;
    final List<FileStats> fileStats = new ArrayList<>();
   
    try {
      prepareTempIJavaProject();
    } catch (final CoreException e) {
      System.err.println(e.getMessage());
      return IApplication.EXIT_OK;
    }
    if (printLog) {
      LogManager.activateLog();
      LogManager.initialize("/home/matteo/SpartanLog");
    }
    
    List<String> destDirList = createDirs();
    
    // copy the base directory on the first round one
    try {
      org.apache.commons.io.FileUtils.copyDirectory(new File(optPath), new File(destDirList.get(0)));
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    
    for(int j = 1; j <= optRounds; j++){
      String srcDir = destDirList.get((j-1));
      String destDir = destDirList.get(j);
      System.out.println("--->> now processing: " + srcDir);
      // process the srcDir
      processRound(fileStats, srcDir);
      // copy the processed files on the new directory (associated to the next round)
      try {
        org.apache.commons.io.FileUtils.copyDirectory(new File(srcDir), new File(destDir));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
      if (optStatsChanges)
        printChangeStatistics(fileStats);
      if (optStatsLines)
        printLineStatistics(fileStats);
      if (printLog)
        LogManager.closeAllWriters();
      return IApplication.EXIT_OK;
    }

  private List<String> createDirs() {
    List<String> destDirList = new ArrayList<String>();
    
    for(int round = 1; round <= optRounds; round++){
      File srcDir = new File(optPath);
  //    System.out.println("File(optPath).getName(): " + new File(optPath).getName());
      String destDirName = new File(optPath).getName() + "_" + round;
  //    System.out.println("destDirName: " + destDirName);
  //    System.out.println(new File(optPath).getParent());
      String destDirFullPath = new File(optPath).getParent() + "/" + destDirName;
      destDirList.add(destDirFullPath);
  //    System.out.println(destDirFullPath);
      File destDir = new File(destDirFullPath);
      if(!destDir.exists())
        destDir.mkdir();
    }
    return destDirList;
  }

  private void processRound(final List<FileStats> fileStats, String copyDir) {
    int done = 0, failed = 0;
    for (final File f : new FilesGenerator(".java", ".JAVA").from(copyDir)) {
      ICompilationUnit u = null;
      try {
        u = openCompilationUnit(f);
        final FileStats s = new FileStats(f);
        for (int i = 0; i < optRounds; ++i) {
          final int n = SpartanizeAll.countSuggestions(u);
          if (n == 0)
            break;
          s.addRoundStat(n);
          eclipse.apply(u);
        }
        FileUtils.writeToFile(determineOutputFilename(f.getAbsolutePath()), u.getSource());
        if (optVerbose)
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
  }
  

  @Override public void stop() {
    // Unused
  }

  String determineOutputFilename(final String path) {
    return !optDoNotOverwrite ? path : path.substring(0, path.lastIndexOf('.')) + "__new.java";
  }

  void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final JavaModelException e) {
      e.printStackTrace();
    } catch (final NullPointerException e) {
      e.printStackTrace();
    }
  }

  void discardTempIProject() {
    try {
      javaProject.close();
      javaProject.getProject().delete(true, null);
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }

  MethodInvocation getMethodInvocation(final CompilationUnit u, final int lineNumber, final MethodInvocation i) {
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

  String getPackageNameFromSource(final String source) {
    final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    p.setSource(source.toCharArray());
    return getPackageNameFromSource(new Wrapper<>(""), p.createAST(null));
  }

  ICompilationUnit openCompilationUnit(final File f) throws IOException, JavaModelException {
    final String source = FileUtils.read(f);
    setPackage(getPackageNameFromSource(source));
    return pack.createCompilationUnit(f.getName(), source, false, null);
  }

  boolean parseArguments(final List<String> args) {
    if (args == null || args.isEmpty()) {
      printHelpPrompt();
      return true;
    }
    for (final String a : args) {
      if ("-N".equals(a))
        optDoNotOverwrite = true;
      if ("-E".equals(a))
        optIndividualStatistics = true;
      try {
        if (a.startsWith("-C"))
          optRounds = Integer.parseUnsignedInt(a.substring(2));
      } catch (final NumberFormatException e) {
        // Ignore
      }
      if ("-V".equals(a))
        optVerbose = true;
      if ("-l".equals(a))
        optStatsLines = true;
      if ("-r".equals(a))
        optStatsChanges = true;
      if ("-L".equals(a))
        printLog = true;
      if ("-B".equals(a))
        backupProjects = true;
      if ("-p".equals(a))
        develop = true;    
      if (!a.startsWith("-"))
        optPath = a;
      try {
        // TODO: Matteo, please check. 
      } catch (final NumberFormatException e) {
        // Ignore
      }
    }
    return optPath == null;
  }

  void prepareTempIJavaProject() throws CoreException {
    final IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("spartanTemp");
    System.out.println("root: " + ResourcesPlugin.getWorkspace().getRoot().getName());
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
    System.out.println("srcRoot: " + srcRoot.toString());
    binFolder.create(false, true, null);
    sourceFolder.create(false, true, null);
    javaProject.setOutputLocation(binFolder.getFullPath(), null);
    System.out.println("binFolder.getFullPath: " + binFolder.getFullPath());
    System.out.println("javaProject.getOutputLocation(): " + javaProject.getOutputLocation().toString());
    System.out.println("javaProject.getOutputLocation().toOSString()" + javaProject.getOutputLocation().toOSString());
    final IClasspathEntry[] buildPath = new IClasspathEntry[1];
    buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
    javaProject.setRawClasspath(buildPath, null);
    
  }

  void printHelpPrompt() {
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
    System.out.println("  -B       backup projects");
    System.out.println("");
    System.out.println("Print statistics:");
    System.out.println("  -l       Show the number of lines before and after Spartanization");
    System.out.println("  -r       Show the number of Spartanizaion made in each round");
    System.out.println("");
    System.out.println("Output:");
    System.out.println("  -logPath Output dir for logs");
    System.out.println("");
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

  private String getPackageNameFromSource(final Wrapper<String> $, final ASTNode n) {
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final PackageDeclaration d) {
        $.set(d.getName() + "");
        return false;
      }
    });
    return $.get();
  }

  private void printChangeStatistics(final List<FileStats> ss) {
    System.out.println("\nTotal changes made: ");
    if (optIndividualStatistics)
      for (final FileStats f : ss) {
        System.out.println("\n  " + f.fileName());
        for (int i = 0; i < optRounds; ++i)
          System.out.println("    Round #" + i + 1 + ": " + (i < 9 ? " " : "") + f.getRoundStat(i));
      }
    else
      for (int i = 0; i < optRounds; ++i) {
        int roundSum = 0;
        for (final FileStats f : ss)
          roundSum += f.getRoundStat(i);
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
      } catch (final IndexOutOfBoundsException x) {
        x.printStackTrace();
        return 0;
      }
    }
  }
}
