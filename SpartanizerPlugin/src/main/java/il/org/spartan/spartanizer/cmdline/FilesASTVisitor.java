package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

public abstract class FilesASTVisitor extends ASTVisitor {
  protected static String[] defaultArguments = as.array(".");
  protected static Class<? extends FilesASTVisitor> clazz;
  private static Constructor<? extends FilesASTVisitor> declaredConstructor;
  protected String outputFolder = "/tmp";
  protected File presentFile;
  protected String presentSourceName;
  protected String presentSourcePath;
  protected Dotter dotter = new Dotter();

  private static Constructor<? extends FilesASTVisitor> declaredConstructor() {
    if (clazz == null) {
      monitor.logProbableBug(clazz, fault.stackCapture());
      System.exit(1);
    }
    try {
      return declaredConstructor != null ? declaredConstructor : clazz.getConstructor();
    } catch (NoSuchMethodException | SecurityException ¢) {
      monitor.logProbableBug(clazz, ¢);
      System.err.println("Make sure that class " + clazz + " is not abstract and that it has a default constructor");
    }
    throw new RuntimeException();
  }

  /** [[SuppressWarningsSpartan]] */
  public static void main(final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    visit(args.length != 0 ? args : defaultArguments);
  }

  public static void visit(final String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
    for (final String ¢ : args)
      declaredConstructor().newInstance().visit(¢);
  }

  protected void done() {
    // Empty by default
  }

  protected void init() {
    // Empty by default
  }

  protected String makeFile(final String fileName) {
    return outputFolder + "/" + presentSourceName + "." + fileName;
  }

  protected void visit(final String path) {
    dotter.click();
    init();
    presentSourceName = system.folder2File(presentSourcePath = path);
    for (final File ¢ : new FilesGenerator(".java").from(presentSourcePath))
      visit(presentFile = ¢);
    done();
  }

  void collect(final CompilationUnit ¢) {
    ¢.accept(this);
  }

  void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  void visit(final File f) {
    dotter.click();
    if (!system.isTestFile(f))
      try {
        collect(FileUtils.read(f));
        dotter.click();
      } catch (final IOException ¢) {
        monitor.infoIOException(¢, "File = " + f);
      }
  }
}
