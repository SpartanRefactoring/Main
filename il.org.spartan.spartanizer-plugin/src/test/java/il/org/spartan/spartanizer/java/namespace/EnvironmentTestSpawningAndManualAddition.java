package il.org.spartan.spartanizer.java.namespace;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.java.namespace.Environment.*;

import org.junit.*;

import fluent.ly.*;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Dec 26, 2016 */
public final class EnvironmentTestSpawningAndManualAddition {
  private final Environment e0 = Environment.genesis();
  private final Environment e1 = e0.spawn();
  private final Environment ee0 = Environment.genesis();
  private final Environment ee1 = ee0.spawn();

  @Test public void defaultDoesntHave() {
    assert e0.nest().doesntHave("Alex");
  }
  @Test public void defaultempty() {
    assert e0.nest().empty();
  }
  @Test public void defaultEMPTYFullName() {
    azzert.that(e0.nest().fullName(), is(""));
  }
  @Test public void defaultfullEntries() {
    assert e0.fullEntries() != null;
  }
  @Test public void defaultFullName() {
    azzert.that(e0.fullName(), is(""));
  }
  @Test public void defaultFullNames() {
    assert e0.fullNames() != null;
  }
  @Test public void defaultGet() {
    assert e0.nest().get("Alex") == null;
  }
  @Test public void defaultHas() {
    assert !e0.nest().has("Alex");
  }
  @Test public void defaultName() {
    azzert.that(e0.name(), is(""));
  }
  @Test public void defaultSize0() {
    azzert.that(e0.size(), is(3));
    azzert.that(e0.fullSize(), is(3));
  }
  @Test public void DoesntHaveFalseResult() {
    assert !e1.nest().doesntHave("Yossi");
  }
  @Test public void empty() {
    e0.put("Alex", new Binding());
    assert !e0.empty();
  }
  @Test public void emptyOne() {
    assert !e1.empty();
  }
  @Test public void emptyTestBothEmpty() {
    assert ee1.empty();
  }
  @Test public void emptyTestFlatEmptyNestNot() {
    ee0.put("Alex", new Binding());
    assert !ee1.empty();
  }
  @Test public void emptyTestNeitherEmpty() {
    ee0.put("Yossi", new Binding());
    ee1.put("Gill", new Binding());
    assert !ee1.empty();
  }
  @Test public void emptyTestNestEmptyFlatNot() {
    ee1.put("Dan", new Binding());
    assert !ee1.empty();
  }
  @Test public void fullSize() {
    azzert.that(e0.fullSize(), is(3));
  }
  @Test public void fullSize0() {
    azzert.that(e0.fullSize(), is(3));
  }
  @Test public void get() {
    e0.put("Alex", new Binding());
    assert e0.get("Alex") != null;
  }
  @Test public void getFromParent() {
    assert e1.get("Alex") != null;
  }
  @Test public void getOne() {
    assert e1.get("Kopzon") != null;
  }
  @Test public void has() {
    e0.put("Alex", new Binding());
    assert e0.has("Alex");
  }
  @Test public void hasInBoth() {
    e1.put("Yo", new Binding());
    assert e1.has("Yo");
  }
  @Test public void hasInParent() {
    assert e1.has("Dan");
  }
  @Test public void hasNowhere() {
    assert !e1.has("Onoes");
  }
  @Test public void hasOne() {
    assert e1.has("Kopzon");
    assert e1.has("Dan");
    assert e1.has("Yossi");
    assert e1.has("Alex");
  }
  @Test public void hidingOne() {
    assert e1.hiding("Alex") != null;
  }
  @Before public void init_one_level() {
    e0.put("Alex", new Binding());
    e0.put("Dan", new Binding());
    e0.put("Yossi", new Binding());
    e1.put("Kopzon", new Binding());
    e1.put("Greenstein", new Binding());
    e1.put("Gill", new Binding());
  }
  @Test public void names() {
    e0.put("Alex", new Binding());
    assert e0.keys().contains("Alex");
  }
  @Test public void namesOne() {
    assert e1.keys().contains("Kopzon");
    assert !e1.keys().contains("Alex");
  }
  @Test public void Nest() {
    azzert.that(e0.nest(), is(NULL));
  }
  @Test public void NestOne() {
    azzert.that(e1.nest(), is(e0));
  }
  @Test public void put() {
    assert e0.put("Alex", new Binding()) == null;
  }
  @Test public void putOne() {
    assert e1.put("Kopzon1", new Binding()) == null;
  }
  @Test public void putOneAndHide() {
    assert e1.put("Alex", new Binding()) != null;
  }
  @Test(expected = IllegalArgumentException.class) public void putTest() {
    e0.nest().put("Dan", new Binding());
  }
  @Test public void size() {
    azzert.that(e0.size(), is(3));
  }
}
