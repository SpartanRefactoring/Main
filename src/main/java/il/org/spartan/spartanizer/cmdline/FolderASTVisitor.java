package il.org.spartan.spartanizer.cmdline;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static il.org.spartan.spartanizer.ast.navigate.trivia.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

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

/** Parse and visit all Java files under a given path.
 * @author Yossi Gil
 * @since Dec 14, 2016 */
public abstract class FolderASTVisitor extends ASTVisitor {
  @External(alias = "i", value = "input folder") protected static String inputFolder = system.windows() ? "" : ".";
  @External(alias = "o", value = "output folder") protected static String outputFolder = "/tmp";
  protected static final String[] defaultArguments = as.array("..");
  protected static Class<? extends FolderASTVisitor> clazz;
  private static Constructor<? extends FolderASTVisitor> declaredConstructor;
  protected File presentFile;
  protected static String presentSourceName;
  private static int $;
  protected String presentSourcePath;
  protected Dotter dotter;
  protected String absolutePath;
  protected String relativePath;
  private boolean silent = true;
  static {
    TrimmerLog.off();
    Trimmer.silent = true;
  }

  private static Constructor<? extends FolderASTVisitor> declaredConstructor() {
    if (clazz == null) {
      monitor.logProbableBug(clazz, fault.stackCapture());
      System.exit(1);
    }
    try {
      return declaredConstructor != null ? declaredConstructor : clazz.getConstructor();
    } catch (NoSuchMethodException | SecurityException ¢) {
      monitor.logProbableBug(clazz, ¢);
      System.err.println("Make sure that class " + clazz + " is not abstract and that it has a default constructor");
      throw new RuntimeException();
    }
  }

  public static void main(final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    visit(args.length != 0 ? args : defaultArguments);
  }

  public static void visit(final String... arguments) throws InstantiationException, IllegalAccessException, InvocationTargetException {
    for (final String ¢ : External.Introspector.extract(arguments, clazz))
      declaredConstructor().newInstance().visit(¢);// NANO - can't, throws
  }

  @SuppressWarnings("static-method") protected void done(final String path) {
    ___.______unused(path);
  }

  @SuppressWarnings("static-method") protected void init(final String path) {
    ___.______unused(path);
  }

  protected static String makeFile(final String fileName) {
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
      u.accept(this);
    } catch (final NullPointerException ¢) {
      ¢.printStackTrace();
    }
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  void visit(final File f) {
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

  /** Check if a file contains Test annotations
   * <p>
   * @param f
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  public static boolean containsTestAnnotation(final File f) {
    String javaCode = null;
    try {
      javaCode = FileUtils.read(f);
    } catch (final IOException x) {
      monitor.infoIOException(x, "File = " + f);
    }
    return containsTestAnnotation(javaCode);
  }

  /** Check if a String contains Test annotations
   * <p>
   * @param f
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  public static boolean containsTestAnnotation(final String javaCode) {
    final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode);
    $ = 0;
    cu.accept(new ASTVisitor() {
      @SuppressWarnings("synthetic-access") @Override public boolean visit(final MethodDeclaration node) {
        if (extract.annotations(node).stream().anyMatch(λ -> "@Test".equals(λ + "")))
          ++$;
        return false;
      }
    });
    return $ > 0;
  }

  public static class FieldsOnly extends FolderASTVisitor {
    public static void main(final String[] args)
        throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      clazz = FieldsOnly.class;
      FolderASTVisitor.main(args);
    }

    @Override public boolean visit(final FieldDeclaration ¢) {
      System.out.println(¢);
      return true;
    }
  }

  public static class SinkMethods extends FolderASTVisitor {
    private static BufferedWriter out;
    private int interesting;
    private int total;

    public static void main(final String[] args)
        throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      clazz = SinkMethods.class;
      try {
        out = new BufferedWriter(new FileWriter("/tmp/out.txt", false));
      } catch (IOException x) {
        monitor.infoIOException(x);
        return;
      }
      FolderASTVisitor.main(args);
    }

    @Override @SuppressWarnings("boxing") public boolean visit(final MethodDeclaration d) {
      ++total;
      if (interesting(d)) {
        ++interesting;
        String summary = squeezeSpaces(theSpartanizer.fixedPoint(removeComments(normalize.code(d + "")))) + "\n";
        System.out.printf("%d/%d=%5.2f%% %s", interesting, total, 100. * interesting / total, summary);
        try {
          out.write(summary);
        } catch (IOException ¢) {
          System.err.println("Error: " + ¢.getMessage());
        }
      }
      return true;
    }

    private static boolean interesting(MethodDeclaration $) {
      if ($.isConstructor())
        return false;
      List<Statement> ss = statements($.getBody());
      return ss != null && ss.size() >= 2 && yieldDescendants.of($).stream().noneMatch(λ -> leaking(λ));
    }

    private static boolean leaking(ASTNode ¢) {
      return iz.nodeTypeIn(¢, ARRAY_CREATION, METHOD_INVOCATION, CLASS_INSTANCE_CREATION, CONSTRUCTOR_INVOCATION, ANONYMOUS_CLASS_DECLARATION,
          SUPER_CONSTRUCTOR_INVOCATION, SUPER_METHOD_INVOCATION, LAMBDA_EXPRESSION);
    }
  }
}
