/**
 *
 */
package il.org.spartan.java;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
@SuppressWarnings("static-method") public class SignatureAnalyzerTest {
  @Test public void createFile() {
    assert null != SignatureAnalyzer.ofFile("Data/file");
  }

  @Test public void createFileStaticType() {
    azzert.assertThat("", SignatureAnalyzer.ofFile("Data/file").getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createFileType() {
    azzert.assertThat("", SignatureAnalyzer.ofFile("Data/file").getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createReaderStaticType() {
    azzert.assertThat("", SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n")).getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createReaderType() {
    azzert.assertThat("", SignatureAnalyzer.ofReader(new StringReader("Hello, World!\n")).getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createString() {
    assert null != SignatureAnalyzer.ofString("Hello, World!\n");
  }

  @Test public void createStringStaticType() {
    azzert.assertThat("", SignatureAnalyzer.ofString("Hello, World!\n").getClass(), is(SignatureAnalyzer.class));
  }

  @Test public void createStringType() {
    azzert.assertThat("", SignatureAnalyzer.ofString("Hello, World!\n").getClass(), is(SignatureAnalyzer.class));
  }
}
