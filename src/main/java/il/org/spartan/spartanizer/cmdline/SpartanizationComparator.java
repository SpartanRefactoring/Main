package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** TODO Matteo Orru' <matteo.orru@cs.technion.ac.il> please add a description
 * @author Matteo Orru' <matteo.orru@cs.technion.ac.il>
 * @since Jan 21, 2017 */
public enum SpartanizationComparator {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  @External(alias = "i", value = "input folder") static String inputFolder = system.isWindows() ? "" : ".";
  @External(alias = "o", value = "output folder") static final String outputFolder = "/tmp";
  static String presentSourcePath;
  @SuppressWarnings("CanBeFinal") static String presentSourceName;
  @SuppressWarnings("CanBeFinal") static int methodNesting;
  @SuppressWarnings("CanBeFinal") static MethodDeclaration lastNode;
  static Dotter dotter = new Dotter();
  private static final CSVLineWriter writer = new CSVLineWriter(makeFile("method-properties"));

  static String makeFile(final String fileName) {
    return outputFolder + "/" + (system.isWindows() || presentSourceName == null ? fileName : presentSourceName + "." + fileName);
  }

  public static void main(final String[] where) {
    collect(where.length != 0 ? where : as.array("."));
    System.err.println("Look for your output here: " + writer.close());
  }

  private static void collect(final String[] where) {
    for (final File ¢ : new FilesGenerator(".java").from(where)) {
      System.out.println(¢.getName());
      presentFile = ¢.getName();
      presentSourcePath = ¢.getPath();
      collect(¢);
    }
  }

  private static void collect(final File f) {
    try {
      final String input = FileUtils.read(f);
      collect(input, "before");
      collect(new InteractiveSpartanizer().fixedPoint(input), "after");
    } catch (final IOException ¢) {
      note.bug(¢);
    }
  }

  private static void collect(final String javaCode, final String id) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode), id);
  }

  private static void collect(final CompilationUnit u, final String id) {
    // dotter.click();
    // noinspection SameReturnValue
    u.accept(new ASTVisitor(true) {
      @Override public boolean visit(final MethodDeclaration ¢) {
        consider(¢, id);
        return true;
      }
    });
  }

  @SuppressWarnings({ "rawtypes", "unchecked" }) static void consider(final MethodDeclaration ¢, final String id) {
    ¢.getStartPosition();
    System.out.println(¢.getName());
    //
    writer.put("StartPosition", ¢.getStartPosition()).put("File", presentFile) //
        .put("Name", ¢.getName()) //
        .put("Path", presentSourcePath) //
        .put("Status", id);
    //
    for (final NamedFunction f : functions())
      writer.put(f.name(), f.function().run(¢));
    writer.nl();
  }

  static String presentFile;

  @SuppressWarnings({ "rawtypes", "unchecked" }) static void consider2(final MethodDeclaration ¢) {
    writer.put("File", presentFile).put("Name", ¢.getName()).put("Path", presentSourcePath);
    for (final NamedFunction f : functions())
      writer.put(f.name(), f.function().run(¢));
    writer.nl();
  }

  public static NamedFunction<?>[] functions() {
    return as.array(//
        m("length - ", metrics::length), //
        m("essence - ", λ -> Essence.of(λ + "").length()), //
        m("tokens - ", λ -> metrics.tokens(λ + "")), //
        m("nodes - ", count::nodes), //
        m("body - ", metrics::bodySize), //
        m("methodDeclaration - ", λ -> !iz.methodDeclaration(λ) ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size()),
        m("tide - ", λ -> clean(λ + "").length()));//
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
        // .put("side-effects-free", sideEffects.free(¢)) // TODO Matteo (for
        // himself): temporarily commented. It throws a NullPointerException
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
        .put("getter", guessName.of(¢.getName() + "") == guessName.GETTER_METHOD) //
        .put("setter", guessName.of(¢.getName() + "") == guessName.SETTTER_METHOD) //
        .put("isMethod", guessName.of(¢.getName() + "") == guessName.IS_METHOD) //
        .put("method", guessName.of(¢.getName() + "") == guessName.METHOD_OR_VARIABLE) //
        .put("unknonwn", guessName.of(¢.getName() + "") == guessName.UNKNOWN) //
        .put("weirdo", guessName.of(¢.getName() + "") == guessName.WEIRDO) //
    ;
    writer.nl();
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
      return name;
    }

    public ToInt<R> function() {
      return f;
    }
  }
}
