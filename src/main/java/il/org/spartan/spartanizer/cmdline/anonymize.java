package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.engine.nominal.namer.*;
import static il.org.spartan.spartanizer.engine.nominal.trivia.*;
import static java.lang.String.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch */
public enum anonymize {
  ;
  public static String testcase(final String name, final String raw) {
    return wrapTest(name, linify(escapeQuotes(format.code(shortenIdentifiers(raw)))));
  }

  public static String unwarpedTestcase(final String raw) {
    return linify(escapeQuotes(format.code(shortenIdentifiers(raw))));
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

  public static String shortenIdentifiers(final String javaFragment) {
    final Wrapper<String> id = new Wrapper<>("start"), Id = new Wrapper<>("START");
    final IDocument $ = new Document(ASTutils.wrapCode(javaFragment));
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource($.get().toCharArray());
    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    final ASTNode n = ASTutils.extractASTNode(javaFragment, cu);
    if (n == null)
      return javaFragment;
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
    return ASTutils.extractCode(javaFragment, $);
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

  public static String makeTipperUnitTest(final String codeFragment) {
    final String $ = squeeze(removeComments(code(essence(codeFragment))));
    return comment() + format("@Test public void test_%s() {\n %s\n}\n", signature($), tipperBody($));
  }
  
  
  public static String makeBloaterUnitTest(final String codeFragment) {
    final String $ = squeeze(removeComments(code(essence(codeFragment))));
    return comment() + format("@Test public void test_%s() {\n %s\n}\n", signature($), bloaterBody($));
  }

  public static String comment() {
    return format("/** Introduced by %s on %s \n(code automatically generated in '%s.java')*/\n", //
        system.userName(), //
        system.now(), //
        system.callingClassName() //
    );
  }

  public static String tipperBody(final String input) {
    for (String $ = format("  trimmingOf(\"%s\") //\n", input), from = input;;) {
      final String to = theSpartanizer.once(from);
      if (theSpartanizer.same(to, from))
        return $ + "  .stays() //\n  ;";
      final Tipper<?> t = theSpartanizer.firstTipper(from);
      assert t != null;
      $ += format(" .using(%s.class, new %s()) //\n", operandClass(t), tipperClass(t))
          + format(" .gives(\"%s\") //\n", escapeQuotes(trivia.essence(to)));
      from = to;
    }
  }
  
  public static String bloaterBody(final String input) {
    for (String $ = format("  trimmingOf(\"%s\") //\n", input), from = input;;) {
      final String to = OperandBloating.bloat(input);
      if (theSpartanizer.same(to, from))
        return $ + "  .stays() //\n  ;";
      $ += format(" .gives(\"%s\") //\n", escapeQuotes(trivia.essence(to)));
      from = to;
    }
  }

  private static String operandClass(final Tipper<?> ¢) {
    return system.className(¢.object());
  }

  private static String tipperClass(final Tipper<?> ¢) {
    return ¢.className() + format(¢.getClass().getTypeParameters().length <= 0 ? "" : "<%s>", operandClass(¢));
  }
}
