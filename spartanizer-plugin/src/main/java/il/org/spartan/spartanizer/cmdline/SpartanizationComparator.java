package il.org.spartan.spartanizer.cmdline;

import java.io.File;
import java.io.IOException;
import java.util.function.ToIntFunction;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;

import fluent.ly.as;
import fluent.ly.note;
import fluent.ly.system;
import il.org.spartan.CSVLineWriter;
import il.org.spartan.bench.Dotter;
import il.org.spartan.collections.FilesGenerator;
import il.org.spartan.external.External;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metric;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.good.InteractiveSpartanizer;
import il.org.spartan.spartanizer.engine.nominal.guessName;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.utils.FileUtils;

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
      //System.out.println(¢.getName());
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
  static void collect(final String javaCode, final String id) {
    collect((CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode), id);
  }
  static void collect(final CompilationUnit u, final String id) {
    // dotter.click();
    // noinspection SameReturnValue
    u.accept(new ASTVisitor(true) {
      @Override public boolean visit(final MethodDeclaration ¢) {
        consider(¢, id);
        return true;
      }
    });
  }
  static void consider(final MethodDeclaration ¢, final String id) {
    ¢.getStartPosition();
    System.out.println(¢.getName());
    //
    writer.put("StartPosition", ¢.getStartPosition()).put("File", presentFile) //
        .put("Name", ¢.getName()) //
        .put("Path", presentSourcePath) //
        .put("Status", id);
    //
    for (final Metric.Integral f : metrics())
      writer.put(f.name, f.apply(¢));
    writer.nl();
  }

  static String presentFile;

  static void consider2(final MethodDeclaration ¢) {
    writer.put("File", presentFile).put("Name", ¢.getName()).put("Path", presentSourcePath);
    for (final Metric f : metrics())
      writer.put(f.name, f.compute(¢));
    writer.nl();
  }
  public static Metric.Integral[] metrics() {
    return as.array(//
        Metric.named("length - ").of((ToIntFunction<ASTNode>) Metrics::length), //
        Metric.named("essence - ").of((ToIntFunction<ASTNode>) Metrics::essence), //
        Metric.named("tokens - ").of((ToIntFunction<ASTNode>) Metrics::tokens), //
        Metric.named("nodes - ").of(countOf::nodes), //
        Metric.named("body - ").of(Metrics::bodySize), //
        Metric.named("methodDeclaration - ").of(Metrics::statements),
        Metric.named("tide - ").of(Metrics::tide));//
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

}
