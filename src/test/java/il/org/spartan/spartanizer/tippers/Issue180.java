package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit test for: {@link MethodDeclarationRenameReturnToDollar}
 * {@link MethodDeclarationRenameSingleParameterToCent} Checks \@param tag value
 * in javadoc is changed with dollar/cent renaming.
 * @author Ori Roth
 * @since 2016
 * @see SingleVariableDeclarationAbbreviation#fixJavadoc testing for comments */
// TODO Roth: add ???
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings("static-method") //
public class Issue180 {
  @Test public void renameToCent() {
    trimmingOf("/**\n" + " * @param s\n" + " */\n" + "int length(String s) {\n" + "  return s.length();\n" + "}")
        .gives("/**\n" + " * @param ¢\n" + " */\n" + "int length(String ¢) {\n" + "  return ¢.length();\n" + "}");
  }
  @Test public void renameToCentRealWorld() {
    trimmingOf("/** Retrieve all operands, including parenthesized ones, under an expression\n" + " * @param x JD\n"
        + " * @return a {@link List} of all operands to the parameter */\n"
        + "public static List<Expression> allOperands(final InfixExpression x) {\n" + "  assert x != null;\n"
        + "  return hop.operands(flatten.of(x));\n" + "}")
            .gives("/** Retrieve all operands, including parenthesized ones, under an expression\n" + " * @param ¢ JD\n"
                + " * @return a {@link List} of all operands to the parameter */\n"
                + "public static List<Expression> allOperands(final InfixExpression ¢) {\n" + "  assert ¢ != null;\n"
                + "  return hop.operands(flatten.of(¢));\n" + "}");
  }
}
