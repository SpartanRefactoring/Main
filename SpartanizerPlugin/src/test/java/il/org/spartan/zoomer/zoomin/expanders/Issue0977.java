package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit test for {@link CasesSplit}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0977 {
  @Test public void simpleSequencers() {
    bloatingOf("switch (x) {\n" + "case 1:\n" + "  f(1);\n" + "case 2:\n" + "  f(2);\n" + "  break;\n" + "case 3:\n" + "  f(3);\n" + "case 4:\n"
        + "  throw new Exception();\n" + "default:\n" + "}")
            .gives("switch (x) {\n" + "case 1:\n" + "  f(1);\n" + "  f(2);\n" + "  break;\n" + "case 2:\n" + "  f(2);\n" + "  break;\n" + "case 3:\n"
                + "  f(3);\n" + "case 4:\n" + "  throw new Exception();\n" + "default:\n" + "}")
            .gives("switch (x) {\n" + "case 1:\n" + "  f(1);\n" + "  f(2);\n" + "  break;\n" + "case 2:\n" + "  f(2);\n" + "  break;\n" + "case 3:\n"
                + "  f(3);\n" + "  throw new Exception();\n" + "case 4:\n" + "  throw new Exception();\n" + "default:\n" + "}")
            .stays();
  }

  @Test public void complexMerging() {
    bloatingOf(
        "switch (x) {\n" + "case 1:\n" + "  f(1);\n" + "case 2:\n" + "  f(2);\n" + "default:\n" + "  f(3);\n" + "  throw new Exception();\n" + "}")
            .gives("switch (x) {\n" + "case 1:\n" + "  f(1);\n" + "  f(2);\n" + "  f(3);\n" + "  throw new Exception();\n" + "case 2:\n" + "  f(2);\n"
                + "default:\n" + "  f(3);\n" + "  throw new Exception();\n" + "}")
            .gives("switch (x) {\n" + "case 1:\n" + "  f(1);\n" + "  f(2);\n" + "  f(3);\n" + "  throw new Exception();\n" + "case 2:\n" + "  f(2);\n"
                + "  f(3);\n" + "  throw new Exception();\n" + "default:\n" + "  f(3);\n" + "  throw new Exception();\n" + "}")
            .stays();
  }

  // see issue #1046
  @Test public void complexSequencer() {
    bloatingOf("switch (a()) {\n" + "case 1:\n" + "  if (b()) {\n" + "    return c();\n" + "  } else {\n" + "    throw d();\n" + "  }\n" + "case 2:\n"
        + "  e();\n" + "}")//
            .stays();
  }

  // see issue #1046
  @Test public void complexSequencerNotLast() {
    bloatingOf("switch (a()) {\n" + "case 1:\n" + "  if (b()) {\n" + "    return c();\n" + "  } else {\n" + "    throw d();\n" + "  }\n" + "  f();\n"
        + "case 2:\n" + "  e();\n" + "}")//
            .stays();
  }

  // see issue #1031
  @Test public void complexSequencerRealWorld() {
    bloatingOf("switch (a()) {\n" + "case 1:\n" + "if (!parameters((MethodDeclaration) $).contains(¢)) {\n" + "  return Kind.method;\n" + "} else {\n"
        + "  return Kind.parameter;\n" + "}\n" + "case 2:\n" + "  e();\n" + "}")//
            .stays();
  }
}
