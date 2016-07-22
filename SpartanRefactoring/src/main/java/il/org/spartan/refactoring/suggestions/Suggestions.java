package il.org.spartan.refactoring.suggestions;

import il.org.spartan.misc.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

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
   * @param inner JD
   */
  public Suggestions(final List<Suggestion> inner) {
    super(inner);
  }
}