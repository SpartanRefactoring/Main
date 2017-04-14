package il.org.spartan.utils.fluent;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.iteration.closures.*;

public class TestFluent_null¢ {
  static String helloString = "Hello";

  @Test public void t() {
    final Integer i1 = null¢.guardingly(String::length).on(helloString);
    azzert.that(i1, is(5));
    final Integer i2 = null¢.guardingly(String::length).on(nullString);
    azzert.that(i1, is(5));
    azzert.isNull(i2);
    final Integer i3 = null¢.guardingly(f).on(nullString);
    azzert.that(i1, is(5));
    azzert.isNull(i2);
    azzert.isNull(i3);
    
    null¢.guardingly(State::getName).on(null);

    californiaCustomer.getAddress().getState().getName();
    nullAddressCustomer.getAddress().getState();
    nullStateCustomer.getAddress().getState();
    nullNameCustomer.getAddress().getState();
  }

  String nullString;
  final Customer californiaCustomer = () -> () -> new State() {
    @Override public String getName() {
      return "California";
    }
  };
  final Function<String, Integer> f = String::length;
  final Customer nullAddressCustomer = new Customer() {
    @Override public Address getAddress() {
      return null;
    }
  };
  final Customer nullNameCustomer = () -> () -> new State() {/***/};
  final Customer nullStateCustomer = new Customer() {
    @Override public Address getAddress() {
      return new Address() {
        @Override public State getState() {
          return null;
        }
      };
    }
  };
  final Customer nullStateCustomer1 = new Customer() {
    @Override public Address getAddress() {
      return new Address() {
        @Override public State getState() {
          return null;
        }
      };
    }
  };

  //@formatter:off
  interface Address { State getState(); }
  interface Customer { Address getAddress(); }
  interface State {default String getName() { return null; } }
}