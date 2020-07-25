package il.org.spartan;

public class MatrixWriter extends CSVLineWriter {
  public MatrixWriter(final String fileName) {
    super(fileName, Renderer.MATRIX);
  }

  @Override public String header() {
    return "";
  }
}
