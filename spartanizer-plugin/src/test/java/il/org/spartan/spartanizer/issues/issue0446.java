package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.junit.Ignore;
import org.junit.Test;

import il.org.spartan.spartanizer.cmdline.JUnitTestMethodFacotry;
import il.org.spartan.spartanizer.tippers.MethodDeclarationRenameReturnToDollar;
import il.org.spartan.spartanizer.tippers.ParenthesizedRemoveExtraParenthesis;
import il.org.spartan.spartanizer.tippers.TwoDeclarationsIntoOne;

/** Tests of GitHub issue thus numbered
 * <p>
 * tests fail because the move to initializer is too careful; t does not allow
 * moving a variable if any declaration is made inside the loop. The correct
 * behavior is to see if the updated variable is defined in the loop, or whether
 * it is dependent on values defined in the loop.
 * @author Yossi Gil
 * @since 2016 */
@Ignore
@SuppressWarnings({ "static-method", "javadoc" }) //
public class issue0446 {
  /** Introduced by Yogi on Fri-Apr-28-09:24:16-IDT-2017 (code generated
   * automatically by {@link JUnitTestMethodFacotry}) */
  @Test public void t10() {
    trimmingOf(
        " A a(B b, A c) { A d = 0; for (byte[] e = f(); d < c;) { A g = c - d; A h = i(b, g); if (h == 0 && (h = b.j(e, 0, ((int) C.k(g, l)))) == -1) break; d += h; } return d; }") //
            .using(MethodDeclaration.class, new MethodDeclarationRenameReturnToDollar()) //
            .gives(" A a(B b,A c){A $=0;for(byte[] e=f();$<c;){A g=c-$;A h=i(b,g);if(h==0&&(h=b.j(e,0,((int)C.k(g,l))))==-1)break;$+=h;}return $;}") //
            .using(VariableDeclarationStatement.class, new TwoDeclarationsIntoOne()) //
            .gives(" A a(B b,A c){A $=0;for(byte[] e=f();$<c;){A g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,((int)C.k(g,l))))==-1)break;$+=h;}return $;}") //
            .using(ParenthesizedExpression.class, new ParenthesizedRemoveExtraParenthesis()) //
            .gives(" A a(B b,A c){A $=0;for(byte[] e=f();$<c;){A g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;$+=h;}return $;}") //
            .stays() //
    ;
  }
  @Test public void t0() {
    trimmingOf("for (int a = 0; a < 100;) {f(a,b); if (x) break; a +=b;}").gives("for (int a = 0; a < 100;a +=b) {f(a,b); if (x) break;}");
  }
  @Test public void t1() {
    trimmingOf("for (int a = 0; a < 100;a++) {f(a,b); if (x) break; a +=b;}").gives("for (int a = 0; a < 100;a +=b, a++) {f(a,b); if (x) break;}");
  }
  @Test public void t2() {
    trimmingOf(
        "int a(A b,int c) {int d = 0; for (byte[] e = f(); d < c;) {int g = c - d;int h = i(b, g); if (h == 0 && (h = b.j(e, 0, ((int) C.k(g, l)))) == -1) break; d += h; } return d; }") //
            .using(MethodDeclaration.class, new MethodDeclarationRenameReturnToDollar()) //
            .gives("int a(A b,L c){L $=0;for(byte[] e=f();$<c;){L g=c-$;L h=i(b,g);if(h==0&&(h=b.j(e,0,((int)C.k(g,l))))==-1)break;$+=h;}return $;}") //
            .using(VariableDeclarationStatement.class, new TwoDeclarationsIntoOne()) //
            .gives("int a(A b,L c){L $=0;for(byte[] e=f();$<c;){L g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,((int)C.k(g,l))))==-1)break;$+=h;}return $;}") //
            .using(ParenthesizedExpression.class, new ParenthesizedRemoveExtraParenthesis()) //
            .gives("int a(A b,L c){L $=0;for(byte[] e=f();$<c;){L g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;$+=h;}return $;}") //
            .stays() //
    ;
  }
  @Test public void t3() {
    trimmingOf("int a(A b,int c) {" + //
        "int d = 0;" + //
        " for (byte[] e = f(); d < c;) {" + //
        "  int g = c - d;" + //
        "  int h = i(b, g);" + //
        "   if (h == 0 && (h = b.j(e, 0, ((int) C.k(g, l)))) == -1)" + //
        "     break;" + //
        "   d += h;" + //
        " }" + //
        " return d;" + //
        "}"//
    )//
        .gives(
            "int a(A b,int c){int $=0;for(byte[] e=f();$<c;){int g=c-$;int h=i(b,g);if(h==0&&(h=b.j(e,0,((int)C.k(g,l))))==-1)break;$+=h;}return $;}")//
        .gives("int a(A b,int c){int $=0;for(byte[] e=f();$<c;){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;$+=h;}return $;}")
        .gives(
            // Edit this to reflect your expectation
            "int a(A b,int c) {" + //
                "int d = 0;" + //
                " for (byte[] e = f(); d < c;d +=h ) {" + //
                "  int g = c - d;" + //
                "  int h = i(b, g);" + //
                "   if (h == 0 && (h = b.j(e, 0, ((int) C.k(g, l)))) == -1)" + //
                "     break;" + //
                "   d += h;" + //
                " }" + //
                " return d;" + //
                "}"//
        //
        )//
        .stays();
  }
  @Test public void t31() {
    trimmingOf("int a(A b,int c){int $=0;for(byte[] e=f();$<c;){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;$+=h;}return $;}")
        .gives("int a(A b,int c){int $=0;for(byte[] e=f();$<c;$+=h){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;}return $;}");
  }
  @Test public void t32() {
    trimmingOf("int $=0;for(byte[] e=f();$<c;){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;$+=h;}return $;")
        .gives("int a(A b,int c){int $=0;for(byte[] e=f();$<c;$+=h){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;}return $;");
  }
  @Test public void t33() {
    trimmingOf("for(byte[] e=f();$<c;){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;$+=h;}return $;")
        .gives("for(byte[] e=f();$<c;$+=h){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;}return $;");
  }
  @Test public void t34() {
    trimmingOf("for(byte[] e=f();$<c;){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;$+=h;}")
        .gives("for(byte[] e=f();$<c;$+=h){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;}");
  }
  @Test public void t35() {
    trimmingOf("for(byte[] e=f();$<c;){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;++$;}")
        .gives("for(byte[] e=f();$<c;++$){int g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;}");
  }
  @Test public void t36() {
    trimmingOf("for(int e=f();$<c;){g=c-$;h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;++$;}")
        .gives("for(int e=f();$<c;++$){g=c-$;h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;}");
  }
  @Test public void t4() {
    trimmingOf("L a(A b,L c){L $=0;for(byte[] e=f();$<c;){L g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;$+=h;}return $;}") //
        .gives("L a(A b,L c){L $=0;for(byte[] e=f();$<c; $+=h){L g=c-$,h=i(b,g);if(h==0&&(h=b.j(e,0,(int)C.k(g,l)))==-1)break;}return $;}") //
        .stays() //
    ;
  }
}
