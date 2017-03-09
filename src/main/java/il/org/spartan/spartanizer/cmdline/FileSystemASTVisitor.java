package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.ast.navigate.trivia.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.io.*;
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
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** Parse and AST visit all Java files under a given path.
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-09 */
public class FileSystemASTVisitor {
  @External(alias = "i", value = "input folder") protected String inputFolder = system.windows() ? "" : ".";
  @External(alias = "o", value = "output folder") protected String outputFolder = system.tmp();
  @External(alias = "s", value = "silent") protected boolean silent;
  protected static final String[] defaultArguments = as.array("..");
  protected static Class<? extends FileSystemASTVisitor> clazz;
  protected File presentFile;
  protected String presentSourceName;
  protected String presentSourcePath;
  protected Dotter dotter;
  protected String absolutePath;
  protected String relativePath;
  private ASTVisitor astVisitor;
  private final List<String> locations;
  static {
    TrimmerLog.off();
    Trimmer.silent = true;
  }

  public FileSystemASTVisitor() {
    this(null);
  }

  public FileSystemASTVisitor(String[] args) {
    locations = External.Introspector.extract(args != null && args.length != 0 ? args : defaultArguments, this);
  }

  public static void main(final String[] args) {
    new FileSystemASTVisitor(args) {
      /* Override here which every method you like */
    }.fire(new ASTVisitor() {
      /* OVerride here which every method you like */
    });
  }

  public void fire(ASTVisitor v) {
    astVisitor = v;
    for (final String location : locations)
      visit(location);// NANO - can't, throws
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

  protected void visit(final String path) {
    init(path);
    presentSourceName = system.folder2File(presentSourcePath = inputFolder + File.separator + path);
    System.err.println("Processing: " + presentSourcePath);
    if (!silent)
      (dotter = new Dotter()).click();
    new FilesGenerator(".java").from(presentSourcePath).forEach(λ -> visit(presentFile = λ));
    done(path);
  }

  void collect(final CompilationUnit u) {
    try {
      u.accept(astVisitor);
    } catch (final NullPointerException ¢) {
      ¢.printStackTrace();
    }
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  void visit(final File f) {
    monitor.debug("Visiting: " + f.getName());
    if (!silent)
      dotter.click();
    if (!system.isTestFile(f) && !containsTestAnnotation(f))
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

  public static boolean containsTestAnnotation(final File $) {
    try {
      return containsTestAnnotation(FileUtils.read($));
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, "File = " + $);
      return true;
    }
  }

  /** Check if a String contains Test annotations
   * <p>
   * @param f
   * @return */
  public static boolean containsTestAnnotation(final String javaCode) {
    final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    final Bool $ = new Bool();
    cu.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration node) {
        if (extract.annotations(node).stream().anyMatch(λ -> "@Test".equals(λ + "")))
          $.set();
        return !$.get();
      }
    });
    return $.get();
  }

  public static class FieldsOnly {
    public static void main(final String[] args) {
      new FileSystemASTVisitor(args).fire(new ASTVisitor() {
        @Override public boolean visit(final FieldDeclaration ¢) {
          System.out.println(¢);
          return true;
        }
      });
    }
  }

  public static class BucketMethods {
    static BufferedWriter out;
    static int interesting;
    static int total;

    public static void main(final String[] args) {
      try {
        out = new BufferedWriter(new FileWriter("/tmp/out.txt", false));
      } catch (final IOException ¢) {
        monitor.infoIOException(¢);
        return;
      }
      new FileSystemASTVisitor(args).fire(new ASTVisitor() {
        @Override @SuppressWarnings("boxing") public boolean visit(final MethodDeclaration d) {
          ++total;
          if (interesting(d)) {
            ++interesting;
            final String summary = squeezeSpaces(theSpartanizer.fixedPoint(removeComments(normalize.code(d + "")))) + "\n";
            System.out.printf("%d/%d=%5.2f%% %s", interesting, total, 100. * interesting / total, summary);
            try {
              out.write(summary);
            } catch (final IOException ¢) {
              System.err.println("Error: " + ¢.getMessage());
            }
          }
          return true;
        }
      });
    }

    static boolean interesting(final MethodDeclaration ¢) {
      return !¢.isConstructor() && interesting(statements(body(¢))) && leaking(descendants.streamOf(¢));
    }

    static boolean leaking(final Stream<ASTNode> ns) {
      return ns.noneMatch(λ -> leaking(λ));
    }

    private static boolean interesting(final List<Statement> ¢) {
      return ¢ != null && ¢.size() >= 2 && !letItBeIn(¢);
    }

    static boolean letItBeIn(final List<Statement> ¢) {
      return ¢.size() == 2 && first(¢) instanceof VariableDeclarationStatement;
    }

    private static boolean leaking(final ASTNode ¢) {
      return iz.nodeTypeIn(¢, ARRAY_CREATION, METHOD_INVOCATION, CLASS_INSTANCE_CREATION, CONSTRUCTOR_INVOCATION, ANONYMOUS_CLASS_DECLARATION,
          SUPER_CONSTRUCTOR_INVOCATION, SUPER_METHOD_INVOCATION, LAMBDA_EXPRESSION);
    }
  }
}
