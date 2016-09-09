package il.org.spartan.classfiles.reify;

import java.io.*;

public final class BufferDataInputStream extends DataInputStream {
  private final BufferInputStream inner;

  public BufferDataInputStream(final BufferInputStream inner) {
    super(inner);
    this.inner = inner;
  }

  public BufferDataInputStream(final byte[] bytes) {
    this(new BufferInputStream(bytes));
  }

  public void align4() {
    if (position() % 4 == 0)
      return;
    skip(4 - position() % 4);
  }

  public boolean eof() {
    return inner.eof();
  }

  public final int position() {
    return inner.position();
  }

  @Override public int read() {
    return inner.read();
  }

  @Override public long skip(final long n) {
    final long $ = inner.skip(n);
    if ($ != n)
      throw new RuntimeException();
    return $;
  }
}