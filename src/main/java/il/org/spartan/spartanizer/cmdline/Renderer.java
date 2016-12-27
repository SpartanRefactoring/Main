package il.org.spartan.spartanizer.cmdline;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public interface Renderer {
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

// @formatter:off
  default String integerField(final String value) { return value; }
  default String realField(final String value) { return value; }
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
  default String null¢() { return "＃"; }
  // @formatter:on

  default String render(Statistic ¢) {
    return ¢ + "";
  }

  enum builtin implements Renderer {
    TXT, TEX {
      @Override public String null¢() {
        return "$\\#$";
      }

      @Override public String render(Statistic ¢) {
        switch (¢) {
          default:
            return super.render(¢);
          case min:
          case max:
            return "$\\" + super.render(¢) + "$";
          case σ:
            return "$\\sigma$";
        }
      }

      // @formatter:off
      @Override public String recordEnd() { return "\\\\\n"; }
      @Override public String recordSeparator() { return "\t&\t"; }
      @Override public String beforeTable() {return "\\toprule\n"; }
      @Override public String afterTable() {return "\\bottomrule\n"; }
      @Override public String afterHeader() { return "\\midrule\n"; }
      @Override public String beforeFooter() { return "\\midrule\n"; }
    // @formatter:on
    },
    TEX2 {
  // @formatter:off
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
        String $ = "";
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

    @Override public void setHeaderCount(int size) {
      builtin.lastSize = size;
    }
  }

  void setHeaderCount(int size);
}
