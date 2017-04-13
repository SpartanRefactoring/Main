package il.org.spartan.utils.fluent;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.iteration.closures.*;

public class TestFluent_null¢ {
    static String a = "Hello";
    String b;
    final Function<String, Integer> f = String::length;
 
    @Test public void t() {
      final Integer i1 = null¢.guardingly(String::length).on(a);
      azzert.that(i1,is(5));
      final Integer i2 = null¢.guardingly(String::length).on(b);
      azzert.that(i1,is(5));
      azzert.isNull(i2);
      final Integer i3 = null¢.guardingly(f).on(b);
      azzert.that(i1,is(5));
      azzert.isNull(i2);
      azzert.isNull(i3);
      
    }

 }