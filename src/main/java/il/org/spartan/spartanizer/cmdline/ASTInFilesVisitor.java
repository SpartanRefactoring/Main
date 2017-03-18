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
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Parse and AST visit all Java files under a given path.
 * <p>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-09 */
public class ASTInFilesVisitor {
  protected static final String[] defaultArguments = as.array("..");
  @External(alias = "s", value = "silent") protected boolean silent;
  @External(alias = "i", value = "input folder") protected final String inputFolder = system.windows() ? "" : ".";
  @External(alias = "o", value = "output folder") protected final String outputFolder = system.tmp;

  public static void main(final String[] args) {
    new ASTInFilesVisitor(args) {
      /* Override here which ever method you like */
    }.fire(new ASTVisitor(true) {
      /* OVerride here which ever method you like */
    });
  }

  static BufferedWriter out;
  static {
    TrimmerLog.off();
    Trimmer.silent = true;
  }
  protected String absolutePath;
  private ASTVisitor astVisitor;
  protected Dotter dotter;
  private final List<String> locations;
  protected File presentFile;
  protected String presentSourceName;
  protected String presentSourcePath;
  protected String relativePath;

  public ASTInFilesVisitor() {
    this(null);
  }

  public ASTInFilesVisitor(final String[] args) {
    locations = External.Introspector.extract(args != null && args.length != 0 ? args : defaultArguments, this);
  }

  private void collect(final CompilationUnit ¢) {
    if (¢ != null)
      ¢.accept(astVisitor);
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  @SuppressWarnings("static-method") protected void done(final String path) {
    ___.______unused(path);
  }

  public void fire(final ASTVisitor ¢) {
    astVisitor = ¢;
    locations.forEach(this::visitLocation);
  }

  @SuppressWarnings("static-method") protected void init(final String path) {
    ___.______unused(path);
  }

  void visit(final File f) {
    monitor.debug("Visiting: " + f.getName());
    if (!silent)
      dotter.click();
    if (system.isProductionCode(f) && wizard.isProductionCode(f))
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

  protected void visitLocation(final String path) {
    init(path);
    presentSourceName = system.folder2File(presentSourcePath = inputFolder + File.separator + path);
    System.err.println("Processing: " + presentSourcePath);
    if (!silent)
      (dotter = new Dotter()).click();
    new FilesGenerator(".java").from(presentSourcePath).forEach(λ -> visit(presentFile = λ));
    done(path);
  }

  public static class BucketMethods {
    static boolean letItBeIn(final List<Statement> ¢) {
      return ¢.size() == 2 && first(¢) instanceof VariableDeclarationStatement;
    }

    public static void main(final String[] args) {
      monitor.set(monitor.LOG_TO_FILE);
      out = system.callingClassUniqueWriter();
      new ASTInFilesVisitor(args) {
        {
          silent = true;
        }
      }.fire(new ASTTrotter() {
        boolean interesting(final List<Statement> ¢) {
          return ¢ != null && ¢.size() >= 2 && !letItBeIn(¢);
        }

        @Override
        protected
        boolean interesting(final MethodDeclaration ¢) {
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
      monitor.set(monitor.LOG_TO_FILE);
      out = system.callingClassUniqueWriter();
      new ASTInFilesVisitor(args) {
        {
          silent = true;
        }
      }.fire(new ASTTrotter() {
        {
          hookRuleOnClass(ExpressionStatement.class, new Rule.Stateful<ExpressionStatement, Void>() {
            @Override public Void fire() {
              return null;
            }
          
            @Override public boolean ok(final ExpressionStatement ¢) {
              return extract.usedNames(¢.getExpression()).size() == 1;
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
      new ASTInFilesVisitor(args).fire(new ASTVisitor(true) {
        @Override public boolean visit(final FieldDeclaration ¢) {
          System.out.println(¢);
          return true;
        }
      });
    }
  }

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
}