package il.org.spartan.spartanizer.cmdline;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.io.*;
import java.lang.invoke.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** Parse and AST visit all Java files under a given path.
 * <p>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-09 */
public class ASTInFilesVisitor {
  /** Determines whether a file is production code, using the heuristic that
   * production code does not contain {@code @}{@link Test} annotations
   * <p>
   * @return */
  public static boolean productionCode(@¢ final File $) {
    try {
      return !containsTestAnnotation(FileUtils.read($));
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, "File = " + $);
      return false;
    }
  }

  /** Check if some java contains {@link Test} annotations
   * <p>
   * @param f
   * @return */
  public static boolean containsTestAnnotation(final String javaCode) {
    final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    final Bool $ = new Bool();
    cu.accept(new ASTTrotter() {
      @Override public boolean visit(final MethodDeclaration node) {
        if (extract.annotations(node).stream().anyMatch(λ -> "@Test".equals(λ + ""))) {
          startFolding();
          $.set();
        }
        return true;
      }
    });
    return $.get();
  }

  public static void main(final String[] args) {
    new ASTInFilesVisitor(args) {
      /* Override here which ever method you like */
    }.fire(new ASTVisitor(true) {
      /* OVerride here which ever method you like */
    });
  }

  protected static Class<? extends ASTInFilesVisitor> clazz;
  protected static final String[] defaultArguments = as.array("..");
  static {
    TrimmerLog.off();
    Trimmer.silent = true;
  }

  public ASTInFilesVisitor() {
    this(null);
  }

  public ASTInFilesVisitor(final String[] args) {
    locations = External.Introspector.extract(args != null && args.length != 0 ? args : defaultArguments, this);
  }

  public void fire(final ASTVisitor ¢) {
    astVisitor = ¢;
    locations.forEach(this::visitLocation);
  }

  private void collect(final CompilationUnit ¢) {
    if (¢ != null)
      ¢.accept(astVisitor);
  }

  @SuppressWarnings("static-method") protected void done(final String path) {
    ___.______unused(path);
  }

  @SuppressWarnings("static-method") protected void init(final String path) {
    ___.______unused(path);
  }

  protected void visitLocation(final String path) {
    init(path);
    presentSourceName = system.folder2File(presentSourcePath = inputFolder + File.separator + path);
    System.err.println("Processing: " + presentSourcePath);
    if (!silent)
      (dotter = new Dotter()).click();
    new FilesGenerator(".java").from(presentSourcePath).forEach(λ -> visit(presentFile = λ));
    done(path);
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  void visit(final File f) {
    monitor.debug("Visiting: " + f.getName());
    if (!silent)
      dotter.click();
    if (system.isProductionCode(f) && productionCode(f))
      try {
        absolutePath = f.getAbsolutePath();
        relativePath = f.getPath();
        collect(FileUtils.read(f));
        if (!silent)
          dotter.click();
      } catch (final IOException ¢) {
        monitor.infoIOException(¢, "File = " + f);
      }
  }

  private ASTVisitor astVisitor;
  private final List<String> locations;
  protected String absolutePath;
  protected Dotter dotter;
  @External(alias = "i", value = "input folder") protected final String inputFolder = system.windows() ? "" : ".";
  @External(alias = "o", value = "output folder") protected final String outputFolder = system.tmp;
  protected File presentFile;
  protected String presentSourceName;
  protected String presentSourcePath;
  protected String relativePath;
  @External(alias = "s", value = "silent") protected boolean silent;

  public static class PrintAllInterfaces {
    public static void main(final String[] args) {
      monitor.set(monitor.LOG_TO_FILE);
      out = system.callingClassUniqueWriter();
      MethodHandles.lookup();
      new ASTInFilesVisitor(args) {
        {
          silent = true;
        }
      }.fire(new ASTTrotter() {
        {
          hook(TypeDeclaration.class, //
              Rule.on((TypeDeclaration t) -> t.isInterface())//
                  .go(λ -> System.out.println(λ.getName())//
          )//
          );
        }
      });
    }
  }

  static boolean letItBeIn(final List<Statement> ¢) {
    return ¢.size() == 2 && first(¢) instanceof VariableDeclarationStatement;
  }

  static BufferedWriter out;

  public static class BucketMethods {
    public static void main(final String[] args) {
      monitor.set(monitor.LOG_TO_FILE);
      out = system.callingClassUniqueWriter();
      new ASTInFilesVisitor(args) {
        {
          silent = true;
        }
      }.fire(new ASTTrotter() {
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

    private static String myClass() {
      return MethodHandles.lookup().lookupClass().getClass().getSimpleName();
    }

    static Class<?> myEnclosingClass() {
      return new Object().getClass().getEnclosingClass();
    }

    static boolean letItBeIn(final List<Statement> ¢) {
      return ¢.size() == 2 && first(¢) instanceof VariableDeclarationStatement;
    }
  }

  public static class ExpressionChain {
    public static void main(final String[] args) {
      monitor.set(monitor.LOG_TO_FILE);
      out = system.callingClassUniqueWriter();
      new ASTInFilesVisitor(args) {
        {
          silent = true;
        }
      }.fire(new ASTTrotter() {
        @Override protected void record(final String summary) {
          try {
            out.write(summary);
          } catch (final IOException ¢) {
            System.err.println("Error: " + ¢.getMessage());
          }
          super.record(summary);
        }

        {
          hook(ExpressionStatement.class, new Rule.Stateful<ExpressionStatement, Void>() {
            @Override public Void fire() {
              return null;
            }

            @Override public boolean ok(final ExpressionStatement ¢) {
              return extract.usedNames(¢.getExpression()).size() == 1;
            }
          });
        }
      });
    }
  }

  public static class FieldsOnly {
    public static void main(final String[] args) {
      new ASTInFilesVisitor(args).fire(new ASTVisitor(true) {
        @Override public boolean visit(final FieldDeclaration ¢) {
          System.out.println(¢);
          return true;
        }
      });
    }
  }
}