package il.org.spartan.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fluent.ly.note;

/**
 * File utils
 *
 * @author Ori Marcovitch
 * @since Dec 4, 2016
 */
public enum file {
  ;
  private static void delete(final String path) {
    if (file.exists(path))
      new File(path).delete();
  }

  private static void rename(final String from, final String to) {
    file.delete(to);
    final Path source = Paths.get(from);
    try {
      Files.move(source, source.resolveSibling(to));
    } catch (final IOException ¢) {
      note.io(¢);
    }
  }

  private static boolean exists(final String path) {
    final File $ = new File(path);
    return $.exists() && !$.isDirectory();
  }

  public static void renameToCSV(final String old) {
    file.rename(old, old + ".csv");
  }
}
