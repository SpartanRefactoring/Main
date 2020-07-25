package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.instanceOf;
import static fluent.ly.azzert.is;
import static fluent.ly.azzert.iz;
import static fluent.ly.azzert.not;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.engine.parse.s;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.logging.Level;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Ignore;
import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.box;
import fluent.ly.note;
import fluent.ly.system;
import il.org.spartan.spartanizer.ast.navigate.compute;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.cmdline.good.theSpartanizer;
import il.org.spartan.spartanizer.tippers.AssignmentToFromInfixIncludingTo;
import il.org.spartan.spartanizer.tippers.BlockSingletonEliminate;
import il.org.spartan.spartanizer.tippers.IfDeadRemove;
import il.org.spartan.spartanizer.tippers.IfEmptyThenEmptyElse;
import il.org.spartan.spartanizer.tippers.IfTrueOrFalse;
import il.org.spartan.spartanizer.tippers.InfixMultiplicationSort;
import il.org.spartan.spartanizer.tippers.LocalInitializedUnusedRemove;
import il.org.spartan.spartanizer.tippers.MethodDeclarationRenameReturnToDollar;
import il.org.spartan.spartanizer.tippers.PrefixNotPushdown;
import il.org.spartan.spartanizer.tippers.RemoveRedundantSwitchReturn;
import il.org.spartan.spartanizer.tipping.TraversalMonitor;
import il.org.spartan.utils.UnderConstruction;
import il.org.spartan.utils.fault;

/** Misc unit tests with no better other place for version 3.00
 * @author Yossi Gil
 * @since 2017-03-09 */
