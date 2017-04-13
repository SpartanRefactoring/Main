package il.org.spartan.spartanizer.research.metatester;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** TODO orenafek: document class
 * @author orenafek
 * @since 2017-04-12 */
@UnderConstruction("OrenAfek -- 12/04/2017")
@SuppressWarnings("all")
public class ASTTestClassGenerator implements TestClassGenerator {
  enum SourceLineType {
    TEST, OTHER;
  }

  EnumMap<SourceLineType, ASTNode> sourceLines;

  public ASTTestClassGenerator() {
    sourceLines = new EnumMap<>(SourceLineType.class);
  }

  private void addToMap(SourceLineType t, ASTNode n) {
    sourceLines.put(t, n);
  }

  private void fillMap(ASTNode root) {
    step.statements(root).stream().forEach(λ -> {
      if (!sourceLines.containsValue(λ))
        addToMap(SourceLineType.OTHER, λ);
    });
  }

  @Override public Class<?> generate(String testClassName, final File originalSourceFile) {
    ASTNode $ = makeAST(originalSourceFile);
    allTestMethods($).stream().forEach(λ -> tests(prefixes(λ)));
    fillMap($);
    return Object.class;
  }

  private List<String> prefixes(MethodDeclaration d) {
    List<String> $ = new ArrayList<>();
    StringBuilder prefix = new StringBuilder();
    step.statements(d).stream().map(λ -> {
      prefix.append(λ + "\n");
      return prefix + "";
    }).forEach($::add);
    return $;
  }

  private List<String> testClassSkelaton(File __){
    List<String> $ = new ArrayList<>();
    sourceLines.entrySet().stream().filter(e -> e.getKey() != SourceLineType.TEST);
    return $;
  }

  private List<String> tests(List<String> prefixes) {
    final Int $ = Int.valueOf(0);
    return prefixes.stream().map(λ -> String.format("@Test public void test%d(){\n%s}", $.next(), λ)).peek(System.out::println)
        .collect(Collectors.toList());
  }

  private List<MethodDeclaration> allTestMethods(ASTNode file) {
    List<MethodDeclaration> $ = new ArrayList<>();
    file.accept(new ASTVisitor() {
      @Override public boolean visit(MethodDeclaration node) {
        boolean result = extract.annotations(node).stream().anyMatch(λ -> haz.name(λ, "Test"));
        if (result) {
          step.statements(node).stream().forEach(λ -> addToMap(SourceLineType.TEST, λ));
          $.add(node);
        }
        return result;
      }
    });
    return $;
  }

  private ASTNode makeAST(final File originalSourceFile) {
    return wizard.ast(readAll(originalSourceFile));
  }

  private String readAll(File f) {
    final StringBuilder $ = new StringBuilder();
    try {
      final BufferedReader linesStream = new BufferedReader(new FileReader(f));
      String line = linesStream.readLine();
      for (int ¢ = 1; line != null; ++¢) {
        $.append(line).append("\n");
        line = linesStream.readLine();
      }
      return $ + "";
    } catch (final IOException ignore) {/**/}
    return "";
  }
}

