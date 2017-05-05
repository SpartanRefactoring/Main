package il.org.spartan;

import java.io.*;

import org.jetbrains.annotations.*;

/** Combines {@link CSVWriter} and {@link CSVLine}, to make it easy to write to
 * a CSV file.
 * @author Yossi Gil
 * @since Dec 25, 2009 */
public class CSVLineWriter extends CSVLine.Ordered {
  protected final CSVWriter writer;
  private int count;

  /** Instantiate this class, setting CSV file to the standard output stream. */
  public CSVLineWriter() {
    this(new CSVWriter());
  }
  public CSVLineWriter(final CSVWriter writer) {
    this.writer = writer;
  }
  public CSVLineWriter(final CSVWriter writer, final Renderer renderer) {
    super(renderer);
    this.writer = writer;
  }
  public CSVLineWriter(final Renderer renderer) {
    this(new CSVWriter(), renderer);
  }
  public CSVLineWriter(final String fileName) {
    writer = new CSVWriter(fileName + extension());
  }
  public CSVLineWriter(final String fileName, final Renderer renderer) {
    super(renderer);
    writer = new CSVWriter(fileName + extension());
  }
  @Nullable public String absolutePath() {
    return writer.file() == null ? null : writer.file().getAbsolutePath();
  }
  public String close() {
    writer.writeln(renderer.allBottom());
    return writer.close();
  }
  public int count() {
    return count;
  }
  public File file() {
    return writer.file();
  }
  @NotNull public String fileName() {
    return writer.fileName();
  }
  public void nl() {
    writer.writeFlush(this);
    ++count;
  }
  @NotNull @SuppressWarnings("static-method") protected String extension() {
    return ".csv";
  }
}
