/**
 *
 */
package il.org.spartan.classfiles.reify;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 27 November 2011 */
@SuppressWarnings("static-method") public class BytesDataInputStreamTest {
  @Test public void align0() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.align4();
    assertEquals(0, b.position() % 4);
    assertEquals(0, b.position());
    b.close();
  }

  @Test public void align1() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.align4();
    assertEquals(0, b.position() % 4);
    assertEquals(4, b.position());
    b.close();
  }

  @Test public void align2() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.read();
    b.align4();
    assertEquals(0, b.position() % 4);
    assertEquals(4, b.position());
    b.close();
  }

  @Test public void align3() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.read();
    b.read();
    assertEquals(3, b.position());
    b.align4();
    assertEquals(0, b.position() % 4);
    assertEquals(4, b.position());
    b.close();
  }

  @Test public void align4() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.read();
    b.read();
    b.read();
    b.align4();
    assertEquals(0, b.position() % 4);
    assertEquals(4, b.position());
    b.close();
  }

  @Test public void align5() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[1000]);
    b.read();
    b.read();
    b.read();
    b.read();
    b.read();
    b.align4();
    assertEquals(0, b.position() % 4);
    assertEquals(8, b.position());
    b.close();
  }
}
