package il.org.spartan.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for utility methods when working with files and directories
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/19
 */
public class FileUtils {
  /**
   * Returns a list of all the .java files found recursively within the provided
   * paths
   *
   * @param paths Directories to scan
   * @return a list of absolute paths to .java files found within the provided
   *         directories. If no files were found, an empty list is returned
   */
  public static List<String> findAllJavaFiles(final String... paths) {
    final List<String> $ = new ArrayList<>();
    if (paths.length == 0)
      return $;
    for (final String s : paths) {
      if (s == null)
        continue;
      final File f = new File(s);
      if (f.exists() && f.isDirectory())
        iterateFiles(new File(s), $);
    }
    return $;
  }

  /**
   * Converts the entire contents of a file into a {@link String}
   *
   * @param f JD
   * @return a string representing the contents of a file.
   * @throws IOException in case of error
   */
  public static String read(final File f) throws IOException {
    final String ls = System.getProperty("line.separator");
    System.err.println(ls.compareTo("\n"));
    final StringBuilder $ = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
      for (String line = reader.readLine(); line != null; line = reader.readLine())
        $.append(line).append(ls);
    }
    return $ + "";
  }

  /**
   * Returns the contents of a source file
   *
   * @param fileName The source file's path
   * @return source file's contents, or an empty string in case of an error
   * @throws IOException in case of error
   */
  public static String readFromFile(final String fileName) throws IOException {
    return read(Paths.get(fileName));
  }

  /**
   * @param fileName where to write
   * @param text     what to write
   * @throws FileNotFoundException in case the file could not be found
   */
  public static void writeToFile(final String fileName, final String text) throws FileNotFoundException {
    try (PrintWriter p = new PrintWriter(fileName)) {
      p.write(text);
      p.flush();
    }
  }

  private static void iterateFiles(final File dir, final List<String> files) {
    if (dir != null)
      for (final File ¢ : dir.listFiles()) {
        if (¢.isDirectory())
          iterateFiles(¢, files);
        if (¢.isFile() && ¢.getName().endsWith(".java"))
          files.add(¢.getAbsolutePath());
      }
  }

  private static String read(final Path ¢) throws IOException {
    return new String(Files.readAllBytes(¢), StandardCharsets.UTF_8);
  }
}
