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
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-09 */
public class FileSystemASTVisitor {
  /** Check if a File contains {@link Test} annotations
   * <p>
   * @param f
   * @return */
  public static boolean noTests(@JD final File $) {
    try {
      return !containsTestAnnotation(FileUtils.read($));
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, "File = " + $);
      return false;
    }
  }

  /** Check if a String contains {@link Test} annotations
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
    new FileSystemASTVisitor(args) {
      /* Override here which every method you like */
    }.fire(new ASTVisitor(true) {
      /* OVerride here which every method you like */
    });
  }

  protected static Class<? extends FileSystemASTVisitor> clazz;
  protected static final String[] defaultArguments = as.array("..");
  static {
    TrimmerLog.off();
    Trimmer.silent = true;
  }

  public FileSystemASTVisitor() {
    this(null);
  }

  public FileSystemASTVisitor(final String[] args) {
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

  protected String makeFile(final String fileName) {
    return outputFolder + File.separator + (system.windows() || presentSourceName == null ? fileName : presentSourceName + "." + fileName);
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
//    System.out.println(" -------------- ");
//    System.out.println(" -------------- > " + f.getName());
//    System.out.println(" -------------- ");
    monitor.debug("Visiting: " + f.getName());
    if (!silent)
      dotter.click();
    if (!system.isTestFile(f) && noTests(f))
      try {
        absolutePath = f.getAbsolutePath();
        relativePath = f.getPath();
        collect(FileUtils.read(f));
        if (!silent)
          dotter.click();
      } catch (final IOException ¢) {
        monitor.infoIOException(¢, "File = " + f);
      } 
//    catch (final Exception ¢) {
//        ¢.printStackTrace();
//      }
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

  public static class BucketMethods {
    public static void main(final String[] args) {
      monitor.set(monitor.LOG_TO_FILE);
      try {
        out = new BufferedWriter(new FileWriter(system.ephemeral(myClass()).dot("txt")));
      } catch (final IOException ¢) {
        monitor.infoIOException(¢);
        return;
      }
      new FileSystemASTVisitor(args) {
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

    static BufferedWriter out;
  }

  public static class ExpressionChain {
    public static void main(final String[] args) {
      monitor.set(monitor.LOG_TO_FILE);
      try {
        out = new BufferedWriter(new FileWriter("/tmp/out.txt", false));
      } catch (final IOException ¢) {
        monitor.infoIOException(¢);
        return;
      }
      new FileSystemASTVisitor(args) {
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

    static BufferedWriter out;
  }

  public static class FieldsOnly {
    public static void main(final String[] args) {
      new FileSystemASTVisitor(args).fire(new ASTVisitor(true) {
        @Override public boolean visit(final FieldDeclaration ¢) {
          System.out.println(¢);
          return true;
        }
      });
    }
  }
}
