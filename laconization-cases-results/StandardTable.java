/* Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. */
package com.google.common.collect;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Maps.safeContainsKey;
import static com.google.common.collect.Maps.safeGet;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps.IteratorBasedAbstractMap;
import com.google.common.collect.Maps.ViewCachingAbstractMap;
import com.google.common.collect.Sets.ImprovedAbstractSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.WeakOuter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

/** {@link Table} implementation backed by a map that associates row keys with
 * column key / value secondary maps. This class provides rapid access to
 * records by the row key alone or by both keys, but not by just the column key.
 * <p>
 * The views returned by {@link #column}, {@link #columnKeySet()}, and
 * {@link #columnMap()} have iterators that don't support {@code remove()}.
 * Otherwise, all optional operations are supported. Null row keys, columns
 * keys, and values are not supported.
 * <p>
 * Lookups by row key are often faster than lookups by column key, because the
 * data is stored in a {@code Map<R, Map<C, V>>}. A method call like {@code
 * column(columnKey).get(rowKey)} still runs quickly, since the row key is
 * provided. However, {@code column(columnKey).size()} takes longer, since an
 * iteration across all row keys occurs.
 * <p>
 * Note that this implementation is not synchronized. If multiple threads access
 * this table concurrently and one of the threads modifies the table, it must be
 * synchronized externally.
 * @author Jared Levy */
