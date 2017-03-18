package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** TODO: Dor Ma'ayan please add a description
 * @author Dor Ma'ayan
 * @since 30-11-2016 */
@SuppressWarnings("static-method")
public class Issue0281 {
  @Test public void test0() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {Statement $ = ¢.getElseStatement();while ($ instanceof IfStatement)"
        + "$ = ((IfStatement) $).getElseStatement();return $;}")//
            .stays();
  }

  @Test public void stAaFiBbAcbdWhcInBcBcdRec() {
    trimmingOf("static A a(final B b) { A c = b.d(); while (c instanceof B) c = ((B) c).d(); return c; }") //
        .using(MethodDeclaration.class, new MethodDeclarationRenameReturnToDollar()) //
        .gives("static A a(final B b){A $=b.d();while($ instanceof B)$=((B)$).d();return $;}") //
        .using(MethodDeclaration.class, new MethodDeclarationRenameSingleParameterToCent()) //
        .gives("static A a(final B ¢){A $=¢.d();while($ instanceof B)$=((B)$).d();return $;}") //
        .using(VariableDeclarationFragment.class, new FragmentInitializerStatementTerminatingScope()) //
        .gives("static A a(final B ¢){while(¢.d()instanceof B)¢.d()=((B)¢.d()).d();return ¢.d();}") //
        .stays() //
    ;
  }

  @Test public void test2() {
    trimmingOf("int a=0;while(a!=5){q=6+9;q--;a+=8;}a=3;")//
        .gives("int a=0;for(;a!=5;a+=8){q=6+9;q--;}a=3;");
  }

  @Test public void test3() {
    trimmingOf("int a=0;while(a!=5){q=6+9;q--;a+=8;}z+=8;a=3;")//
        .gives("int a=0;for(;a!=5;a+=8){q=6+9;q--;}z+=8;a=3;");
  }
}
