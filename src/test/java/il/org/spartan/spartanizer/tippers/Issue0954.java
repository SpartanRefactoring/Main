package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;

/** A test case for {@link FragmentInlineIntoNext}. See issue #954.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-01-20 [[SuppressWarningsSpartan]] */
@SuppressWarnings("static-method")
public class Issue0954 {
  @Ignore // TODO Yossi Gil
  @Test public void a() {
    trimmingOf("" //
        + "void f() {\n" //
        + "Object[] objectArray = {value};\n" //
        + "String arrayString = Arrays.deepToString(objectArray);\n" //
        + "anotherStatement();\n" //
        + "}")
            .gives("" //
                + "void f() {\n" //
                + "Arrays.deepToString(new Object[] {value});\n" //
                + "anotherStatement();\n" //
                + "}");
  }
}
