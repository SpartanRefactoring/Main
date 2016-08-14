package il.org.spartan.refactoring.suggestions;

import java.util.*;

import il.org.spartan.*;

/**
 * @author Yossi Gil
 *
 * @since 2016`
 */
public class Suggestions extends Wrapper<List<Suggestion>> {
  /** instantiates this class */
  public Suggestions() {
    super(new ArrayList<>());
  }
  /**
   * instantiates this class
   *
   * @param inner
   *          JD
   */
  public Suggestions(final List<Suggestion> inner) {
    super(inner);
  }
}