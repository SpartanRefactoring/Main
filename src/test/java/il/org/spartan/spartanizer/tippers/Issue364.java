package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Unit test for {@link DeclarationInitializerStatementTerminatingScope}
 * Inlining of {@link ArrayInitializer}
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue364 {
  @Test public void emptyInitializer() {
    trimmingOf("Object[] os = {};\n" + "System.out.println(os);").gives("System.out.println((new Object[] {}));");
  }

  @Test public void realLifeExample() {
    trimmingOf("if (opterr) {\n" + "  final Object[] msgArgs = { progname, Character.valueOf((char) c) + \"\" };\n"
        + "  System.err.println(MessageFormat.format(_messages.getString(\"getopt.requires2\"), msgArgs));\n" + "}\n" + "X();")
            .gives("if (opterr) {\n"
                + "  System.err.println(MessageFormat.format(_messages.getString(\"getopt.requires2\"), (new Object[] { progname, Character.valueOf((char) c) + \"\" })));\n"
                + "}\n" + "X();");
  }
}
