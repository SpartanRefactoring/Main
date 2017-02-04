package il.org.spartan.spartanizer.leonidas;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An abstraction layer for the functionality of @{link TipperFactory}
 * and @{Matcher}.<br>
 * Allows easy match checking of patterns against strings and creation of
 * tippers from patterns.
 * @author Ori Marcovitch
 * @since 2016 */
public enum leonidasSays {
  ;
  public static class blockTurns {
    final Tipper<Block> tipper;
    final String string;

    public blockTurns(final Tipper<Block> tipper, final String _s) {
      this.tipper = tipper;
      string = _s;
    }

    public void into(@NotNull final String rrr) {
      final Document document = new Document(wrapCode(string));
      final ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setSource(document.get().toCharArray());
      final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
      final ASTRewrite r = ASTRewrite.create(cu.getAST());
      final ASTNode n = extractStatementIfOne(extractASTNode(string, cu));
      final Bool tipped = new Bool();
      n.accept(new ASTVisitor() {
        @Override public boolean visit(final Block ¢) {
          if (!tipper.canTip(¢))
            return true;
          tipped.inner = true;
          tipper.tip(¢).go(r, null);
          return true;
        }
      });
      assert tipped.inner : "cannot tip anything";
      final TextEdit edits = r.rewriteAST(document, null);
      try {
        edits.apply(document);
      } catch (@NotNull MalformedTreeException | BadLocationException ¢) {
        monitor.logEvaluationError(this, ¢);
      }
      azzertEquals(rrr, document);
    }
  }

  static class expression {
    @NotNull private static ASTNode ast(final String s2) {
      return extractStatementIfOne(wizard.ast(s2));
    }

    final String s;

    expression(final String s) {
      this.s = s;
    }

    public void matches(final String s2) {
      assert Matcher.patternMatcher(s, "").matches(ast(s2));
    }

    public void notmatches(final String s2) {
      assert !Matcher.patternMatcher(s, "").matches(ast(s2));
    }
  }

  public static class statementsTipper {
    private final Tipper<Block> tipper;

    public statementsTipper(@NotNull final String pattern, @NotNull final String replacement) {
      tipper = TipperFactory.patternTipper(pattern, replacement);
    }

    public statementsTipper(@NotNull final String pattern, @NotNull final String replacement, @NotNull final String description) {
      tipper = TipperFactory.patternTipper(pattern, replacement, description);
    }

    public statementsTipper(final UserDefinedTipper<Block> tipper) {
      this.tipper = tipper;
    }

    public void nottips(final String ¢) {
      assert !tipper.canTip(az.block(wizard.ast(¢)));
    }

    public void tips(final String ¢) {
      assert tipper.canTip(az.block(wizard.ast(¢)));
    }

    @NotNull public blockTurns turns(final String ¢) {
      return new blockTurns(tipper, ¢);
    }
  }

  public static class tipper {
    private final UserDefinedTipper<ASTNode> tipper;

    public tipper(@NotNull final String pattern, @NotNull final String replacement) {
      tipper = TipperFactory.patternTipper(pattern, replacement);
    }

    public tipper(@NotNull final String pattern, @NotNull final String replacement, @NotNull final String description) {
      tipper = TipperFactory.patternTipper(pattern, replacement, description);
    }

    public tipper(final UserDefinedTipper<ASTNode> tipper) {
      this.tipper = tipper;
    }

    public void nottips(final String ¢) {
      assert !tipper.canTip(wizard.ast(¢));
    }

    public void tips(final String ¢) {
      assert tipper.canTip(extractStatementIfOne(wizard.ast(¢)));
    }

    @NotNull public turns turns(final String ¢) {
      return new turns(tipper, ¢);
    }
  }

  public static class turns {
    final UserDefinedTipper<ASTNode> tipper;
    final String string;

    public turns(final UserDefinedTipper<ASTNode> tipper, final String _s) {
      this.tipper = tipper;
      string = _s;
    }

