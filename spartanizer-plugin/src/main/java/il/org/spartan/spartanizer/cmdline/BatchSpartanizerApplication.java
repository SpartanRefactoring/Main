package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.clean;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

import fluent.ly.___;
import fluent.ly.note;
import fluent.ly.system;
import il.org.spartan.CSVStatistics;
import il.org.spartan.Essence;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.cmdline.good.InteractiveSpartanizer;
import il.org.spartan.spartanizer.cmdline.library.FileHeuristics;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.spartanizer.tipping.categories.Nominal;
import il.org.spartan.utils.FileUtils;

/** Scans files named by outputFolder, forget test files, and collect
 * statistics. It does everything BatchSpartanizer does, but using the
 * {@link EventApplicator}
 * @author Matteo Orru' */
@SuppressWarnings("TooBroadScope")
public
final class BatchSpartanizerApplication implements IApplication {
  private static final String folder = "/tmp";
  private static final String script = "./essence";
  private static final InteractiveSpartanizer interactiveSpartanizer = new InteractiveSpartanizer().disable(Nominal.class)
      .disable(Category.Nanos.class);
  private static String outputDir;
  private IPackageFragmentRoot srcRoot;
  private IPackageFragment pack;

  /* (non-Javadoc)
   *
   * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
   * IApplicationContext) */
  @Override public Object start(@SuppressWarnings("unused") final IApplicationContext __) {
    return IApplication.EXIT_OK;
  }
  ICompilationUnit openCompilationUnit(final File ¢) throws JavaModelException, IOException {
    final String $ = FileUtils.read(¢);
    setPackage(wizard.getPackageNameFromSource($));
    return pack.createCompilationUnit(¢.getName(), $, false, null);
  }
  private void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
  /** Discard compilation unit u
   * @param u */
  void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final NullPointerException | JavaModelException ¢) {
      note.bug(this, ¢);
    }
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
    final IJavaProject javaProject = JavaCore.create(p);
    final IFolder binFolder = p.getFolder("bin"), sourceFolder = p.getFolder("src");
    srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);
    binFolder.create(false, true, null);
    sourceFolder.create(false, true, null);
    javaProject.setOutputLocation(binFolder.getFullPath(), null);
    final IClasspathEntry[] buildPath = new IClasspathEntry[1];
    buildPath[0] = JavaCore.newSourceEntry(srcRoot.getPath());
    javaProject.setRawClasspath(buildPath, null);
  }
  /* (non-Javadoc)
   *
   * @see org.eclipse.equinox.app.IApplication#stop() */
  @Override public void stop() {
    ___.nothing();
  }
  // public static void main(final String[] args) {
  // if (args.length == 0)
  // printHelpPrompt();
  // else {
  // parseCommandLineArgs(args);
  // if (inputDir != null && outputDir != null) {
  // final File input = new File(inputDir);
  // if (!input.isDirectory()) {
  // System.out.println("Analyzing single file: " + input.getAbsolutePath());
  // new BatchSpartanizer2(input.getAbsolutePath()).fire();
  // } else {
  // System.out.println("Analyzing directory: " + input.getAbsolutePath());
  // for (final File ¢ : input.listFiles())
  // if (¢.getName().endsWith(".java") || containsJavaFileOrJavaFileItSelf(¢)) {
  // System.out.println(¢.getAbsolutePath());
  // new BatchSpartanizer2(¢.getAbsolutePath()).fire();
  // }
  // }
  // }
  // if (defaultDir) {
  // new BatchSpartanizer2(".", "current-working-directory").fire();
  // for (final String ¢ : args)
  // new BatchSpartanizer2(¢).fire();
  // }
  // }
  // }
  public static ProcessBuilder runScript¢(final String pathname) {
    final ProcessBuilder $ = system.runScript();
    $.redirectErrorStream(true);
    $.command(script, pathname);
    return $;
  }
  static void printHelpPrompt() {
    System.out.println(system.myShortClassName());
    System.out.println("Options:");
    System.out.println("  -d       default directory: use the current directory for the analysis");
    System.out.println("  -o       output directory: here go the results of the analysis");
    System.out.println("  -i       input directory: place here the projects that you want to analyze.");
    System.out.println("");
  }

  private int classesDone;
  private PrintWriter befores;
  private PrintWriter afters;
  private CSVStatistics report;

  private BatchSpartanizerApplication(final String path) {
    this(path, system.folder2File(path));
  }
  @SuppressWarnings("unused") private BatchSpartanizerApplication(final String presentSourcePath, final String name) {
    final File dir = new File(folder + outputDir);
    if (!dir.exists())
      System.out.println(dir.mkdir());
  }
  boolean collect(final AbstractTypeDeclaration in) {
    final int length = in.getLength(), tokens = Metrics.tokens(in + ""), nodes = countOf.nodes(in), body = Metrics.bodySize(in),
        tide = clean(in + "").length(), essence = Essence.of(in + "").length();
    final String out = interactiveSpartanizer.fixedPoint(in + "");
    final int length2 = out.length(), tokens2 = Metrics.tokens(out), tide2 = clean(out).length(), essence2 = Essence.of(out).length(),
        wordCount = system.wc(Essence.of(out));
    final ASTNode from = makeAST.COMPILATION_UNIT.from(out);
    final int nodes2 = countOf.nodes(from), body2 = Metrics.bodySize(from);
    System.err.println(++classesDone + " " + extract.category(in) + " " + extract.name(in));
    befores.print(in);
    afters.print(out);
    report.summaryFileName();
    report//
        .put("TipperCategory", extract.category(in))//
        .put("Name", extract.name(in))//
        .put("Nodes1", nodes)//
        .put("Nodes2", nodes2)//
        .put("Δ Nodes", nodes - nodes2)//
        .put("δ Nodes", FileHeuristics.d(nodes, nodes2))//
        .put("δ Nodes %", FileHeuristics.p(nodes, nodes2))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", FileHeuristics.d(body, body2))//
        .put("% Body", FileHeuristics.p(body, body2))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", FileHeuristics.d(tokens, tokens2))//
        .put("% Tokens", FileHeuristics.p(tokens, tokens2))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", FileHeuristics.d(length, length2))//
        .put("% Length", FileHeuristics.p(length, length2))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", FileHeuristics.d(tide, tide2))//
        .put("δ Tide2", FileHeuristics.p(tide, tide2))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", FileHeuristics.d(essence, essence2))//
        .put("% Essence", FileHeuristics.p(essence, essence2))//
        .put("Words)", wordCount).put("R(T/L)", FileHeuristics.ratio(length, tide)) //
        .put("R(E/L)", FileHeuristics.ratio(length, essence)) //
        .put("R(E/T)", FileHeuristics.ratio(tide, essence)) //
        .put("R(B/S)", FileHeuristics.ratio(nodes, body)) //
    ;
    report.nl();
    return false;
  }
}
