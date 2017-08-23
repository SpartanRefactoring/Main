package il.org.spartan.spartanizer.cmdline.good;

import java.io.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.visitor.*;

public class BucketMethods {
  public static void main(final String[] args) {
    try (BufferedWriter out = system.callingClassUniqueWriter()) {
      new MasterVisitor(args) {/**/}.visitAll(new ASTTrotter() {
        @Override protected void record(final String summary) {
          try {
            out.write(summary);
          } catch (final IOException ¢) {
            note.bug(¢);
          }
          super.record(summary);
        }
      });
    } catch (IOException ¢) {
      throw new RuntimeException(¢);
    }
  }
}