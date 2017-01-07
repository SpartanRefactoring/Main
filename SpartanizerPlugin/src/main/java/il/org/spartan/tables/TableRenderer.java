package il.org.spartan.tables;

import java.util.*;

import il.org.spartan.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public interface TableRenderer {
  static String empty() {
    return "";
  }

  static String nl() {
    return "\n";
  }

  static String tab() {
    return "\t";
  }

  default String extension() {
    return toString().toLowerCase();
  }

  default String renderRow(final Collection<Object> values) {
    final StringBuilder $ = new StringBuilder(recordBegin());
    final Separator s = new Separator(recordSeparator());
    for (final Object ¢ : values)
      $.append(s)
          .append(¢ instanceof Object[] ? cellArray((Object[]) ¢)
              : ¢ instanceof Integer ? cellInteger((Integer) ¢) //
                  : ¢ instanceof Double ? cellReal((Double) ¢) //
                      : ¢);
    return $ + recordEnd();
  }

  default String cellArray(final Object[] ¢) {
    return separate.these(¢).by(arraySeparator());
  }

  // @formatter:off
  default String arraySeparator() { return "; "; }
  default String cellInteger(final Integer ¢) { return ¢ + ""; }
  default String cellReal(final Double ¢) { return ¢ + ""; }
  default String stringField(final String value) { return value; }
  default String beforeTable() { return empty(); }
  default String afterTable() { return empty(); }
  default String beforeHeader() { return empty(); }
  default String afterHeader() { return empty(); }
  default String beforeFooter() { return empty(); }
  default String afterFooter() { return empty(); }
  default String recordBegin() { return empty(); }
  default String recordEnd() {return nl(); }
  default String recordSeparator()  { return tab(); }
  default String headerLineBegin() { return recordBegin(); }
  default String headerLineEnd() { return recordEnd(); }
  default String headerSeparator() { return recordSeparator(); }
  default String footerBegin() { return recordBegin();}
  default String footerEnd() { return recordEnd();}
  default String footerSeparator() { return recordSeparator(); }
  default String null¢() { return "Nº"; }
  // @formatter:on

  default String render(final Statistic ¢) {
    return ¢ + "";
  }

  enum builtin implements TableRenderer {
    TXT, TEX {
      @Override public String null¢() {
        return "$\\#$";
      }

      @Override public String render(final Statistic ¢) {
        switch (¢) {
          default:
            return "\\hfill" + super.render(¢);
          case min:
          case max:
            return "\\hfill" + "$\\" + super.render(¢) + "$";
          case σ:
            return "\\hfill" + "$\\sigma$";
          case Σ:
            return "\\hfill" + "$\\Sum$";
        }
      }

      // @formatter:off
      @Override public String arraySeparator() { return ", "; }
      @Override public String recordEnd() { return " \\\\\n"; }
      @Override public String recordSeparator() { return "\t&\t"; }
      @Override public String beforeTable() {return "\\toprule\n"; }
      @Override public String afterTable() {return "\\bottomrule\n"; }
      @Override public String afterHeader() { return "\\midrule\n"; }
      @Override public String beforeFooter() { return "\\midrule\n"; }
    // @formatter:on
    },
    TEX2 {
  // @formatter:off
    @Override public String arraySeparator() { return ", "; }
    @Override public String footerEnd() { return "\\\\\n"; }
    @Override public String recordSeparator() { return "\t&\t"; }
    @Override public String beforeTable() {return "\\hline\n"; }
    @Override public String afterTable() {return "\\hline\n"; }
    @Override public String afterHeader() { return "\\hline\n"; }
    @Override public String beforeFooter() { return "\\hline\n"; }
  // @formatter:on
    },
    CSV {
    // @formatter:off
    @Override public String footerEnd() { return "\n"; }
    @Override public String recordSeparator() { return ","; }
    // @formatter:on
    },
    MARKDOWN {
      @Override public String afterHeader() {
        String $ = "| ";
        for (int ¢ = 0; ¢ < lastSize; ++¢)
          $ += "--- |";
        return $ + nl();
      }

      // @formatter:off
      @Override public String beforeTable() { return nl(); }
      @Override public String afterTable() { return nl(); }
      @Override public String recordBegin() { return "|" ; }
      @Override public String recordEnd() { return " |\n"; }
      @Override public String recordSeparator() { return " | "; }
    // @formatter:on
    };
    static int lastSize;

    @Override public void setHeaderCount(final int size) {
      builtin.lastSize = size;
    }
  }

  void setHeaderCount(int size);
}
