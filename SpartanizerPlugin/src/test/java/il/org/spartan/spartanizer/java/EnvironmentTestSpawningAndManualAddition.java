package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.java.Environment.*;

import org.junit.*;

import il.org.spartan.*;

public final class EnvironmentTestSpawningAndManualAddition {
  Environment e0 = Environment.genesis();
  Environment e1 = e0.spawn();
  Environment ee0 = Environment.genesis();
  Environment ee1 = ee0.spawn();

  @Test public void defaultDoesntHave() {
    azzert.that(e0.nest().doesntHave("Alex"), is(true));
  }
  @Test public void defaultempty() {
    azzert.that(e0.nest().empty(), is(true));
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
    azzert.that(e0.nest().has("Alex"), is(false));
  }
  @Test public void defaultName() {
    azzert.that(e0.name(), is(""));
  }
  @Test public void defaultSize() {
    azzert.that(e0.size(), is(0));
    azzert.that(e0.fullSize(), is(0));
  }
  @Test public void DoesntHaveFalseResult() {
    azzert.that(e1.nest().doesntHave("Yossi"), is(false));
  }
  @Test public void empty() {
    e0.put("Alex", new Information());
    azzert.that(e0.empty(), is(false));
  }
  @Test public void emptyOne() {
    azzert.that(e1.empty(), is(false));
  }
  @Test public void emptyTestBothEmpty() {
    azzert.that(ee1.empty(), is(true));
  }
  @Test public void emptyTestFlatEmptyNestNot() {
    ee0.put("Alex", new Information());
    azzert.that(ee1.empty(), is(false));
  }
  @Test public void emptyTestNeitherEmpty() {
    ee0.put("Yossi", new Information());
    ee1.put("Gill", new Information());
    azzert.that(ee1.empty(), is(false));
  }
  @Test public void emptyTestNestEmptyFlatNot() {
    ee1.put("Dan", new Information());
    azzert.that(ee1.empty(), is(false));
  }
  @Test public void get() {
    e0.put("Alex", new Information());
    assert e0.get("Alex") != null;
  }
  @Test public void getFromParent() {
    assert e1.get("Alex") != null;
    assert e1.get("Alex").blockScope == null;
  }
  @Test public void getOne() {
    assert e1.get("Kopzon") != null;
    assert e1.get("Kopzon").blockScope == null;
  }
  @Test public void has() {
    e0.put("Alex", new Information());
    azzert.that(e0.has("Alex"), is(true));
  }
  @Test public void hasInBoth() {
    e1.put("Yossi", new Information());
    azzert.that(e1.has("Yossi"), is(true));
  }
  @Test public void hasInParent() {
    azzert.that(e1.has("Dan"), is(true));
  }
  @Test public void hasNowhere() {
    azzert.that(e1.has("Onoes"), is(false));
  }
  @Test public void hasOne() {
    azzert.that(e1.has("Kopzon"), is(true));
    azzert.that(e1.has("Dan"), is(true));
    azzert.that(e1.has("Yossi"), is(true));
    azzert.that(e1.has("Alex"), is(true));
  }
  @Test public void hidingOne() {
    assert e1.hiding("Alex") != null;
  }
  @Before public void init_one_level() {
    e0.put("Alex", new Information());
    e0.put("Dan", new Information());
    e0.put("Yossi", new Information());
    e1.put("Kopzon", new Information());
    e1.put("Greenstein", new Information());
    e1.put("Gill", new Information());
  }
  @Test public void names() {
    e0.put("Alex", new Information());
    azzert.that(e0.names().contains("Alex"), is(true));
  }
  @Test public void namesOne() {
    azzert.that(e1.names().contains("Kopzon"), is(true));
    azzert.that(e1.names().contains("Alex"), is(false));
  }
  @Test public void Nest() {
    azzert.that(e0.nest(), is(EMPTY));
  }
  @Test public void NestOne() {
    azzert.that(e1.nest(), is(e0));
  }
  @Test public void put() {
    assert e0.put("Alex", new Information()) == null;
  }
  @Test public void putOne() {
    assert e1.put("Kopzon1", new Information()) == null;
  }
  @Test public void putOneAndHide() {
    assert e1.put("Alex", new Information()) != null;
  }
  @Test(expected = IllegalArgumentException.class) public void putTest() {
    e0.nest().put("Dan", new Information());
  }
  // ==================================declaresDown Tests================
}