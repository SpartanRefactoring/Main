package il.org.spartan.java;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.experimental.theories.*;
import org.junit.runner.*;

import il.org.spartan.files.visitors.*;
import il.org.spartan.files.visitors.FileSystemVisitor.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;

/** @author Yossi Gil
 * @since 16/05/2011 */
@RunWith(Theories.class) @SuppressWarnings("static-method")
//
public class TestFullTokenization {
  @DataPoints public static File[] javaFiles() throws IOException {
    final Set<File> $ = new TreeSet<>();
    new JavaFilesVisitor(".", new PlainFileOnlyAction() {
      @Override public void visitFile(final File f) {
        $.add(f);
      }
    }).go();
    return Iterables.toArray($, File.class);
  }

  public static String read(final File f) throws IOException {
    final char[] $ = new char[(int) f.length()];
    final FileReader fileReader = new FileReader(f);
    final int n = fileReader.read($);
    fileReader.close();
    return new String(Arrays.copyOf($, n));
  }

  public static void write(final File f, final String text) throws IOException {
    final Writer w = new FileWriter(f);
    w.write(text);
    w.close();
  }

  private final File fin = new File("test/data/UnicodeFile");

  @Test public void brace_brace_newline() throws IOException {
    final String s = "{}\n";
    assertEquals(s, TokenAsIs.stringToString(s));
  }

  @Theory public void fullTokenization(final File f) throws IOException {
    assertEquals(read(f), TokenAsIs.fileToString(f));
  }

  @Test public void some_method() throws IOException {
    final String s = Separate.nl(
        //
        "private static int circularSum(final int[] a, final int[] b, final int offset) {", //
        "  int $ = 0;", //
        " for (int i = 0; i < a.length; i++)", //
        "    $ += a[i] * b[(i + offset) % b.length];", //
        "  return $;", //
        "}", //
        " ", //
        " ", //
        "  ");
    assertEquals(s, TokenAsIs.stringToString(s));
  }

  @Test public void unicode() throws IOException {
    final String s = "יוסי";
    assertEquals(s, TokenAsIs.stringToString(s));
  }

  @Test public void unicodeFileAgainstFileOutput() throws IOException {
    final String s = TokenAsIs.fileToString(fin);
    final File fout = new File(fin.getPath() + ".out");
    write(fout, s);
    assertEquals(s, read(fout));
    assertEquals(read(fin), read(fout));
  }

  @Test public void unicodeFileAgainstString() throws IOException {
    assertEquals(read(fin), TokenAsIs.fileToString(fin));
  }

  @Test public void unicodeFileLenth() throws IOException {
    assertTrue(fin.length() > TokenAsIs.fileToString(fin).length());
  }
}
