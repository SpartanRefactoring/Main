package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Unit test for
 * {@link DeclarationInitializerStatementTerminatingScope}Inlining of
 * {@link ArrayInitializer}
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0364 {
  @Test public void emptyInitializer() {
    trimmingOf("Object[] os = {};\n" + "System.out.println(os);").givesEither("System.out.println((new Object[] {}));",
        "System.out.println(new Object[] {});");
  }

  @Test public void realLifeExample() {
    trimmingOf("if (opterr) {\n" + "  final Object[] msgArgs = { progname, Character.valueOf((char) c) + \"\" };\n"
        + "  System.err.println(MessageFormat.format(_messages.getString(\"getopt.requires2\"), msgArgs));\n" + "}\n" + "X();")
            .gives("if (opterr) {\n"
                + "  System.err.println(MessageFormat.format(_messages.getString(\"getopt.requires2\"), (new Object[] { progname, Character.valueOf((char) c) + \"\" })));\n"
                + "}\n" + "X();");
  }
  
  @Ignore
  @Test public void notTerminating() {
    trimmingOf("void f() {\n" + "  String[] x = {\"\"};" + "  g(x);" + "  h();" + "}")
        .gives("void f() {\n" + "  g(new String[] {\"\"});" + "  h();" + "}");
  }

  @Test public void notTerminatingRealWorld() {
    trimmingOf("@Test public void failureCausesExitCodeOf1() throws Exception {\n"
        + "  String[] cmd = {System.getProperty(\"java.home\") + File.separator + \"bin\"\n"
        + "    + File.separator + \"java\", \"-cp\", getClass().getClassLoader().getResource(\".\").getFile()\n"
        + "    + File.pathSeparator + System.getProperty(\"java.class.path\"), getClass().getName() + \"$Exit\"};\n"
        + "  Process process = Runtime.getRuntime().exec(cmd);\n" + "  for (InputStream ¢ = process.getInputStream(); ¢.read() != -1;)\n" + "    ;\n"
        + "  wizard.assertEquals(EXIT_CODE, process.waitFor());\n" + "}")
            .gives("@Test public void failureCausesExitCodeOf1() throws Exception {\n"
                + "  Process process = Runtime.getRuntime().exec(new String[] {System.getProperty(\"java.home\") + File.separator + \"bin\"\n"
                + "    + File.separator + \"java\", \"-cp\", getClass().getClassLoader().getResource(\".\").getFile()\n"
                + "    + File.pathSeparator + System.getProperty(\"java.class.path\"), getClass().getName() + \"$Exit\"});\n"
                + "  for (InputStream ¢ = process.getInputStream(); ¢.read() != -1;)\n" + "    ;\n"
                + "  wizard.assertEquals(EXIT_CODE, process.waitFor());\n" + "}");
  }
}
