package il.org.spartan;

import org.jetbrains.annotations.*;

public class MatrixWriter extends CSVLineWriter {
  public MatrixWriter(final String fileName) {
    super(fileName, Renderer.MATRIX);
  }
  @Override @NotNull public String header() {
    return "";
  }
}
