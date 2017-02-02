package il.org.spartan.tables;

import java.io.*;

import il.org.spartan.*;
import org.jetbrains.annotations.NotNull;

/** TODO: Yossi Gil <tt>yossi.gil@gmail.com</tt> please add a description
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public class TableWriter implements Closeable {
  /** Create a new instance, writing into a given named file
   * @param fileName the name of the output file
   * @throws IOException */
  public TableWriter(@NotNull final TableRenderer renderer, @NotNull final String basePath) throws IOException {
    this.renderer = renderer;
    fileName = basePath.replaceAll("\\.[a-z0-9]*$", "") + "." + renderer.extension();
    file = new File(fileName);
    writer = new FileWriter(file);
    write(renderer.beforeTable());
  }

  public void write(@NotNull final String s) {
    try {
      writer.write(s);
      writer.flush();
    } catch (final IOException ¢) {
      throw new RuntimeException(¢);
    }
  }

  @NotNull
  public final File file;
  /** The name of the file into which records are written. */
  @NotNull
  public final String fileName;
  @NotNull
  public final OutputStreamWriter writer;
  @NotNull
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

  public void write(@NotNull final Row<?> ¢) {
    if (!headerPrinted) {
      headerPrinted = true;
      writeHeader(¢);
    }
    writeData(¢);
  }

  public void writeFooter(@NotNull final Row<?> ¢) {
    if (!footerPrinted) {
      write(renderer.beforeFooter());
      footerPrinted = true;
    }
    write(renderer.footerBegin() + separate.these(¢.values()).by(renderer.footerSeparator()) + renderer.footerEnd());
  }

  private void writeData(@NotNull final Row<?> ¢) {
    write(renderer.recordBegin() + separate.these(¢.values()).by(renderer.recordSeparator()) + renderer.recordEnd());
  }

  private void writeHeader(@NotNull final Row<?> ¢) {
    renderer.setHeaderCount(¢.size());
    write(renderer.beforeHeader() + //
        renderer.headerLineBegin() + writeHeaderInner(¢) + renderer.headerLineEnd() + //
        renderer.afterHeader());
  }

  @NotNull
  private String writeHeaderInner(@NotNull final Row<?> r) {
    final Separator s = new Separator(renderer.headerSeparator());
    final StringBuilder $ = new StringBuilder();
    r.keySet().forEach(λ -> $.append(s).append(λ != null ? λ : renderer.null¢()));
    return $ + "";
  }
}
