package fluent.ly;

import java.util.function.*;

import org.junit.*;

import fluent.ly.nil.*;

public class TestFluent_nil {
  static String helloString = "Hello";

  @Test public void t1() {
    nil.g(State::getName).on(Address::getState).on(Customer::getAddress).on(californiaCustomer);
  }
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
    U<String, State> g = nil.g(State::getName);
    Function<Address, State> f2 = Address::getState;
    U<String, Address> g1 = g.on(f2);
    U<String, Customer> g2 = g1.on(Customer::getAddress);
    g2.on(californiaCustomer);
    // Operand<String> operand =
    // nil.guardingly(californiaCustomer).to(Customer::getAddress).to(Address::getState).to(State::getName);
    // String s = operand.get();
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