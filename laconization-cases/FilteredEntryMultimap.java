/* Copyright (C) 2012 The Guava Authors
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
  class A {
    void g() {
      class V {
        public boolean r() {
            Iterator<Entry<K, C<V>>> e = f();
            while (e.hasNext()) 
              a++;
        }
      }
    }
  }

void r() {
            Iterator<Entry<K, C<V>>> e = f();
            while (e.g()) 
              ++a;
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
    public void f() {
      
      return new O() {
        boolean g() {
          return r(new Predicate<Map.Entry<K, Collection<V>>>(){});
        }
      };
    }
  @class K {
    public void f() {
      return new O() {
        boolean g() {
          return r(new Predicate<Map.Entry<K, Collection<V>>>(){});
        }
      };
    }
  }
  @WeakOuter
  class Keys extends Multimaps.Keys<K, V> {
    @Override
    public Set<Multiset.Entry<K>> entrySet() {
      return new Multisets.EntrySet<K>() {

        boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> k) {
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
public void f1() {
      return new O() {
        boolean g() {
          return r(new O<Map.Entry<K, Collection<V>>>(){});
        }
      };
    }
public void f2() {
      return new O() {
        boolean g() {
          return r(new O<Map.Entry<K, Collection<V>>>(){});
        }
      };
    }

public void f3() { return new O() { boolean g() { return r(new O<M.E<K,C<V>>>(){}); } }; }
int f4(){return new O(){boolean g() {return r(new O<M.E<K,C<V>>>(){});}};}
int f5(){return new O(){boolean g() {return r(new O<M.E<K,C<V>>>(){});}};}
int f6(){return new O(){void g() {return r(new O<E<K,C<V>>>(){});}};}
int f7(){return new O(){void g() {r(new O<E<K,C<V>>>(){});}};}
int f8(){return new O(){void g() {r(new O<E<C<V>>>(){});}};}
int f9(){return new O(){void g() {new O<E<C<V>>>(){};}};}
int f10(){return new O(){int g() {new O<E<C<V>>>(){};}};}
int f11(){return new O(){int g() {new O<E<C>>(){};}};}
int f12(){return new O(){int g() {O<E<C<K>>> a;}};}

int f13(){return new O(){boolean g() {return r(new O<M.E<K, C>>(){});}};}
}
