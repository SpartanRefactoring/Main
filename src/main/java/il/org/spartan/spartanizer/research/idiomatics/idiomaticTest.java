package il.org.spartan.spartanizer.research.idiomatics;
import static il.org.spartan.spartanizer.research.idiomatics.idiomatic.*;
import il.org.spartan.azzert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static il.org.spartan.azzert.is;
import static il.org.spartan.lisp.first;

/** Created by yogi on 10/02/17. */
@SuppressWarnings({ "javadoc", "static-method" })
public class idiomaticTest {
  @Test public void use0() {
    azzert.notNull(new idiomatic.Storer<>(this));
  }

  @Test public void use08() {
    azzert.isNull(idiomatic.unless(true).eval(Object::new));
  }

  @Test public void use09() {
    azzert.notNull(idiomatic.unless(false).eval(Object::new));
  }

  @Test public void use1() {
    azzert.notNull(new idiomatic.Storer<>(this));
    new idiomatic.Storer<>(this).when(true);
  }

  @Test public void use10() {
    azzert.notNull(idiomatic.vhen(true).eval(Object::new));
  }

  @Test public void use11() {
    azzert.isNull(idiomatic.vhen(false).eval(Object::new));
  }

  @Test public void use2() {
    azzert.notNull(idiomatic.take(this));
    azzert.isNull(idiomatic.take(this).when(false));
  }

  @Test public void use3() {
    azzert.that(idiomatic.take(this).when(true), is(this));
  }

  @Test public void use4() {
    azzert.isNull(idiomatic.take(this).when(false));
  }

  @Test public void use5() {
    azzert.that(idiomatic.take(this).unless(false), is(this));
  }

  @Test public void use6() {
    azzert.isNull(idiomatic.take(this).unless(true));
  }

  @Test public void use7() {
    azzert.isNull(idiomatic.take(this).unless(true));
    azzert.isNull(idiomatic.take(null).unless(true));
    azzert.isNull(idiomatic.take(null).unless(false));
  }

  String mapper(final String ¢) {
    return ¢ + ¢;
  }

  String mapper(final Integer ¢) {
    return ¢ + "";
  }

  @Test public void useMapper() {
    final List<String> before = new ArrayList<>();
    before.add("1");
    before.add("2");
    before.add("3");
    final List<String> after = idiomatic.on(before).map(this::mapper);
    azzert.that(first(after), is("11"));
    azzert.that(after.get(1), is("22"));
    azzert.that(after.get(2), is("33"));
  }

  @Test @SuppressWarnings("boxing") public void useMapper2() {
    final List<Integer> before = new ArrayList<>();
    before.add(1);
    before.add(2);
    before.add(3);
    final List<String> after = idiomatic.on(before).map(this::mapper);
    azzert.that(first(after), is("1"));
    azzert.that(after.get(1), is("2"));
    azzert.that(after.get(2), is("3"));
  }

  @Test @SuppressWarnings("boxing") public void useFilter() {
    final List<Integer> before = new ArrayList<>();
    before.add(1);
    before.add(2);
    before.add(3);
    final List<Integer> after = idiomatic.on(before).filter(λ -> λ % 2 == 1);
    azzert.that(first(after).intValue(), is(1));
    azzert.that(after.get(1).intValue(), is(3));
  }

  @Test public void useReduce() {
    final List<String> before = new ArrayList<>();
    before.add("1");
    before.add("2");
    before.add("3");
    azzert.that(idiomatic.on(before).reduce((x, y) -> x + y), is("123"));
  }

  @Test public void useMax() {
    final List<String> before = new ArrayList<>();
    before.add("1");
    before.add("2");
    before.add("3");
    azzert.that(idiomatic.on(before).max(String::compareTo), is("3"));
  }

  @Test public void useMin() {
    final List<String> before = new ArrayList<>();
    before.add("1");
    before.add("2");
    before.add("3");
    azzert.that(idiomatic.on(before).min(String::compareTo), is("1"));
  }

  @Test @SuppressWarnings("boxing") public void whenNullsEval() {
    final Object o = new Object();
    when(o).nulls().eval(() -> o.hashCode()).elze(() -> o.hashCode());
  }
}
