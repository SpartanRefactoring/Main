package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;
import static il.org.spartan.utils.fluent.note.*;
import static org.hamcrest.collection.IsEmptyCollection.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** Misc unit tests with no better other place for version 3.00
 * @author Yossi Gil
 * @since 2017-03-09 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Version300 {
  @Test public void a() {
    azzert.that(
        theSpartanizer.once(//
            "boolean a(int[] b, int c){ if (d == c) return true; return false; }"), //
        iz(//
            "boolean a(int[] b, int c){ return d == c? true : false; }") //
    );
  }

  /** Introduced by Yogi on Wed-Apr-12-10:56:30-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Ignore("Yossi Gil") @Test public void inta6FinalAbNewAaIntc2dccdcc2() {
    trimminKof("int a = 6; final A b = new A(a); int c = 2 + d; c(c - d); c(c * 2);") //
        .using(new LocalVariableInitializedUnusedRemove(), VariableDeclarationFragment.class) //
        .gives("int a=6;new A(a);int c=2+d;c(c-d);c(c*2);") //
        .using(new InfixMultiplicationSort(), InfixExpression.class) //
        .gives("int a=6;new A(a);int c=2+d;c(c-d);c(2*c);") //
        .gives("new A(6);int c=2+d;c(c-d);c(2*c);") //
        .stays() //
    ;
  }

  @Test public void i0() {
    trimminKof("int a=1;return a > 0;")//
        .gives("return 1>0;");
  }

  @Test public void i1() {
    trimminKof("int k=1;return k > 0;")//
        .gives("return 1>0;");
  }

  @FunctionalInterface
  interface Find {
    //@formatter:off
    default Find prerequisite() { return null; }
    void find();
    default boolean B(final int ¢) { return fault.bool(bug(box.it(¢))); }
    default boolean B(final Object ... __) { return fault.bool(bug(__)); }
    default void S(final int ¢) { bug(box.it(¢)); }
    default void S(final Object ... __) { bug(__); }
    default void andAlso(final Find ¢){bug(¢);}
    default void butNot(final Find ¢){bug(¢);}
    default void orElse(final Find ¢){bug(¢);}
    default void andIs(final Runnable __1, final Runnable __2){bug(__1); bug(__2);}
    //@formatter:on
  }

  interface Replace extends Find {
    void replace();
  }

  Replace xxx = new Replace() {
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

  @Test public void abcd() {
    trimminKof("a = !(b ? c : d)") //
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

  @Test public void overridePublicStatementreplacementNotNullFinalBlockbNotNullFinalListStatementssextractstatementsbIfidenticalssstatementsbhazhidingsssReturnNullNullableFinalASTNodeparentazstatementparentbIfparentNulliztryStatementparentReturnreorganizeStatementbSwitchsssizeCase0ReturnmakeemptyStatementbCase1FinalStatementfirstssIfizblockEssentialReturnsubjectstatementtoBlockReturncopyofDefaultReturnreorganizeNestedStatementb() {
    trimminKof("/**/" + //
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

  /** Introduced by Yogi on Tue-Mar-28-02:39:50-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_aPublicBaCFinalDbCFinalEBcdebIffcebghcReturnNullFFinalGijkibIfiNulllmiReturnnbSwitchcoCase0ReturnpqbCase1FinalBrrscIfltrReturnukrvReturnwxrDefaultReturnyb() {
    trimminKof(
        "public B a(D b){X c=d.e(b);if(f(c,x)||g.h(c)){return n;}G i=j.k(i(b));if((i==n)||l.m(i)){return n(b);}switch(c.o()){case 0:return p.q(b);case 1:B r;r=s(c);if(l.t(r))return u.k(r).v();return w.x(r);default:return y(b);}}") //
            .using(new MethodDeclarationRenameReturnToDollar(), MethodDeclaration.class) //
            .gives(
                "public B a(D b){X c=d.e(b);if(f(c,x)||g.h(c)){return n;}G i=j.k(i(b));if((i==n)||l.m(i)){return n(b);}switch(c.o()){case 0:return p.q(b);case 1:B $;$=s(c);if(l.t($))return u.k($).v();return w.x($);default:return y(b);}}") //
            .using(new BlockSingleton(), Block.class) //
            .gives(
                "public B a(D b){X c=d.e(b);if(f(c,x)||g.h(c))return n;G i=j.k(i(b));if((i==n)||l.m(i))return n(b);switch(c.o()){case 0:return p.q(b);case 1:B $;$=s(c);if(l.t($))return u.k($).v();return w.x($);default:return y(b);}}") //
    ;
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
    trimminKof("{}") //
        .gives("") //
        .stays() //
    ;
  }

  /** Automatically generated on Thu-Mar-16-08:15:41-IST-2017, copied by
   * Yossi */
  @Test public void ifaAb() {
    trimminKof("if (a) { A b; }") //
        .using(new IfDeadRemove(), IfStatement.class) //
        .gives("{}") //
        .gives("") //
        .stays() //
    ;
  }

  @Test public void ifab() {
    trimminKof("if (a++ == b++) { }") //
        .using(new IfEmptyThenEmptyElse(), IfStatement.class) //
        .gives("a++;b++;") //
        .gives("++a;++b;") //
        .stays() //
    ;
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement() {
    trimminKof("if(a) { int i = 3; }")//
        .gives("{}") //
        .gives("") //
        .stays();
  }

  @Test public void ifDoNotRemoveBracesWithVariableDeclarationStatement2() {
    trimminKof("if(a) { Object o; }")//
        .gives("{}") //
        .gives("") //
        .stays();
  }

  @Ignore @Test public void issue73c() {
    trimminKof("int foo(Integer integer, ASTNode astn){return integer + astn.hashCode();}")//
        .gives("int foo(Integer integer,ASTNode n){return integer+n.hashCode();}") //
        .gives("int foo(Integer i, ASTNode n){return i + n.hashCode();}") //
        .stays();
  }

  @Ignore @Test public void inlineArrayInitialization1() {
    trimminKof("public void multiDimensionalIntArraysAreEqual(){ " //
        + " int[][] int1={{1, 2, 3}, {4, 5, 6}}; " //
        + " int[][] int2={{1, 2, 3}, {4, 5, 6}}; " //
        + " assertArrayEquals(int1, int2); " //
        + "}")
            .gives("public void multiDimensionalIntArraysAreEqual(){ " //
                + " int[][] int1={{1, 2, 3}, {4, 5, 6}}" //
                + " , int2={{1, 2, 3}, {4, 5, 6}}; " //
                + " assertArrayEquals(int1, int2); " //
                + "}")
            .gives("public void multiDimensionalIntArraysAreEqual(){ " //
                + " assertArrayEquals(new int[][]{{1,2,3},{4,5,6}},new int[][]{{1,2,3},{4,5,6}}); " //
                + "}");
  }

  @Test public void intaIntbForb100bb1IfFalseBreakReturnb() {
    trimminKof("int a(int b) { for (; b < 100; b = b + 1) if (false) break; return b; }") //
        .using(new AssignmentToFromInfixIncludingTo(), Assignment.class) //
        .gives("int a(int b){for(;b<100;b+=1)if(false)break;return b;}") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("int a(int b){for(;b<100;b+=1){}return b;}")//
    ;
  }

  public void kill(final String from, final String to) {
    azzert.that(kill(make(from)), iz(to));
  }

  @Ignore @Test public void killer() {
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

  @Ignore @Test public void killerArrayInitiaizer() {
    kill("A __ = new A[q()] ={ f(), g(), h(),++i };", "{q(); f(); g(); h(); ++i;}");
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

  // @Ignore("Unignore one by one")
  @Test public void negationPushdownTernary() {
    trimminKof("a = !(b ? c: d)")//
        .using(new PrefixNotPushdown(), PrefixExpression.class)//
        .gives("a=b?!c:!d") //
    ;
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
    trimminKof("boolean a(int[] b, int c){ if (d == c) return true; return false; }") //
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
    trimminKof("switch(n.getNodeType()){case BREAK_STATEMENT:return 0;case CONTINUE_STATEMENT:return 1;case RETURN_STATEMENT:return 2;"
        + "case THROW_STATEMENT:return 3;default:return-1;}")//
            .stays();
  }

  @Test public void x() {
    trimminKof("int f(int i) { for(;i<100;i=i+1) if(false) return; return i; }")//
        .gives("int f(int ¢){for(;¢<100;¢=¢+1)if(false)return;return ¢;}") //
        .gives("int f(int ¢){for(;¢<100;¢+=1){}return ¢;}");
  }

  /** Introduced by Yossi on Thu-Mar-16-12:37:12-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void x1() {
    trimminKof("int a(int b) { for (; b < 100; b = b + 1) if (false) break; return b; }") //
        .using(new AssignmentToFromInfixIncludingTo(), Assignment.class) //
        .gives("int a(int b){for(;b<100;b+=1)if(false)break;return b;}") //
        .using(new IfTrueOrFalse(), IfStatement.class) //
        .gives("int a(int b){for(;b<100;b+=1){}return b;}");
  }

  @UnderConstruction private Block kill(final Expression ¢) {
    final Block $ = ¢.getAST().newBlock();
    statements($).addAll(wizard.decompose(¢));
    return $;
  }
}
