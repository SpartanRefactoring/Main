package il.org.spartan.java;

public class RichToken {
  private final Token token;
  private final RawTokenizer tokenizer;

  private RichToken(final RawTokenizer tokenizer, final Token token) {
    this.token = token;
    this.tokenizer = tokenizer;
  }
  public int chars() {
    return tokenizer.chars();
  }
  public int column() {
    return tokenizer.column();
  }
  public final boolean isError() {
    return token.isError();
  }
  public boolean isNL() {
    return token.isNL();
  }
  public int line() {
    return tokenizer.line();
  }
  public String location() {
    return tokenizer.location();
  }
  public final String name() {
    return token.name();
  }
  public final int ordinal() {
    return token.ordinal();
  }
  public String text() {
    return tokenizer.text();
  }
  public String token() {
    return tokenizer.token();
  }
  @Override public String toString() {
    return token + "";
  }

  public static class Factory {
    private final RawTokenizer tokenizer;

    public Factory(final RawTokenizer tokenizer) {
      this.tokenizer = tokenizer;
    }
    public RichToken make(final Token ¢) {
      return new RichToken(tokenizer, ¢);
    }
  }
}
