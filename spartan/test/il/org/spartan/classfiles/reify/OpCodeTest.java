package il.org.spartan.classfiles.reify;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 27 November 2011 */
@SuppressWarnings("static-method") public class OpCodeTest {
  @Test public void tableSwitch() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[] { //
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
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[] {
        //
        0, (byte) OpCode.TABLESWITCH.ordinal(), 0, 0, //
        0, 0, 0, 0, //
        0, 0, 0, 55, //
        0, 0, 0, 56, //
        0, 0, 0, 1, //
        0, 0, 0, 1, //
    });
    azzert.that(OpCode.read(b).opCode, is(OpCode.NOP));
    azzert.that(OpCode.read(b).opCode, is(OpCode.TABLESWITCH));
    OpCode.read(b);
    assert b.eof();
    b.close();
  }

  @Test public void tableSwitch2() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[] {
        //
        0, 0, (byte) OpCode.TABLESWITCH.ordinal(), 0, //
        0, 0, 0, 0, //
        0, 0, 0, 55, //
        0, 0, 0, 56, //
        0, 0, 0, 1, //
        0, 0, 0, 1, //
    });
    azzert.that(OpCode.read(b).opCode, is(OpCode.NOP));
    azzert.that(OpCode.read(b).opCode, is(OpCode.NOP));
    azzert.that(OpCode.read(b).opCode, is(OpCode.TABLESWITCH));
    assert b.eof();
    b.close();
  }

  @Test public void tableSwitch3() throws IOException {
    @NotNull final BufferDataInputStream b = new BufferDataInputStream(new byte[] {
        //
        0, 0, 0, (byte) OpCode.TABLESWITCH.ordinal(), //
        0, 0, 0, 0, //
        0, 0, 0, 55, //
        0, 0, 0, 56, //
        0, 0, 0, 1, //
        0, 0, 0, 1, //
    });
    azzert.that(OpCode.read(b).opCode, is(OpCode.NOP));
    azzert.that(OpCode.read(b).opCode, is(OpCode.NOP));
    azzert.that(OpCode.read(b).opCode, is(OpCode.NOP));
    azzert.that(OpCode.read(b).opCode, is(OpCode.TABLESWITCH));
    assert b.eof();
    b.close();
  }
}
