package il.org.spartan;

import org.jetbrains.annotations.*;

public class MatrixWriter extends CSVLineWriter {
  public MatrixWriter(final String fileName) {
    super(fileName, Renderer.MATRIX);
  }

  // no header to print;
  @NotNull @Override public String header() {
    return "";
  }
}
