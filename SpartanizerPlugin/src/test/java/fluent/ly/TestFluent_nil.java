package fluent.ly;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.iteration.closures.*;

public class TestFluent_nil {
  static String helloString = "Hello";

  @Test public void t() {
    final Integer i1 = nil.guardingly(String::length).on(helloString);
    azzert.that(i1, azzert.is(5));
    final Integer i2 = nil.guardingly(String::length).on(nullString);
    azzert.that(i1, azzert.is(5));
    azzert.isNull(i2);
    final Integer i3 = nil.guardingly(f).on(nullString);
    azzert.that(i1, azzert.is(5));
    azzert.isNull(i2);
    azzert.isNull(i3);
    nil.guardingly(State::getName).on(null);
    nil.guardingly(State::getName).on(californiaCustomer.getAddress().getState());
  }

  String nullString;
  final Customer californiaCustomer = () -> () -> new State() {
    @Override public String getName() {
      return "California";
    }
  };
  final Function<String, Integer> f = String::length;
  final Customer nullAddressCustomer = () -> null;
  final Customer nullNameCustomer = () -> () -> new State() {/***/
  };
  final Customer nullStateCustomer = () -> () -> null;
  final Customer nullStateCustomer1 = () -> () -> null;

  //@formatter:off
  interface Address { State getState(); }
  interface Customer { Address getAddress(); }
  interface State {default String getName() { return null; } }
}