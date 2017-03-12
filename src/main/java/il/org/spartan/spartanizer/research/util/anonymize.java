package il.org.spartan.spartanizer.research.util;

import static il.org.spartan.spartanizer.utils.Wrap.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch */
public enum anonymize {
  ;
  public static String testcase(final String name, final String raw) {
    return wrapTest(name, linify(trivia.escapeQuotes(format.code(shortenIdentifiers(raw)))));
  }

  public static String unwarpedTestcase(final String raw) {
    return linify(trivia.escapeQuotes(format.code(shortenIdentifiers(raw))));
  }

  public static String code(final String raw) {
    return format.code(shortenIdentifiers(raw));
  }

  private static String wrapTest(final String name, final String code) {
    return String.format("  @Test public void %s() {\n" //
        + "    trimmingOf(\n \"%s\" \n)\n" //
        + "       .gives(\n" //
        + "    // Edit this to reflect your expectation\n" //
        + "     \"%s\"\n//\n)//\n.stays()\n;\n}", name, code, code);
  }

  /** Renders the Strings a,b,c, ..., z, x1, x2, ... for lower case identifiers
   * and A, B, C, ..., Z, X1, X2, ... for upper case identifiers */
  static String renderIdentifier(final String old) {
    switch (old) {
      case "START":
        return "A";
      case "X":
        return "X1";
      case "start":
        return "a";
      case "z":
        return "x1";
      default:
        return old.length() == 1 ? String.valueOf((char) (old.charAt(0) + 1)) : String.valueOf(old.charAt(0)) + (old.charAt(1) + 1);
    }
  }

  /** Separate the string to lines
   * @param ¢ string to linify
   * @return */
  private static String linify(final String ¢) {
    String $ = "";
    try (Scanner scanner = new Scanner(¢)) {
      while (scanner.hasNextLine())
        $ += "\"" + scanner.nextLine() + "\"" + (!scanner.hasNextLine() ? "" : " + ") + "//\n";
    }
    return $;
  }

  public static String shortenIdentifiers(final String s) {
    final Wrapper<String> id = new Wrapper<>("start"), Id = new Wrapper<>("START");
    final IDocument $ = new Document(ASTutils.wrapCode(s));
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource($.get().toCharArray());
    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    final ASTNode n = ASTutils.extractASTNode(s, cu);
    final ASTRewrite r = ASTRewrite.create(ast);
    final Map<String, String> renaming = new HashMap<>();
    n.accept(new ASTVisitor(true) {
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
    applyChanges($, r);
    return ASTutils.extractCode(s, $);
  }

  private static void applyChanges(final IDocument d, final ASTRewrite r) {
    try {
      r.rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      ¢.printStackTrace();
    }
  }

  public static void main(final String[] args) {
    System.out.println("enter whatever:");
    try (Scanner reader = new Scanner(System.in)) {
      String s = "";
      while (reader.hasNext())
        s += "\n" + reader.nextLine();
      System.out.println(anonymize.testcase(namer.signature(s), s));
    }
  }

  public static String makeUnitTest(final String codeFragment) {
    final String $ = trivia.squeeze(trivia.removeComments(code(essence(codeFragment))));
    return String.format("%s @Test public void %s() {\n %s\n}\n", anonymize.comment(), namer.signature($), anonymize.body($));
  }

  public static String comment() {
    return String.format("/** Automatically generated on %s */\n", system.now());
  }

  public static String body(final String input) {
    for (String $ = String.format("  trimmingOf(\"%s\") //\n", input), from = input;;) {
      final String to = theSpartanizer.once(from);
      if (theSpartanizer.same(to, from))
        return $ + "  .stays() //\n  ;\n";
      final Tipper<?> t = theSpartanizer.firstTipper(from);
      assert t != null;
      assert t.current() != null;
      $ += String.format(" .using(%s.class,new %s()) //\n", t.current().getClass().getSimpleName(), t.className());
      $ += String.format(" .gives(\"%s\") //\n", trivia.escapeQuotes(essence(to)));
      from = to;
    }
  }
}
