package il.org.spartan.spartanizer.research.metatester;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** TODO orenafek: document class
 * @author orenafek
 * @since 2017-04-12 */
@UnderConstruction("OrenAfek -- 12/04/2017")
@SuppressWarnings("all")
public class ASTTestClassGenerator implements TestClassGenerator {
  private ASTNode root;

  enum SourceLineType {
    TEST, OTHER;
  }

  Set<ASTNode> sourceLines;

  public ASTTestClassGenerator() {
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

  private void addToMap(ASTNode ¢) {
    sourceLines.add(¢);
  }

  private void removeOriginalTestsFromTree(ASTNode root) {
    sourceLines.stream() //
        .peek(λ -> System.out.println("Node = " + λ)) //
        .forEach(λ -> az.abstractTypeDeclaration(λ.getParent()).bodyDeclarations().remove(λ));
  }

  private void fillMap(ASTNode root) {
    root.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(N __) {
        return false;
      }
    });
    step.statements(root).stream().forEach(λ -> {
      if (!sourceLines.contains(λ))
        addToMap(λ);
    });
  }

  @Override public Class<?> generate(String testClassName, final File originalSourceFile) {
    root = makeAST(originalSourceFile);
    modifyClass();
    testClassSkelaton(allTestMethods().stream().map(λ -> tests(prefixes(λ))).collect(Collectors.toList()));
    
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

  private String testClassSkelaton(List<List<String>> tests) {
    removeOriginalTestsFromTree(root);
    StringBuilder $ = new StringBuilder();
    $.append(root + "");
    $.replace($.lastIndexOf("}"), $.length(), "");
    tests.stream().map(l -> l.stream().reduce((s1, s2) -> s1 + s2).orElse("")).forEach($::append);
    $.append("\n}\n");
    return $ + "";
  }

  private List<String> tests(List<String> prefixes) {
    final Int $ = Int.valueOf(0);
    return prefixes.stream().map(λ -> String.format("@Test public void test%d(){\n%s}", $.next(), λ)).collect(Collectors.toList());
  }

  private List<MethodDeclaration> allTestMethods() {
    List<MethodDeclaration> $ = new ArrayList<>();
    root.accept(new ASTVisitor() {
      @Override public boolean visit(MethodDeclaration node) {
        boolean result = extract.annotations(node).stream().anyMatch(λ -> haz.name(λ, "Test"));
        if (result) {
          addToMap(node);
          // step.statements(node).stream().forEach(λ ->
          // addToMap(SourceLineType.TEST, λ));
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
