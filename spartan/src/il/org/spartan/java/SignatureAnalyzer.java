/**
 *
 */
package il.org.spartan.java;

import java.io.*;

import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
public class SignatureAnalyzer {
  public static SignatureAnalyzer ofFile(final String fileName) {
    ___.______unused(fileName);
    return new SignatureAnalyzer();
  }

  public static SignatureAnalyzer ofReader(final StringReader stringReader) {
    ___.______unused(stringReader);
    return new SignatureAnalyzer();
  }

  public static SignatureAnalyzer ofString(final String s) {
    ___.______unused(s);
    return new SignatureAnalyzer();
  }
}
