package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Alex Kopzon
 * @since 2016  */
@SuppressWarnings("static-method") public class Issue390 {
  @Ignore @Test public void emptyInitializer() {
    trimmingOf("for (final Expression ¢ : operands)"
        + "if (iz.parenthesizedExpression(¢) && iz.assignment(az.parenthesizedExpression(¢).getExpression())) {"
        + "final Assignment a = az.assignment(az.parenthesizedExpression(¢).getExpression());" + "final SimpleName var = az.simpleName(step.left(a));"
        + "for (final VariableDeclarationFragment f : fragments(s))" + "if ((f.getName() + \"\").equals(var + \"\")) {"
        + "f.setInitializer(duplicate.of(step.right(a)));" + "operands.set(operands.indexOf(¢), ¢.getAST().newSimpleName(var + \"\"));}}")
            .gives("for (final Expression ¢ : operands)"
                + "if (iz.parenthesizedExpression(¢) && iz.assignment(az.parenthesizedExpression(¢).getExpression())) {"
                + "final Assignment a = az.assignment(az.parenthesizedExpression(¢).getExpression());"
                + "final SimpleName var = az.simpleName(step.left(a));" + "for (final VariableDeclarationFragment f : fragments(s))"
                + "if ((f.getName() + \"\").equals(var + \"\")) {" + "f.setInitializer(duplicate.of(step.right(a)));"
                + "operands.set(operands.indexOf(¢), ¢.getAST().newSimpleName(var + \"\"));}}")
            .stays();
  }
  
  @Test public void a() {
    trimmingOf("@Override public String getOpeningMessage(final Map<attribute, Object> m) {int cs = getCUsCount(m);return getTipperName(m) + cs + plurals(\"file\", cs); }")
            .gives("@Override public String getOpeningMessage(final Map<attribute, Object> ¢) {int cs = getCUsCount(¢);return getTipperName(¢) + cs + plurals(\"file\", cs); }")
            .stays();
  }
}
