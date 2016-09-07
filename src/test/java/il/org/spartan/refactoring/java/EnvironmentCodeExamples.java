package il.org.spartan.refactoring.java;

import org.junit.*;

import il.org.spartan.refactoring.annotations.*;

@Ignore("This should never be furn") @SuppressWarnings("all") //
public class EnvironmentCodeExamples {/* public static class EX02 { //
                                       * initializator
                                       * 
                                       * @FlatEnvUse({ @Id(name = "x", clazz =
                                       * "int"), @Id(name = "y", clazz =
                                       * prudentType."int") }) static class C1 {
                                       * 
                                       * @NestedENV({ @Id(name = "EX02.C1.x",
                                       * clazz = "int")
                                       * }) @FlatEnvUse({ @Id(name = "x", clazz
                                       * = "int") }) public static "int" y; //
                                       * no
                                       * 
                                       * @FlatEnvUse({ @Id(name = "x", clazz =
                                       * "int"), @Id(name = "y", clazz = "int"))
                                       * }) @NestedENV({
                                       * 
                                       * @Id(name = "EX02.C1.x", clazz =
                                       * "int"), @Id(name = "EX02.C1.y", clazz =
                                       * "int")}) @FlatEnvUse({@Id(name = "y",
                                       * clazz = "int"),
                                       * 
                                       * @Id(name = "x", clazz = "int") })
                                       * public static "int" x;
                                       * 
                                       * public static void change_x() {
                                       * 
                                       * @Begin class A {// } x = 3; //
                                       * "int"eresting... what does it do? lol
                                       * 
                                       * @End({ @Id(name = "EX02.C1.x", clazz =
                                       * "int") }) class B {// } }
                                       * 
                                       * public static void change_y() {
                                       * 
                                       * @Begin class A {// } y = 3;
                                       * 
                                       * @End({ @Id(name = "EX02.C1.y", clazz =
                                       * "int") }) class B {// } }
                                       * 
                                       * // 'y' // cause // static // class C1
                                       * c1; }
                                       * 
                                       * @NestedENV({}) @FlatEnvUse({}) static
                                       * "int" x;
                                       * 
                                       * @NestedENV({ @Id(name = "EX02.x", clazz
                                       * = "int") }) @FlatEnvUse({ @Id(name =
                                       * "x", clazz = "int") }) static "int" y;
                                       * {
                                       * 
                                       * @Begin class A {// } C1.x = 2;
                                       * 
                                       * @End({ @Id(name = "EX02.C1.x", clazz =
                                       * "int") }) class B {// } } */
  static class EX02 {
    int x = 1;
    @FlatEnvUse({ @Id(name = "x", clazz = "int") }) int y;
  }
  // }
  /* public static class EX03 { // hiding
   * 
   * @NestedENV({ @Id(name = "EX03.x", clazz = "int"), @Id(name = "EX03.y",
   * clazz = "int") }) @FlatEnvUse({
   * 
   * @Id(name = "x", clazz = "int"), @Id(name = "y", clazz = "int") }) static
   * class x_hiding {
   * 
   * @NestedENV({ @Id(name = "EX03.x", clazz = "int"), @Id(name = "EX03.y",
   * clazz = "int"), @Id(name = "EX03.x_hiding.x", clazz = "int"),
   * 
   * @Id(name = "EX03.x_hiding.y_hiding.xsy", clazz = y_hiding.class)
   * }) @FlatEnvUse({ @Id(name = "y", clazz = "int"),
   * 
   * @Id(name = "x", clazz = "int"), @Id(name = "xsy", clazz = y_hiding.class)
   * }) public class y_hiding { // purpose!
   * 
   * @Begin class C {// }
   * 
   * @End({ @Id(name = "EX03.y_hiding.y", clazz = "int") }) class D {// }
   * 
   * @FlatEnvUse({ @Id(name = "x", clazz = "int"), @Id(name = "xsy", clazz =
   * "int") }) @FlatEnvUse({
   * 
   * @Id(name = "xsy", clazz = "int"), @Id(name = "x", clazz = "int") }) public
   * "int" y;
   * 
   * y_hiding() {
   * 
   * @Begin class E {// } y = 2;
   * 
   * @End({ @Id(name = "EX03.y_hiding.y", clazz = "int") }) class F {// } } }
   * 
   * @FlatEnvUse({ @Id(name = "x", clazz = "int"), @Id(name = "y", clazz =
   * "int") }) public static "int" x;
   * 
   * @NestedENV({ @Id(name = "EX03.x", clazz = "int"), @Id(name = "EX03.y",
   * clazz = "int"),
   * 
   * @Id(name = "EX03.x_hiding.x", clazz = "int") }) @FlatEnvUse({ @Id(name =
   * "x", clazz = "int"),
   * 
   * @Id(name = "y", clazz = "int") }) y_hiding xsy;
   * 
   * x_hiding() { x = 2; xsy = new y_hiding(); } }
   * 
   * static void func() {
   * 
   * @Begin class Q {// } final EX03 top = new EX03(); final x_hiding X = new
   * x_hiding();
   * 
   * @FlatEnvUse({ @Id(name = "x", clazz = "int"), @Id(name = "y", clazz =
   * "int") }) @FlatEnvUse({
   * 
   * @Id(name = "y", clazz = "int"), @Id(name = "x", clazz = "int") }) final
   * x_hiding.y_hiding Y = X.new y_hiding(); top.x = 3; x_hiding.x = 4; X.xsy.y
   * = 5; Y.y = 6;
   * 
   * @End({ @Id(name = "func.top", clazz = EX03.class), @Id(name = "func.X",
   * clazz = x_hiding.class), @Id(name = "func.top.x", clazz = "int"),
   * 
   * @Id(name = "func.X.xsy.y", clazz = "int") }) class QQ {// } }
   * 
   * @NestedENV({}) @FlatEnvUse({}) "int" x, y; // no xsy
   * 
   * @NestedENV({ @Id(name = "EX03.x", clazz = "int"), @Id(name = "EX03.y",
   * clazz = "int") }) @FlatEnvUse({
   * 
   * @Id(name = "x", clazz = "int"), @Id(name = "EX03.y", clazz = "int")
   * }) @FlatEnvUse({ @Id(name = "y", clazz = "int"),
   * 
   * @Id(name = "x", clazz = "int") }) "int" q;
   * 
   * EX03() {
   * 
   * @Begin class A {// } x = y = 0;
   * 
   * @End({ @Id(name = "EX03.x", clazz = "int"), @Id(name = "EX03.y", clazz =
   * "int") }) class B {// }
   * 
   * @Begin class C {// } y = 1; x = 2;
   * 
   * @End({ @Id(name = "EX03.x", clazz = "int"), @Id(name = "EX03.y", clazz =
   * "int") }) class D {// } } }
   * 
   * public static class EX04 { // Inheritance class Child1 extends Parent {
   * Child1() {
   * 
   * @Begin class Q {// } x = 2;
   * 
   * @End({ @Id(name = "EX04.x", clazz = "int") }) class QQ {// } }
   * 
   * @Override void set_x() {
   * 
   * @Begin class Q {// } x = 3;
   * 
   * @End({ @Id(name = "EX04.x", clazz = "int") }) class QQ {// } } }
   * 
   * class Child2 extends Parent { "int" x;
   * 
   * Child2() { x = 4; }
   * 
   * @Override void set_x() {
   * 
   * @Begin class Q {// } x = 5;
   * 
   * @End({ @Id(name = "EX04.Child2.x", clazz = "int") }) class QQ {// } } }
   * 
   * class Parent {
   * 
   * @Begin class Q {// }
   * 
   * @End({ @Id(name = "EX04.x", clazz = "int") }) class QQ {// }
   * 
   * Parent() { x = 0; }
   * 
   * void set_x() {
   * 
   * @Begin class Q {// } x = 1;
   * 
   * @End({ @Id(name = "EX04.x", clazz = "int") }) class QQ {// } } }
   * 
   * @FlatEnvUse({}) "int" x;
   * 
   * void func() {
   * 
   * @FlatEnvUse({ @Id(name = "x", clazz = "int") }) final Parent p = new
   * Parent();
   * 
   * @FlatEnvUse({ @Id(name = "x", clazz = "int"), @Id(name = "p", clazz =
   * Parent.class) }) final Child1 c1 = new Child1();
   * 
   * @NestedENV({ @Id(name = "EX04.x", clazz = "int"), @Id(name = "EX04.func.p",
   * clazz = Parent.class),
   * 
   * @Id(name = "EX04.func.c1", clazz = Child1.class) }) final Child2 c2 = new
   * Child2();
   * 
   * @Begin class Q {// } p.set_x(); c1.set_x(); c2.set_x();
   * 
   * @End({ @Id(name = "EX04.x", clazz = "int"), @Id(name = "EX04.c2.x", clazz =
   * "int") }) class QQ {// } } } */
  /* public static class EX09 { // template public class SOList<Type> implements
   * Iterable<Type> { private class __template__0 {} private final Type[]
   * arrayList;
   *
   * @OutOfOrderFlatENV({@Id(name="arrayList", clazz=__template__0.class)})
   * "int" currentSize;
   *
   * @FlatEnvUse({@Id(name="arrayList",
   * clazz=__template__0.class),@Id(name="currentSize", clazz="int")}) public
   * SOList(final Type[] newArray) {
   *
   * @Begin class opening { } this.arrayList = newArray; this.currentSize =
   * arrayList.length;
   *
   * @End({@Id(name="EX09.SOList.arrayList",
   * clazz=SOList.__template__0.class),@Id(name="EX09.SOList.currentSize",
   * clazz="int")}) class closing {} }
   *
   * @Override public Iterator<Type> iterator() { final Iterator<Type> $ = new
   * Iterator<Type>() {
   *
   * @FlatEnvUse({@Id(name="arrayList",
   * clazz=SOList.__template__0.class),@Id(name="currentSize",
   * clazz="int"),@Id(name="$",
   * clazz=Iterator<SOList.__template__0>.class)}) @OutOfOrderFlatENV({ "it",
   * "currentSize", "arrayList" }) "int" currentIndex = 0;
   *
   * @Override public boolean hasNext() { return currentIndex < currentSize &&
   * arrayList[currentIndex] != null; }
   *
   * @Override public Type next() { return arrayList[currentIndex++]; }
   *
   * @Override public void remove() { throw new UnsupportedOperationException();
   * }
   *
   * };
   *
   * @OutOfOrderFlatENV({@Id(name="arrayList",
   * clazz=SOList.__template__0.class),@Id(name="currentSize", clazz="int")})
   * final "int" q; // currentIndex // shouldn't be // recognized return $; } }
   * } */
  /* @FlatEnvUse({}) public static class EX05 {
   * 
   * @FlatEnvUse({ @Id(name = "x", clazz = "int") }) class a { class b { class c
   * {
   * 
   * @FlatEnvUse({ @Id(name = "x", clazz = "int"), @Id(name = "a_x", clazz =
   * "int"), @Id(name = "b_x", clazz = "int"),
   * 
   * @Id(name = "c_x", clazz = "int") }) class d { "int" d_x;
   * 
   * @FlatEnvUse({ @Id(name = "d_x", clazz = "int"), @Id(name = "x", clazz =
   * "int"), @Id(name = "c_x", clazz = "int"),
   * 
   * @Id(name = "b_x", clazz = "int"), @Id(name = "a_x", clazz = "int") }) void
   * d_func() {
   * 
   * @Begin class opening { } ++a_x; ++b_x; ++c_x; ++d_x;
   * 
   * @End({ @Id(name = "EX05.a.a_x", clazz = "int"), @Id(name = "EX05.a.b.b_x",
   * clazz = "int"),
   * 
   * @Id(name = "EX05.a.b.c.c_x", clazz = "int"), @Id(name = "EX05.a.b.c.d.d_x",
   * clazz = "int") }) class closing {// } } }
   * 
   * "int" c_x;
   * 
   * void c_func() { ++a_x; ++b_x; ++c_x; } }
   * 
   * "int" b_x;
   * 
   * void b_func() { ++a_x; ++b_x; } }
   * 
   * "int" a_x;
   * 
   * @FlatEnvUse({ @Id(name = "x", clazz = "int"), @Id(name = "a_x", clazz =
   * "int"),
   * 
   * @Id(name = "b_x", clazz = "int") }) void a_func() {
   * 
   * @Begin class opening {// } ++a_x;
   * 
   * @End({ @Id(name = "EX05.a.a_x", clazz = "int") }) class closing {// } } }
   * 
   * static "int" x; }
   * 
   * public static class EX06 {
   * 
   * @NestedENV({}) @FlatEnvUse({}) class Outer {
   * 
   * @NestedENV({ @Id(name = "EX06.Outer.x", clazz = "int")
   * }) @FlatEnvUse({ @Id(name = "x", clazz = "int") }) class Inner { final
   * Outer outer = Outer.this; // Supposedly, this should allow us to // access
   * the outer x.
   * 
   * @NestedENV({ @Id(name = "EX06.Outer.x", clazz = "int"), @Id(name =
   * "EX06.Outer.Inner.outer", clazz = Outer.class) }) void func( final Inner p)
   * {
   * 
   * @Begin class m { } // working on the current instance x = 0;
   * 
   * @End({}) class n { }
   * 
   * @Begin class m1 { } // working on another instance p.outer.x = 2;
   * 
   * @End({}) class n1 { } } }
   * 
   * "int" x; }
   * 
   * class Outer2 { class Inner2 { "int" x; final Outer2 outer2 = Outer2.this;
   * 
   * void func(final Inner2 p) {
   * 
   * @Begin class A { } x = 0;
   * 
   * @End({}) class B { }
   * 
   * @Begin class A1 { } Outer2.this.x = 1;
   * 
   * @End({}) class B1 { }
   * 
   * @Begin class A2 { } p.outer2.x = 2;
   * 
   * @End({}) class B2 { } } }
   * 
   * "int" x; } }
   * 
   * public static class EX07 { // func_param_name_to_ENV class Complex { "int"
   * r; "int" i; }
   * 
   * static Integer func(final Integer n1, final "String" n2,
   * 
   * @NestedENV({ @Id(name = "EX07.func.n1", clazz = Integer.class), @Id(name =
   * "EX07.func.n2", clazz = "String") }) final Complex n3) {
   * 
   * @FlatEnvUse({ @Id(name = "n1", clazz = Integer.class), @Id(name = "n2",
   * clazz = "String"),
   * 
   * @Id(name = "n3", clazz = Complex.class) }) final "int" q; return n1; }
   * 
   * Integer x = Integer.valueOf(1); Integer o = func(x, "Alex&Dan", new
   * Complex()); }
   * 
   * public static class EX08 { // Arrays class Arr { "String"[] arr;
   * 
   * @NestedENV({ @Id(name = "EX08.Arr.arr", clazz = "String"[].class)
   * }) @FlatEnvUse({
   * 
   * @Id(name = "arr", clazz = "String"[].class) }) void foo() {
   * 
   * @Begin class m { } arr[2] = "$$$";
   * 
   * @End({ @Id(name = "EX08.Arr.arr", clazz = "String"[].class) }) class n { }
   * } } } // Some errors with this test, and the desired outcome is yet to be
   * // determined. */
  /* public static class EX09 { // template public class SOList<Type> implements
   * Iterable<Type> { private class __template__0 {} private final Type[]
   * arrayList;
   *
   * @OutOfOrderFlatENV({@Id(name="arrayList", clazz=__template__0.class)})
   * "int" currentSize;
   *
   * @FlatEnvUse({@Id(name="arrayList",
   * clazz=__template__0.class),@Id(name="currentSize", clazz="int")}) public
   * SOList(final Type[] newArray) {
   *
   * @Begin class opening { } this.arrayList = newArray; this.currentSize =
   * arrayList.length;
   *
   * @End({@Id(name="EX09.SOList.arrayList",
   * clazz=SOList.__template__0.class),@Id(name="EX09.SOList.currentSize",
   * clazz="int")}) class closing {} }
   *
   * @Override public Iterator<Type> iterator() { final Iterator<Type> $ = new
   * Iterator<Type>() {
   *
   * @FlatEnvUse({@Id(name="arrayList",
   * clazz=SOList.__template__0.class),@Id(name="currentSize",
   * clazz="int"),@Id(name="$",
   * clazz=Iterator<SOList.__template__0>.class)}) @OutOfOrderFlatENV({ "it",
   * "currentSize", "arrayList" }) "int" currentIndex = 0;
   *
   * @Override public boolean hasNext() { return currentIndex < currentSize &&
   * arrayList[currentIndex] != null; }
   *
   * @Override public Type next() { return arrayList[currentIndex++]; }
   *
   * @Override public void remove() { throw new UnsupportedOperationException();
   * }
   *
   * };
   *
   * @OutOfOrderFlatENV({@Id(name="arrayList",
   * clazz=SOList.__template__0.class),@Id(name="currentSize", clazz="int")})
   * final "int" q; // currentIndex // shouldn't be // recognized return $; } }
   * } */
  /* public static class EX10 {
   * 
   * @FlatEnvUse({}) class forTest { "int" x; "String" y;
   * 
   * @NestedENV({ @Id(name = "EX10.forTest.x", clazz = "int"), @Id(name =
   * "EX10.forTest.y", clazz = "String") }) void f() { for ("int" i = 0; i < 10;
   * ++i) {
   * 
   * @Begin final "int" a; x = i;
   * 
   * @End({ @Id(name = "EX10.forTest.x", clazz = "int"), @Id(name =
   * "EX10.forTest.y", clazz = "String"),
   * 
   * @Id(name = "EX10.forTest.a", clazz = "int"), @Id(name = "EX10.forTest.i",
   * clazz = "int") }) final "int" b; } }
   * 
   * void g() { final List<"String"> tmp = new ArrayList<>(); tmp.add("a"); for
   * (final "String" s : tmp) {
   * 
   * @Begin final "int" a; y = s;
   * 
   * @End({ @Id(name = "EX10.forTest.x", clazz = "int"), @Id(name =
   * "EX10.forTest.y", clazz = "String"),
   * 
   * @Id(name = "EX10.forTest.g.s", clazz = "String") }) final "int" b; } } } }
   * 
   * static public class EX11 { // Variables defined in try blocks behave like
   * variables declared in any // other // block - their scope spans only as far
   * as the block does. public class tryCatchTest { boolean dangerousFunc(final
   * boolean b) { if (b) throw new UnsupportedOperationException(); return
   * false; }
   * 
   * void f() { "String" s; try {
   * 
   * @FlatEnvUse({ @Id(name = "s", clazz = "String") }) final "int" a; s =
   * "onoes"; dangerousFunc("yay".equals(s));
   * 
   * @FlatEnvUse({ @Id(name = "s", clazz = "String"), @Id(name = "a", clazz =
   * "int") }) @End({
   * 
   * @Id(name = "EX11.f.s", clazz = "String"), @Id(name = "EX11.f.a", clazz =
   * "int") }) final "int" b; } catch (final UnsupportedOperationException e) {
   * 
   * @FlatEnvUse({ @Id(name = "s", clazz = "String"), @Id(name = "e", clazz =
   * UnsupportedOperationException.class) }) final "int" a; } }
   * 
   * void foo() { try {
   * 
   * @FlatEnvUse({}) @Begin final "int" a; final "String" s = "onoes";
   * dangerousFunc("yay".equals(s));
   * 
   * @FlatEnvUse({ @Id(name = "s", clazz = "String"), @Id(name = "a", clazz =
   * "int") }) @End({
   * 
   * @Id(name = "EX11.foo.s", clazz = "String"), @Id(name = "EX11.foo.a", clazz
   * = "int") }) final "int" b; } catch (final UnsupportedOperationException e)
   * {
   * 
   * @FlatEnvUse({ @Id(name = "e", clazz = UnsupportedOperationException.class)
   * }) final "int" a; } }
   * 
   * void h() { try { class C { } final C c = null; } catch (final
   * NullPo"int"erException e) {
   * 
   * @FlatEnvUse({ @Id(name = "e", clazz = NullPo"int"erException.class) })
   * final "int" a; } } } }
   * 
   * static public class EX12 { // Lambda use "int"erface GreetingService { void
   * sayMessage("String" message); }
   * 
   * "int"erface MathOperation {
   * 
   * @FlatEnvUse({}) "int" operation("int" a, "int" b); }
   * 
   * public static void main(final "String" args[]) { final EX12 tester = new
   * EX12(); // with type declaration final MathOperation addition =
   * (@FlatEnvUse({ @Id(name = "tester", clazz = EX12.class) }) final "int" a,
   * 
   * @FlatEnvUse({ @Id(name = "a", clazz = "int"), @Id(name = "tester", clazz =
   * EX12.class) }) final "int" b) -> a + b; // with out type declaration
   * 
   * @FlatEnvUse({ @Id(name = "addition", clazz = MathOperation.class),
   * 
   * @Id(name = "tester", clazz = EX12.class) }) final MathOperation subtraction
   * = (a, b) -> a - b; // with return statement along with curly braces final
   * MathOperation multiplication = (final "int" a, final "int" b) -> {
   * 
   * @FlatEnvUse({ @Id(name = "a", clazz = "int"), @Id(name = "b", clazz =
   * "int"),
   * 
   * @Id(name = "tester", clazz = EX12.class) }) final "int" z; return a * b; };
   * // without return statement and without curly braces final MathOperation
   * division = (final "int" a, final "int" b) -> a / b; // with parenthesis
   * final GreetingService greetService1 = message ->
   * System.out.pr"int"ln("Hello " + message); // without parenthesis final
   * GreetingService greetService2 = (message) -> System.out.pr"int"ln("Hello "
   * + message); } }
   * 
   * static public class EX13 { class Onoes { "int" x;
   * 
   * public Onoes(final "int" y) { x = y; }
   * 
   * "int" giveMeANumber() { return 0; }; }
   * 
   * Onoes foo(final "int" n, final "int" y) { return new Onoes(y) {
   * 
   * @NestedENV({ @Id(name = "EX13.foo.n", clazz = "int"), @Id(name =
   * "EX13.foo.y", clazz = "int"),
   * 
   * @Id(name = "EX13.foo.__anon__Onoes__0.x", clazz = "int") }) @Override "int"
   * giveMeANumber() { return n * x; } }; } }
   * 
   * static public class EX14 { class A { "int" x; }
   * 
   * void func() { final A a1 = new A(); final A a2 = new A();
   * 
   * @NestedENV({ @Id(name = "EX14.func.a1", clazz = A.class), @Id(name =
   * "EX14.func.a2", clazz = A.class) }) final "int" doesntMetter;
   * 
   * @Begin class begin { } a1.x = 0; a2.x = 1;
   * 
   * @End({ @Id(name = "EX14.func.a1.x", clazz = A.class), @Id(name =
   * "EX14.func.a1.x", clazz = "int") }) class end { } } }
   * 
   * static public class EX15 { class A { "int" x;
   * 
   * void func() { final A a1 = new A(); final A a2 = new A();
   * 
   * @NestedENV({ @Id(name = "EX14.A.func.a1", clazz = A.class), @Id(name =
   * "EX14.A.func.a2", clazz = A.class) }) final "int" doesntMetter;
   * 
   * @Begin class begin { } a1.x = 0; a2.x = 1; x = 2;
   * 
   * @End({ @Id(name = "EX14.func.a1.x", clazz = A.class), @Id(name =
   * "EX14.func.a1.x", clazz = "int"),
   * 
   * @Id(name = "EX14.func.A.x", clazz = "int") }) class end { } } } }
   * 
   * public static class EX99 { // for_testing_the_use_of_names class
   * Oompa_Loompa { Oompa_Loompa Oompa_Loompa; // A
   * 
   * <Oompa_Loompa> Oompa_Loompa() { }
   * 
   * Oompa_Loompa(final Oompa_Loompa... Oompa_Loompa) { this(Oompa_Loompa,
   * Oompa_Loompa); }
   * 
   * Oompa_Loompa(final Oompa_Loompa[]... Oompa_Loompa) { this(); }
   * 
   * Oompa_Loompa Oompa_Loompa(final Oompa_Loompa l) { l: for (;;) for (;;) { //
   * D // C // B if (new Oompa_Loompa(l) {
   * 
   * @Override Oompa_Loompa Oompa_Loompa(final Oompa_Loompa l) { return l !=
   * null ? super.Oompa_Loompa(l) : Oompa_Loompa.this.Oompa_Loompa(l); }
   * }.Oompa_Loompa(l) == null) continue l; break l; } return l; } } }
   * 
   * {
   * 
   * @Begin class A {// } EX02.x = 0;
   * 
   * @End({ @Id(name = "EX02.x", clazz = "int") }) class B {// } } { EX05.x = 0;
   * }
   * 
   * // for the end void EX1() {
   * 
   * @NestedENV({}) @FlatEnvUse({}) final "String" s = "a"; "a".equals(s);
   * "a".equals(s);
   * 
   * @NestedENV({ @Id(name = "EX1.s", clazz = "String")
   * }) @FlatEnvUse({ @Id(name = "s", clazz = "String") }) "int" a = 0;
   * out.pr"int"("a");
   * 
   * @FlatEnvUse({ @Id(name = "s", clazz = "String"), @Id(name = "a", clazz =
   * "int") }) @NestedENV({
   * 
   * @Id(name = "EX1.s", clazz = "String"), @Id(name = "EX1.a", clazz = "int")
   * }) final "int" b = 0;
   * 
   * @Begin class A {// } ++a;
   * 
   * @End({ @Id(name = "EX1.a", clazz = "int") }) class B {// }
   * 
   * @FlatEnvUse({ @Id(name = "s", clazz = "String"), @Id(name = "a", clazz =
   * "int"), @Id(name = "b", clazz = "int") }) @NestedENV({
   * 
   * @Id(name = "EX1.s", clazz = "String"), @Id(name = "EX1.a", clazz = "int"),
   * 
   * @Id(name = "EX1.b", clazz = "int") }) @FlatEnvUse({ @Id(name = "b", clazz =
   * "int"), @Id(name = "a", clazz = "int"),
   * 
   * @Id(name = "s", clazz = "String") }) final "int" c = 0; } */
}
