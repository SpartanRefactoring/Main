package il.org.spartan.spartanizer.research.metatester;
import java.util.Objects;

/**
 * @author Oren Afek
 * @since 3/27/2017
 */
public class SourceLine {

    public static final SourceLine EMPTY = new SourceLine("", "", -1);
    protected String testClassName;
    protected String testName;
    protected String content;
    protected int lineNo;

    protected SourceLine(String testClassName, String content, int lineNo) {
        this.testClassName = testClassName;
        this.content = content;
        this.lineNo = lineNo;
    }

    public boolean contains(String subString) {
        return this.content.contains(subString);
    }

    @Override
    @SuppressWarnings("boxing")
    public String toString() {
        return String.format("%s.%s(%d): %s", testClassName, testName, lineNo, content);
    }

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public static class SourceLineFactory {
        private String testClassName;
        private String testMethodName;

        public SourceLineFactory(String testName) {
            this.testClassName = testName;
        }

        public SourceLineFactory setTestMethodName(String testMethodName) {
            this.testMethodName = testMethodName;
            return this;
        }

        public SourceLine createSourceLine(String content, int lineNo) {
            return Objects.equals(content, "") ? EMPTY
                : content.contains("import") ? new ImportLine(testClassName, content, lineNo)
                    : (!content.contains("assert") ? new SourceLine(testClassName, content.trim(), lineNo)
                        : new TestLine(testMethodName, testClassName, content, lineNo));
        }

    }


}
