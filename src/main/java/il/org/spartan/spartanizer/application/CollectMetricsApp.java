package il.org.spartan.spartanizer.application;

import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.plugin.Plugin;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** IApplication for collecting metrics pre and post Spartanization
 * @author Matteo Orru'
 * @year 2016 */
public final class CollectMetricsApp implements IApplication {
  private static final String OUTPUT = System.getProperty("user.home") + "/halstead.csv";
  private static final String SPARTAN_OUTPUT = System.getProperty("user.home") + "/spartan_halstead.csv";
  private static CSVStatistics output;
  private static IJavaProject javaProject;
  private static IPackageFragmentRoot srcRoot;
  private static IPackageFragment pack;
  private static String path;
  private static int optRounds = 1;

  // app methods
  static void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final JavaModelException e) {
      Plugin.log(e);
    } catch (final NullPointerException e) {
      Plugin.log(e);
    }
  }

  static String getPackageNameFromSource(final String source) {
    final ASTParser p = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    p.setSource(source.toCharArray());
    return getPackageNameFromSource(new Wrapper<>(""), p.createAST(null));
  }

  static ICompilationUnit openCompilationUnit(final File f) throws IOException, JavaModelException {
    final String source = FileUtils.read(f);
    setPackage(getPackageNameFromSource(source));
    return pack.createCompilationUnit(f.getName(), source, false, null);
  }

  static void prepareTempIJavaProject() throws CoreException {
    ResourcesPlugin.getWorkspace();
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

  static void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }

  private static void copyFile(final File source, final File target) throws IOException {
    try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(target)) {
      final byte[] buf = new byte[1024];
      int length;
      while ((length = in.read(buf)) > 0)
        out.write(buf, 0, length);
    }
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

  private static CSVStatistics init(final String outputReportDir) {
    try {
      return new CSVStatistics(outputReportDir, "property");
    } catch (final IOException e) {
      throw new RuntimeException(outputReportDir, e);
    }
  }

  private static void printStatistics(final String prefix, final String outputPath) {
    output = init(outputPath);
    for (final File ¢ : new FilesGenerator(".java").from(path))
      try {
        // This line is going to give you trouble if you process class by class.
        output.put("File", ¢.getName());
        final String javaCode = FileUtils.read(¢);
        output.put("Characters", javaCode.length());
        final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
        report(prefix, cu);
        // output.close();
      } catch (final IOException e) {
        System.err.println(e.getMessage());
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

  private static void spartanize() { // final CompilationUnit u) {
    // TODO: try to it do first with one wring only.
    // I think this is going be
    // better.
    try {
      prepareTempIJavaProject();
    } catch (final CoreException e) {
      System.err.println(e.getMessage());
      // return IApplication.EXIT_OK;
    }
    for (final File f : new FilesGenerator(".java", ".JAVA").from(path)) {
      ICompilationUnit u = null;
      try {
        u = openCompilationUnit(f);
        for (int i = 0; i < optRounds; ++i) {
          final int n = SpartanizeAll.countSuggestions(u);
          if (n == 0)
            break;
          eclipse.apply(u);
        }
        System.out.println("Spartanized file " + f.getAbsolutePath());
      } catch (final JavaModelException | IOException e) {
        System.err.println(f + ": " + e.getMessage());
      } catch (final Exception e) {
        System.err.println("An unexpected error has occurred on file " + f + ": " + e.getMessage());
        e.printStackTrace();
      } finally {
        //
        discardCompilationUnit(u);
      }
    }
  }

  public void copy(final File sourceLocation, final File targetLocation) throws IOException {
    if (!sourceLocation.isDirectory())
      copyFile(sourceLocation, targetLocation);
    else
      copyDirectory(sourceLocation, targetLocation);
  }

  @Override public Object start(final IApplicationContext arg0) {
    final String[] args = (String[]) arg0.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    System.out.println(path);
    path = args[0];
    printStatistics("Before-", OUTPUT);
    spartanize();
    printStatistics("After-", SPARTAN_OUTPUT);
    return IApplication.EXIT_OK;
  }

  @Override public void stop() {
    // Unused
  }

  private void copyDirectory(final File source, final File target) throws IOException {
    if (!target.exists())
      target.mkdir();
    for (final String f : source.list())
      copy(new File(source, f), new File(target, f));
  }
}