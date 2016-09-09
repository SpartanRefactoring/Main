// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan;

import static il.org.spartan.utils.____.*;

import java.io.*;

import il.org.spartan.streotypes.*;

/** A class to generate a CSV (comma separated values) file, by writing into it
 * a sequence of {@link CSVLine} records.
 * @author Yossi Gil, 2008/06/20 */
@Instantiable public class CSVWriter {
  private static FileWriter open(final File f) {
    try {
      return new FileWriter(f);
    } catch (final IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private File file;
  private String header = null;
  /** The name of the file into which records are written. */
  private final String fileName;
  private final OutputStreamWriter inner;

  /** Create a new instance, writing into the standard output stream. */
  public CSVWriter() {
    inner = new OutputStreamWriter(System.out);
    fileName = "";
  }

  /** Create a new instance, writing into a given named file
   * @param fileName the name of the output file */
  public CSVWriter(final String fileName) {
    this.fileName = fileName;
    file = new File(fileName);
    inner = open(file);
  }

  /** Close the file after completion. No further writes are allowed.
   * @return fileName */
  public String close() {
    try {
      inner.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return fileName;
  }

  public File file() {
    return file;
  }

  public String fileName() {
    return fileName;
  }

  /** Find the common header of all CSV lines written to this file.
   * @return the header of the first line written (all further lines must
   *         agree) */
  public String header() {
    return header;
  }

  public void write(final AbstractStringProperties c) {
    writeln(c.line());
  }

  /** Write a new CSV line into the file. All CSV lines written into the file
   * must have the exact same header.
   * @param cl the CSV line to be written */
  public void write(final CSVLine cl) {
    require(header == null || header.equals(cl.header()), "\n" + header + "\n" + cl.header());
    if (header == null)
      writeln(header = cl.header());
    writeln(cl.line());
  }

  /** Same as {@link #write(CSVLine)}, except that the file buffer is flushed
   * after successful write.
   * @param cl the CSV line to be written */
  public void writeFlush(final CSVLine cl) {
    try {
      write(cl);
      inner.flush();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  void writeln(final String s) {
    try {
      inner.write(s);
      inner.write("\n");
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
