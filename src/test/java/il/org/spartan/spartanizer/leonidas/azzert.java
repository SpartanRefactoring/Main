package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class azzert {
  public static expression that(final String ¢) {
    return new expression(¢);
  }

  public static tipper tipper(final String p, final String s, final String d) {
    return new tipper(p, s, d);
  }
}

class expression {
  final String s;

  public expression(final String s) {
    this.s = s;
  }

  public void matches(final String s2) {
    assertTrue(Matcher.matches(wizard.ast(s), wizard.ast(s2)));
  }

  public void notmatches(final String s2) {
    assertFalse(Matcher.matches(wizard.ast(s), wizard.ast(s2)));
  }
}

class tipper {
  private final UserDefinedTipper<ASTNode> tipper;

  public tipper(final String p, final String r, final String d) {
    tipper = TipperFactory.tipper(p, r, d);
  }

  public void nottips(final String ¢) {
    assertFalse(tipper.canTip(wizard.ast(¢)));
  }

  public void tips(final String ¢) {
    assertTrue(tipper.canTip(wizard.ast(¢)));
  }

  public turns turns(final String ¢) {
    return new turns(tipper, ¢);
  }
}

class turns {
  private final UserDefinedTipper<ASTNode> tipper;
  private final String s;

  public turns(final UserDefinedTipper<ASTNode> tipper, final String _s) {
    this.tipper = tipper;
    s = _s;
  }

  /** XXX: This is a bug of auto-laconize [[SuppressWarningsSpartan]] */
  public void into(final String res) {
    final Document document = new Document(ASTutils.wrapCode(s));
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource(document.get().toCharArray());
    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    final ASTNode n = ASTutils.extractASTNode(s, cu);
    final ASTRewrite r = ASTRewrite.create(ast);
    try {
      assertTrue(tipper.canTip(n));
      tipper.tip(n).go(r, null);
    } catch (final TipperFailure e) {
      e.printStackTrace();
      fail();
    }
    final TextEdit edits = r.rewriteAST(document, null);
    try {
      edits.apply(document);
    } catch (MalformedTreeException | BadLocationException e) {
      e.printStackTrace();
      fail();
    }
    azzertEquals(res, document);
  }

  private static void azzertEquals(final String s, final Document d) {
    assertEquals(s, ASTutils.extractCode(s, d));
  }
}
