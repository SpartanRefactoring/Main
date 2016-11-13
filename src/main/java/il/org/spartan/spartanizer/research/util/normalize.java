package il.org.spartan.spartanizer.research.util;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class normalize {
  public static String testcase(final String raw, final int name, final int issue) {
    return wrapTest(name, issue, linify(escapeQuotes(format.code(shortenIdentifiers(raw)))));
  }
  public static String codeFragment(final String raw) {
    return format.code(shortenIdentifiers(raw));
  }
  /** escapes all "s
   * @param ¢
   * @return */
  private static String escapeQuotes(final String ¢) {
    return ¢.replace("\"", "\\\"");
  }
  private static String wrapTest(final int report, final int issue, final String code) {
    return "  @Test public void report" + report + "() {" + //
        "\n\ttrimmingOf(\"// From use case of issue" + issue + //
        "\" //\n + " + code + "\n).gives(\"// Edit this to reflect your expectation, but leave this comment\" + //\n" //
        + code + //
        "\n).stays();\n}";
  }
  /** Renders the Strings a,b,c, ..., z, x1, x2, ... for lower case identifiers
   * and A, B, C, ..., Z, X1, X2, ... for upper case identifiers */
  static String renderIdentifier(final String old) {
    return "start".equals(old) ? "a"
        : "START".equals(old) ? "A"
            : "z".equals(old) ? "x1"
                : "Z".equals(old) ? "X1"
                    : old.length() == 1 ? String.valueOf((char) (old.charAt(0) + 1))
                        : String.valueOf(old.charAt(0)) + String.valueOf(old.charAt(1) + 1);
  }
  /** Separate the string to lines
   * @param ¢ string to linify
   * @return */
  private static String linify(final String ¢) {
    String $ = "";
    try (Scanner scanner = new Scanner(¢)) {
      while (scanner.hasNextLine())
        $ += "\"" + scanner.nextLine() + "\"" + (!scanner.hasNextLine() ? "" : " + ") + "//";
    }
    return $;
  }
  public static String shortenIdentifiers(final String s) {
    final Map<String, String> renaming = new HashMap<>();
    final Wrapper<String> id = new Wrapper<>("start");
    final Wrapper<String> Id = new Wrapper<>("START");
    final Document document = new Document(ASTutils.wrapCode(s));
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource(document.get().toCharArray());
    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    final ASTNode n = ASTutils.extractASTNode(s, cu);
    final ASTRewrite r = ASTRewrite.create(ast);
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final StringLiteral node) {
        final StringLiteral lit = ast.newStringLiteral();
        lit.setLiteralValue("str");
        r.replace(node, lit, null);
        return super.visit(node);
      }
      @Override public void preVisit(final ASTNode ¢) {
        if (!iz.simpleName(¢) && !iz.qualifiedName(¢))
          return;
        final String name = ((Name) ¢).getFullyQualifiedName();
        if (!renaming.containsKey(name))
          if (name.charAt(0) < 'A' || name.charAt(0) > 'Z') {
            id.set(renderIdentifier(id.get()));
            renaming.put(name, id.get());
          } else {
            Id.set(renderIdentifier(Id.get()));
            renaming.put(name, Id.get());
          }
        r.replace(¢, ast.newSimpleName(renaming.get(name)), null);
      }
    });
    try {
      r.rewriteAST(document, null).apply(document);
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException e) {
      e.printStackTrace();
    }
    return ASTutils.extractCode(s, document);
  }
  public static void main(final String args[]) {
    try (Scanner reader = new Scanner(System.in)) {
      String s = "";
      while (reader.hasNext())
        s += "\n" + reader.nextLine();
      System.out.println(normalize.testcase(s, 234, 285));
    }
  }
}