// TODO: Yossi Gil
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public class Version300 {
  @FunctionalInterface
  interface Find {
    default void andAlso(final Find ¢) {
      note.bug(¢);
    }
    default void andIs(final Runnable __1, final Runnable __2) {
      note.bug(__1);
      note.bug(__2);
    }
    //@formatter:on
    default boolean B(final int ¢) {
      return fault.bool(note.bug(box.it(¢)));
    }
    default boolean B(final Object... __) {
      return fault.bool(note.bug(__));
    }
    default void butNot(final Find ¢) {
      note.bug(¢);
    }
    void find();
    default void orElse(final Find ¢) {
      note.bug(¢);
    }
    //@formatter:off
    default Find prerequisite() { return null; }
    default void S(final int ¢) { note.bug(box.it(¢)); }
    default void S(final Object ... __) { note.bug(__); }
  }
  interface Replace extends Find {
    void replace();
  }
  @Test public void a() {
    azzert.that(
        theSpartanizer.once(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c? true : false; }") //
    );
  }
  @Test public void abcd() {
    trimmingOf("a = !(b ? c : d)") //
        .using(new PrefixNotPushdown(), PrefixExpression.class) //
        .gives("a=b?!c:!d") //
        .stays() //
    ;
  }

  @Test public void b() {
    azzert.that(
        theSpartanizer.twice(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c || false; }") //
    );
  }

  @Test public void c() {
    azzert.that(
        theSpartanizer.thrice(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c; }") //
    );
  }

  @Test public void d() {
    azzert.that(
        theSpartanizer.repetitively(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c; }") //
    );
  }

  /** Automatically generated on Thu-Mar-16-08:26:53-IST-2017, copied by
   * Yossi */
  @Test public void genererated13() {
    trimmingOf("{}") //
        .gives("") //
        .stays() //
    ;
  }

  public Replace getXxx() {
    return new Replace() {
      {
        andAlso(() -> {/**/});
        andIs(() -> S(1), () -> {
          if (B(1))
            S(1);
          else if (B(2))
            S(new Object());
        });
      }

      @Override public void find() {
        if (B(0))
          S(1);
        else
          S(1);
      }

      @Override public void replace() {
        if (B(0))
          S();
        else
          S(1);
      }
    };
  }

  @Test public void i0() {
    trimmingOf("int a=1;return a > 0;")//
        .gives("return 1>0;");
  }

  @Test public void i1() {
    trimmingOf("int k=1;return k > 0;")//
        .gives("return 1>0;");
  }

  /** Automatically generated on Thu-Mar-16-08:15:41-IST-2017, copied by
   * Yossi */
  @Test public void ifaAb() {
    trimmingOf("if (a) { A b; }") //
        .using(new IfDeadRemove(), IfStatement.class) //
        .gives("{}") //
        .gives("") //
        .stays() //
    ;
  }

  @Test public void ifab() {
    trimmingOf("if (a++ == b++) { }") //
        .using(new IfEmptyThenEmptyElse(), IfStatement.class) //
        .gives("a++;b++;") //
        .gives("++a;++b;") //
        .stays() //
    ;
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement() {
    trimmingOf("if(a) { int i = 3; }")//
        .gives("{}") //
        .gives("") //
        .stays();
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement2() {
    trimmingOf("if(a) { Object o; }")//
        .gives("{}") //
        .gives("") //
        .stays();
  }

  @Test public void inlineArrayInitialization1() {
    trimmingOf("public void multiDimensionalIntArraysAreEqual(){ " //
        + " int[][] int1={{1, 2, 3}, {4, 5, 6}}; " //
        + " int[][] int2={{1, 2, 3}, {4, 5, 6}}; " //
        + " assertArrayEquals(int1, int2); " //
        + "}")
            .gives("public void multiDimensionalIntArraysAreEqual(){ " //
                + " int[][] int1={{1, 2, 3}, {4, 5, 6}}" //
                + " , int2={{1, 2, 3}, {4, 5, 6}}; " //
                + " assertArrayEquals(int1, int2); " //
                + "}").stays();
  }

  /** Introduced by Yogi on Wed-Apr-12-10:56:30-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Ignore("Yossi Gil") @Test public void inta6FinalAbNewAaIntc2dccdcc2() {
    trimmingOf("int a = 6; final A b = new A(a); int c = 2 + d; c(c - d); c(c * 2);") //
        .using(new LocalInitializedUnusedRemove(), VariableDeclarationFragment.class) //
        .gives("int a=6;new A(a);int c=2+d;c(c-d);c(c*2);") //
        .using(new InfixMultiplicationSort(), InfixExpression.class) //
        .gives("int a=6;new A(a);int c=2+d;c(c-d);c(2*c);") //
        .gives("new A(6);int c=2+d;c(c-d);c(2*c);") //
        .stays() //
    ;
  }

  @Test public void intaIntbForb100bb1IfFalseBreakReturnb() {
    trimmingOf("int a(int b) { for (; b < 100; b = b + 1) if (false) break; return b; }") //
        .using(new AssignmentToFromInfixIncludingTo(), Assignment.class) //
        .gives("int a(int b){for(;b<100;b+=1)if(false)break;return b;}") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("int a(int b){for(;b<100;b+=1){}return b;}")//
    ;
  }

  @Test public void issue73c() {
    trimmingOf("int foo(Integer integer, ASTNode astn){return integer + astn.hashCode();}")//
        .gives("int foo(Integer integer,ASTNode n){return integer+n.hashCode();}") //
        .stays();
  }

  public void kill(final String from, final String to) {
    azzert.that(kill(make(from)), iz(to));
  }

  @Test public void killer() {
    {
      final Expression e = make("int i = 3;");
      kill(e);
      (kill(e) + "").hashCode();
      azzert.that(kill(e), instanceOf(Block.class));
      assert kill(e) != null;
      assert kill(e).statements() != null;
      assert statements(kill(e)) != null;
      azzert.that(statements(kill(e)), is(empty()));
    }
    {
      final Expression e = make("int i = f();");
      kill(e);
      (kill(e) + "").hashCode();
      azzert.that(kill(e), instanceOf(Block.class));
      assert kill(e) != null;
      assert kill(e).statements() != null;
      azzert.that(statements(kill(e)), not(empty()));
      assert statements(kill(e)) != null;
    }
    kill("int _ = f();", "{f();}");
    kill("int _ = 3 + f();", "{f();}");
    kill("int _ = g() + f();", "{g();f();}");
    kill("int _ = i++ + f();", "{i++;f();}");
    kill("int _ = i++ + i--;", "{i++;i--;}");
    kill("int _ = ++i + i--;", "{++i;i--;}");
    kill("int _ = -i + i--;", "{i--;}");
    kill("int _ = b==q();", "{q();}");
    kill("int _ = (a=b);", "{a=b;}");
    kill("int _ = (a=b++);", "{a=b++;}");
    kill("int _ = new A();", "{new A();}");
    kill("int _ = new A(){};", "{new A(){};}");
    kill("int _ = super.f();", "{super.f();}");
    kill("int _ = new int[++i * j--];", "{++i; j--;}");
    kill("A __ = new A(f(),i++);", "{new A(f(),i++);}");
    kill("A _ =  ((a=b)*i++)+f(g())*((a=b)*i++) + ++j;", "{a=b; i++; f(g());a=b;i++; ++j;}");
  }

  //Spartanizer problem
  @Ignore
  @Test public void killerArrayInitiaizer() {
    kill("A __ = new A[q()] ={ f(), g(), h(),++i };", "{q(); f(); g(); h(); ++i;}");
  }

  @Test public void localIfUpdate1() {
    TraversalMonitor.logger.setLevel(Level.ALL);
    trimmingOf("int a=0;if(y)a+=3;f(a,a,a);")//
        .gives("int a=y ? 0 + 3 : 0;f(a,a,a);") //
        .gives("int a=!y ? 0 : 0+3;f(a,a,a);") //
        .gives("int a=!y ? 0 : 3;f(a,a,a);") //
        .gives("int a=y ? 3 : 0;f(a,a,a);") //
        .stays();
  }

  @Test public void localIfUpdate2() {
    trimmingOf("String u=s;if(s.equals(y))u +=s + blah;S.h(u);").gives("String u=s.equals(y)? s +(s + blah):s;S.h(u);");
  }

  @Test public void localIfUpdate3() {
    trimmingOf("int a=f();return a +=2 * a;")//
    .gives("int a=f();return a + 2 * a;")//
        .stays();
  }

  public Expression make(final String statement) {
    return findFirst.instanceOf(VariableDeclarationFragment.class).in(s(statement)).getInitializer();
  }

  @Test public void myClassNameTest() {
    azzert.that(system.myFullClassName(), is(getClass().getCanonicalName()));
  }

  @Test public void myShortClassNameTest() {
    azzert.that(system.myShortClassName(), is(getClass().getSimpleName()));
  }

  // @forget("Unignore one by one")
  @Test public void negationPushdownTernary() {
    trimmingOf("a = !(b ? c: d)")//
        .using(new PrefixNotPushdown(), PrefixExpression.class)//
        .gives("a=b?!c:!d") //
    ;
  }

  @Test public void overridePublicStatementreplacementNonNullFinalBlockbNonNullFinalListStatementssextractstatementsbIfidenticalssstatementsbhazhidingsssReturnNullNullableFinalASTNodeparentazstatementparentbIfparentNulliztryStatementparentReturnreorganizeStatementbSwitchsssizeCase0ReturnmakeemptyStatementbCase1FinalStatementfirstssIfizblockEssentialReturnsubjectstatementtoBlockReturncopyofDefaultReturnreorganizeNestedStatementb() {
    trimmingOf("/**/" + //
        "@A public B a(@C D b) {" + //
        "    @C E<B> c = d.x;" + //
        "    if (f(c, x) || g.h(c)) {" + //
        "      return null;" + //
        "    }" + //
        "    @F G i = j.k(i(b));" + //
        "    if ((i == null) || l.m(i)) {" + //
        "      return n(b);" + //
        "    }" + //
        "    switch (c.o()) {" + //
        "      case 0:" + //
        "        return p.q(b);" + //
        "      case 1:" + //
        "        B r;" + //
        "        r = s(c);" + //
        "        if (l.t(r))" + //
        "          return u.k(r).v();" + //
        "        return w.x(r);" + //
        "      default:" + //
        "        return y(b);" + //
        "    }" + //
        "  }"//
    ).gives(
        // Edit this to reflect your expectation
        "/**/" + //
            "@A public B a(@C D b) {" + //
            "    @C E<B> c = d.x;" + //
            "    if (f(c, x) || g.h(c)) {" + //
            "      return null;" + //
            "    }" + //
            "    @F G i = j.k(i(b));" + //
            "    if ((i == null) || l.m(i)) {" + //
            "      return n(b);" + //
            "    }" + //
            "    switch (c.o()) {" + //
            "      case 0:" + //
            "        return p.q(b);" + //
            "      case 1:" + //
            "        B $;" + //
            "        $ = s(c);" + //
            "        if (l.t($))" + //
            "          return u.k($).v();" + //
            "        return w.x($);" + //
            "      default:" + //
            "        return y(b);" + //
            "    }" + //
            "  }"//
    //
    )//
        .gives(
            "@A public B a(@C D b){@C E<B>c=d.x;if(f(c,x)||g.h(c))return null;@F G i=j.k(i(b));if((i==null)||l.m(i))return n(b);switch(c.o()){case 0:return p.q(b);case 1:B $;$=s(c);if(l.t($))return u.k($).v();return w.x($);default:return y(b);}}") //
        .stays();
  }

  @Test public void stA() {
    azzert.that(
        theSpartanizer.repetitively(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c; }") //
    );
  }

  @Test public void stAgives() {
    trimmingOf("boolean a(int[] b, int c){ if (d == c) return true; return false; }") //
        .gives("boolean a(int[] b, int c){ return d == c ? true: false; }") //
        .gives("boolean a(int[] b, int c){ return d == c || false; }") //
        .gives("boolean a(int[] b, int c){ return d == c; }") //
        .stays() //
    ;
  }

  @Test public void stB() {
    azzert.that(
        theSpartanizer.repetitively(//
            "A a(A b) throws B { A $; $ = b; return $; }"), //
        iz("A a(A b) throws B { return b; }"));
  }

  @Test public void stC() {
    azzert.that(
        theSpartanizer.repetitively(//
            "A a(A b) throws B { A $ = b; return $; }"), //
        iz("A a(A b) throws B { return b; }"));
  }

  @Test public void stZ() {
    azzert.that(
        theSpartanizer.repetitively(//
            "A a(A b)throws B{ A c; c = b; return c; }"), //
        iz(//
            "A a(A b) throws B { return b; }"));
  }

  /** trimmer wraps with void method so it is tipped by
   * {@link RemoveRedundantSwitchReturn} */
  @Ignore("Yuval Simon") @Test public void switchSimplifyCaseAfterDefault1() {
    trimmingOf("switch(n.getNodeType()){case BREAK_STATEMENT:return 0;case CONTINUE_STATEMENT:return 1;case RETURN_STATEMENT:return 2;"
        + "case THROW_STATEMENT:return 3;default:return-1;}")//
            .stays();
  }

  /** Introduced by Yogi on Tue-Mar-28-02:39:50-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_aPublicBaCFinalDbCFinalEBcdebIffcebghcReturnNullFFinalGijkibIfiNulllmiReturnnbSwitchcoCase0ReturnpqbCase1FinalBrrscIfltrReturnukrvReturnwxrDefaultReturnyb() {
    trimmingOf(
        "public B a(D b){X c=d.e(b);if(f(c,x)||g.h(c)){return n;}G i=j.k(i(b));if((i==n)||l.m(i)){return n(b);}switch(c.o()){case 0:return p.q(b);case 1:B r;r=s(c);if(l.t(r))return u.k(r).v();return w.x(r);default:return y(b);}}") //
            .using(new MethodDeclarationRenameReturnToDollar(), MethodDeclaration.class) //
            .gives(
                "public B a(D b){X c=d.e(b);if(f(c,x)||g.h(c)){return n;}G i=j.k(i(b));if((i==n)||l.m(i)){return n(b);}switch(c.o()){case 0:return p.q(b);case 1:B $;$=s(c);if(l.t($))return u.k($).v();return w.x($);default:return y(b);}}") //
            .using(new BlockSingletonEliminate(), Block.class) //
            .gives(
                "public B a(D b){X c=d.e(b);if(f(c,x)||g.h(c))return n;G i=j.k(i(b));if((i==n)||l.m(i))return n(b);switch(c.o()){case 0:return p.q(b);case 1:B $;$=s(c);if(l.t($))return u.k($).v();return w.x($);default:return y(b);}}") //
    ;
  }

  @Test public void x() {
    trimmingOf("int f(int i) { for(;i<100;i=i+1) if(false) return; return i; }")//
        .gives("int f(int ¢){for(;¢<100;¢=¢+1)if(false)return;return ¢;}") //
        .gives("int f(int ¢){for(;¢<100;¢+=1){}return ¢;}");
  }

  /** Introduced by Yossi on Thu-Mar-16-12:37:12-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void x1() {
    trimmingOf("int a(int b) { for (; b < 100; b = b + 1) if (false) break; return b; }") //
        .using(new AssignmentToFromInfixIncludingTo(), Assignment.class) //
        .gives("int a(int b){for(;b<100;b+=1)if(false)break;return b;}") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("int a(int b){for(;b<100;b+=1){}return b;}");
  }

  @UnderConstruction private Block kill(final Expression ¢) {
    final Block $ = ¢.getAST().newBlock();
    statements($).addAll(compute.decompose(¢));
    return $;
  }
}
