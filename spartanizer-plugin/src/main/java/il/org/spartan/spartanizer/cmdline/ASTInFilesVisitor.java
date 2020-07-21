package il.org.spartan.spartanizer.cmdline;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.lang.invoke.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.library.*;
import il.org.spartan.utils.*;
import junit.framework.*;

/** Parse and AST visit all Java files under a given path.
 * <p>
 * @author Yossi Gil
 * @since 2017-03-09 */
public class ASTInFilesVisitor {
  /** What arguments to use if none are passed to main */
  protected static final String[] defaultArguments = as.array("..");
  /** Where to place our reports? */
  @External(alias = "o", value = "output folder") @SuppressWarnings("CanBeFinal") protected String outputFolder = system.tmp;
  /** The starting point of all input folders */ 
  @External(alias = "i", value = "input folder") @SuppressWarnings("CanBeFinal") protected String inputFolder = system.isWindows() ? "" : ".";
  protected static BufferedWriter out;
  protected String absolutePath;
  protected ASTVisitor astVisitor;
  protected File currentFile;
  private static String currentLocation;
  protected final List<String> locations;
  protected String presentSourceName;
  protected String presentSourcePath;
  protected String relativePath;
  @External(alias = "s", value = "silent") protected boolean silent;

  public static class BucketMethods {
    static boolean letItBeIn(final List<Statement> ¢) {
      return ¢.size() == 2 && the.firstOf(¢) instanceof VariableDeclarationStatement;
    }
    public static void main(final String[] args) {
      out = system.callingClassUniqueWriter();
      new ASTInFilesVisitor(args) {/**/}.visitAll(new ASTTrotter() {
        boolean interesting(final List<Statement> ¢) {
          return ¢ != null && ¢.size() >= 2 && !letItBeIn(¢);
        }
        @Override boolean interesting(final MethodDeclaration ¢) {
          return !¢.isConstructor() && interesting(statements(body(¢))) && leaking(descendants.streamOf(¢));
        }
        boolean leaking(final ASTNode ¢) {
          return iz.nodeTypeIn(¢, ARRAY_CREATION, METHOD_INVOCATION, CLASS_INSTANCE_CREATION, CONSTRUCTOR_INVOCATION, ANONYMOUS_CLASS_DECLARATION,
              SUPER_CONSTRUCTOR_INVOCATION, SUPER_METHOD_INVOCATION, LAMBDA_EXPRESSION);
        }
        boolean leaking(final Stream<ASTNode> ¢) {
          return ¢.noneMatch(this::leaking);
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

  public static class ExpressionChain {
    public static void main(final String[] args) {
      out = system.callingClassUniqueWriter();
      new ASTInFilesVisitor(args) {/**/}.visitAll(new ASTTrotter() {
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

  public static class FieldsOnly {
    public static void main(final String[] args) {
      new ASTInFilesVisitor(args).visitAll(new ASTVisitor(true) {
        @Override public boolean visit(final FieldDeclaration ¢) {
          System.out.println(¢);
          return true;
        }
      });
    }
  }

  public interface Listener extends Tapper {
    @Override default void beginBatch() {/**/}
    //@formatter:off
    @Override default  void  beginFile()      {/**/}
    @Override default  void  beginLocation()  {/**/}
    @Override default void endBatch() {/**/}
    @Override default  void  endFile()        {/**/}
    @Override default  void  endLocation()    {/**/}
    //@formatter:on
  }

  public static class PrintAllInterfaces {
    public static void main(final String[] args) {
      out = system.callingClassUniqueWriter();
      MethodHandles.lookup();
      new ASTInFilesVisitor(args) {/**/}.visitAll(new ASTTrotter() {
        {
          final Rule<TypeDeclaration, Object> r = Rule.on(TypeDeclaration::isInterface).go(λ -> System.out.println(λ.getName()));
          final Predicate<TypeDeclaration> q = λ -> {
            System.out.println(λ);
            return λ.isInterface();
          };
          on(TypeDeclaration.class).hook(r.beforeCheck((Consumer<TypeDeclaration>) System.out::println).beforeCheck(q).afterCheck((Consumer<TypeDeclaration>) System.out::println)
              .beforeCheck(TypeDeclaration::isInterface).afterCheck(q).afterCheck(TypeDeclaration::isInterface));
        }
      });
    }
  }

  // classes
  interface Tapper {
    void beginBatch();
    //@formatter:off
    void beginFile();
    void beginLocation();
    void endBatch();
    void endFile();
    void endLocation();
    //@formatter:on
  }

  /** @formatter:on */
  static class Tappers implements Tapper {
    /** @formatter:on */
    private final List<Tapper> inner = new LinkedList<>();

  /** @formatter:off */
  @Override public void beginBatch() { inner.forEach(Tapper::beginBatch); }
  @Override public void beginFile() { inner.forEach(Tapper::beginFile); }
  @Override public void beginLocation() { inner.forEach(Tapper::beginLocation); }
  @Override public void endBatch() { inner.forEach(Tapper::endBatch); }
  @Override public void endFile() { inner.forEach(Tapper::endFile); }
  @Override public void endLocation() { inner.forEach(Tapper::endLocation); }

    public Tappers pop() {
      inner.remove(inner.size() - 1);
      return this;
    }

    public Tappers push(final Tapper ¢) {
      inner.add(¢);
      return this;
    }
  }
  /** Check whether given string containing Java code contains {@link Test}
   * annotations
   * <p>
   * @param function
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
  static boolean letItBeIn(final List<Statement> ¢) {
    return ¢.size() == 2 && the.firstOf(¢) instanceof VariableDeclarationStatement;
  }
  public static void main(final String[] args) {
    new ASTInFilesVisitor(args) {
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
      .push(new Listener() {
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
  // constructors
  public ASTInFilesVisitor() {
    this(null);
  }

  public ASTInFilesVisitor(final String[] args) {
    locations = External.Introspector.extract(args != null && args.length != 0 ? args : defaultArguments, this);
  }
  // methods
  private void collect(final CompilationUnit ¢) {
    if (¢ != null)
      ¢.accept(astVisitor);
  }

  void collect(final String javaCode) {
  collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
 }

  public static String getCurrentLocation() {
    return currentLocation;
  }

  public ASTInFilesVisitor listen(final Listener ¢) {
    notify.push(¢);
    return this;
  }

  public static void setCurrentLocation(final String currentLocation) {
    ASTInFilesVisitor.currentLocation = currentLocation;
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
    if (FileHeuristics.isProductionCode(f) && productionCode(f))
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