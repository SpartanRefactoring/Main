package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** TODO:  Matteo Orru' <matteo.orru@cs.technion.ac.il> please add a description
 * @author  Matteo Orru' <matteo.orru@cs.technion.ac.il>
 * @since Jan 21, 2017
 */
public class SpartanizationComparator {
  
  @External(alias = "i", value = "input folder") protected static String inputFolder = system.windows() ? "" : ".";
  @External(alias = "o", value = "output folder") protected static String outputFolder = "/tmp";
  
  protected static String presentSourcePath;
  protected static String presentSourceName;
  static int methodNesting;
  static MethodDeclaration lastNode;
  protected static Dotter dotter = new Dotter();
  
  private final static CSVLineWriter writer = new CSVLineWriter(makeFile("method-properties"));
  
  protected static String makeFile(final String fileName) {
    return outputFolder + "/" + (system.windows() || presentSourceName == null ? fileName : presentSourceName + "." + fileName);
  }
  
  public static void main(final String[] where) {
    collect(where.length != 0 ? where : as.array("."));
    System.err.println("Look for your output here: " + writer.close());
  }

  private static void collect(final String[] where) {
    for (final File ¢ : new FilesGenerator(".java").from(where)){
      System.out.println(¢.getName());
      presentFile = ¢.getName();
      presentSourcePath = ¢.getPath();
      collect(¢);
    }
  }
  
  private static void collect(final File f) {
    try {
      String input = FileUtils.read(f);
      collect(input, "before");
      final String output = new InteractiveSpartanizer().fixedPoint(input);
      collect(output, "after");
    } catch (final IOException ¢) {
      System.err.println(¢.getMessage());
    }
  }
  
  private static void collect(String javaCode, String id) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode), id);
  }

  private static void collect(CompilationUnit u, String id) {
  //  dotter.click();
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration ¢) {
        consider(¢, id);
        return true;
      }
    });
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected static void consider(MethodDeclaration ¢, String id) {
      ¢.getStartPosition();
      System.out.println(¢.getName());
      //
      writer.put("StartPosition", ¢.getStartPosition())
            .put("File", presentFile) //
            .put("Name", ¢.getName()) //
            .put("Path", presentSourcePath) //
            .put("Status",id);
      //
      for(NamedFunction f: functions())
        writer.put(f.name(), f.function().run(¢));
      writer.nl();
  }

  @SuppressWarnings("unused")
  private static void collect(final String javaCode) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  protected static String presentFile;
  
  private static void collect(final CompilationUnit ¢) {
    collect(¢, "");
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected static void consider2(MethodDeclaration ¢) {
    writer.put("File", presentFile).put("Name", ¢.getName()).put("Path", presentSourcePath);
    for(NamedFunction f: functions())
      writer.put(f.name(), f.function().run(¢));
    writer.nl();
  }

  @SuppressWarnings("rawtypes")
  public static NamedFunction[] functions() {
    return functions("");
  }
  
  static void consider(final MethodDeclaration ¢) {
    final Type type = ¢.getReturnType2();
    writer.put("File", presentFile) //
        .put("Name", ¢.getName()) //
        .put("Constructor", ¢.isConstructor()) //
        .put("Varargs", ¢.isVarargs()) //
        .put("abstract", iz.abstract¢(¢)) //
        .put("default", iz.default¢(¢)) //
        .put("final", iz.final¢(¢)) //
        .put("nested", methodNesting > 0) //
        .put("nesting", ¢ == lastNode) //
        .put("private", iz.private¢(¢)) //
        .put("protected", iz.protected¢(¢)) //
        .put("public", iz.public¢(¢)) //
        .put("static", iz.static¢(¢)) //
        .put("synchronized", iz.synchronized¢(¢)) //
        .put("empty", extract.statements(¢).isEmpty()) //
        .put("single", extract.statements(¢).size() == 1) //
        .put("double", extract.statements(¢).size() == 1) //
//        .put("side-effects-free", sideEffects.free(¢)) // TODO Matteo (for himself): temporarily commented. It throws a NullPointerException
        .put("linear", !haz.unknownNumberOfEvaluations(¢)) //
        .put("array", type != null && type.isArrayType()) //
        .put("parameterized", type != null && type.isParameterizedType()) //
        .put("primitive", type != null && type.isPrimitiveType()) //
        .put("simple", type != null && type.isSimpleType()) //
        .put("qualified", type != null && type.isQualifiedType()) //
        .put("no-arguments", type != null && ¢.parameters().isEmpty()) //
        .put("unary", ¢.parameters().size() == 1) //
        .put("binary", ¢.parameters().size() == 2) //
        .put("no-exceptions", ¢.thrownExceptionTypes().isEmpty())//
        .put("one-exception", ¢.thrownExceptionTypes().size() == 1) //
        .put("getter", NameGuess.of(¢.getName() + "") == NameGuess.GETTER_METHOD) //
        .put("setter", NameGuess.of(¢.getName() + "") == NameGuess.SETTTER_METHOD) //
        .put("isMethod", NameGuess.of(¢.getName() + "") == NameGuess.IS_METHOD) //
        .put("method", NameGuess.of(¢.getName() + "") == NameGuess.METHOD_OR_VARIABLE) //
        .put("unknonwn", NameGuess.of(¢.getName() + "") == NameGuess.UNKNOWN) //
        .put("weirdo", NameGuess.of(¢.getName() + "") == NameGuess.WEIRDO) //
    ;
    writer.nl();
  }

  @SuppressWarnings({ "rawtypes" })
  public static NamedFunction[] functions(String id) {
    id = format(id);
    return as.array(//
        m("length" + id, (¢) -> (¢ + "").length()), //
        m("essence" + id, (¢) -> Essence.of(¢ + "").length()), //
        m("tokens" + id, (¢) -> metrics.tokens(¢ + "")), //
        m("nodes" + id, count::nodes), //
        m("body" + id, metrics::bodySize), //
//        m("methodDeclaration" + id, (¢) -> az.methodDeclaration(¢) == null ? -1 : extract.statements(az.methodDeclaration(¢).getBody()).size()),
        m("tide" + id, (¢) -> clean(¢ + "").length()));//
  }
  
  private static String format(String id) {
    return " - " + id;
  }

  static NamedFunction<ASTNode> m(final String name, final ToInt<ASTNode> f) {
    return new NamedFunction<>(name, f);
  }
  
  @FunctionalInterface
  public interface ToInt<R> {
    int run(R r);
  }

  static class NamedFunction<R> {
    final String name;
    final ToInt<R> f;

    NamedFunction(final String name, final ToInt<R> f) {
      this.name = name;
      this.f = f;
    }

    public String name() {
      return this.name;
    }

    public ToInt<R> function() {
      return this.f;
    }
  }
  
}
