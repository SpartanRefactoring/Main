package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Unit test for {@link FragmentInitializerStatementTerminatingScope}Inlining
 * of {@link ArrayInitializer}
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0364 {
  @Test public void emptyInitializer() {
    trimmingOf("Object[] os = {};\nS.out.println(os);").givesEither("S.out.println((new Object[] {}));",
        "S.out.println(new Object[] {});");
  }

  @Test public void realLifeExample() {
    trimmingOf("if (opterr) {\n  final Object[] msgArgs = { progname, Character.valueOf((char) c) + \"\" };\n"
        + "  S.err.println(MessageFormat.format(_messages.getString(\"getopt.requires2\"), msgArgs));\n}\nX();")
            .gives("if (opterr) {\n"
                + "  S.err.println(MessageFormat.format(_messages.getString(\"getopt.requires2\"), (new Object[] { progname, Character.valueOf((char) c) + \"\" })));\n"
                + "}\nX();");
  }

  @Test public void notTerminating() {
    trimmingOf("void f() {\n  String[] x = {\"\"};  g(x);  h();}").gives("void f() {\n  g(new String[] {\"\"});  h();}");
  }

  @Test public void notTerminatingRealWorld() {
    trimmingOf("@Test public void fz() throws Exception {\n"
        + "  String[] cmd = {S.p(\"java.home\") + File.separator + \"bin\"\n"
        + "    + File.separator + \"java\", \"-cp\", getClass().getClassLoader().getResource(\".\").getFile()\n"
        + "    + File.pathSeparator + S.p(\"java.class.path\"), getClass().getName() + \"$Exit\"};\n"
        + "  Process o = R.getR().exec(cmd);\n  for (I ¢ = o.getI(); ¢.read() != -1;)\n    ;\n"
        + "  wizard.assertEquals(EXIT_CODE, o.waitFor());\n}")
            .gives("@Test public void fz() throws Exception {\n"
                + "  Process o = R.getR().exec(new String[] {S.p(\"java.home\") + File.separator + \"bin\"\n"
                + "    + File.separator + \"java\", \"-cp\", getClass().getClassLoader().getResource(\".\").getFile()\n"
                + "    + File.pathSeparator + S.p(\"java.class.path\"), getClass().getName() + \"$Exit\"});\n"
                + "  for (I ¢ = o.getI(); ¢.read() != -1;)\n    ;\n"
                + "  wizard.assertEquals(EXIT_CODE, o.waitFor());\n}");
  }
}
