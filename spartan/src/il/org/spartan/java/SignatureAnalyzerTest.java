/**
 *
 */
package il.org.spartan.java;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
@SuppressWarnings("static-method") public class SignatureAnalyzerTest {
  @Test public void createFile() {
    assert null != SignatureAnalyzer.ofFile("Data/file");
  }

  @Test public void createFileStaticType() {
    final SignatureAnalyzer __ = SignatureAnalyzer.ofFile("Data/file");
    assertEquals(SignatureAnalyzer.class, __.getClass());
  }

  @Test public void createFileType() {
    assertEquals(SignatureAnalyzer.class, SignatureAnalyzer.ofFile("Data/file").getClass());
  }

  @Test public void createReaderStaticType() {
    final SignatureAnalyzer __ = SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n"));
    assertEquals(SignatureAnalyzer.class, __.getClass());
  }

  @Test public void createReaderType() {
    assertEquals(SignatureAnalyzer.class, SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n")).getClass());
  }

  @Test public void createString() {
    assert null != SignatureAnalyzer.ofString("Hello, World!\n");
  }

  @Test public void createStringStaticType() {
    final SignatureAnalyzer __ = SignatureAnalyzer.ofString("Hello, World!\n");
    assertEquals(SignatureAnalyzer.class, __.getClass());
  }

  @Test public void createStringType() {
    assertEquals(SignatureAnalyzer.class, SignatureAnalyzer.ofString("Hello, World!\n").getClass());
  }
}
