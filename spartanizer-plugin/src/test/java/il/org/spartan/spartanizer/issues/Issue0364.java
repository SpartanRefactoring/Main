package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link LocalInitializedStatementTerminatingScope}Inlining of
 * {@link ArrayInitializer}
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0364 {
  @Test public void emptyInitializer() {
    trimmingOf("Object[] os = {};S.out.println(os);").givesEither("S.out.println((new Object[] {}));", "S.out.println(new Object[] {});");
  }
  @Test public void notTerminating() {
    trimmingOf("void f() { String[] x = {\"\"}; g(x); h();}")//
        .gives("void f() { g(new String[] {\"\"}); h();}");
  }
  @Test public void notTerminatingRealWorld() {
    trimmingOf("@Test public void fz() throws E { String[] cmd = {S.p(\"java.home\") + F.s + \"bin\""
        + " + F.s + \"java\", \"-cp\", gc().gl().getResource(\".\").getFile() + F.p + S.p(\"java.class.path\"), gc().n() + \"$Exit\"};"
        + " Process o = R.getR().e(cmd); for (I ¢ = o.getI(); ¢.r() != -1;) ; w.a(EXIT_CODE, o.waitFor());}")
            .gives("@Test public void fz() throws E { Process o = R.getR().e(new String[] {S.p(\"java.home\") + F.s + \"bin\""
                + " + F.s + \"java\", \"-cp\", gc().gl().getResource(\".\").getFile() + F.p + S.p(\"java.class.path\"), gc().n() + \"$Exit\"});"
                + " for (I ¢ = o.getI(); ¢.r() != -1;) ; w.a(EXIT_CODE, o.waitFor());}");
  }
  @Test public void realLifeExample() {
    trimmingOf("if (opterr) { final Object[] msgArgs = { progname, Character.valueOf((char) c) + \"\" };"
        + " S.err.println(MessageFormat.format(_messages.getString(\"getopt.requires2\"), msgArgs));}X();")
            .gives("if (opterr) {"
                + " S.err.println(MessageFormat.format(_messages.getString(\"getopt.requires2\"), (new Object[] { progname, Character.valueOf((char) c) + \"\" })));"
                + "}X();");
  }
}
