package il.org.spartan.spartanizer.application;

import java.io.*;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.collections.FilesGenerator.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.application.Application.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wring.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;
import il.org.spartan.utils.*;

/** IApplication for collecting metrics pre and post Spartanization 
 * @author Matteo Orru'
 * @year 2016 */
public class CollectMetricsApp implements IApplication {
  
  private static final String OUTPUT = System.getProperty("user.home") + "/halstead.csv";
  private static final String SPARTAN_OUTPUT = System.getProperty("user.home") + "/spartan_halstead.csv";
  private static CSVStatistics output;
  private static IJavaProject javaProject;
  private static IPackageFragmentRoot srcRoot;
  private static IPackageFragment pack;
  private static String path;
  private static int optRounds = 1;

  // app methods
  
  @Override public Object start(IApplicationContext arg0) throws Exception {
    String[] args = (String[]) arg0.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    System.out.println(path);
    path = args[0];
    printStatistics("Before-", OUTPUT);
    spartanize();
    printStatistics("After-", SPARTAN_OUTPUT);
//    go(args.length != 0 ? args : new String[] { "." });
//    System.err.println("Your output should be here: " + output.close());    
    return IApplication.EXIT_OK;
  }

  private static void printStatistics(String prefix, String outputPath) {
    output = init(outputPath);
    for (final File ¢ : new FilesGenerator(".java").from(path))
      try {
        // This line is going to give you trouble if you process class by class.
        output.put("File", ¢.getName());
        final String javaCode = FileUtils.read(¢);
        output.put("Characters", javaCode.length());
        final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
        report(prefix, cu);
//        output.close();
      } catch (final IOException e) {
        System.err.println(e.getMessage());
      }
    
  }

  @Override public void stop() {
    // Unused
  }

//  public static void main(final String[] where) {
//    go(where.length != 0 ? where : new String[] { "." });
//    System.err.println("Your output should be here: " + output.close());
//  }
  
//  /**
//   * 
//   * @param where
//   */
  
//  private static void go(final String[] where) {
//    for (final File ¢ : new FilesGenerator(".java").from(where))
//      try {
//        // This line is going to give you trouble if you process class by class.
//        output.put("File", ¢.getName());
//        go(FileUtils.read(¢));
//      } catch (final IOException e) {
//        System.err.println(e.getMessage());
//      }
//  }
  
  /**
   * 
   * @param f
   */

//  private static void go(final File f) {
//    try {
//      // This line is going to give you trouble if you process class by class.
//      output.put("File", f.getName());
//      go(FileUtils.read(f));
//    } catch (final IOException e) {
//      System.err.println(e.getMessage());
//    }
//  }
  
  /**
   * 
   * @param javaCode
   */

//  private static void go(final String javaCode) {
//    output.put("Characters", javaCode.length());
//    final CompilationUnit before = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
//    report("Before-", before);
////    final CompilationUnit after = spartanize(before);
//    spartanize();
////    assert after != null;
////    report("After-", after);
//  }

  @SuppressWarnings("unchecked") 
  private static void spartanize() { // final CompilationUnit u) {
    // TODO: try to it do first with one wring only. 
    // I think this is going be
    // better.
    try {
      prepareTempIJavaProject();
    } catch (CoreException e) {
      System.err.println(e.getMessage());
//      return IApplication.EXIT_OK;
    }
    int done = 0, failed = 0;
    for (final File f : new FilesGenerator(".java", ".JAVA").from(path)) {
      ICompilationUnit u = null;
      try {
        u = openCompilationUnit(f);
//        final FileStats s = new FileStats(f);
        for (int i = 0; i < optRounds; ++i) {
          final int n = SpartanizeAll.countSuggestions(u);
          if (n == 0)
            break;
//          s.addRoundStat(n);
          eclipse.apply(u);
        }
//        FileUtils.writeToFile(determineOutputFilename(f.getAbsolutePath()), u.getSource());
//        if (optVerbose)
            System.out.println("Spartanized file " + f.getAbsolutePath());
//        s.countLinesAfter();
//        fileStats.add(s);
        ++done;
      } catch (final JavaModelException | IOException e) {
        System.err.println(f + ": " + e.getMessage());
        ++failed;
      } catch (final Exception e) {
        System.err.println("An unexpected error has occurred on file " + f + ": " + e.getMessage());
        e.printStackTrace();
        ++failed;
      } finally {
        //
        discardCompilationUnit(u);
      }
    }
   
//    return IApplication.EXIT_OK;
  }
  
  static void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final JavaModelException e) {
      e.printStackTrace();
    } catch (final NullPointerException e) {
      e.printStackTrace();
    }
  }
  
  static ICompilationUnit openCompilationUnit(final File f) throws IOException, JavaModelException {
    final String source = FileUtils.read(f);
    setPackage(getPackageNameFromSource(source));
    return pack.createCompilationUnit(f.getName(), source, false, null);
  }
  
  static String getPackageNameFromSource(final String source) {
    final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    p.setSource(source.toCharArray());
    return getPackageNameFromSource(new Wrapper<>(""), p.createAST(null));
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
  
  static void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }

  static void prepareTempIJavaProject() throws CoreException {
    @SuppressWarnings("unused") IWorkspace ws = ResourcesPlugin.getWorkspace();
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
  
  private static CSVStatistics init(String outputReportDir) {
    try {
      return new CSVStatistics(outputReportDir, "property");
    } catch (final IOException e) {
      throw new RuntimeException(outputReportDir, e);
    }
  }
  
  

  /** Bug, what happens if we have many classes in the same file? Also, we do
   * not want to count imports, and package instructions. Write a method that
   * finds all classes, which could be none, at the upper level, and collect on
   * these. Note that you have to print the file name which is common to all
   * classes. Turn this if you like into a documentation
   * @param string */
  private static void report(final String prefix, final CompilationUnit ¢) {
    // TODO Matteo make sure that the counting does not include comments. Do
    // this by adding stuff to the metrics suite.
    output.put(prefix + "Length", ¢.getLength());
    // TODO: Yossi, make this even more clever, by using function interfaces..
    output.put(prefix + "Count", metrics.count(¢));
    output.put(prefix + "Non whites", metrics.countNonWhites(¢));
    output.put(prefix + "Condensed size", metrics.condensedSize(¢));
    output.put(prefix + "Lines", metrics.lineCount(¢));
    output.put(prefix + "Dexterity", metrics.dexterity(¢));
    output.put(prefix + "Leaves", metrics.leaves(¢));
    output.put(prefix + "Nodes", metrics.nodes(¢));
    output.put(prefix + "Internals", metrics.internals(¢));
    output.put(prefix + "Vocabulary", metrics.vocabulary(¢));
    output.put(prefix + "Literacy", metrics.literacy(¢));
    output.nl();
   }
  
  public void copy(File sourceLocation, File targetLocation) throws IOException {
    if (sourceLocation.isDirectory()) {
        copyDirectory(sourceLocation, targetLocation);
    } else {
        copyFile(sourceLocation, targetLocation);
    }
  }

  private void copyDirectory(File source, File target) throws IOException {
    if (!target.exists()) {
        target.mkdir();
    }

    for (String f : source.list()) {
        copy(new File(source, f), new File(target, f));
    }
  }

  private static void copyFile(File source, File target) throws IOException {        
    try (
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target)
    ) {
        byte[] buf = new byte[1024];
        int length;
        while ((length = in.read(buf)) > 0) {
            out.write(buf, 0, length);
        }
    }
  }
}