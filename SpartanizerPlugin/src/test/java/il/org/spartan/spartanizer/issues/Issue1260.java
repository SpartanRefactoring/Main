package il.org.spartan.spartanizer.issues;

import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.*;

import il.org.spartan.utils.*;

/** Tests for reducers
 * @author oran1248
 * @since 2017-04-20 */
@SuppressWarnings("static-method")
public class Issue1260 {
  @Test public void list() {
    ListMergingReducer<Integer> r = new ListMergingReducer<>();
    assertThat(IntStream.range(1, 31).boxed().collect(Collectors.toList()), is(r.reduce(IntStream.range(1, 11).boxed().collect(Collectors.toList()),
        IntStream.range(11, 21).boxed().collect(Collectors.toList()), IntStream.range(21, 31).boxed().collect(Collectors.toList()))));
    assertEquals(new LinkedList<Integer>(), r.reduce());
    List<Integer> l = new LinkedList<>();
    assertEquals(l, r.reduce(l));
  }

  @Test @SuppressWarnings("boxing") public void integer() {
    IntegerAdditionReducer r = new IntegerAdditionReducer();
    assertEquals(Box.box(0), r.reduce());
    assertEquals(Box.box(1), r.reduce(1));
    assertEquals(Box.box(3), r.reduce(1, 2));
    assertEquals(Box.box(6), r.reduce(1, 2, 3));
  }

  @Test @SuppressWarnings("boxing") public void firstNotNull() {
    FirstNotNullReducer<Integer> r = new FirstNotNullReducer<>();
    assertEquals(null, r.reduce());
    assertEquals(Box.box(1), r.reduce(1, null, 2, null));
    assertEquals(Box.box(2), r.reduce(null, null, 2, 3));
  }

  @Test @SuppressWarnings("boxing") public void booleanOr() {
    BooleanOrReducer r = new BooleanOrReducer();
    assertEquals(Box.box(false), r.reduce());
    assertEquals(Box.box(false), r.reduce(null, null));
    assertEquals(Box.box(true), r.reduce(null, false, true, false));
  }
}
