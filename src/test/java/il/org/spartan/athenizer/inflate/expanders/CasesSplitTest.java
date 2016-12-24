package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Unit test for {@link CasesSplit}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-25 [[SuppressWarningsSpartan]] */
@SuppressWarnings("static-method")
public class CasesSplitTest {
  @Test public void simpleSequencers() {
    expandingOf("" //
        + "switch (x) {\n" //
        + "case 1:\n" //
        + "  f(1);\n" //
        + "case 2:\n" //
        + "  f(2);\n" //
        + "  break;\n" //
        + "case 3:\n" //
        + "  f(3);\n" //
        + "case 4:\n" //
        + "  throw new Exception();\n" //
        + "default:\n" //
        + "}")
            .gives("" //
                + "switch (x) {\n" //
                + "case 1:\n" //
                + "  f(1);\n" //
                + "  f(2);\n" //
                + "  break;\n" //
                + "case 2:\n" //
                + "  f(2);\n" //
                + "  break;\n" //
                + "case 3:\n" //
                + "  f(3);\n" //
                + "case 4:\n" //
                + "  throw new Exception();\n" //
                + "default:\n" //
                + "}")
            .gives("" //
                + "switch (x) {\n" //
                + "case 1:\n" //
                + "  f(1);\n" //
                + "  f(2);\n" //
                + "  break;\n" //
                + "case 2:\n" //
                + "  f(2);\n" //
                + "  break;\n" //
                + "case 3:\n" //
                + "  f(3);\n" //
                + "  throw new Exception();\n" //
                + "case 4:\n" //
                + "  throw new Exception();\n" //
                + "default:\n" //
                + "}")
            .stays();
  }

  @Test public void complexMerging() {
    expandingOf("" //
        + "switch (x) {\n" //
        + "case 1:\n" //
        + "  f(1);\n" //
        + "case 2:\n" //
        + "  f(2);\n" //
        + "default:\n" //
        + "  f(3);\n" //
        + "  throw new Exception();\n" //
        + "}")
            .gives("" //
                + "switch (x) {\n" //
                + "case 1:\n" //
                + "  f(1);\n" //
                + "  f(2);\n" //
                + "  f(3);\n" //
                + "  throw new Exception();\n" //
                + "case 2:\n" //
                + "  f(2);\n" //
                + "default:\n" //
                + "  f(3);\n" //
                + "  throw new Exception();\n" //
                + "}")
            .gives("" //
                + "switch (x) {\n" //
                + "case 1:\n" //
                + "  f(1);\n" //
                + "  f(2);\n" //
                + "  f(3);\n" //
                + "  throw new Exception();\n" //
                + "case 2:\n" //
                + "  f(2);\n" //
                + "  f(3);\n" //
                + "  throw new Exception();\n" //
                + "default:\n" //
                + "  f(3);\n" //
                + "  throw new Exception();\n" //
                + "}")
            .stays();
  }
}
