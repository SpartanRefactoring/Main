package il.org.spartan.java;

import java.io.*;

import org.jetbrains.annotations.*;

import fluent.ly.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
public class SignatureAnalyzer {
  @NotNull public static SignatureAnalyzer ofFile(final String fileName) {
    Object[] ____ = { fileName };
    forget.em(____);
    return new SignatureAnalyzer();
  }

  @NotNull public static SignatureAnalyzer ofReader(final StringReader ¢) {
    Object[] ____ = { ¢ };
    forget.em(____);
    return new SignatureAnalyzer();
  }

  @NotNull public static SignatureAnalyzer ofString(final String ¢) {
    Object[] ____ = { ¢ };
    forget.em(____);
    return new SignatureAnalyzer();
  }
}
