package il.org.spartan.utils;

import java.io.*;
import java.nio.file.*;

import org.jetbrains.annotations.*;

/** File utils
 * @author Ori Marcovitch
 * @since Dec 4, 2016 */
public enum file {
  ;
  private static void delete(@NotNull final String path) {
    if (file.exists(path))
      new File(path).delete();
  }

  private static void rename(@NotNull final String from, @NotNull final String to) {
    file.delete(to);
    final Path source = Paths.get(from);
    try {
      Files.move(source, source.resolveSibling(to));
    } catch (@NotNull final IOException ¢) {
      monitor.infoIOException(¢);
    }
  }

  private static boolean exists(@NotNull final String path) {
    final File $ = new File(path);
    return $.exists() && !$.isDirectory();
  }

  public static void renameToCSV(@NotNull final String old) {
    file.rename(old, old + ".csv");
  }
}
