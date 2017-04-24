package il.org.spartan.spartanizer.research.metatester;

import java.util.*;

/** @author Oren Afek
 * @since 3/27/2017 */
public class SourceLine {
  public static final SourceLine EMPTY = new SourceLine("", "", -1);
  protected String testClassName;
  protected String testName;
  protected String content;
  protected int lineNo;

  protected SourceLine(final String testClassName, final String content, final int lineNo) {
    this.testClassName = testClassName;
    this.content = content;
    this.lineNo = lineNo;
  }

  public boolean contains(final String subString) {
    return content.contains(subString);
  }

  @Override @SuppressWarnings("boxing") public String toString() {
    return String.format("%s.%s(%d): %s", testClassName, testName, lineNo, content);
  }

  public String getTestClassName() {
    return testClassName;
  }

  public void setTestClassName(final String testClassName) {
    this.testClassName = testClassName;
  }

  public String getTestName() {
    return testName;
  }

  public void setTestName(final String testName) {
    this.testName = testName;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public int getLineNo() {
    return lineNo;
  }

  public void setLineNo(final int lineNo) {
    this.lineNo = lineNo;
  }

  public static class SourceLineFactory {
    private final String testClassName;
    private String testMethodName;

    public SourceLineFactory(final String testName) {
      testClassName = testName;
    }

    public SourceLineFactory setTestMethodName(final String testMethodName) {
      this.testMethodName = testMethodName;
      return this;
    }

    public SourceLine createSourceLine(final String content, final int lineNo) {
      return Objects.equals(content, "") ? EMPTY
          : content.contains("import") ? new ImportLine(testClassName, content, lineNo)
              : !content.contains("assert") ? new SourceLine(testClassName, content.trim(), lineNo)
                  : new TestLine(testMethodName, testClassName, content, lineNo);
    }
  }
}
