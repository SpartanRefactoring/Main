package il.org.spartan.classfiles.reify;

import static fluent.ly.azzert.*;

import java.io.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 27 November 2011 */
@SuppressWarnings("static-method") public class BytesDataInputStreamTest {
  @Test public void align0() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.align4();
    azzert.that(b.position() % 4, is(0));
    azzert.that(b.position(), is(0));
    b.close();
  }

  @Test public void align1() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.align4();
    azzert.that(b.position() % 4, is(0));
    azzert.that(b.position(), is(4));
    b.close();
  }

  @Test public void align2() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.read();
    b.align4();
    azzert.that(b.position() % 4, is(0));
    azzert.that(b.position(), is(4));
    b.close();
  }

  @Test public void align3() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.read();
    b.read();
    azzert.that(b.position(), is(3));
    b.align4();
    azzert.that(b.position() % 4, is(0));
    azzert.that(b.position(), is(4));
    b.close();
  }

  @Test public void align4() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.read();
    b.read();
    b.read();
    b.align4();
    azzert.that(b.position() % 4, is(0));
    azzert.that(b.position(), is(4));
    b.close();
  }

  @Test public void align5() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.read();
    b.read();
    b.read();
    b.read();
    b.align4();
    azzert.that(b.position() % 4, is(0));
    azzert.that(b.position(), is(8));
    b.close();
  }
}
