package il.org.spartan.spartanizer.cmdline.visitor;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.library.*;
import il.org.spartan.spartanizer.cmdline.visitor.*;
import il.org.spartan.utils.*;
import junit.framework.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-07-04 */
public class MasterVisitor {
  private static ASTVisitor visitor;
  @External(alias = "c", value = "corpus name") @SuppressWarnings("CanBeFinal") protected static String corpus = "";
  protected static final String[] defaultArguments = as.array("..");
  @External(alias = "i", value = "input folder") @SuppressWarnings("CanBeFinal") protected static String inputFolder = system.isWindows() ? "" : ".";
  @External(alias = "o", value = "output folder") @SuppressWarnings("CanBeFinal") protected static String outputFolder = system.tmp;
  public static void main(final String[] args) {
    new MasterVisitor(args) {
      /* Override here which ever method you like */
    }.visitAll(new ASTVisitor(true) {
      /* OVerride here which ever method you like */
    });
  }
  private static String[] args(String[] args) {
    return args != null && args.length != 0 ? args : defaultArguments;
  } 
  
  private static void collect(final CompilationUnit ¢) {
    if (¢ != null)
      ¢.accept(visitor);
  }

  static void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
   }
  
  public final List<String> locations = new ArrayList<>();
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

  protected String absolutePath;
  private String locationPath;
  protected String relativePath;
  public String location;

  @External(alias = "s", value = "silent") protected boolean silent;
  public MasterVisitor(String[] args) {
    locations.addAll(External.Introspector.extract(args(args), this));
  }

  public MasterVisitor listen(final Tapper ¢) {
    notify.push(¢);
    return this;
  }

  public void visitAll(final ASTVisitor ¢) {
    notify.beginBatch();
    visitor = ¢;
    locations.forEach(
        λ -> {
          location = λ;
          notify.beginLocation();
          visitLocation();
          notify.endLocation();
        }
        );
    notify.endBatch();
  }

  private void visitFile(final File f) {
    notify.beginFile();
    if (FileHeuristics.p(f))
      try {
        absolutePath = f.getAbsolutePath();
        relativePath = f.getPath();
        collect(FileUtils.read(f));
      } catch (final IOException ¢) {
        note.io(¢, "File = " + f);
      }
    notify.endFile();
  }

  protected void visitLocation() {
    system.folder2File(locationPath = inputFolder + File.separator + location); 
    new FilesGenerator(".java").from(locationPath)
                               .forEach(λ -> visitFile(λ));
  }}
