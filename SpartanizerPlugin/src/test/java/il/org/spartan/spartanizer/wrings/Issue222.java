package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for centification of a single parameter to a function even if it
 * defines a "$" variable
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue222 {
  @Test public void chocolate1() {
    trimmingOf(//
        "static List<Expression> operands(final InfixExpression x) {\n" //
            + "  if (x == null)\n" //
            + "    return null;\n" //
            + "  int y = x;\n" //
            + "  final List<Expression> $ = new ArrayList<>();\n" //
            + "  $.add(left(x));\n" //
            + "  $.add(right(x));\n" //
            + "  if (x.hasExtendedOperands())\n" //
            + "    $.addAll(step.extendedOperands(x));\n" //
            + "  return $;\n" //
            + "}\n") //
                .stays();
  }

  @Test public void chocolate2() {
    trimmingOf("private boolean continue¢(final List<VariableDeclarationFragment> fs) {\n" + //
        "for (final VariableDeclarationFragment f : fs){\n" + //
        "int b = f(f);\n" + //
        "f(f,b);\n" + //
        "return g(f,b,f());\n" + //
        "}\n" + //
        "return true;\n" + //
        "}").stays();
  }

  @Test public void chocolate3() {
    trimmingOf("int f(int a) {\n" + //
        "for (int b: fs)\n" + //
        "return 0;\n" + //
        "return 1;\n" + //
        "}").stays();
  }

  @Test public void vanilla() {
    trimmingOf(//
        "static List<Expression> operands(final InfixExpression x) {\n" //
            + "  if (x == null)\n" //
            + "    return null;\n" //
            + "  final List<Expression> $ = new ArrayList<>();\n" //
            + "  $.add(left(x));\n" //
            + "  $.add(right(x));\n" //
            + "  if (x.hasExtendedOperands())\n" //
            + "    $.addAll(step.extendedOperands(x));\n" //
            + "  return $;\n" //
            + "}\n") //
                .gives("static List<Expression> operands(final InfixExpression ¢) {\n" //
                    + "  if (¢ == null)\n" //
                    + "    return null;\n" //
                    + "  final List<Expression> $ = new ArrayList<>();\n" //
                    + "  $.add(left(¢));\n" //
                    + "  $.add(right(¢));\n" //
                    + "  if (¢.hasExtendedOperands())\n" //
                    + "    $.addAll(step.extendedOperands(¢));\n" //
                    + "  return $;\n" //
                    + "}\n") //
                .stays();
  }
}
