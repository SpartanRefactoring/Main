package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class leonidasSays {
  public static expression that(final String ¢) {
    return new expression(¢);
  }
  public static tipper tipper(final String p, final String s, final String d) {
    return new tipper(p, s, d);
  }
  public static tipper tipper(final Tipper<ASTNode> ¢) {
    return new tipper(¢);
  }

  public static class tipper {
    private final Tipper<ASTNode> tipper;

    public tipper(final String p, final String r, final String d) {
      tipper = TipperFactory.tipper(p, r, d);
    }
    public tipper(final Tipper<ASTNode> t) {
      tipper = t;
    }
    public void nottips(final String ¢) {
      assert !tipper.canTip(extractStatementIfOne(wizard.ast(¢)));
    }
    public void tips(final String ¢) {
      assert tipper.canTip(extractStatementIfOne(wizard.ast(¢)));
    }
    public turns turns(final String ¢) {
      return new turns(tipper, ¢);
    }
  }

  static class expression {
    final String s;

    public expression(final String s) {
      this.s = s;
    }
    public void matches(final String s2) {
      assert Matcher.matches(wizard.ast(s), wizard.ast(s2));
    }
    public void notmatches(final String s2) {
      assert !Matcher.matches(wizard.ast(s), wizard.ast(s2));
    }
  }

  public static class turns {
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
    private static void azzertEquals(final String s, final Document d) {
      String actual = null;
      switch (GuessedContext.find(s)) {
        case COMPILATION_UNIT_LOOK_ALIKE:
          actual = d.get();
          break;
        case EXPRESSION_LOOK_ALIKE:
          actual = d.get().substring(23, d.get().length() - 3);
          break;
        case METHOD_LOOKALIKE:
          actual = d.get().substring(9, d.get().length() - 2);
          break;
        case OUTER_TYPE_LOOKALIKE:
          actual = d.get();
          break;
        case STATEMENTS_LOOK_ALIKE:
          actual = d.get().substring(15, d.get().length() - 1);
          break;
        default:
          break;
      }
      assertEquals(Essence.of(s), Essence.of(actual));
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
          return findFirst.instanceOf(Block.class, u);
        default:
          break;
      }
      return null;
    }
    private static String wrapCode(final String ¢) {
      switch (GuessedContext.find(¢)) {
        case COMPILATION_UNIT_LOOK_ALIKE:
          return ¢;
        case EXPRESSION_LOOK_ALIKE:
          return "class X{int f(){return " + ¢ + ";}}";
        case METHOD_LOOKALIKE:
          return "class X{" + ¢ + "}";
        case OUTER_TYPE_LOOKALIKE:
          return ¢;
        case STATEMENTS_LOOK_ALIKE:
          return "class X{int f(){" + ¢ + "}}";
        default:
          fail(¢ + " is not like anything I know...");
      }
      return null;
    }

    final Tipper<ASTNode> tipper;
    private final String s;

    public turns(final Tipper<ASTNode> tipper, final String _s) {
      this.tipper = tipper;
      s = _s;
    }
    /** XXX: This is a bug of auto-laconize [[SuppressWarningsSpartan]] */
    public void into(final String res) {
      final Document document = new Document(wrapCode(s));
      final ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setSource(document.get().toCharArray());
      final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
      final AST ast = cu.getAST();
      final ASTRewrite r = ASTRewrite.create(ast);
      final ASTNode n = extractStatementIfOne(extractASTNode(s, cu));
      n.accept(new ASTVisitor() {
        @Override public void preVisit(final ASTNode node) {
          if (tipper.canTip(node))
            tipper.tip(node).go(r, null);
        }
      });
      final TextEdit edits = r.rewriteAST(document, null);
      try {
        edits.apply(document);
      } catch (MalformedTreeException | BadLocationException x) {
        monitor.logEvaluationError(this, x);
      }
      azzertEquals(res, document);
    }
  }

  static ASTNode extractStatementIfOne(final ASTNode ¢) {
    return !iz.block(¢) || az.block(¢).statements().size() != 1 ? ¢ : (ASTNode) az.block(¢).statements().get(0);
  }
}
