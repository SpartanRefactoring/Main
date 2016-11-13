package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see Issue #717 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-05 */
@SuppressWarnings("static-method") //
@Ignore // TODO: Nikita and Alex: your tests fail now after unused variables were removed.
public class Issue717 {
  MethodDeclaration fiveStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; int e;}");
  MethodDeclaration oneStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; }");
  MethodDeclaration fourStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; ; ; ; }");
  private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

  @Test public void isCompiled() {
    assert true;
  }
  @Test public void nullCheckReturnsFalse() {
    assertFalse(determineIf.hasBigBlock(null));
  }
  @Test public void fiveStatBlockReturnsTrue() {
    assertTrue(determineIf.hasBigBlock(fiveStatMethod));
  }
  @Test public void oneStatBlockReturnsFalse() {
    assertFalse(determineIf.hasBigBlock(oneStatMethod));
  }
  @Test public void fourStatBlockReturnsFalse() {
    assertFalse(determineIf.hasBigBlock(fourStatMethod));
  }
  private String generateRandomString(final int maxLen) {
    final StringBuffer randStr = new StringBuffer();
    int len = 0;
    final Random randomGenerator = new Random();
    len = randomGenerator.nextInt(maxLen);
    if (len <= 0)
      len = 1;
    for (int ¢ = 0; ¢ < len; ++¢)
      randStr.append(CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length())));
    return randStr + "";
  }

  private final int MAX_NAME_SIZE = 100;
  private final int MAX_STAT_AMOUNT = 100;

  @Test public void randomBigBlockReturnsTrue() {
    final String methodName = generateRandomString(MAX_NAME_SIZE);
    final String firstStat = "{int x;";
    final String nextStat = "x=4;";
    int statAmount = 0;
    final Random randomGenerator = new Random();
    statAmount = randomGenerator.nextInt(MAX_STAT_AMOUNT);
    if (statAmount < 4)
      statAmount = 4;
    String randomBigBlock = "public void " + methodName + "()" + firstStat;
    for (int ¢ = 0; ¢ < statAmount; ++¢)
      randomBigBlock += nextStat;
    randomBigBlock += "}";
    assertTrue(determineIf.hasBigBlock((MethodDeclaration) wizard.ast(randomBigBlock)));
  }
  @Test public void methodWithNoBodyReturnsFalse() {
    assertFalse(determineIf.hasBigBlock((MethodDeclaration) wizard.ast("public int a(String a);")));
  }
  @Test public void methodWithNoStatementsReturnsFalse() {
    assertFalse(determineIf.hasBigBlock((MethodDeclaration) wizard.ast("public int f(int x){}")));
  }
  @Test public void bigBlockWithAnnotationReturnsTrue() {
    assertTrue(determineIf.hasBigBlock((MethodDeclaration) wizard.ast("@Override public int f(){;;;;;}")));
  }
  @Test public void smallBlockWithAnnotationReturnsFalse() {
    assertFalse(determineIf.hasBigBlock((MethodDeclaration) wizard.ast("@Inherited private void g(){;;;;}")));
  }
  @Test public void smallBlockWithModifierReturnsFalse() {
    assertFalse(determineIf.hasBigBlock((MethodDeclaration) wizard.ast("public static void g(){;;;;}")));
  }
  @Test public void smallBlockWithModifierReturnsTrue() {
    assertTrue(determineIf.hasBigBlock((MethodDeclaration) wizard.ast("private static void g(){;;;;;}")));
  }
}