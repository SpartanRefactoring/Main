package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.LocalInitializedInlineIntoNext;

/** A test case for {@link LocalInitializedInlineIntoNext}. See issue #954.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-01-20 */
@SuppressWarnings("static-method")
public class Issue0954 {
  @Test public void a() {
    trimmingOf("/**/" //
        + "void f() {\n" //
        + "Object[] objectArray = {value};\n" //
        + "String arrayString = Arrays.deepToString(objectArray);\n" //
        + "anotherStatement();\n" //
        + "}")
            .gives("/**/" //
                + "void f() {\n" //
                + "String arrayString = Arrays.deepToString(new Object[] {value});\n" //
                + "anotherStatement();\n" //
                + "}");
  }
}
