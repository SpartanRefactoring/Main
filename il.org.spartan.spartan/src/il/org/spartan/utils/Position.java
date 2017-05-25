package il.org.spartan.utils;

import java.io.*;

/** Represents a position in a file, including a column and line number.
 * @author Yossi Gil <yogi@cs.technion.ac.il> 13/06/2007 */
public final class Position implements Comparable<Position>, Serializable {
  private static final long serialVersionUID = -0x7E3694C5AD70F8F3L;
  public final int line;
  public final int column;

  /** @param line the line of this position
   * @param column the column of this position */
  public Position(final int line, final int column) {
    this.line = line;
    this.column = column;
  }
  public boolean before(final Position ¢) {
    return compareTo(¢) < 0;
  }
  @Override public int compareTo(final Position ¢) {
    return line != ¢.line ? line - ¢.line : column - ¢.column;
  }
  @Override public boolean equals(final Object ¢) {
    return ¢ == this || ¢ != null && getClass() == ¢.getClass() && column == ((Position) ¢).column && line == ((Position) ¢).line;
  }
  @Override public int hashCode() {
    return line ^ column;
  }
  public Position nextChar() {
    return new Position(line, column + 1);
  }
  public Position nextLine() {
    return new Position(line + 1, 1);
  }
  @Override public String toString() {
    return "(" + line + ":" + column + ")";
  }
}
