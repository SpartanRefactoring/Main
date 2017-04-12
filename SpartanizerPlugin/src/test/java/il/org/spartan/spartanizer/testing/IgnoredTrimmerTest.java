package il.org.spartan.spartanizer.testing;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** @author Yossi Gil*@since 2014-07-10 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class IgnoredTrimmerTest {
  public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimminKof("@SuppressWarnings()int$=(Class<T>)findClass(className);\n")//
        .stays();
  }

  /** Introduced by Yogi on Wed-Apr-12-10:49:02-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void intab0Intc3Intdc2Intedc19be2dea() {
    trimminKof("int a = b(0); int c = 3; int d = c + 2; int e = d + c - 19; b(e * 2 - d / e + a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=b(0),c=3;int d=c+2;int e=d+c-19;b(e*2-d/e+a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=b(0),c=3,d=c+2;int e=d+c-19;b(e*2-d/e+a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=b(0),c=3,d=c+2,e=d+c-19;b(e*2-d/e+a);") //
        .using(new LocalVariableInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int c=3,d=c+2,e=d+c-19;b(e*2-d/e+b(0));") //
        .using(new InfixAdditionSort(), InfixExpression.class) //
        .gives("int c=3,d=c+2,e=c+d-19;b(e*2-d/e+b(0));") //
        .using(new InfixMultiplicationSort(), InfixExpression.class) //
        .gives("int c=3,d=c+2,e=c+d-19;b(2*e-d/e+b(0));") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Wed-Apr-12-10:51:24-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void inta5b3Intcb2Intdcb19aec() {
    trimminKof("int a = 5, b = 3; int c = b + 2; int d = c + b - 19 + a; e(c);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3,c=b+2;int d=c+b-19+a;e(c);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3,c=b+2,d=c+b-19+a;e(c);") //
        .using(new LocalVariableInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int a=5,b=3,c=b+2;e(c);") //
        .using(new LocalVariableInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int b=3,c=b+2;e(c);") //
        .using(new LocalVariableInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int b=3;e((b+2));") //
        .using(new LocalVariableInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("e((3+2));") //
        .using(new ParenthesizedRemoveExtraParenthesis(), ParenthesizedExpression.class) //
        .gives("e(3+2);") //
        .using(new InfixAdditionEvaluate(), InfixExpression.class) //
        .gives("e(5);") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Wed-Apr-12-10:53:05-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void inta5Intb3Intcb2Intdcb19ed2cda() {
    trimminKof("int a = 5; int b = 3; int c = b + 2; int d = c + b - 19; e(d * 2 - c / d + a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3;int c=b+2;int d=c+b-19;e(d*2-c/d+a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3,c=b+2;int d=c+b-19;e(d*2-c/d+a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3,c=b+2,d=c+b-19;e(d*2-c/d+a);") //
        .using(new LocalVariableInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int b=3,c=b+2,d=c+b-19;e(d*2-c/d+5);") //
        .using(new InfixAdditionSort(), InfixExpression.class) //
        .gives("int b=3,c=b+2,d=b+c-19;e(d*2-c/d+5);") //
        .using(new InfixMultiplicationSort(), InfixExpression.class) //
        .gives("int b=3,c=b+2,d=b+c-19;e(2*d-c/d+5);") //
        .stays() //
    ;
  }

  @Test public void inlineSingleUse04() {
    trimminKof("int x=6;final C b=new C(x);int y=2+b.j;y(y-b.j);y(y*2);").gives("final C b=new C((6));int y=2+b.j;y(y-b.j);y(y*2);");
  }

  @Test public void inlineSingleUse05() {
    trimminKof("int x=6;final C b=new C(x);int y=2+b.j;y(y+x);y(y*x);")//
        .gives("int x=6;int y=2+(new C(x)).j;y(y+x);y(y*x);");
  }

  @Test public void inlineSingleUse09() {
    trimminKof(
        "final A a=new D().new A(V){\nABRA\n{\nCADABRA\n{V;);wizard.assertEquals(5,a.new Context().lineCount());final PureIterable&lt;Mutant&gt;ms=a.generateMutants();wizard.assertEquals(2,count(ms));final PureIterator&lt;Mutant&gt;i=ms.iterator();assert(i.hasNext());wizard.assertEquals(V;{\nABRA\nABRA\n{\nCADABRA\n{\nV;,i.next().text);assert(i.hasNext());wizard.assertEquals(V;{\nABRA\n{\nCADABRA\nCADABRA\n{\nV;,i.next().text);assert!(i.hasNext());")
            .stays();
  }

  @Test public void inlineSingleUse10() {
    trimminKof(
        "final A a=new A(\"{\nABRA\n{\nCADABRA\n{\");wizard.assertEquals(5,a.new Context().lineCount());final PureIterable<Mutant>ms=a.mutantsGenerator();wizard.assertEquals(2,count(ms));final PureIterator<Mutant>i=ms.iterator();assert(i.hasNext());wizard.assertEquals(\"{\nABRA\nABRA\n{\nCADABRA\n{\n\",i.next().text);assert(i.hasNext());wizard.assertEquals(\"{\nABRA\n{\nCADABRA\nCADABRA\n{\n\",i.next().text);assert!(i.hasNext());")
            .stays();
  }

  @Test public void reanmeReturnVariableToDollar01() {
    trimminKof("public C(int i){j=2*i;public final int j;public C y6(){final C res=new C(6);S.x.f(res.j);return res;")
        .gives("public C(int i){j=2*i;public final int j;public C y6(){final C$=new C(6);S.x.f($.j);return$;");
  }

  @Test public void reanmeReturnVariableToDollar02() {
    trimminKof(
        "int res=b.l();if(b.c(1))return res*2;if(res%2==0)return++res;if(b.startsWith(\"y\")){return y(res);int x=res+6;if(x>1)return res+x;res-=1;return res;")
            .gives("int$=b.l();if(b.c(1))return$*2;if($%2==0)return++$;if(b.startsWith(\"y\")){return y($);int x=$+6;if(x>1)return$+x;$-=1;return$;");
  }

  @Test public void reanmeReturnVariableToDollar03() {
    trimminKof(
        "public C(int i){j=2*i;public final int j;public int y7(final String b){final C res=new C(b.l());if(b.c(1))return res.j;int x=b.l()/2;if(x==3)return x;x=y(res.j-x);return x;")
            .gives(
                "public C(int i){j=2*i;public final int j;public int y7(final String b){final C res=new C(b.l());if(b.c(1))return res.j;int$=b.l()/2;if($==3)return$;$=y(res.j-$);return$;");
  }

  @Test public void reanmeReturnVariableToDollar06() {
    trimminKof(
        "j=2*i;}public final int j;public void y6(){final C res=new C(6);final Runnable r=new Runnable(){@Override public void system(){final C res2=new C(res.j);S.x.f(res2.j);doStuff(res2);private int doStuff(final C r){final C res=new C(r.j);return res.j+1;S.x.f(res.j);")
            .gives(
                "j=2*i;}public final int j;public void y6(){final C res=new C(6);final Runnable r=new Runnable(){@Override public void system(){final C res2=new C(res.j);S.x.f(res2.j);doStuff(res2);private int doStuff(final C r){final C$=new C(r.j);return$.j+1;S.x.f(res.j);");
  }

  @Test public void reanmeReturnVariableToDollar07() {
    trimminKof(
        "j=2*i;}public final int j;public C y6(){final C res=new C(6);final Runnable r=new Runnable(){@Override public void system(){res=new C(8);S.x.f(res.j);doStuff(res);private void doStuff(C res2){S.x.f(res2.j);private C res;S.x.f(res.j);return res;")
            .gives(
                "j=2*i;}public final int j;public C y6(){final C$=new C(6);final Runnable r=new Runnable(){@Override public void system(){res=new C(8);S.x.f(res.j);doStuff(res);private void doStuff(C res2){S.x.f(res2.j);private C res;S.x.f($.j);return$;");
  }

  @Test public void reanmeReturnVariableToDollar08() {
    trimminKof("public C(int i){j=2*i;public final int j;public C y6(){final C res=new C(6);if(res.j==0)return null;S.x.f(res.j);return res;")
        .gives("public C(int i){j=2*i;public final int j;public C y6(){final C$=new C(6);if($.j==0)return null;S.x.f($.j);return$;");
  }

  @Test public void reanmeReturnVariableToDollar09() {
    trimminKof("public C(int i){j=2*i;public final int j;public C y6(){final C res=new C(6);if(res.j==0)return null;S.x.f(res.j);return null;")
        .stays();
  }

  @Test public void reanmeReturnVariableToDollar10() {
    trimminKof("@Override public I[]g(final M m){try{final L s=All.get((String)m.getAttribute(Builder.L_TYPE_KEY));")
        .gives("@Override public I[]g(final M m){try{final L$=All.get((String)m.getAttribute(Builder.L_TYPE_KEY));");
  }

  @Test public void renameVariableUnderscore2() {
    trimminKof("class A{int__;int f(int__){return__;}}")//
        .gives("class A{int____;int f(int____){return____;}}");
  }

  @Test public void replaceClassInstanceCreationWithFactoryClassInstanceCreation() {
    trimminKof("Character x=new Character(new Character(f()));")//
        .gives("Character x=Character.valueOf(Character.valueOf(f()));");
  }

  @Test public void stringFromBuilderAddParenthesis() {
    trimminKof("new StringBuilder(f()).append(1+1).toString()")//
        .gives("\"\"+f()+(1+1)");
  }

  @Test public void stringFromBuilderGeneral() {
    trimminKof("new StringBuilder(myName).append(\"\'s grade is\").append(100).toString()")//
        .gives("myName+\"\'s grade is\"+100");
  }

  @Test public void stringFromBuilderNoStringComponents() {
    trimminKof("new StringBuilder(0).append(1).toString()")//
        .gives("\"\"+0+1");
  }
}