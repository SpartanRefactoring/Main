package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit test for the GitHub issue thus numbered. case of inlining into the
 * expression of an enhanced for
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-16 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore("Ori Roth: All tests fail")
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0293 {
  @Test public void t1() {
    trimmingOf("/* * This is a comment */ int i = 6; int j = 2; int k = i+2; S.x.f(i-j+k); ")
        .gives("/* * This is a comment */ int j = 2; int i = 6; int k = i+2; S.x.f(i-j+k); ");
  }

  @Test public void t2() {
    trimmingOf("/* * This is a comment */ int i = 6, h = 7; int j = 2; int k = i+2; S.x.f(i-j+k); ")
        .gives("/* * This is a comment */ int h = 7; int j = 2; int i = 6; int k = i+2; S.x.f(i-j+k); ");
  }

  @Test public void t3() {
    trimmingOf("/* * This is a comment */ int i = 6; int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m); y(i); y(i+m); ")
        .gives("/* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m); int i = 6; y(i); y(i+m); ");
  }

  @Test public void t4() {
    trimmingOf(
        " /* * This is a comment */ int i = 6; int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m); final C bc = new C(i); y(i+m+bc.j); private static class C { public C(int i) { j = 2*i; public final int j; ")
            .gives(
                " /* * This is a comment */ int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m); int i = 6; final C bc = new C(i); y(i+m+bc.j); private static class C { public C(int i) { j = 2*i; public final int j; ");
  }

  @Test public void t5() {
    trimmingOf("/* * This is a comment */ int i = y(0); int j = 3; int k = j+2; int m = k + j -19; y(m*2 - k/m + i); y(i+m); ")
        .gives("/* * This is a comment */ int j = 3; int k = j+2; int i = y(0); int m = k + j -19; y(m*2 - k/m + i); y(i+m); ");
  }

  @Test public void t6() {
    trimmingOf("/* * This is a comment */ int i = y(0); int h = 8; int j = 3; int k = j+2 + y(i); int m = k + j -19; y(m*2 - k/m + i); y(i+m); ")
        .gives("/* * This is a comment */ int h = 8; int i = y(0); int j = 3; int k = j+2 + y(i); int m = k + j -19; y(m*2 - k/m + i); y(i+m); ");
  }

  @Test public void t7() {
    trimmingOf(
        "public final int j; private C yada6() { final C res = new C(6); final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; S.x.f(res.j); return res; ")
            .gives(
                "public final int j; private C yada6() { final Runnable r = new Runnable() { @Override public void system() { res = new C(8); S.x.f(res.j); doStuff(res); private void doStuff(C res2) { S.x.f(res2.j); private C res; final C res = new C(6); S.x.f(res.j); return res; ");
  }
}
