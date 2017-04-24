package il.org.spartan.java;

import static il.org.spartan.java.Token.*;

import org.jetbrains.annotations.*;

public class LightWeightParser {
  private static boolean in(final Token t, @NotNull final Token[] ts) {
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

  @NotNull private String getIdentifier() {
    skipUntil(IDENTIFIER);
    return tokenizer.text();
  }

  private Token skipUntil(@NotNull final Token... ¢) {
    for (@Nullable Token $ = tokenizer.next(); $ != null; $ = tokenizer.next())
      if (in($, ¢))
        return $;
    return null;
  }
}
