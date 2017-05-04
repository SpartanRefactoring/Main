// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

// TODO: I commented this import. Had it been renamed?

// import static fluent.ly.___.*;
import java.io.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** A class to generate a CSV (comma separated values) file, by writing into it
 * a sequence of {@link CSVLine} records.
 * @author Yossi Gil, 2008/06/20 */
@Instantiable public class CSVWriter {
  @Nullable private static FileWriter open(@NotNull final File $) {
    try {
      return new FileWriter($);
    } catch (@NotNull final IOException ¢) {
      return null;
    }
  }

  private @Nullable File file;
  private @Nullable String header;
  /** The name of the file into which records are written. */
  @NotNull private final String fileName;
  @Nullable private final OutputStreamWriter inner;

  /** Create a new instance, writing into the standard output stream. */
  public CSVWriter() {
    inner = new OutputStreamWriter(System.out);
    fileName = "";
  }

  /** Create a new instance, writing into a given named file
   * @param fileName the name of the output file */
  public CSVWriter(@NotNull final String fileName) {
    this.fileName = fileName;
    file = new File(fileName);
    inner = open(file);
  }

  /** Close the file after completion. No further writes are allowed.
   * @return fileName */
  @NotNull public String close() {
    try {
      inner.close();
    } catch (@NotNull final IOException ¢) {
      ¢.printStackTrace();
    }
    return fileName;
  }

  public File file() {
    return file;
  }

  @NotNull public String fileName() {
    return fileName;
  }

  /** Find the common header of all CSV lines written to this file.
   * @return the header of the first line written (all further lines must
   *         agree) */
  public String header() {
    return header;
  }

  public void write(@NotNull final AbstractStringProperties ¢) {
    writeln(¢.line());
  }

  /** Write a new CSV line into the file. All CSV lines written into the file
   * must have the exact same header.
   * @param cl the CSV line to be written */
  public void write(@NotNull final CSVLine cl) {
    // TODO: Yossi, I can not find this function. Had it been removed/renamed.
    // Still not working too
    // require(header == null || header.equals(cl.header()), "\n" + header +
    // "\n" + cl.header());
    if (header == null)
      writeln(header = cl.header());
    writeln(cl.line());
  }

  /** Same as {@link #write(CSVLine)}, except that the file buffer is flushed
   * after successful write.
   * @param cl the CSV line to be written */
  public void writeFlush(@NotNull final CSVLine cl) {
    try {
      write(cl);
      inner.flush();
    } catch (@NotNull final IOException ¢) {
      ¢.printStackTrace();
    }
  }

  void writeln(@NotNull final String s) {
    try {
      inner.write(s);
      inner.write("\n");
    } catch (@NotNull final IOException ¢) {
      ¢.printStackTrace();
    }
  }
}
