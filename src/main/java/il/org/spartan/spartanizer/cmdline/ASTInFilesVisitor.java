package il.org.spartan.spartanizer.cmdline;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.lang.invoke.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;
import junit.framework.*;
import nano.ly.*;

/** Parse and AST visit all Java files under a given path.
 * <p>
 * @author Yossi Gil
 * @since 2017-03-09 */
public class ASTInFilesVisitor {
  protected static final String[] defaultArguments = as.array("..");
  protected static BufferedWriter out;

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

  static boolean letItBeIn(final List<Statement> ¢) {
    return ¢.size() == 2 && first(¢) instanceof VariableDeclarationStatement;
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
  private ASTVisitor astVisitor;

  private final List<String> locations;

  protected String absolutePath;

  @External(alias = "c", value = "corpus name") @SuppressWarnings("CanBeFinal") protected String corpus = "";
  @External(alias = "i", value = "input folder") @SuppressWarnings("CanBeFinal") protected String inputFolder = system.isWindows() ? "" : ".";
  @External(alias = "o", value = "output folder") @SuppressWarnings("CanBeFinal") protected String outputFolder = system.tmp;
  protected File currentFile;
  protected String presentSourceName;
  protected String presentSourcePath;
  protected String relativePath;
  @External(alias = "s", value = "silent") protected boolean silent;
  private String currentLocation;
  public ASTInFilesVisitor() {
    this(null);
  }

  public ASTInFilesVisitor(final String[] args) {
    locations = External.Introspector.extract(args != null && args.length != 0 ? args : defaultArguments, this);
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

  private void collect(final CompilationUnit ¢) {
    if (¢ != null)
      ¢.accept(astVisitor);
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

    void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  public static class BucketMethods {
    public static void main(final String[] args) {
      out = system.callingClassUniqueWriter();
      new ASTInFilesVisitor(args) {/**/}.visitAll(new ASTTrotter() {
        @Override protected void record(final String summary) {
          try {
            out.write(summary);
          } catch (final IOException ¢) {
            System.err.println("Error: " + ¢.getMessage());
          }
          super.record(summary);
        }

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
      });
    }

    static boolean letItBeIn(final List<Statement> ¢) {
      return ¢.size() == 2 && first(¢) instanceof VariableDeclarationStatement;
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

  public static class PrintAllInterfaces {
    public static void main(final String[] args) {
      out = system.callingClassUniqueWriter();
      MethodHandles.lookup();
      new ASTInFilesVisitor(args) {/**/}.visitAll(new ASTTrotter() {
        {
          final Rule<TypeDeclaration, Object> r = Rule.on((final TypeDeclaration t) -> t.isInterface()).go(λ -> System.out.println(λ.getName()));
          final Predicate<TypeDeclaration> p = λ -> λ.isInterface(), q = λ -> {
            System.out.println(λ);
            return λ.isInterface();
          };
          final Consumer<TypeDeclaration> c = λ -> System.out.println(λ);
          on(TypeDeclaration.class).hook(r.beforeCheck(c).beforeCheck(q).afterCheck(c).beforeCheck(p).afterCheck(q).afterCheck(p));
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

  public ASTInFilesVisitor listen(final Listener ¢) {
    notify.push(¢);
    return this;
  }

  public String getCurrentLocation() {
    return currentLocation;
  }

  public void setCurrentLocation(final String currentLocation) {
    this.currentLocation = currentLocation;
  }
}