    public void into(@NotNull final String expected) {
      final Document document = new Document(wrapCode(string));
      final ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setSource(document.get().toCharArray());
      final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
      final ASTRewrite r = ASTRewrite.create(cu.getAST());
      final ASTNode n = extractStatementIfOne(extractASTNode(string, cu));
      final Bool tipped = new Bool();
      n.accept(new ASTVisitor() {
        @Override public void preVisit(final ASTNode ¢) {
          if (!tipper.canTip(¢))
            return;
          tipped.inner = true;
          tipper.tip(¢).go(r, null);
        }
      });
      assert tipped.inner : "cannot tip anything";
      final TextEdit edits = r.rewriteAST(document, null);
      try {
        edits.apply(document);
      } catch (@NotNull MalformedTreeException | BadLocationException ¢) {
        monitor.logEvaluationError(this, ¢);
      }
      azzertEquals(expected, document);
    }
  }

  static void azzertEquals(@NotNull final String s, @NotNull final Document d) {
    String actual = null;
    switch (GuessedContext.find(s)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
      case OUTER_TYPE_LOOKALIKE:
        actual = d.get();
        break;
      case EXPRESSION_LOOK_ALIKE:
        actual = d.get().substring(23, d.get().length() - 3);
        break;
      case METHOD_LOOK_ALIKE:
        actual = d.get().substring(9, d.get().length() - 2);
        break;
      case STATEMENTS_LOOK_ALIKE:
        actual = extractStatementIfOne(findFirst.instanceOf(Statement.class).in(wizard.ast(d.get()))) + "";
        break;
      default:
        azzert.that(Essence.of(actual).replaceAll(" ", ""), is(Essence.of(s).replaceAll(" ", "")));
    }
  }

  static ASTNode extractASTNode(@NotNull final String s, final CompilationUnit u) {
    switch (GuessedContext.find(s)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
      case OUTER_TYPE_LOOKALIKE:
        return u;
      case METHOD_LOOK_ALIKE:
        return findSecond(MethodDeclaration.class, u);
      case EXPRESSION_LOOK_ALIKE:
        return findSecond(Expression.class, findFirst.instanceOf(MethodDeclaration.class).in(u));
      case STATEMENTS_LOOK_ALIKE:
        return extractStatementIfOne(findFirst.instanceOf(Block.class).in(u));
      default:
        return null;
    }
  }

  @NotNull static ASTNode extractStatementIfOne(@NotNull final ASTNode ¢) {
    return !iz.block(¢) || statements(az.block(¢)).size() != 1 ? ¢ : (ASTNode) first(statements(az.block(¢)));
  }

  @Nullable static <N extends ASTNode> N findSecond(@NotNull final Class<?> c, @Nullable final ASTNode n) {
    if (n == null)
      return null;
    final Wrapper<Boolean> foundFirst = new Wrapper<>();
    foundFirst.set(Boolean.FALSE);
    final Wrapper<ASTNode> $ = new Wrapper<>();
    n.accept(new ASTVisitor() {
      @Override public boolean preVisit2(@NotNull final ASTNode ¢) {
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

  @NotNull public static statementsTipper statementsTipper(@NotNull final String p, @NotNull final String s, @NotNull final String d) {
    return new statementsTipper(TipperFactory.statementsPattern(p, s, d));
  }

  @NotNull public static expression that(final String ¢) {
    return new expression(¢);
  }

  @NotNull public static tipper tipper(@NotNull final String p, @NotNull final String s, @NotNull final String d) {
    return new tipper(p, s, d);
  }

  @NotNull public static tipper tipper(final UserDefinedTipper<ASTNode> ¢) {
    return new tipper(¢);
  }

  static String wrapCode(@NotNull final String ¢) {
    switch (GuessedContext.find(¢)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
      case OUTER_TYPE_LOOKALIKE:
        return ¢;
      case EXPRESSION_LOOK_ALIKE:
        return "class X{int f(){return " + ¢ + ";}}";
      case METHOD_LOOK_ALIKE:
        return "class X{" + ¢ + "}";
      case STATEMENTS_LOOK_ALIKE:
        return "class X{int f(){" + ¢ + "}}";
      default:
        fail(¢ + " is not like anything I know...");
        break;
    }
    return null;
  }
}
