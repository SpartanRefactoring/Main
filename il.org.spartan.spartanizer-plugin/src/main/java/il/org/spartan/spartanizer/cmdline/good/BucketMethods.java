package il.org.spartan.spartanizer.cmdline.good;

import java.io.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;

public class BucketMethods {
  public static void main(final String[] args) {
    GrandVisitor.out = system.callingClassUniqueWriter();
    new GrandVisitor(args) {/**/}.visitAll(new ASTTrotter() {
      @Override protected void record(final String summary) {
        try {
          GrandVisitor.out.write(summary);
        } catch (final IOException ¢) {
          note.bug(¢);
        }
        super.record(summary);
      }
    });
  }
}