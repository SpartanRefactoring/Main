package il.org.spartan.classfiles;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.annotation.*;

/** @author Yossi Gil
 * @since 11 November 2011 */
public class RobustReader {
   protected static DataInputStream asDataInputStream( final InputStream ¢) {
    return ¢ == null ? null : new DataInputStream(¢);
  }
   protected static FileInputStream asFileInputStream( final File $) {
    if ($ == null)
      return null;
    try {
      return new FileInputStream($);
    } catch ( final FileNotFoundException e) {
      return null;
    }
  }

  private final List<Exception> errors = new ArrayList<>();
  /** The stream from which the remainder of this class file can be read. */
  private final DataInputStream inner;

  public RobustReader(final DataInputStream inner) {
    this.inner = inner;
  }
  /** Instantiate {@link RobustReader} from a given {@link File}
   * @param f an arbitrary file object */
  public RobustReader(final File f) {
    this(asFileInputStream(f));
  }
  /** Instantiate {@link RobustReader}.
   * @param is */
  public RobustReader(final InputStream is) {
    this(asDataInputStream(is));
  }
  /** Instantiate {@link RobustReader}.
   * @param fileName an arbitrary file name */
  public RobustReader( final String fileName) {
    this(new File(fileName));
  }
  public final void close() {
    if (inner != null)
      try {
        inner.close();
      } catch ( final IOException ¢) {
        recordError(¢);
      }
  }
  public final boolean hasErrors() {
    return !errors.isEmpty();
  }
  public final double readDouble() {
    if (inner == null)
      return 0;
    try {
      return inner.readDouble();
    } catch ( final IOException $) {
      return recordError($);
    }
  }
  public final float readFloat() {
    if (inner == null)
      return 0;
    try {
      return inner.readFloat();
    } catch ( final IOException $) {
      return recordError($);
    }
  }
  public final int readInt() {
    if (inner == null)
      return 0;
    try {
      return inner.readInt();
    } catch ( final IOException $) {
      return recordError($);
    }
  }
  public final long readLong() {
    if (inner == null)
      return 0;
    try {
      return inner.readLong();
    } catch ( final IOException $) {
      return recordError($);
    }
  }
  public final int readUnsignedByte() {
    if (inner == null)
      return 0;
    try {
      return inner.readUnsignedByte();
    } catch ( final IOException $) {
      return recordError($);
    }
  }
  public final int readUnsignedShort() {
    if (inner == null)
      return 0;
    try {
      return inner.readUnsignedShort();
    } catch ( final IOException $) {
      return recordError($);
    }
  }
  public final String readUTF() {
    if (inner == null)
      return "";
    try {
      return inner.readUTF();
    } catch ( final IOException ¢) {
      recordError(¢);
      return null;
    }
  }
  public final void skipBytes(final int i) {
    for (int ¢ = 0; ¢ < i; ++¢)
      readUnsignedByte();
  }
   protected final byte[] readBytes( final byte[] $) {
    if (inner == null)
      return new byte[0];
    for (int left = $.length; left > 0;)
      left -= readBytes($, $.length - left, left);
    return $;
  }
  protected int recordError(final Exception ¢) {
    errors.add(¢);
    return 0;
  }
  int readBytes( final byte[] $, final int offset, final int howMany) {
    try {
      return inner.read($, offset, howMany);
    } catch ( final IOException ¢) {
      recordError(¢);
      return howMany;
    }
  }
}
