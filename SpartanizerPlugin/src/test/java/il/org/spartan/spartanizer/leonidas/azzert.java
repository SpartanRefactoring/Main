package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
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
  static <N extends ASTNode> N findSecond(final Class<?> c, final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<Boolean> foundFirst = new Wrapper<>();
    foundFirst.set(Boolean.FALSE);
    final Wrapper<ASTNode> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if ($.get() != null)
          return false;
        if (¢.getClass() != c && !c.isAssignableFrom(¢.getClass()))
          return true;
        if (foundFirst.get().booleanValue()) {
          $.set(¢);
          assert $.get() == ¢;
          return false;
        }
        foundFirst.set(Boolean.TRUE);
        return true;
      }
    });
    @SuppressWarnings("unchecked") final N $$ = (N) $.get();
    return $$;
  }

  private static ASTNode extractASTNode(final String s, final CompilationUnit u) {
    switch (GuessedContext.find(s)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
        return u;
      case EXPRESSION_LOOK_ALIKE:
        return findSecond(Expression.class, findFirst.methodDeclaration(u));
      case METHOD_LOOKALIKE:
        return findSecond(MethodDeclaration.class, u);
      case OUTER_TYPE_LOOKALIKE:
        return u;
      case STATEMENTS_LOOK_ALIKE:
        return findSecond(Block.class, u);
      default:
        break;
    }
    return null;
  }

  private static String wrapCode(final String s) {
    switch (GuessedContext.find(s)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
        return s;
      case EXPRESSION_LOOK_ALIKE:
        return "class X{int f(){return " + s + ";}}";
      case METHOD_LOOKALIKE:
        return "class X{" + s + "}";
      case OUTER_TYPE_LOOKALIKE:
        return s;
      case STATEMENTS_LOOK_ALIKE:
        return "class X{int f(){" + s + "}}";
      default:
        fail(s + " is not like anything I know...");
    }
    return null;
  }

  private final UserDefinedTipper<ASTNode> tipper;
  private final String s;

  public turns(final UserDefinedTipper<ASTNode> tipper, final String _s) {
    this.tipper = tipper;
    s = _s;
  }

  public void into(final String res) {
    final Document document = new Document(wrapCode(s));
    final ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setSource(document.get().toCharArray());
    final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    final AST ast = cu.getAST();
    final ASTRewrite r = ASTRewrite.create(ast);
    final ASTNode n = extractASTNode(s, cu);
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

  private void azzertEquals(final String res, final Document document) {
    switch (GuessedContext.find(s)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
        assertEquals(res, document.get());
        break;
      case EXPRESSION_LOOK_ALIKE:
        assertEquals(res, document.get().substring(23, document.get().length() - 3));
        break;
      case METHOD_LOOKALIKE:
        assertEquals(res, document.get().substring(9, document.get().length() - 2));
        break;
      case OUTER_TYPE_LOOKALIKE:
        assertEquals(res, document.get());
        break;
      case STATEMENTS_LOOK_ALIKE:
        assertEquals(res, document.get().substring(16, document.get().length() - 3));
        break;
      default:
        break;
    }
  }
}
