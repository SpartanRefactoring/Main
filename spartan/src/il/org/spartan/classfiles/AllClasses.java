package il.org.spartan.classfiles;

import static fluent.ly.___.*;

import java.io.*;

import org.jetbrains.annotations.*;

import fluent.ly.*;
import il.org.spartan.files.visitors.*;
import il.org.spartan.files.visitors.FileSystemVisitor.Action.*;

public class AllClasses {
  public static void main(final String[] args) throws IOException, StopTraversal {
    for (final String root : CLASSPATH.asArray())
      new ClassFilesVisitor(root, new FileSystemVisitor.FileOnlyAction() {
        @Override public void visitFile(@NotNull final File ¢) {
          System.out.println(Filename.path2class(¢.getAbsolutePath(), root));
        }

        @Override public void visitZipEntry(@NotNull final String entryName, final InputStream __) {
          forget.it(__);
          System.out.println(Filename.path2class(entryName, root));
        }
      }).go();
  }
}
