package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Environment.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Type;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;


public class EnvTest {
//  =================== default ===================
  Environment e_bad = EMPTY; //bad exercise.
  Environment e0 = Environment.genesis();
  
  @Test public void defaultSize() {
    azzert.that(e0.size(), is(0));
    azzert.that(e0.fullSize(), is(0));
  }
  @Test public void defaultDoesntHave() {
    azzert.that(e0.doesntHave("Alex"), is(null));
  }
  @Test public void defaultempty() {
    azzert.that(e0.empty(), is(true));
  }
  @Test public void defaultfullEntries() {
    azzert.that(e0.fullEntries(), is(null));
  }
  @Test public void defaultGet() {
    azzert.that(e0.get("Alex"), is(null));
  }
  @Test public void defaultHas() {
    azzert.that(e0.has("Alex"), is(false));
  }
  @Test public void defaultFullName() {
    azzert.that(e0.fullName(), is(null));
  }  
  @Test public void defaultName() {
    azzert.that(e0.name(), is(""));
  }
  @Test public void defaultFullNames() {
    azzert.that(e0.fullNames(), is(null));
  }
  
//=================== basic ===================
  @Test public void Nest() {
    azzert.that(e0.nest(), is(EMPTY));
  }
  @Test public void put() {
    azzert.that(e0.put("Alex", new Information()), is(null));
    azzert.that(e0.put("Dan", new Information()), is(null));
    azzert.that(e0.put("Yossi", new Information()), is(null));
  }
  @Test public void get() {
    azzert.that(e0.get("Alex").blockScope, is(null));
    azzert.that(e0.get("Alex").hiding, is(null));
    azzert.that(e0.get("Alex").type, is(null));
    azzert.that(e0.get("Alex").self, is(null));
  }
  @Test public void has() {
    azzert.that(e0.has("Alex"), is(true));
    azzert.that(e0.has("Dan"), is(true));
    azzert.that(e0.has("Yossi"), is(true));
  }
  @Test public void names() {
    azzert.that(e0.names().contains("Alex"), is(true));
  }
  @Test public void empty() {
    azzert.that(e0.empty(), is(false));
  }
  
//=================== nesting one level ===================
  
  Environment e1 = e0.spawn();
  
  @Test public void NestOne() {
    azzert.that(e1.nest(), is(e0));
  }
  @Test public void putOne() {
    azzert.that(e1.put("Kopzon", new Information()), is(null));
    azzert.that(e1.put("Greenstien", new Information()), is(null));
    azzert.that(e1.put("Gill", new Information()), is(null));
    // not returning null, but Information about hiding!!!
    azzert.that(e1.put("Alex", new Information()).blockScope, is(null));
  }
  @Ignore public void getOne() {
    azzert.that(e1.get("Alex").blockScope, is(null));
    azzert.that(e1.get("Alex").hiding, is(null));
    azzert.that(e1.get("Alex").type, is(null));
    azzert.that(e1.get("Alex").self, is(null));
  }
  @Ignore public void hasOne() {
    azzert.that(e1.has("Alex"), is(true));
    azzert.that(e1.has("Dan"), is(true));
    azzert.that(e1.has("Yossi"), is(true));
  }
  @Ignore public void namesOne() {
    azzert.that(e1.names().contains("Alex"), is(true));
  }
  @Ignore public void emptyOne() {
    azzert.that(e1.empty(), is(false));
  }
  
//=================== nesting complex ===================


}
