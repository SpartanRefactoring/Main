package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see Issue0 #717 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-05 */
@SuppressWarnings("static-method") //
public class Issue0717 {
  private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
  private static final int MAX_NAME_SIZE = 100;
  private static final int MAX_STAT_AMOUNT = 100;
  @Nullable final MethodDeclaration fiveStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; int e;}");
  @Nullable final MethodDeclaration oneStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; }");
  @Nullable final MethodDeclaration fourStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; ; ; ; }");

  @Test public void bigBlockWithAnnotationReturnsTrue() {
    assert determineIf.hasBigBlock((MethodDeclaration) wizard.ast("@Override public int f(){;;;;;}"));
  }

  @Test public void fiveStatBlockReturnsTrue() {
    assert determineIf.hasBigBlock(fiveStatMethod);
  }

  @Test public void fourStatBlockReturnsFalse() {
    assert !determineIf.hasBigBlock(fourStatMethod);
  }

  @NotNull private String generateRandomString(final int maxLen) {
    final StringBuilder $ = new StringBuilder();
    int len;
    final Random randomGenerator = new Random();
    len = randomGenerator.nextInt(maxLen);
    if (len <= 0)
      len = 1;
    $.append(CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length() - 10)));
    range.from(1).to(len).forEach(λ -> $.append(CHAR_LIST.charAt(randomGenerator.nextInt(CHAR_LIST.length()))));
    return $ + "";
  }

  @Test public void isCompiled() {
    assert true;
  }

  @Test public void methodWithNoBodyReturnsFalse() {
    assert !determineIf.hasBigBlock((MethodDeclaration) wizard.ast("public int a(String a);"));
  }

  @Test public void methodWithNoStatementsReturnsFalse() {
    assert !determineIf.hasBigBlock((MethodDeclaration) wizard.ast("public int f(int x){}"));
  }

  @Test public void nullCheckReturnsFalse() {
    assert !determineIf.hasBigBlock(null);
  }

  @Test public void oneStatBlockReturnsFalse() {
    assert !determineIf.hasBigBlock(oneStatMethod);
  }

  @Test public void randomBigBlockReturnsTrue() {
    final String methodName = generateRandomString(MAX_NAME_SIZE), firstStat = "{int x; ++x;", nextStat = "x=4;";
    final Random random = new Random();
    final int statAmount = random.nextInt(MAX_STAT_AMOUNT) < 6 ? 6 : random.nextInt(MAX_STAT_AMOUNT);
    String randomBigBlock = "public void " + methodName + "()" + firstStat;
    for (int ¢ = 0; ¢ < statAmount; ++¢)
      randomBigBlock += nextStat;
    randomBigBlock += "}";
    assert determineIf.hasBigBlock((MethodDeclaration) wizard.ast(randomBigBlock));
  }

  @Test public void smallBlockWithAnnotationReturnsFalse() {
    assert !determineIf.hasBigBlock((MethodDeclaration) wizard.ast("@Inherited private void g(){;;;;}"));
  }

  @Test public void smallBlockWithModifierReturnsFalse() {
    assert !determineIf.hasBigBlock((MethodDeclaration) wizard.ast("public static void g(){;;;;}"));
  }

  @Test public void smallBlockWithModifierReturnsTrue() {
    assert determineIf.hasBigBlock((MethodDeclaration) wizard.ast("private static void g(){;;;;;}"));
  }
}