@GwtCompatible class StandardTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
  @GwtTransient final Map<R, Map<C, V>> backingMap;
  @GwtTransient final Supplier<? extends Map<C, V>> factory;

  StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
    this.backingMap = backingMap;
    this.factory = factory;
  }

  // Accessors
  @Override public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
    return rowKey != null && columnKey != null && super.contains(rowKey, columnKey);
  }

  @Override public boolean containsColumn(@Nullable Object columnKey) {
    if (columnKey == null)
      return false;
    for (Map<C, V> ¢ : backingMap.values())
      if (safeContainsKey(¢, columnKey))
        return true;
    return false;
  }

  @Override public boolean containsRow(@Nullable Object rowKey) {
    return rowKey != null && safeContainsKey(backingMap, rowKey);
  }

  @Override public boolean containsValue(@Nullable Object value) {
    return value != null && super.containsValue(value);
  }

  @Override public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
    return (rowKey == null || columnKey == null) ? null : super.get(rowKey, columnKey);
  }

  @Override public boolean isEmpty() {
    return backingMap.isEmpty();
  }

  @Override public int size() {
    int $ = 0;
    for (Map<C, V> ¢ : backingMap.values())
      $ += ¢.size();
    return $;
  }

  // Mutators
  @Override public void clear() {
    backingMap.clear();
  }

  private Map<C, V> getOrCreate(R rowKey) {
    Map<C, V> $ = backingMap.get(rowKey);
    if ($ == null) {
      $ = factory.get();
      backingMap.put(rowKey, $);
    }
    return $;
  }

  @CanIgnoreReturnValue @Override public V put(R rowKey, C columnKey, V value) {
    checkNotNull(rowKey);
    checkNotNull(columnKey);
    checkNotNull(value);
    return getOrCreate(rowKey).put(columnKey, value);
  }

  @CanIgnoreReturnValue @Override public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
    if ((rowKey == null) || (columnKey == null))
      return null;
    Map<C, V> map = safeGet(backingMap, rowKey);
    if (map == null)
      return null;
    V $ = map.remove(columnKey);
    if (map.isEmpty())
      backingMap.remove(rowKey);
    return $;
  }

  void r7(Map<R, V> $) {
    for (Iterator<Entry<R, V>> ¢ = $.entrySet().iterator(); ¢.hasNext();)
      if (¢.hashCode() > 12)
        return;
  }

  void r6(Map<R, V> $) {
    for (Iterator<Entry<R, V>> ¢ = $.entrySet().iterator(); ¢.hasNext();)
      if (¢.hashCode() > 12)
        return;
  }

  void r5() {
    for (Iterator<Entry<R, V>> ¢ = (new LinkedHashMap<R, V>()).entrySet().iterator(); ¢.hasNext();)
      if (¢.hashCode() > 12)
        return;
  }

  private int r4() {
    Map<R, V> $ = new LinkedHashMap<R, V>();
    for (Iterator<Entry<R, V>> ¢ = $.entrySet().iterator(); ¢.hasNext();)
      if (¢.next().hashCode() == 2)
        break;
    return $.hashCode();
  }

  private int r3(Object column) {
    Map<R, V> $ = new LinkedHashMap<R, V>();
    for (Iterator<Entry<R, V>> ¢ = $.entrySet().iterator(); ¢.hasNext();)
      if (¢.next().getValue().remove(column) != null)
        break;
    return $.hashCode();
  }

  private int r2(Object column) {
    Map<R, V> $ = new LinkedHashMap<R, V>();
    for (Iterator<Entry<R, V>> iterator = $.entrySet().iterator(); iterator.hasNext();) {
      Entry<R, Map<C, V>> entry = iterator.next();
      V value = entry.getValue().remove(column);
      if (value != null) {
        $.put(entry.getKey(), value);
        if (entry.getValue().isEmpty())
          iterator.remove();
      }
    }
    return $.hashCode();
  }

  @CanIgnoreReturnValue private Map<R, V> r1(Object column) {
    Map<R, V> $ = new LinkedHashMap<R, V>();
    // XXX: spartanized
    for (Iterator<Entry<R, Map<C, V>>> iterator = backingMap.entrySet().iterator(); iterator.hasNext();) {
      Entry<R, Map<C, V>> entry = iterator.next();
      V value = entry.getValue().remove(column);
      if (value != null) {
        $.put(entry.getKey(), value);
        if (entry.getValue().isEmpty())
          iterator.remove();
      }
    }
    return $;
  }

  @CanIgnoreReturnValue private Map<R, V> removeColumn(Object column) {
    Map<R, V> $ = new LinkedHashMap<R, V>();
    // XXX: spartanized
    for (Iterator<Entry<R, Map<C, V>>> iterator = backingMap.entrySet().iterator(); iterator.hasNext();) {
      Entry<R, Map<C, V>> entry = iterator.next();
      V value = entry.getValue().remove(column);
      if (value != null) {
        $.put(entry.getKey(), value);
        if (entry.getValue().isEmpty())
          iterator.remove();
      }
    }
    return $;
  }

  private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
    return value != null && value.equals(get(rowKey, columnKey));
  }

  /** Remove a row key / column key / value mapping, if present. */
  private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
    if (!containsMapping(rowKey, columnKey, value))
      return false;
    remove(rowKey, columnKey);
    return true;
  }

  // Views
  /** Abstract set whose {@code isEmpty()} returns whether the table is empty
   * and whose {@code clear()} clears all table mappings. */
  @WeakOuter private abstract class TableSet<T> extends ImprovedAbstractSet<T> {
    @Override public boolean isEmpty() {
      return backingMap.isEmpty();
    }

    @Override public void clear() {
      backingMap.clear();
    }
  }

  /** {@inheritDoc}
   * <p>
   * The set's iterator traverses the mappings for the first row, the mappings
   * for the second row, and so on.
   * <p>
   * Each cell is an immutable snapshot of a row key / column key / value
   * mapping, taken at the time the cell is returned by a method call to the set
   * or its iterator. */
  @Override public Set<Cell<R, C, V>> cellSet() {
    return super.cellSet();
  }

  @Override Iterator<Cell<R, C, V>> cellIterator() {
    return new CellIterator();
  }

  private class CellIterator implements Iterator<Cell<R, C, V>> {
    final Iterator<Entry<R, Map<C, V>>> rowIterator = backingMap.entrySet().iterator();
    Entry<R, Map<C, V>> rowEntry;
    Iterator<Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();

    @Override public boolean hasNext() {
      return rowIterator.hasNext() || columnIterator.hasNext();
    }

    @Override public Cell<R, C, V> next() {
      if (!columnIterator.hasNext()) {
        rowEntry = rowIterator.next();
        columnIterator = rowEntry.getValue().entrySet().iterator();
      }
      Entry<C, V> columnEntry = columnIterator.next();
      return Tables.immutableCell(rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
    }

    @Override public void remove() {
      columnIterator.remove();
      if (rowEntry.getValue().isEmpty())
        rowIterator.remove();
    }
  }

  @Override public Map<C, V> row(R rowKey) {
    return new Row(rowKey);
  }

  class Row extends IteratorBasedAbstractMap<C, V> {
    final R rowKey;

    Row(R rowKey) {
      this.rowKey = checkNotNull(rowKey);
    }

    Map<C, V> backingRowMap;

    Map<C, V> backingRowMap() {
      return backingRowMap != null && (!backingRowMap.isEmpty() || !backingMap.containsKey(rowKey)) ? backingRowMap
          : (backingRowMap = computeBackingRowMap());
    }

    Map<C, V> computeBackingRowMap() {
      return backingMap.get(rowKey);
    }

    // Call this every time we perform a removal.
    void maintainEmptyInvariant() {
      if (backingRowMap() == null || !backingRowMap.isEmpty())
        return;
      backingMap.remove(rowKey);
      backingRowMap = null;
    }

    @Override public boolean containsKey(Object key) {
      Map<C, V> backingRowMap = backingRowMap();
      return (key != null && backingRowMap != null) && Maps.safeContainsKey(backingRowMap, key);
    }

    @Override public V get(Object key) {
      Map<C, V> backingRowMap = backingRowMap();
      return key == null || backingRowMap == null ? null : Maps.safeGet(backingRowMap, key);
    }

    @Override public V put(C key, V value) {
      checkNotNull(key);
      checkNotNull(value);
      return backingRowMap != null && !backingRowMap.isEmpty() ? backingRowMap.put(key, value) : StandardTable.this.put(rowKey, key, value);
    }

    @Override public V remove(Object key) {
      Map<C, V> backingRowMap = backingRowMap();
      if (backingRowMap == null)
        return null;
      V $ = Maps.safeRemove(backingRowMap, key);
      maintainEmptyInvariant();
      return $;
    }

    @Override public void clear() {
      Map<C, V> backingRowMap = backingRowMap();
      if (backingRowMap != null)
        backingRowMap.clear();
      maintainEmptyInvariant();
    }

    @Override public int size() {
      Map<C, V> $ = backingRowMap();
      return ($ == null) ? 0 : $.size();
    }

    @Override Iterator<Entry<C, V>> entryIterator() {
      final Map<C, V> map = backingRowMap();
      if (map == null)
        return Iterators.emptyModifiableIterator();
      final Iterator<Entry<C, V>> iterator = map.entrySet().iterator();
      return new Iterator<Entry<C, V>>() {
        @Override public boolean hasNext() {
          return iterator.hasNext();
        }

        @Override public Entry<C, V> next() {
          final Entry<C, V> entry = iterator.next();
          return new ForwardingMapEntry<C, V>() {
            @Override protected Entry<C, V> delegate() {
              return entry;
            }

            @Override public V setValue(V value) {
              return super.setValue(checkNotNull(value));
            }

            @Override public boolean equals(Object ¢) {
              // TODO(lowasser): identify why this affects GWT tests
              return standardEquals(¢);
            }
          };
        }

        @Override public void remove() {
          iterator.remove();
          maintainEmptyInvariant();
        }
      };
    }
  }

  /** {@inheritDoc}
   * <p>
   * The returned map's views have iterators that don't support
   * {@code remove()}. */
  @Override public Map<R, V> column(C columnKey) {
    return new Column(columnKey);
  }

  private class Column extends ViewCachingAbstractMap<R, V> {
    final C columnKey;

    Column(C columnKey) {
      this.columnKey = checkNotNull(columnKey);
    }

    @Override public V put(R key, V value) {
      return StandardTable.this.put(key, columnKey, value);
    }

    @Override public V get(Object key) {
      return StandardTable.this.get(key, columnKey);
    }

    @Override public boolean containsKey(Object key) {
      return StandardTable.this.contains(key, columnKey);
    }

    @Override public V remove(Object key) {
      return StandardTable.this.remove(key, columnKey);
    }

    /** Removes all {@code Column} mappings whose row key and value satisfy the
     * given predicate. */
    @CanIgnoreReturnValue boolean removeFromColumnIf(Predicate<? super Entry<R, V>> r) {
      boolean $ = false;
      // XXX: spartanized
      for (Iterator<Entry<R, Map<C, V>>> iterator = backingMap.entrySet().iterator(); iterator.hasNext();) {
        Entry<R, Map<C, V>> entry = iterator.next();
        Map<C, V> map = entry.getValue();
        V value = map.get(columnKey);
        if (value != null && r.apply(Maps.immutableEntry(entry.getKey(), value))) {
          map.remove(columnKey);
          $ = true;
          if (map.isEmpty())
            iterator.remove();
        }
      }
      return $;
    }

    @Override Set<Entry<R, V>> createEntrySet() {
      return new EntrySet();
    }

    @WeakOuter private class EntrySet extends ImprovedAbstractSet<Entry<R, V>> {
      @Override public Iterator<Entry<R, V>> iterator() {
        return new EntrySetIterator();
      }

      @Override public int size() {
        int $ = 0;
        for (Map<C, V> ¢ : backingMap.values())
          if (¢.containsKey(columnKey))
            ++$;
        return $;
      }

      @Override public boolean isEmpty() {
        return !containsColumn(columnKey);
      }

      @Override public void clear() {
        removeFromColumnIf(alwaysTrue());
      }

      @Override public boolean contains(Object ¢) {
        return ¢ instanceof Entry && containsMapping(((Entry<?, ?>) ¢).getKey(), columnKey, ((Entry<?, ?>) ¢).getValue());
      }

      @Override public boolean remove(Object ¢) {
        return ¢ instanceof Entry && removeMapping(((Entry<?, ?>) ¢).getKey(), columnKey, ((Entry<?, ?>) ¢).getValue());
      }

      @Override public boolean retainAll(Collection<?> ¢) {
        return removeFromColumnIf(not(in(¢)));
      }
    }

    private class EntrySetIterator extends AbstractIterator<Entry<R, V>> {
      final Iterator<Entry<R, Map<C, V>>> iterator = backingMap.entrySet().iterator();

      @Override protected Entry<R, V> computeNext() {
        while (iterator.hasNext()) {
          final Entry<R, Map<C, V>> entry = iterator.next();
          if (entry.getValue().containsKey(columnKey)) {
            @WeakOuter class EntryImpl extends AbstractMapEntry<R, V> {
              @Override public R getKey() {
                return entry.getKey();
              }

              @Override public V getValue() {
                return entry.getValue().get(columnKey);
              }

              @Override public V setValue(V value) {
                return entry.getValue().put(columnKey, checkNotNull(value));
              }
            }
            return new EntryImpl();
          }
        }
        return endOfData();
      }
    }

    @Override Set<R> createKeySet() {
      return new KeySet();
    }

    @WeakOuter private class KeySet extends Maps.KeySet<R, V> {
      KeySet() {
        super(Column.this);
      }

      @Override public boolean contains(Object ¢) {
        return StandardTable.this.contains(¢, columnKey);
      }

      @Override public boolean remove(Object ¢) {
        return StandardTable.this.remove(¢, columnKey) != null;
      }

      @Override public boolean retainAll(final Collection<?> ¢) {
        return removeFromColumnIf(Maps.<R> keyPredicateOnEntries(not(in(¢))));
      }
    }

    @Override Collection<V> createValues() {
      return new Values();
    }

    @WeakOuter private class Values extends Maps.Values<R, V> {
      Values() {
        super(Column.this);
      }

      @Override public boolean remove(Object ¢) {
        return ¢ != null && removeFromColumnIf(Maps.<V> valuePredicateOnEntries(equalTo(¢)));
      }

      @Override public boolean removeAll(final Collection<?> ¢) {
        return removeFromColumnIf(Maps.<V> valuePredicateOnEntries(in(¢)));
      }

      @Override public boolean retainAll(final Collection<?> ¢) {
        return removeFromColumnIf(Maps.<V> valuePredicateOnEntries(not(in(¢))));
      }
    }
  }

  @Override public Set<R> rowKeySet() {
    return rowMap().keySet();
  }

  private transient Set<C> columnKeySet;

  /** {@inheritDoc}
   * <p>
   * The returned set has an iterator that does not support {@code remove()}.
   * <p>
   * The set's iterator traverses the columns of the first row, the columns of
   * the second row, etc., skipping any columns that have appeared
   * previously. */
  @Override public Set<C> columnKeySet() {
    return columnKeySet != null ? columnKeySet : (columnKeySet = new ColumnKeySet());
  }

  @WeakOuter private class ColumnKeySet extends TableSet<C> {
    @Override public Iterator<C> iterator() {
      return createColumnKeyIterator();
    }

    @Override public int size() {
      return Iterators.size(iterator());
    }

    @Override public boolean remove(Object o) {
      if (o == null)
        return false;
      boolean $ = false;
      // XXX: spartanized
      for (Iterator<Map<C, V>> iterator = backingMap.values().iterator(); iterator.hasNext();) {
        Map<C, V> map = iterator.next();
        if (map.keySet().remove(o)) {
          $ = true;
          if (map.isEmpty())
            iterator.remove();
        }
      }
      return $;
    }

    @Override public boolean removeAll(Collection<?> c) {
      checkNotNull(c);
      boolean $ = false;
      for (Iterator<Map<C, V>> iterator = backingMap.values().iterator(); iterator.hasNext();) {
        Map<C, V> map = iterator.next();
        if (Iterators.removeAll(map.keySet().iterator(), c)) {
          $ = true;
          if (map.isEmpty())
            iterator.remove();
        }
      }
      return $;
    }

    @Override public boolean retainAll(Collection<?> c) {
      checkNotNull(c);
      boolean $ = false;
      for (Iterator<Map<C, V>> iterator = backingMap.values().iterator(); iterator.hasNext();) {
        Map<C, V> map = iterator.next();
        if (map.keySet().retainAll(c)) {
          $ = true;
          if (map.isEmpty())
            iterator.remove();
        }
      }
      return $;
    }

    @Override public boolean contains(Object ¢) {
      return containsColumn(¢);
    }
  }

  /** Creates an iterator that returns each column value with duplicates
   * omitted. */
  Iterator<C> createColumnKeyIterator() {
    return new ColumnKeyIterator();
  }

  private class ColumnKeyIterator extends AbstractIterator<C> {
    // Use the same map type to support TreeMaps with comparators that aren't
    // consistent with equals().
    final Map<C, V> seen = factory.get();
    final Iterator<Map<C, V>> mapIterator = backingMap.values().iterator();
    Iterator<Entry<C, V>> entryIterator = Iterators.emptyIterator();

    @Override protected C computeNext() {
      while (true)
        if (!entryIterator.hasNext()) {
          if (!mapIterator.hasNext())
            return endOfData();
          entryIterator = mapIterator.next().entrySet().iterator();
        } else {
          Entry<C, V> $ = entryIterator.next();
          if (!seen.containsKey($.getKey())) {
            seen.put($.getKey(), $.getValue());
            return $.getKey();
          }
        }
    }
  }

  /** {@inheritDoc}
   * <p>
   * The collection's iterator traverses the values for the first row, the
   * values for the second row, and so on. */
  @Override public Collection<V> values() {
    return super.values();
  }

  private transient Map<R, Map<C, V>> rowMap;

  @Override public Map<R, Map<C, V>> rowMap() {
    return rowMap != null ? rowMap : (rowMap = createRowMap());
  }

  Map<R, Map<C, V>> createRowMap() {
    return new RowMap();
  }

  @WeakOuter class RowMap extends ViewCachingAbstractMap<R, Map<C, V>> {
    @Override public boolean containsKey(Object key) {
      return containsRow(key);
    }

    /**
     * performing cast only when key is in backing map and has the correct type
     * @param key
     * @return
     */
    @Override @SuppressWarnings("unchecked") public Map<C, V> get(Object key) {
      return !containsRow(key) ? null : row((R) key);
    }

    @Override public Map<C, V> remove(Object key) {
      return (key == null) ? null : backingMap.remove(key);
    }

    @Override protected Set<Entry<R, Map<C, V>>> createEntrySet() {
      return new EntrySet();
    }

    @WeakOuter class EntrySet extends TableSet<Entry<R, Map<C, V>>> {
      @Override public Iterator<Entry<R, Map<C, V>>> iterator() {
        return Maps.asMapEntryIterator(backingMap.keySet(), new Function<R, Map<C, V>>() {
          @Override public Map<C, V> apply(R rowKey) {
            return row(rowKey);
          }
        });
      }

      @Override public int size() {
        return backingMap.size();
      }

      @Override public boolean contains(Object o) {
        if (!(o instanceof Entry))
          return false;
        Entry<?, ?> entry = (Entry<?, ?>) o;
        return entry.getKey() != null && entry.getValue() instanceof Map && Collections2.safeContains(backingMap.entrySet(), entry);
      }

      @Override public boolean remove(Object o) {
        if (!(o instanceof Entry))
          return false;
        Entry<?, ?> entry = (Entry<?, ?>) o;
        return entry.getKey() != null && entry.getValue() instanceof Map && backingMap.entrySet().remove(entry);
      }
    }
  }

  private transient ColumnMap columnMap;

  @Override public Map<C, Map<R, V>> columnMap() {
    return columnMap != null ? columnMap : (columnMap = new ColumnMap());
  }

  @WeakOuter private class ColumnMap extends ViewCachingAbstractMap<C, Map<R, V>> {
    @Override @SuppressWarnings("unchecked") public Map<R, V> get(Object key) {
      return !containsColumn(key) ? null : column((C) key);
    }

    @Override public boolean containsKey(Object key) {
      return containsColumn(key);
    }

    @Override public Map<R, V> remove(Object key) {
      return !containsColumn(key) ? null : removeColumn(key);
    }

    @Override public Set<Entry<C, Map<R, V>>> createEntrySet() {
      return new ColumnMapEntrySet();
    }

    @Override public Set<C> keySet() {
      return columnKeySet();
    }

    @Override Collection<Map<R, V>> createValues() {
      return new ColumnMapValues();
    }

    @WeakOuter class ColumnMapEntrySet extends TableSet<Entry<C, Map<R, V>>> {
      @Override public Iterator<Entry<C, Map<R, V>>> iterator() {
        return Maps.asMapEntryIterator(columnKeySet(), new Function<C, Map<R, V>>() {
          @Override public Map<R, V> apply(C columnKey) {
            return column(columnKey);
          }
        });
      }

      @Override public int size() {
        return columnKeySet().size();
      }

      @Override public boolean contains(Object o) {
        if (o instanceof Entry) {
          Entry<?, ?> entry = (Entry<?, ?>) o;
          if (containsColumn(entry.getKey())) {
            // The cast to C occurs only when the key is in the map, implying
            // that it has the correct type.
            @SuppressWarnings("unchecked") C columnKey = (C) entry.getKey();
            return get(columnKey).equals(entry.getValue());
          }
        }
        return false;
      }

      @Override public boolean remove(Object o) {
        if (!contains(o))
          return false;
        Entry<?, ?> entry = (Entry<?, ?>) o;
        removeColumn(entry.getKey());
        return true;
      }

      @Override public boolean removeAll(Collection<?> ¢) {
        /* We can't inherit the normal implementation (which calls
         * Sets.removeAllImpl(Set, *Collection*) because, under some
         * circumstances, it attempts to call columnKeySet().iterator().remove,
         * which is unsupported. */
        checkNotNull(¢);
        return Sets.removeAllImpl(this, ¢.iterator());
      }

      @Override public boolean retainAll(Collection<?> c) {
        checkNotNull(c);
        boolean $ = false;
        for (C columnKey : Lists.newArrayList(columnKeySet().iterator()))
          if (!c.contains(Maps.immutableEntry(columnKey, column(columnKey)))) {
            removeColumn(columnKey);
            $ = true;
          }
        return $;
      }
    }

    @WeakOuter private class ColumnMapValues extends Maps.Values<C, Map<R, V>> {
      ColumnMapValues() {
        super(ColumnMap.this);
      }

      @Override public boolean remove(Object o) {
        for (Entry<C, Map<R, V>> ¢ : ColumnMap.this.entrySet())
          if (¢.getValue().equals(o)) {
            removeColumn(¢.getKey());
            return true;
          }
        return false;
      }

      @Override public boolean removeAll(Collection<?> c) {
        checkNotNull(c);
        boolean $ = false;
        for (C columnKey : Lists.newArrayList(columnKeySet().iterator()))
          if (c.contains(column(columnKey))) {
            removeColumn(columnKey);
            $ = true;
          }
        return $;
      }

      @Override public boolean retainAll(Collection<?> c) {
        checkNotNull(c);
        boolean $ = false;
        for (C columnKey : Lists.newArrayList(columnKeySet().iterator()))
          if (!c.contains(column(columnKey))) {
            removeColumn(columnKey);
            $ = true;
          }
        return $;
      }
    }
  }

  private static final long serialVersionUID = 0;
}
