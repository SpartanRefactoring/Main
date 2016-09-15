package il.org.spartan.java;

import static il.org.spartan.java.Token.*;

public class LightWeightParser {
  private static boolean in(final Token t, final Token[] ts) {
    for (final Token tʹ : ts)
      if (t == tʹ)
        return true;
    return false;
  }

  private final Tokenizer tokenizer;

  public LightWeightParser(final Tokenizer tokenizer) {
    this.tokenizer = tokenizer;
  }

  public void clazz() {
    final Token t = skipUntil(__class, __enum, __interface, AT_INTERFACE);
    if (t == null)
      return;
    getIdentifier();
    skipUntil(LBRACE);
  }

  public void field() {
  }

  public void file() {
  }

  public void method() {
  }

  public void statement() {
  }

  public void variable() {
  }

  private String getIdentifier() {
    skipUntil(IDENTIFIER);
    return tokenizer.text();
  }

  private Token skipUntil(final Token... ts) {
    for (Token t = tokenizer.next(); t != null; t = tokenizer.next())
      if (in(t, ts))
        return t;
    return null;
  }
}
