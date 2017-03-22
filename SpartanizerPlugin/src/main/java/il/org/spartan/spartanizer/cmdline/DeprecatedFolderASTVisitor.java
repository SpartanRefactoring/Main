package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.engine.nominal.trivia.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

/** Parse and visit all Java files under a given path.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Dec 14, 2016 */
public abstract class DeprecatedFolderASTVisitor extends ASTVisitor {
  @External(alias = "i", value = "input folder") @NotNull protected static String inputFolder = system.windows() ? "" : ".";
  @External(alias = "o", value = "output folder") @NotNull protected static String outputFolder = "/tmp";
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
  private final boolean silent = false;
  static {
    TrimmerLog.off();
    Trimmer.silent = true;
  }

  private static Constructor<? extends DeprecatedFolderASTVisitor> declaredConstructor() {
    if (clazz == null) {
      monitor.logProbableBug(clazz, fault.stackCapture());
      System.exit(1);
    }
    try {
      return declaredConstructor != null ? declaredConstructor : clazz.getConstructor();
    } catch (@NotNull NoSuchMethodException | SecurityException ¢) {
      monitor.logProbableBug(clazz, ¢);
      System.err.println("Make sure that class " + clazz + " is not abstract and that it has a default constructor");
      throw new RuntimeException();
    }
  }

  public static void main(@NotNull final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    visit(args.length != 0 ? args : defaultArguments);
  }

  public static void visit(@NotNull final String... arguments) throws InstantiationException, IllegalAccessException, InvocationTargetException {
    for (final String ¢ : External.Introspector.extract(arguments, clazz))
      declaredConstructor().newInstance().visit(¢);// NANO - can't, throws
  }

  @SuppressWarnings("static-method") protected void done(final String path) {
    ___.______unused(path);
  }

  @SuppressWarnings("static-method") protected void init(final String path) {
    ___.______unused(path);
  }

  @NotNull protected static String makeFile(final String fileName) {
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

  void collect(@NotNull final CompilationUnit u) {
    try {
      u.accept(this);
    } catch (@NotNull final NullPointerException ¢) {
      ¢.printStackTrace();
    }
  }

  void collect(@NotNull final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  void visit(@NotNull final File f) {
    if (!silent)
      dotter.click();
    if (!Utils.isTestFile(f) && ASTInFilesVisitor.productionCode(f))
      try {
        absolutePath = f.getAbsolutePath();
        relativePath = f.getPath();
        collect(FileUtils.read(f));
        if (!silent)
          dotter.click();
      } catch (@NotNull final IOException ¢) {
        monitor.infoIOException(¢, "File = " + f);
      }
  }

  public static class FieldsOnly extends DeprecatedFolderASTVisitor {
    public static void main(@NotNull final String[] args)
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

    public static void main(@NotNull final String[] args)
        throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      clazz = BucketMethods.class;
      try {
        out = new BufferedWriter(new FileWriter("/tmp/out.txt", false));
      } catch (@NotNull final IOException ¢) {
        monitor.infoIOException(¢);
        return;
      }
      DeprecatedFolderASTVisitor.main(args);
    }

    @Override @SuppressWarnings("boxing") public boolean visit(@NotNull final MethodDeclaration d) {
      ++total;
      if (interesting(d)) {
        ++interesting;
        final String summary = squeeze(theSpartanizer.repetitively(removeComments(anonymize.code(d + "")))) + "\n";
        System.out.printf("%d/%d=%5.2f%% %s", interesting, total, 100. * interesting / total, summary);
        try {
          out.write(summary);
        } catch (@NotNull final IOException ¢) {
          System.err.println("Error: " + ¢.getMessage());
        }
      }
      return true;
    }

    private static boolean interesting(@NotNull final MethodDeclaration ¢) {
      return !¢.isConstructor() && interesting(statements(body(¢))) && leaking(descendants.streamOf(¢));
    }

    private static boolean leaking(@NotNull final Stream<ASTNode> ¢) {
      return ¢.noneMatch(BucketMethods::leaking);
    }

    private static boolean interesting(@Nullable final List<Statement> ¢) {
      return ¢ != null && ¢.size() >= 2 && !letItBeIn(¢);
    }

    static boolean letItBeIn(@NotNull final List<Statement> ¢) {
      return ¢.size() == 2 && first(¢) instanceof VariableDeclarationStatement;
    }

    private static boolean leaking(final ASTNode ¢) {
      return iz.nodeTypeIn(¢, ARRAY_CREATION, METHOD_INVOCATION, CLASS_INSTANCE_CREATION, CONSTRUCTOR_INVOCATION, ANONYMOUS_CLASS_DECLARATION,
          SUPER_CONSTRUCTOR_INVOCATION, SUPER_METHOD_INVOCATION, LAMBDA_EXPRESSION);
    }
  }
}
