// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

import java.io.*;

import org.jetbrains.annotations.*;

import il.org.spartan.files.visitors.FileSystemVisitor.*;
import il.org.spartan.files.visitors.FileSystemVisitor.Action.*;
import il.org.spartan.streotypes.*;
import il.org.spartan.strings.*;
import fluent.ly.*;

/** A program to search for a ".class" file in the file system.
 * @author Yossi Gil Mar 29, 2007 */
@Application public class FindClassFile {
  static final String name = "where";
  static boolean reportCounts;
  static boolean findFirstOnly;

  public static void main(@NotNull final String[] args) {
    if (args.length == 0) {
      System.err.printf("Usage: %s [ -n ] [ -f ] className [className ...]\n", name);
      System.exit(1);
    }
    for (final String arg : args)
      if (!processOption(arg))
        try {
          System.out.println("Searching for class: " + arg + " in " + Parenthesize.square(File.listRoots()));
          new FileSystemVisitor(File.listRoots(), new Searcher("." + arg + ".class"), ".class").go();
        } catch (@NotNull final IOException ¢) {
          System.err.println(¢.getMessage());
        } catch (@NotNull final StopTraversal e) {
          //
        }
  }

  private static boolean processOption(final String option) {
    if ("-n".equals(option))
      reportCounts = true;
    else {
      if (!"-f".equals(option))
        return false;
      findFirstOnly = true;
    }
    return true;
  }

  /** An {@Action} searching for files with a given suffix, which counts the
   * various kinds of file system entities it encounters during the search.
   * @author Yossi Gil
   * @since 21/05/2007 */
  public static class Searcher implements Action {
    private final String sought;
    private int directories;
    private int files;
    private int zips;
    private int entries;

    public Searcher(final String sought) {
      this.sought = sought;
    }

    @Override public void visitDirectory(@NotNull final File ¢) {
      ++directories;
      report("Directory: " + ¢.getAbsolutePath());
    }

    @Override public void visitFile(@NotNull final File ¢) throws StopTraversal {
      ++files;
      report("File: " + ¢.getAbsolutePath());
      check(¢.getName(), ¢.getAbsolutePath());
    }

    @Override public void visitZip(@NotNull final File ¢) {
      ++zips;
      report("Archive: " + ¢.getAbsolutePath());
    }

    @Override public void visitZipDirectory(final String zipName, final String entryName, final InputStream s) {
      ___.unused(s);
      report("Archive directory: " + entryName + " in zip " + zipName);
    }

    @Override public void visitZipEntry(final String zipName, @NotNull final String entryName, final InputStream s) throws StopTraversal {
      ___.unused(s);
      ++entries;
      report("Archive entry: " + entryName);
      check(entryName, zipName);
    }

    private void check(@NotNull final String file, final String directory) throws StopTraversal {
      if (!file.endsWith(sought))
        return;
      System.out.printf("%s: %s\n", file, directory);
      if (FindClassFile.findFirstOnly)
        throw new StopTraversal();
    }

    private void report(final String ¢) {
      if (FindClassFile.reportCounts)
        System.out.println(¢ + ". " + directories + " directories, " + files + " class files, " + zips + " ZIP archives, " + entries + " entries.");
    }
  }
}
