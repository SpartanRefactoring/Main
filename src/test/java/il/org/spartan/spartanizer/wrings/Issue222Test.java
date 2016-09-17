package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** Unit tests for centification of a single parameter to a function even if it
 * defines a "$" variable
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue222Test {
  @Test public void chocolate1() {
    trimming(//
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
    trimming(
        "private boolean continue¢(final List<VariableDeclarationFragment> fs) {\n" + //
          "for (final VariableDeclarationFragment f : fs)\n" + //
            "if (!continue¢(f.getName()))\n" + //
              "return false;\n" + //
          "return true;\n" + //
        "}").stays();
  }

  @Test public void vanilla() {
    trimming(//
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
                .to("static List<Expression> operands(final InfixExpression ¢) {\n" //
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
