package il.org.spartan.java;

import static il.org.spartan.java.Token.AT_INTERFACE;
import static il.org.spartan.java.Token.IDENTIFIER;
import static il.org.spartan.java.Token.LBRACE;
import static il.org.spartan.java.Token.__class;
import static il.org.spartan.java.Token.__enum;
import static il.org.spartan.java.Token.__interface;

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
    if (skipUntil(__class, __enum, __interface, AT_INTERFACE) == null)
      return;
    getIdentifier();
    skipUntil(LBRACE);
  }

  public void field() {
    //
  }

  public void file() {
    //
  }

  public void method() {
    //
  }

  public void statement() {
    //
  }

  public void variable() {
    //
  }

  private String getIdentifier() {
    skipUntil(IDENTIFIER);
    return tokenizer.text();
  }

  private Token skipUntil(final Token... ¢) {
    for (Token $ = tokenizer.next(); $ != null; $ = tokenizer.next())
      if (in($, ¢))
        return $;
    return null;
  }
}
