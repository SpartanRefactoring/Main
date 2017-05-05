package il.org.spartan.classfiles.reify;

import java.io.*;

import org.jetbrains.annotations.*;

public final class BufferDataInputStream extends DataInputStream {
  @NotNull private final BufferInputStream inner;

  public BufferDataInputStream(@NotNull final BufferInputStream inner) {
    super(inner);
    this.inner = inner;
  }
  public BufferDataInputStream(@NotNull final byte[] bytes) {
    this(new BufferInputStream(bytes));
  }
  public void align4() {
    if (position() % 4 != 0)
      skip(4 - position() % 4);
  }
  public boolean eof() {
    return inner.eof();
  }
  public int position() {
    return inner.position();
  }
  @Override public int read() {
    return inner.read();
  }
  @Override public long skip(final long ¢) {
    final long $ = inner.skip(¢);
    if ($ != ¢)
      throw new RuntimeException();
    return $;
  }
}