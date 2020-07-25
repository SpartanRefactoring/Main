package il.org.spartan.java;

import java.io.StringReader;

import fluent.ly.forget;

/** @author Yossi Gil
 * @since 19 November 2011 */
public class SignatureAnalyzer {
  public static SignatureAnalyzer ofFile(final String fileName) {
    forget.em(new Object[] { fileName });
    return new SignatureAnalyzer();
  }

  public static SignatureAnalyzer ofReader(final StringReader ¢) {
    forget.em(new Object[] { ¢ });
    return new SignatureAnalyzer();
  }

  public static SignatureAnalyzer ofString(final String ¢) {
    forget.em(new Object[] { ¢ });
    return new SignatureAnalyzer();
  }
}
