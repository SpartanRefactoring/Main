package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.library.*;
import il.org.spartan.utils.*;
import junit.framework.*;

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

  /** Check whether given string containing Java code contains {@link Test}
   * annotations
   * <p>
   * @param f
   * @return */
  public static boolean containsTestAnnotation(final String javaCode) {
    final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    final Bool $ = new Bool();
    cu.accept(new ASTTrotter() {
      @Override public boolean visit(final MethodDeclaration node) {
        if (extract.annotations(node).stream().noneMatch(λ -> "@Test".equals(λ + "")))
          return true;
        startFolding();
        $.set();
        return true;
      }
    });
    return $.get();
  }

interface template  {
  interface B{}
}
  /**
   * Current contains the information on the current file being under analysis 
   * 
   * @author Yossi Gil
   * @since 2017-07-16
   */

  public static class Current {
    public Current(List<String> locations) {
      this.locations = locations.subList(0, locations.size());
    }
    public File file;
    public String fileName;
    public String absolutePath;
    public String location;
    public BufferedWriter out;
    public final List<String> locations;
    public ASTVisitor visitor;
    public String relativePath;
    public String locationPath;
    public String locationName;
    protected String before;
    protected String after;
  }
  
  
  public JavaProductionFilesVisitor(Current current) {
    this.current = current;
  }
  
  public JavaProductionFilesVisitor(String[] args) {
    List<String> extract = External.Introspector.extract(args(args), this);
    System.err.println("extract.size:\t" + extract.get(0));
    current = new Current(extract);
  }

  private String[] args(String[] args) {
    System.err.println("---->" + args);
    return args != null && args.length != 0 ? args : defaultArguments;
  }
  
  public static void main(final String[] args) throws IOException {
    new GrandVisitor(args) {
      /* Override here which ever method you like */
    }.visitAll(new ASTVisitor(true) {
      /* OVerride here which ever method you like */
    });
  }
  /** Determines whether a file is production code, using the heuristic that
   * production code does not contain {@code @}{@link Test} annotations
   * <p>
   * @return */
  public static boolean productionCode(@¢ final File $) {
    try {
      return !containsTestAnnotation(FileUtils.read($));
    } catch (final IOException ¢) {
      note.io(¢, "File = " + $);
      return false;
    }
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
    current.visitor = ¢;
    current.locations.forEach(
        λ -> {
          current.location = λ;
          notify.beginLocation();
          visitLocation();
          notify.endLocation();
        }
        );
    notify.endBatch();
  }

  public void visitFile(final File f) {
    notify.beginFile();
    if (Utils.isProductionCode(f) && productionCode(f))
      try {
        current.absolutePath = f.getAbsolutePath();
        current.relativePath = f.getPath();
        collect(FileUtils.read(f));
      } catch (final IOException ¢) {
        note.io(¢, "File = " + f);
      }
    notify.endFile();
  }

  private void collect(final CompilationUnit ¢) {
    if (¢ != null)
      ¢.accept(current.visitor);
  }

  protected void visitLocation() {
    //notify.beginLocation();
    current.locationName = system.folder2File(current.locationPath = inputFolder + File.separator + current.location); 
    new FilesGenerator(".java").from(current.locationPath)
                               .forEach(λ -> {//notify.beginFile();
                               visitFile(current.file = λ);
                               //notify.endFile();
                               });
    //notify.endLocation();
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
   }}
