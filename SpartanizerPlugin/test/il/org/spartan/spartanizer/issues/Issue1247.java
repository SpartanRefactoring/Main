package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link StringFromStringBuilder}
 * @author Ori Roth
 * @since 2017-04-21 */
@SuppressWarnings("static-method")
public class Issue1247 {
  @Test public void stringFromBuilderAddParenthesis() {
    trimmingOf("new StringBuilder(f()).append(1+1).toString()") //
        .using(MethodInvocation.class, new MethodInvocationToStringToEmptyStringAddition()) //
        .gives("\"\"+new StringBuilder(f()).append(1+1)") //
        .using(InfixExpression.class, new InfixConcatenationEmptyStringLeft()) //
        .gives("new StringBuilder(f()).append(1+1)+\"\"") //
        .using(new StringFromStringBuilder()) //
        .gives("\"\" + f() + (1+1) + \"\"") //
        .gives("\"\" + f() + (1+1)");
  }

  @Test public void stringFromBuilderGeneral() {
    trimmingOf("new StringBuilder(myName).append(\"\'s grade is\").append(100).toString()") //
        .using(new StringFromStringBuilder()) //
        .gives("\"\" + myName+\"\'s grade is\"+100") //
        .gives("myName + \"\" + \"\'s grade is\" + 100") //
        .gives("myName + \"\'s grade is\" + 100") //
        .stays();
  }

  @Test public void stringFromBuilderNoStringComponents() {
    trimmingOf("new StringBuilder(0).append(1).toString()") //
        .using(new StringFromStringBuilder()) //
        .gives("\"\"+0+1");
  }

  @Test public void stringFromBuilderSimple() {
    trimmingOf("new StringBuilder(1).toString()") //
        .using(new StringFromStringBuilder()) //
        .gives("\"\"+1");
  }

  @Test public void stringFromBuilderSimple2() {
    trimmingOf("new StringBuilder(1) + \"\"") //
        .using(new StringFromStringBuilder()) //
        .gives("\"\" + 1 + \"\"");
  }

  @Test public void stringFromBuilderSimplest() {
    trimmingOf("new StringBuilder().toString()") //
        .using(new StringFromStringBuilder()) //
        .gives("\"\"");
  }

  @Test public void stringFromBuilderSimplest2() {
    trimmingOf("new StringBuilder() + \"\"") //
        .using(new StringFromStringBuilder()) //
        .gives("\"\" + \"\"") //
        .gives("\"\"");
  }
}
