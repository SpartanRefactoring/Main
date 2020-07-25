package il.org.spartan.spartanizer.cmdline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import fluent.ly.as;
import fluent.ly.note;
import fluent.ly.system;
import il.org.spartan.bench.Dotter;
import il.org.spartan.collections.FilesGenerator;
import il.org.spartan.external.External;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.utils.FileUtils;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-07-04 */
public class JavaProductionFilesVisitor {
  protected static final String[] defaultArguments = as.array("..");
  @External(alias = "c", value = "corpus name") @SuppressWarnings("CanBeFinal") protected static String corpus = "";
  @External(alias = "i", value = "input folder") @SuppressWarnings("CanBeFinal") protected static String inputFolder = system.isWindows() ? "" : ".";
  @External(alias = "o", value = "output folder") @SuppressWarnings("CanBeFinal") protected static String outputFolder = system.tmp;
  @External(alias = "s", value = "silent") protected boolean silent;
  public final Current current;

  interface template {
    interface B {}
  }

  /** Current contains the information on the current file being under analysis
   * @author Yossi Gil
   * @since 2017-07-16 */
  public static class Current {
    public Current(List<String> locations) {
      CurrentData.locations = locations.subList(0, locations.size());
    }

    public CurrentData data = new CurrentData();
  }

  public JavaProductionFilesVisitor(Current current) {
    this.current = current;
  }
  public JavaProductionFilesVisitor(String[] args) {
    current = new Current(External.Introspector.extract(args(args), this));
  }
  private static String[] args(String[] args) {
    return args != null && args.length != 0 ? args : defaultArguments;
  }
  public static void main(final String[] args) {
    new GrandVisitor(args) {
      /* Override here which ever method you like */
    }.visitAll(new ASTVisitor(true) {
      /* OVerride here which ever method you like */
    });
  }

  public final Tappers notify = new Tappers()//
      .push(new Tapper() {
    /** @formatter:off */
          Dotter dotter = new Dotter();
          @Override public void beginBatch() { dotter.click(); }
          @Override public void beginFile() { dotter.click(); }
          @Override public void beginLocation() { dotter.click(); }
          @Override public void endBatch() { dotter.end(); }
          @Override public void endFile() { dotter.click(); }
          @Override public void endLocation() { dotter.clear(); }
          }
        );

  public JavaProductionFilesVisitor listen(final Tapper ¢) {
    notify.push(¢);
    return this;
  }

  public void visitAll(final ASTVisitor ¢) {
    notify.beginBatch();
    CurrentData.visitor = ¢;

      CurrentData.locations.forEach(
          λ -> {
            CurrentData.location = λ;
            notify.beginLocation();
            visitLocation();
            notify.endLocation();
          }
          );
      notify.endBatch();

    
  }

  public void visitFile(final File f) {
    //System.out.println(f);
    notify.beginFile();
    //if (FileHeuristics.p(f))
      try {
        CurrentData.absolutePath = f.getAbsolutePath();
        CurrentData.relativePath = f.getPath();
        collect(FileUtils.read(f));
      } catch (final IOException ¢) {
        note.io(¢, "File = " + f);
      }
    notify.endFile();
  }

  private static void collect(final CompilationUnit ¢) {
    if (¢ != null)
      ¢.accept(CurrentData.visitor);
  }

  protected void visitLocation() {
    CurrentData.locationName = system.folder2File(CurrentData.locationPath = inputFolder + File.separator + CurrentData.location); 
    new FilesGenerator(".java").from(CurrentData.locationPath)
                               .forEach(λ -> visitFile(CurrentData.file = λ));
  }

  static void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
   }}
