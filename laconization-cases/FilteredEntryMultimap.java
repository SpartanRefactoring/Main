/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.CollectPreconditions.checkNonnegative;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps.ViewCachingAbstractMap;
import com.google.j2objc.annotations.WeakOuter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Implementation of {@link Multimaps#filterEntries(Multimap, Predicate)}.
 *
 * @author Jared Levy
 * @author Louis Wasserman
 */
@GwtCompatible
class FilteredEntryMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V> {
  final class ValuePredicate implements Predicate<V> {
    private final K key;

    ValuePredicate(K key) {
      this.key = key;
    }

    @Override
    public boolean apply(@Nullable V value) {
      return satisfies(key, value);
    }
  }

  @WeakOuter
  class AsMap extends ViewCachingAbstractMap<K, Collection<V>> {
    @Override
    Collection<Collection<V>> createValues() {
      @WeakOuter
      class ValuesImpl extends Maps.Values<K, Collection<V>> {
        @Override
        public boolean remove(@Nullable Object o) {
          if (o instanceof Collection) {
            Collection<?> c = (Collection<?>) o;
            Iterator<Entry<K, Collection<V>>> entryIterator =
                unfiltered.asMap().entrySet().iterator();
            while (entryIterator.hasNext()) {
              Entry<K, Collection<V>> entry = entryIterator.next();
              K key = entry.getKey();
              Collection<V> collection =
                  filterCollection(entry.getValue(), new ValuePredicate(key));
              if (!collection.isEmpty() && c.equals(collection)) {
                if (collection.size() != entry.getValue().size())
                  collection.clear();
                else
                  entryIterator.remove();
                return true;
              }
            }
          }
          return false;
        }
      }
      return new ValuesImpl();
    }
  }

  @Override
  Multiset<K> createKeys() {
    return new Keys();
  }

  @WeakOuter
  class Keys extends Multimaps.Keys<K, V> {
    Keys() {
      super(FilteredEntryMultimap.this);
    }

    @Override
    public Set<Multiset.Entry<K>> entrySet() {
      return new Multisets.EntrySet<K>() {

        private boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> k) {
          return FilteredEntryMultimap.this
              .removeEntriesIf(
                  new Predicate<Map.Entry<K, Collection<V>>>() {
                    @Override
                    public boolean apply(Map.Entry<K, Collection<V>> ¢) {
                      return k.apply(
                          Multisets.immutableEntry(¢.getKey(), ¢.getValue().size()));
                    }
                  });
        }
      };
    }
  }
}
