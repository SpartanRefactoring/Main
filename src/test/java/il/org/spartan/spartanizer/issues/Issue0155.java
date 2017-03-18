package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for{@link FragmentInitializerStatementTerminatingScope}
 * @author Ori Roth
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0155 {
  @Test public void inlineFinal() {
    trimmingOf("for(int i=0;i<versionNumbers.length;++i){final String nb=versionNumbers[i];$[i]=Integer.parseInt(nb);}")
        .gives("for(int i=0;i<versionNumbers.length;++i){$[i]=Integer.parseInt(versionNumbers[i]);}");
  }

  @Test public void inlineNonFinalIntoClassInstanceCreation() {
    trimmingOf("void h(int x){++x;final int y=x;new Object(){@Override public int hashCode(){return y;}};}")//
        .stays();
  }

  @Test public void issue64a() {
    trimmingOf("void f(){final int a=f();new Object(){@Override public int hashCode(){return a;}};}").stays();
  }

  @Test public void issue64b1() {
    trimmingOf("void f(){new Object(){@Override public int hashCode(){return 3;}};}")//
        .stays();
  }

  @Test public void issue64b2() {
    trimmingOf("void f(){final int a=3;new Object(){@Override public int hashCode(){return a;}};}").stays();
  }

  @Test public void issue64c() {
    trimmingOf("void f(int x){++x;final int a=x;new Object(){@Override public int hashCode(){return a;}};}")//
        .stays();
  }
}
