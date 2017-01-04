package il.org.spartan.tables;

import java.io.*;

import il.org.spartan.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public class RecordWriter implements Closeable {
  /** Create a new instance, writing into a given named file
   * @param fileName the name of the output file
   * @throws IOException */
  public RecordWriter(final TableRenderer renderer, final String basePath) throws IOException {
    this.renderer = renderer;
    this.fileName = basePath.replaceAll("\\.[a-z0-9]*$", "") + "." + renderer.extension();
    file = new File(this.fileName);
    writer = new FileWriter(file);
    write(renderer.beforeTable());
  }

  public void write(final String s) {
    try {
      writer.write(s);
      writer.flush();
    } catch (final IOException ¢) {
      throw new RuntimeException(¢);
    }
  }

  public final File file;
  /** The name of the file into which records are written. */
  public final String fileName;
  public final OutputStreamWriter writer;
  public final TableRenderer renderer;
  private boolean headerPrinted;
  private boolean footerPrinted;

  @Override public void close() {
    try {
      if (footerPrinted)
        write(renderer.afterFooter());
      write(renderer.afterTable());
      writer.close();
    } catch (final IOException ¢) {
      throw new RuntimeException(¢);
    }
  }

  public void write(final Record<?> ¢) {
    if (!headerPrinted) {
      headerPrinted = true;
      writeHeader(¢);
    }
    writeData(¢);
  }

  public void writeFooter(final Record<?> ¢) {
    if (!footerPrinted) {
      write(renderer.beforeFooter());
      footerPrinted = true;
    }
    write(renderer.footerBegin() + separate.these(¢.values()).by(renderer.footerSeparator()) + renderer.footerEnd());
  }

  private void writeData(final Record<?> ¢) {
    write(renderer.recordBegin() + separate.these(¢.values()).by(renderer.recordSeparator()) + renderer.recordEnd());
  }

  private void writeHeader(final Record<?> ¢) {
    renderer.setHeaderCount(¢.size());
    write(renderer.beforeHeader() + //
        renderer.headerLineBegin() + writeHeaderInner(¢) + renderer.headerLineEnd() + //
        renderer.afterHeader());
  }

  private String writeHeaderInner(final Record<?> r) {
    final Separator s = new Separator(renderer.headerSeparator());
    final StringBuffer $ = new StringBuffer();
    for (final String o : r.keySet())
      $.append(s).append(o != null ? o : renderer.null¢());
    return $ + "";
  }
}
