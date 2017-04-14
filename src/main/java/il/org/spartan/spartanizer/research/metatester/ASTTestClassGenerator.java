package il.org.spartan.spartanizer.research.metatester;

import static il.org.spartan.spartanizer.research.metatester.FileUtils.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** TODO orenafek: document class
 * @author orenafek
 * @since 2017-04-12 */
@UnderConstruction("OrenAfek -- 12/04/2017")
public class ASTTestClassGenerator implements TestClassGenerator {
  class Test {
    public ASTNode source;
    public List<MemberValuePair> values;

    public Test(ASTNode source, List<MemberValuePair> values) {
      this.source = source;
      this.values = values;
    }
  }

  private ASTNode root;

  enum SourceLineType {
    TEST, OTHER;
  }

  Set<Test> sourceLines;
  private final Class<?> testClass;
  private final String sourcePath;
  public final String packageName;
  private final Int testNoGenerator = Int.valueOf(0);

  public ASTTestClassGenerator(final Class<?> testClass) {
    this.testClass = testClass;
    sourcePath = makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\", testClass));
    packageName = packageName("\\.", testClass);
    sourceLines = new HashSet<>();
  }

  private void modifyClass() {
    root.accept(new ASTVisitor() {
      @Override public boolean visit(TypeDeclaration node) {
        SimpleName $ = az.typeDeclaration(node).getName();
        $.setIdentifier($ + "_Meta");
        Annotation a = extract.annotations(node).stream()//
            .filter(λ -> haz.name(λ, "RunWith"))//
            .findAny().orElse(null);
        if (a != null)
          node.modifiers().remove(a);
        return true;
      }
    });
  }

  void addToMap(Test t) {
    sourceLines.add(t);
  }

  private void removeOriginalTestsFromTree() {
    sourceLines.stream() //
        .map(x -> x.source) //
        .forEach(λ -> az.abstractTypeDeclaration(λ.getParent()).bodyDeclarations().remove(λ));
  }
  
  @Override public Class<?> generate(String testClassName, final File originalSourceFile) {
    root = makeAST(originalSourceFile);
    modifyClass();
    removeUnnecessaryImports();
    String $ = testClassSkelaton(allTestMethods().stream().map(λ -> tests(λ, prefixes(λ))).collect(Collectors.toList()));
    return loadClass(testClassName, format.code($), testClass, sourcePath);
  }

  private static List<String> prefixes(Test t) {
    List<String> $ = new ArrayList<>();
    StringBuilder prefix = new StringBuilder();
    step.statements((MethodDeclaration) t.source).stream().map(λ -> {
      prefix.append(λ + "\n");
      return prefix + "";
    }).forEach($::add);
    return $;
  }
  
  private void removeUnnecessaryImports() {
    List<Class<?>> importsToRemove = Arrays.asList(MetaTester.class);
    root.accept(new ASTVisitor() {
      @Override public boolean visit(ImportDeclaration node) {
        if (importsToRemove.stream().map(x -> x.getPackage().getName())
            .anyMatch(x -> node.getName().toString().contains(x)) ||
            node.getName().toString().equals("org.junit.runner")) {
          node.delete();
          return true;
        }
        return false;
      }
    });
  }

  private String testClassSkelaton(List<List<String>> tests) {
    removeOriginalTestsFromTree();
    StringBuilder $ = new StringBuilder();
    $.append(root + "");
    $.replace($.lastIndexOf("}"), $.length(), "");
    tests.stream().map(l -> l.stream().reduce((s1, s2) -> s1 + s2).orElse("")).forEach($::append);
    $.append("\n}\n");
    return $.toString()
        .replaceFirst("@SuppressWarnings\\([\"a-zA-Z0-9-{}]*\\)[\n\t\r ]*public class","public class")
        .replace("public class", "@SuppressWarnings(\"all\") public class");
  }

  private static <T> String removeBraces(List<T> l) {
    StringBuilder $ = new StringBuilder();
    l.stream().forEach(e -> $.append(e + ", "));
    return $.substring(0, $.length() - 2);
  }

  @SuppressWarnings("boxing") private List<String> tests(Test t, List<String> prefixes) {
    String annotaionString = "@Test" + (t.values != null ? "(" + removeBraces(t.values) + ")" : "");
    return prefixes.stream().map(λ -> String.format("%s public void test%d(){\n%s}", annotaionString, testNoGenerator.next(), λ))
        .collect(Collectors.toList());
  }

  private List<Test> allTestMethods() {
    List<Test> $ = new ArrayList<>();
    root.accept(new ASTVisitor() {
      @Override public boolean visit(MethodDeclaration node) {
        Annotation a = extract.annotations(node).stream().filter(λ -> haz.name(λ, "Test")).findAny().orElse(null);
        if (a != null) {
          List<MemberValuePair> l = a instanceof NormalAnnotation ? step.values(((NormalAnnotation) a)) : null;
          Test t = new Test(node, l);
          addToMap(t);
          $.add(t);
        }
        return a != null;
      }
    });
    return $;
  }

  private static ASTNode makeAST(final File originalSourceFile) {
    return wizard.ast(readAll(originalSourceFile));
  }

  private static String readAll(File f) {
    final StringBuilder $ = new StringBuilder();
    try (final BufferedReader linesStream = new BufferedReader(new FileReader(f))) {
      String line = linesStream.readLine();
      for (; line != null;) {
        $.append(line).append("\n");
        line = linesStream.readLine();
      }
      return $ + "";
    } catch (@SuppressWarnings("unused") final IOException ignore) {/**/}
    return "";
  }
}
