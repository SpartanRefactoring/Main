/**
 *
 */
package il.org.spartan.classfiles.reify;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 27 November 2011 */
@SuppressWarnings("static-method") public class OpCodeTest {
  @Test public void tableSwitch() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[] { //
        (byte) OpCode.TABLESWITCH.ordinal(), 0, 0, 0, //
        0, 0, 0, 0, //
        0, 0, 0, 55, //
        0, 0, 0, 56, //
        0, 0, 0, 1, //
        0, 0, 0, 1, //
    });
    OpCode.read(b);
    assert b.eof();
    b.close();
  }

  @Test public void tableSwitch1() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[] {
        //
        0, (byte) OpCode.TABLESWITCH.ordinal(), 0, 0, //
        0, 0, 0, 0, //
        0, 0, 0, 55, //
        0, 0, 0, 56, //
        0, 0, 0, 1, //
        0, 0, 0, 1, //
    });
    assertEquals(OpCode.NOP, OpCode.read(b).opCode);
    assertEquals(OpCode.TABLESWITCH, OpCode.read(b).opCode);
    OpCode.read(b);
    assert b.eof();
    b.close();
  }

  @Test public void tableSwitch2() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[] {
        //
        0, 0, (byte) OpCode.TABLESWITCH.ordinal(), 0, //
        0, 0, 0, 0, //
        0, 0, 0, 55, //
        0, 0, 0, 56, //
        0, 0, 0, 1, //
        0, 0, 0, 1, //
    });
    assertEquals(OpCode.NOP, OpCode.read(b).opCode);
    assertEquals(OpCode.NOP, OpCode.read(b).opCode);
    assertEquals(OpCode.TABLESWITCH, OpCode.read(b).opCode);
    assert b.eof();
    b.close();
  }

  @Test public void tableSwitch3() throws IOException {
    final BufferDataInputStream b = new BufferDataInputStream(new byte[] {
        //
        0, 0, 0, (byte) OpCode.TABLESWITCH.ordinal(), //
        0, 0, 0, 0, //
        0, 0, 0, 55, //
        0, 0, 0, 56, //
        0, 0, 0, 1, //
        0, 0, 0, 1, //
    });
    assertEquals(OpCode.NOP, OpCode.read(b).opCode);
    assertEquals(OpCode.NOP, OpCode.read(b).opCode);
    assertEquals(OpCode.NOP, OpCode.read(b).opCode);
    assertEquals(OpCode.TABLESWITCH, OpCode.read(b).opCode);
    assert b.eof();
    b.close();
  }
}
