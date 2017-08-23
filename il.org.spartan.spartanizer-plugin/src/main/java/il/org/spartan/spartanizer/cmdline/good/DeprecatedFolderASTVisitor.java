package il.org.spartan.spartanizer.cmdline.good;

import static il.org.spartan.spartanizer.engine.nominal.Trivia.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.library.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Parse and visit all Java files under a given path.
 * @author Yossi Gil
 * @since Dec 14, 2016 */
public abstract class DeprecatedFolderASTVisitor extends ASTVisitor {
  @External(alias = "i", value = "input folder") @SuppressWarnings("CanBeFinal") protected static String inputFolder = system.isWindows() ? "" : ".";
  @External(alias = "o", value = "output folder") @SuppressWarnings("CanBeFinal") protected static String outputFolder = "/tmp";
  protected static final String[] defaultArguments = as.array("..");
  protected static Class<? extends DeprecatedFolderASTVisitor> clazz;
  private static Constructor<? extends DeprecatedFolderASTVisitor> declaredConstructor;
  protected File presentFile;
  protected static String presentSourceName;
  static int whatThisGlobalStaticVariableDoing;
  protected String presentSourcePath;
  protected Dotter dotter;
  protected String absolutePath;
  protected String relativePath;
  private boolean silent;
  static {
    TraversalMonitor.off();
  }

  private static Constructor<? extends DeprecatedFolderASTVisitor> declaredConstructor() {
    if (clazz == null) {
      note.bug(clazz, fault.stackCapture());
      System.exit(1);
    }
    try {
      return declaredConstructor != null ? declaredConstructor : clazz.getConstructor();
    } catch (NoSuchMethodException | SecurityException ¢) {
      note.bug(clazz, ¢);
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
    forget.em(new Object[] { path });
  }
  @SuppressWarnings("static-method") protected void init(final String path) {
    forget.em(new Object[] { path });
  }
  protected static String makeFile(final String fileName) {
    return outputFolder + File.separator + (system.isWindows() || presentSourceName == null ? fileName : presentSourceName + "." + fileName);
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
      note.bug(this, ¢);
    }
  }
  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }
  void visit(final File f) {
    if (!silent)
      dotter.click();
    if (!FileHeuristics.isTestFile(f) && FileHeuristics.productionCode(f))
      try {
        absolutePath = f.getAbsolutePath();
        relativePath = f.getPath();
        collect(FileUtils.read(f));
        if (!silent)
          dotter.click();
      } catch (final IOException ¢) {
        note.io(¢, "File = " + f);
      }
  }

  public static class FieldsOnly extends DeprecatedFolderASTVisitor {
    public static void main(final String[] args)
        throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      clazz = FieldsOnly.class;
      DeprecatedFolderASTVisitor.main(args);
    }
    @Override public boolean visit(final FieldDeclaration ¢) {
      System.out.println(¢);
      return true;
    }
  }

  public static class BucketMethods extends DeprecatedFolderASTVisitor {
    private static BufferedWriter out;
    private int interesting;
    private int total;

    public static void main(final String[] args)
        throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      clazz = BucketMethods.class;
      try {
        out = new BufferedWriter(new FileWriter("/tmp/out.txt", false));
      } catch (final IOException ¢) {
        note.io(¢);
        return;
      }
      DeprecatedFolderASTVisitor.main(args);
    }
    @Override @SuppressWarnings("boxing") public boolean visit(final MethodDeclaration d) {
      ++total;
      if (!interesting(d))
        return true;
      ++interesting;
      final String summary = squeeze(theSpartanizer.repetitively(removeComments(JUnitTestMethodFacotry.code(d + "")))) + "\n";
      System.out.printf("%d/%d=%5.2f%% %s", interesting, total, 100. * interesting / total, summary);
      try {
        out.write(summary);
      } catch (final IOException ¢) {
        System.err.println("Error: " + ¢.getMessage());
      }
      return true;
    }
    private static boolean interesting(final MethodDeclaration ¢) {
      return !¢.isConstructor() && interesting(statements(body(¢))) && MethodProperty.callingOtherMethods(¢);
    }
    private static boolean interesting(final List<Statement> ¢) {
      return ¢ != null && ¢.size() >= 2 && !MethodProperty.letItBeIn(¢);
    }
  }
}
