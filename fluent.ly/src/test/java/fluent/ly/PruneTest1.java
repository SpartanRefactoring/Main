package fluent.ly;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

/** A JUnit test class for the enclosing class.
 * @author Yossi Gil, the Technion.
 * @since 27/08/2008 */
@SuppressWarnings({ "static-method" }) //
public class PruneTest1 {
  final String[] alternatingArray = new String[] { null, "A", null, null, "B", null, null, null, "C", null };
  final String[] NonNullArray = { "1", "2", "4" };
  private final lazy<List<String>> sparseCollection = () -> as.list(null, null, null, null, null, "A", null, null, null, "B", null, "C", null, null,
      null, null, null);

  @Test public void nullsNonNullArrayLength() {
    assertEquals(NonNullArray.length, prune.nulls(NonNullArray).length);
  }
  @Test public void nullsNullArrayItems() {
    assertEquals("1", prune.nulls(NonNullArray)[0]);
    assertEquals("2", prune.nulls(NonNullArray)[1]);
    assertEquals("4", prune.nulls(NonNullArray)[2]);
  }
  @Test public void nullsPruneArrayAltenatingItems() {
    assertEquals("A", prune.nulls(alternatingArray)[0]);
    assertEquals("B", prune.nulls(alternatingArray)[1]);
    assertEquals("C", prune.nulls(alternatingArray)[2]);
  }
  @Test public void nullsPruneArrayAltenatingLength() {
    assertEquals(3, prune.nulls(alternatingArray).length);
  }
  @Test public void nullsPruneSparseCollectionContents() {
    final String[] a = prune.nils(sparseCollection.get()).toArray(new String[3]);
    assertEquals("A", a[0]);
    assertEquals("B", a[1]);
    assertEquals("C", a[2]);
    assertEquals(3, a.length);
  }
  @Test public void nullsPruneSparseCollectionLength() {
    assertEquals(3, prune.nils(sparseCollection.get()).size());
  }
  @Test public void nullsPrunNonNull() {
    assert prune.nils(sparseCollection.get()) != null;
  }
  @Test public void shrinkArray() {
    assertEquals(0, prune.shrink(new Object[10]).length);
  }
  @Test public void shrinkEmptyArray() {
    assertEquals(0, prune.shrink(new Object[0]).length);
  }
  @Test public void whitesEmptyArray() {
    assertEquals(0, prune.whites().length);
  }
  @Test public void whitesEmptyList() {
    assertEquals(0, prune.whites().length);
  }
}