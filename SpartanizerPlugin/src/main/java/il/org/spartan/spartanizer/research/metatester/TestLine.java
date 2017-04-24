package il.org.spartan.spartanizer.research.metatester;

/** @author Oren Afek
 * @since 3/27/2017 */
public class TestLine extends SourceLine {
  protected TestLine(final String testName, final String testClassName, final String content, final int lineNo) {
    super(testClassName, content, lineNo);
    this.testName = testName.replace("@Test", "");
  }

  @SuppressWarnings("boxing") public String generateTestMethod() {
    return String.format("\tpublic @Test void %s_%d() {\n\t%s\n\t}", getTestName(), getLineNo(), getContent());
  }
}
