package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** A test case for {@link LocalVariableIntializedInlineIntoNext}. See issue #954.
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
