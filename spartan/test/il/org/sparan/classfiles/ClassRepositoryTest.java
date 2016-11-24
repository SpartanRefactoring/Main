package il.org.sparan.classfiles;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.classfiles.*;

/** A class to test {@link ClassRepository} including path names with funny
 * UNICODE characters. If some of the text here is unreadable, you should change
 * encoding to UTF-8. In Eclipse this is done through the following menu chain:
 * Project/Properties/Info/Text File Encoding. In other versions, your may try:
 * Window -> Preferences -> General -> Workspace // -> text file encoding. Then
 * choose "Other", "UTF-8"
 * @author Itay Maman Jul 11, 2006 */
@SuppressWarnings("static-method") //
public class ClassRepositoryTest {
  public static void main(final String[] args) {
    System.out.println(new ClassRepository(ClassRepositoryTest.class));
  }

  @Test public void noDuplications() {
    @NotNull final ClassRepository cr = new ClassRepository(ClassRepositoryTest.class);
    @NotNull final Set<String> set = new HashSet<>();
    for (@NotNull final File f : cr.getRoots()) {
      @NotNull final String abs = f.getAbsolutePath();
      assert set.contains(abs) : abs;
      set.add(abs);
    }
  }

  //
  // Sample classes (serve as input values)
  //
  public static class MyClass {
    // No body
  }
}
