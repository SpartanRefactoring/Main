package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Failing test of bug in {@link ForRedundantContinue}
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-08
 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1047 {
  //TODO doron: unignore this test in {@link Issue0147} and in {@link AdvancedGivesTests}
  @Test public void a() {
    trimmingOf("for(int ¢=0; ¢<5;++¢){++¢; continue;}")//
        .gives("for(int ¢=0; ¢<5;++¢)++¢;");//
  }
  @Test public void test2() {
    trimmingOf("for (int ¢ = 0 ; ¢ < 5 ; ++¢) {++¢; continue;}")//
        .givesEither("for (int ¢ = 0 ; ¢ < 5 ; ++¢) {++¢;}", //
            "for (int ¢ = 0 ; ¢ < 5 ; ++¢) ++¢;");
  }
  @Test public void t3() {
    trimmingOf("for (int i = 0; i < length; ++i) {"
        + "char c1 = s1.charAt(i);"
        + "char c2 = s2.charAt(i);"
        + "if (c1 == c2)"
        + "  continue;"
        + "int alphaIndex = getAlphaIndex(c1);"
        + "if (alphaIndex >= 26 || alphaIndex != getAlphaIndex(c2))"
        + "  return false;"
        + "continue;"
        + "}")
    .gives("for (int i = 0; i < length; ++i) {"
        + "char c1 = s1.charAt(i);"
        + "char c2 = s2.charAt(i);"
        + "if (c1 == c2)"
        + "  continue;"
        + "int alphaIndex = getAlphaIndex(c1);"
        + "if (alphaIndex >= 26 || alphaIndex != getAlphaIndex(c2))"
        + "  return false;"
        + "}");
  }
}
