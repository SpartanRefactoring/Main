package metatester;

/** @author Oren Afek
 * @since 3/27/2017 */

/**
 * A POJO represents an import line in original test
 */
public class ImportLine extends SourceLine {
  protected ImportLine(final String testClassName, final String content, final int lineNo) {
    super(testClassName, content, lineNo);
  }
}
