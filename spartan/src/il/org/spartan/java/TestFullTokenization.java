package il.org.spartan.java;

import static fluent.ly.azzert.*;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;
import org.junit.experimental.theories.*;
import org.junit.runner.*;

import fluent.ly.*;
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
    @NotNull final Set<File> $ = new TreeSet<>();
    new JavaFilesVisitor(".", new PlainFileOnlyAction() {
      @Override public void visitFile(final File ¢) {
        $.add(¢);
      }
    }).go();
    return Iterables.toArray($, File.class);
  }

  public static String read(@NotNull final File f) throws IOException {
    @NotNull final char[] $ = new char[(int) f.length()];
    @NotNull final FileReader fileReader = new FileReader(f);
    final int n = fileReader.read($);
    fileReader.close();
    return String.valueOf(Arrays.copyOf($, n));
  }

  public static void write(@NotNull final File f, @NotNull final String text) throws IOException {
    @NotNull final Writer w = new FileWriter(f);
    w.write(text);
    w.close();
  }

  private final File fin = new File("test/data/UnicodeFile");

  @Test public void brace_brace_newline() {
    azzert.that(TokenAsIs.stringToString("{}\n"), is("{}\n"));
  }

  @Theory public void fullTokenization(@NotNull final File ¢) throws IOException {
    azzert.that(TokenAsIs.fileToString(¢), is(read(¢)));
  }

  @Test public void some_method() {
    @NotNull final String s = Separate.nl(
        //
        "private static int circularSum(final int[] a, final int[] b, final int offset) {", //
        "  int $ = 0;", //
        " for (int i = 0; i <a.length; i++)", //
        "    $ += a[i] * b[(i + offset) % b.length];", //
        "  return $;", //
        "}", //
        " ", //
        " ", //
        "  ");
    azzert.that(TokenAsIs.stringToString(s), is(s));
  }

  @Test public void unicode() {
    azzert.that(TokenAsIs.stringToString("יוסי"), is("יוסי"));
  }

  @Test public void unicodeFileAgainstFileOutput() throws IOException {
    @NotNull final String s = TokenAsIs.fileToString(fin);
    @NotNull final File fout = new File(fin.getPath() + ".out");
    write(fout, s);
    azzert.that(read(fout), is(s));
    azzert.that(read(fout), is(read(fin)));
  }

  @Test public void unicodeFileAgainstString() throws IOException {
    azzert.that(TokenAsIs.fileToString(fin), is(read(fin)));
  }

  @Test public void unicodeFileLenth() throws IOException {
    assert fin.length() > TokenAsIs.fileToString(fin).length();
  }
}
