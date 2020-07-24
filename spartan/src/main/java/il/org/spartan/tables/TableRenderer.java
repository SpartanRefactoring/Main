package il.org.spartan.tables;

import java.util.Collection;

import fluent.ly.separate;
import il.org.spartan.Separator;

/** TODO Yossi Gil Document Classn
 * @author Yossi Gil
 * @since 2016-12-25 */
@FunctionalInterface
public interface TableRenderer {
  enum builtin implements TableRenderer {
    TXT, TEX {
      @Override public String afterHeader() {
        return "\\midrule" + NL;
      }
      @Override public String afterTable() {
        return "\\bottomrule" + NL;
      }
      // @formatter:off
      @Override
       public String arraySeparator() { return ", "; }
      @Override
       public String beforeFooter() { return "\\midrule" + NL; }
    // @formatter:on
      @Override public String beforeTable() {
        return "\\toprule" + NL;
      }
      @Override public String nil() {
        return "$\\#$";
      }
      @Override public String recordEnd() {
        return " \\\\" + NL;
      }
      @Override public String recordSeparator() {
        return "\t&\t";
      }
      @Override public String render(final Statistic ¢) {
        switch (¢) {
          case Σ:
            return "\\hfill$\\Sum$";
          case σ:
            return "\\hfill$\\sigma$";
          case max:
          case min:
            return "\\hfill$\\" + super.render(¢) + "$";
          default:
            return "\\hfill" + super.render(¢);
        }
      }
    },
    TEX2 {
      @Override public String afterHeader() {
        return "\\hline" + NL;
      }
      @Override public String afterTable() {
        return "\\hline" + NL;
      }
    // @formatter:off
    @Override
     public String arraySeparator() { return ", "; }
    @Override
     public String beforeFooter() { return "\\hline" + NL; }
  // @formatter:on
      @Override public String beforeTable() {
        return "\\hline" + NL;
      }
      @Override public String footerEnd() {
        return "\\\\" + NL;
      }
      @Override public String recordSeparator() {
        return "\t&\t";
      }
    },
    CSV {
    // @formatter:off
    @Override public String footerEnd() { return NL; }
    @Override
     public String recordSeparator() { return ","; }
    // @formatter:on
    },
    MARKDOWN {
      @Override public String afterHeader() {
        String $ = "| ";
        for (int ¢ = 0; ¢ < lastSize; ++¢)
          $ += "--- |";
        return $ + NL;
      }
      @Override public String afterTable() {
        return NL;
      }
      // @formatter:off
      @Override public String beforeTable() { return NL; }
      @Override
       public String recordBegin() { return "|" ; }
      @Override
       public String recordEnd() { return " |" + NL; }
      @Override
       public String recordSeparator() { return " | "; }
    // @formatter:on
    };
    static int lastSize;

    @Override public void setHeaderCount(final int size) {
      builtin.lastSize = size;
    }
  }

  String NL = System.getProperty("line.separator");

  default String cellReal(final Double ¢) {
    return ¢.longValue() != ¢.doubleValue() ? ¢ + "" : cellInt(Long.valueOf(¢.longValue()));
  }
  static String empty() {
    return "";
  }
  static String tab() {
    return "\t";
  }
  default String afterFooter() {
    return empty();
  }
  default String afterHeader() {
    return empty();
  }
  default String afterTable() {
    return empty();
  }
  // @formatter:off

  default String arraySeparator() { return "; "; }

  default String beforeFooter() { return empty(); }

  default String beforeHeader() { return empty(); }
  default String beforeTable() { return empty(); }
  default String cellArray(final Object[] ¢) {
    return separate.these(¢).by(arraySeparator());
  }

  default String cellInt(final Long ¢) { return ¢ + ""; }

  default String extension() {
    return toString().toLowerCase();
  }

  default String footerBegin() { return recordBegin();}
  default String footerEnd() { return recordEnd();}

  default String footerSeparator() { return recordSeparator(); }

  default String headerLineBegin() { return recordBegin(); }
  default String headerLineEnd() { return recordEnd(); }

  default String headerSeparator() { return recordSeparator(); }

  default String nil() { return "Nº"; }
  // @formatter:on
  default String recordBegin() {
    return empty();
  }
  default String recordEnd() {
    return NL;
  }
  default String recordSeparator() {
    return tab();
  }
  default String render(final Statistic ¢) {
    return ¢ + "";
  }
  default String renderRow(final Collection<Object> values) {
    final StringBuilder $ = new StringBuilder(recordBegin());
    final Separator s = new Separator(recordSeparator());
    values.forEach(λ -> $.append(s)
        .append(λ instanceof Object[] ? cellArray((Object[]) λ)
            : λ instanceof Integer ? cellInt(Long.valueOf(((Integer) λ).intValue())) : λ instanceof Long ? cellInt((Long) λ) //
                : λ instanceof Double ? cellReal((Double) λ) : λ));
    return $ + recordEnd();
  }
  void setHeaderCount(int size);
  default String stringField(final String value) {
    return value;
  }
}
