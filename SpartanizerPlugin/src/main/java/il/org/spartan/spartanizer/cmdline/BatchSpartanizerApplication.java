package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.equinox.app.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** Scans files named by folder, ignore test files, and collect statistics. It
 * does everything BatchSpartanizer does, but using the {@link EventApplicator}
 * @author Matteo Orru'
 * @year 2016 */
public final class BatchSpartanizerApplication implements IApplication {
  private static final String folder = "/tmp";
  private static final String script = "./essence";
  private static final InteractiveSpartanizer interactiveSpartanizer = new InteractiveSpartanizer().disable(Nominal.class).disable(Nanos.class);
  private static String outputDir;
  // private EventApplicator e = new EventApplicator();
  private IJavaProject javaProject;
  private IPackageFragmentRoot srcRoot;
  private IPackageFragment pack;

  /* (non-Javadoc)
   *
   * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
   * IApplicationContext) */
  @Override public Object start(@SuppressWarnings("unused") final IApplicationContext __) {
    // if(arg0.getArguments().size() == 0)
    // optPath = ".";
    // // place here instructions from command line
    //// if (!a.startsWith("-"))
    //// optPath = a;
    //// ;
    //// optPath = ".";
    // try {
    // prepareTempIJavaProject();
    // } catch (CoreException x) {
    // x.printStackTrace();
    // }
    // int done = 0, failed = 0;
    // for (final File f : new FilesGenerator(".java", ".JAVA").from(optPath)) {
    // ICompilationUnit u = null;
    // try {
    // u = openCompilationUnit(f);
    // new EventApplicator().defaultListenerSilent()
    // .defaultPassesFew()
    // .defaultRunContext()
    // .defaultSelection()
    // .defaultRunAction()
    // .passes(1)
    // .go();
    // .defaultRunAction(getSpartanizer(¢))
    // .passes(1)
    // .selection(Selection.Util.by(¢).buildAll())
    // .go());
    // final FileStats s = new FileStats(f);
    // for (int i = 0; i <optRounds; ++i) {
    // final int n = new LaconizeProject().countTips();
    // if (n == 0)
    // break;
    //// s.addRoundStat(n);
    // new Trimmer().apply(u);
    // }
    // FileUtils.writeToFile(determineOutputFilename(f.getAbsolutePath()),
    // u.getSource());
    // if (optVerbose)
    // System.out.println("Spartanized file " + f.getAbsolutePath());
    // s.countLinesAfter();
    // fileStats.add(s);
    // ++done;
    // } catch (final JavaModelException | IOException e) {
    // System.err.println(f + ": " + e.getMessage());
    // ++failed;
    // } catch (final Exception e) {
    // System.err.println("An unexpected error has occurred on file " + f + ": "
    // + e.getMessage());
    // e.printStackTrace();
    // ++failed;
    // } finally {
    // discardCompilationUnit(u);
    // }
    // }
    return IApplication.EXIT_OK;
  }
  ICompilationUnit openCompilationUnit(final File f) throws IOException, JavaModelException {
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
  void setPackage(final String name) throws JavaModelException {
    pack = srcRoot.createPackageFragment(name, false, null);
  }
  /** Discard compilation unit u
   * @param u */
  void discardCompilationUnit(final ICompilationUnit u) {
    try {
      u.close();
      u.delete(true, null);
    } catch (final JavaModelException e) {
      monitor.logEvaluationError(this, e);
    } catch (final NullPointerException e) {
      monitor.logEvaluationError(this, e);
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
    System.out.println("Batch gUIBatchLaconizer");
    System.out.println("");
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
    final int length = in.getLength();
    final int tokens = metrics.tokens(in + "");
    final int nodes = count.nodes(in);
    final int body = metrics.bodySize(in);
    final int tide = clean(in + "").length();
    final int essence = code.essence(in + "").length();
    final String out = interactiveSpartanizer.fixedPoint(in + "");
    final int length2 = out.length();
    final int tokens2 = metrics.tokens(out);
    final int tide2 = clean(out + "").length();
    final int essence2 = code.essence(out + "").length();
    final int wordCount = code.wc(code.essence(out + ""));
    final ASTNode from = makeAST.COMPILATION_UNIT.from(out);
    final int nodes2 = count.nodes(from);
    final int body2 = metrics.bodySize(from);
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
        .put("δ Nodes", system.d(nodes, nodes2))//
        .put("δ Nodes %", system.p(nodes, nodes2))//
        .put("Body", body)//
        .put("Body2", body2)//
        .put("Δ Body", body - body2)//
        .put("δ Body", system.d(body, body2))//
        .put("% Body", system.p(body, body2))//
        .put("Length1", length)//
        .put("Tokens1", tokens)//
        .put("Tokens2", tokens2)//
        .put("Δ Tokens", tokens - tokens2)//
        .put("δ Tokens", system.d(tokens, tokens2))//
        .put("% Tokens", system.p(tokens, tokens2))//
        .put("Length1", length)//
        .put("Length2", length2)//
        .put("Δ Length", length - length2)//
        .put("δ Length", system.d(length, length2))//
        .put("% Length", system.p(length, length2))//
        .put("Tide1", tide)//
        .put("Tide2", tide2)//
        .put("Δ Tide2", tide - tide2)//
        .put("δ Tide2", system.d(tide, tide2))//
        .put("δ Tide2", system.p(tide, tide2))//
        .put("Essence1", essence)//
        .put("Essence2", essence2)//
        .put("Δ Essence", essence - essence2)//
        .put("δ Essence", system.d(essence, essence2))//
        .put("% Essence", system.p(essence, essence2))//
        .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
        .put("R(E/L)", system.ratio(length, essence)) //
        .put("R(E/T)", system.ratio(tide, essence)) //
        .put("R(B/S)", system.ratio(nodes, body)) //
    ;
    report.nl();
    return false;
  }
  // void collect(final CompilationUnit u) {
  //
  // EventApplicator a = new EventApplicator().defaultListenerSilent()
  // .defaultPassesFew()
  // .defaultRunContext();
  // Object ccu = Selection.Util.getCurrentCompilationUnit(¢);
  // a.selection(Selection.Util.get());
  // .defaultRunAction();
  // .defaultListenerNoisy().defaultRunAction(
  // SingleTipper.getApplicator(¢)).passes(1).selection(Selection.Util.getCurrentCompilationUnit(u).buildAll()).go();
  //
  // EventApplicator dn = a.defaultListenerNoisy();
  // EventApplicator dra = dn.defaultRunAction();
  // dra.passes(1);
  // System.out.println(dra.passes());
  // Object sta = SingleTipper.getApplicator(¢);
  // .defaultRunAction(SingleTipper.getApplicator(¢)).passes(1).selection(Selection.Util.getCurrentCompilationUnit(¢).buildAll()).go());
  // u.accept(new ASTVisitor() {
  // @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
  // return collect(¢);
  // }
  //
  // @Override public boolean visit(final EnumDeclaration ¢) {
  // return collect(¢);
  // }
  //
  // @Override public boolean visit(final TypeDeclaration ¢) {
  // return collect(¢);
  // }
  // });
  // }
  //
  // void collect(final File f) {
  // if (!system.isTestFile(f))
  // try {
  // collect(FileUtils.read(f));
  // } catch (final IOException e) {
  // monitor.infoIOException(e, "File = " + f);
  // }
  // }
  //
  // void collect(final String javaCode) {
  // collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  // }
  //
  // void fire() {
  // collect();
  // runEssence();
  // runWordCount();
  // System.err.printf("\n Our batch applicator had %d tippers dispersed over %d
  // hooks\n", //
  // box.it(interactiveSpartanizer.toolbox.tippersCount()), //
  // box.it(interactiveSpartanizer.toolbox.hooksCount())//
  // );
  // }
  //
  // void runEssence() {
  // system.shellEssenceMetrics(beforeFileName);
  // system.shellEssenceMetrics(afterFileName);
  // }
  //
  // private void applyEssenceCommandLine() {
  // try {
  // final String essentializedCodeBefore = system.runScript(beforeFileName);
  // final String essentializedCodeAfter = system.runScript(afterFileName);
  // final int numWordEssentialBefore = essentializedCodeBefore.trim().length();
  // final int numWordEssentialAfter = essentializedCodeAfter.trim().length();
  // System.err.println("Word Count Essentialized before: " +
  // numWordEssentialBefore);
  // System.err.println("Word Count Essentialized after: " +
  // numWordEssentialAfter);
  // System.err.println("Difference: " + (numWordEssentialAfter -
  // numWordEssentialBefore));
  // } catch (final IOException e) {
  // System.err.println(e.getMessage());
  // }
  // }
  //
  // private void collect() {
  // System.err.printf(
  // "Input path=%s\n" + //
  // "Collective before path=%s\n" + //
  // "Collective after path=%s\n" + //
  // "\n", //
  // presentSourcePath, //
  // beforeFileName, //
  // afterFileName);
  // try (PrintWriter b = new PrintWriter(new FileWriter(beforeFileName)); //
  // PrintWriter a = new PrintWriter(new FileWriter(afterFileName))) {
  // befores = b;
  // afters = a;
  // report = new CSVStatistics(reportFileName, "property");
  // for (final File ¢ : new FilesGenerator(".java").from(presentSourcePath))
  // collect(¢);
  // } catch (final IOException x) {
  // x.printStackTrace();
  // System.err.println(classesDone + " files processed; processing of " +
  // presentSourcePath + " failed for some I/O reason");
  // }
  // applyEssenceCommandLine();
  // System.err.print("\n Done: " + classesDone + " files processed.");
  // System.err.print("\n Summary: " + report.close());
  // }
  //
  // private void runWordCount() {
  // system.bash("wc " + separate.these(beforeFileName, afterFileName,
  // system.essenced(beforeFileName), system.essenced(afterFileName)));
  // }
  //
  // private static boolean containsJavaFileOrJavaFileItSelf(final File f) {
  // if (f.getName().endsWith(".java"))
  // return true;
  // if (f.isDirectory())
  // for (final File ff : f.listFiles())
  // if (f.isDirectory() && containsJavaFileOrJavaFileItSelf(ff) ||
  // f.getName().endsWith(".java"))
  // return true;
  // return false;
  // }
  //
  // }
}