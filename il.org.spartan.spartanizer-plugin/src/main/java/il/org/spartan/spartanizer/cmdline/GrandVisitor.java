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

/** Parse and AST visit all Java files under a given path.
 * <p>
 * @author Yossi Gil
 * @since 2017-03-09 */
public class GrandVisitor {
  public static class ExpressionChain {
    public static void main(final String[] args) {
      out = system.callingClassUniqueWriter();
      new GrandVisitor(args) {/**/}.visitAll(new ASTTrotter() {
        {
          hookClassOnRule(ExpressionStatement.class, new Rule.Stateful<ExpressionStatement, Void>() {
            @Override public Void fire() {
              return null;
            }
            @Override public boolean ok(final ExpressionStatement ¢) {
              return compute.useSpots(¢.getExpression()).size() == 1;
            }
          });
        }

        @Override protected void record(final String summary) {
          try {
            out.write(summary);
          } catch (final IOException ¢) {
            System.err.println("Error: " + ¢.getMessage());
          }
          super.record(summary);
        }
      });
    }
  }

  protected static final String[] defaultArguments = as.array("..");
  public static BufferedWriter out;

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
  public static void main(final String[] args) {
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

  protected String absolutePath;
  private ASTVisitor astVisitor;
  @External(alias = "c", value = "corpus name") @SuppressWarnings("CanBeFinal") protected String corpus = "";
  protected File currentFile;
  private String currentLocation;
  @External(alias = "i", value = "input folder") @SuppressWarnings("CanBeFinal") protected String inputFolder = system.isWindows() ? "" : ".";
  private final List<String> locations;
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

  @External(alias = "o", value = "output folder") @SuppressWarnings("CanBeFinal") protected String outputFolder = system.tmp;

  protected String presentSourceName;

  protected String presentSourcePath;

  protected String relativePath;

    @External(alias = "s", value = "silent") protected boolean silent;

  public GrandVisitor() {
    this(null);
  }

  public GrandVisitor(final String[] args) {
    locations = External.Introspector.extract(args != null && args.length != 0 ? args : defaultArguments, this);
  }

  private void collect(final CompilationUnit ¢) {
    if (¢ != null)
      ¢.accept(astVisitor);
  }

  void collect(final String javaCode) {
  collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
 }

  public String getCurrentLocation() {
    return currentLocation;
  }

  public GrandVisitor listen(final Tapper ¢) {
    notify.push(¢);
    return this;
  }

  public void setCurrentLocation(final String currentLocation) {
    this.currentLocation = currentLocation;
  }

  public void visitAll(final ASTVisitor ¢) {
    notify.beginBatch();
    astVisitor = ¢;
    locations.forEach(
        λ -> {
          setCurrentLocation(λ);
          visitLocation();
        }
        );
    notify.endBatch();
  }

  public void visitFile(final File f) {
    notify.beginFile();
    if (Utils.isProductionCode(f) && productionCode(f))
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
    notify.beginLocation();
    presentSourceName = system.folder2File(presentSourcePath = inputFolder + File.separator + getCurrentLocation());
    new FilesGenerator(".java").from(presentSourcePath).forEach(λ -> visitFile(currentFile = λ));
    notify.endLocation();
  }
}