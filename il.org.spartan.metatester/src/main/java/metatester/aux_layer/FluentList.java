package metatester.aux_layer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class FluentList<T> {

    private static final Set<Collector.Characteristics> CH_ID =
            Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    private List<T> inner;

    public FluentList() {
        this.inner = new ArrayList<>();
    }

    public static <T>
    Collector<T, ?, FluentList<T>> toFluentList() {
        return new Collector<T, FluentList<T>, FluentList<T>>() {
            @Override
            public Supplier<FluentList<T>> supplier() {
                return FluentList::new;
            }

            @Override
            public BiConsumer<FluentList<T>, T> accumulator() {
                return FluentList::add;
            }

            @Override
            public BinaryOperator<FluentList<T>> combiner() {
                return FluentList::addAll;
            }

            @Override
            public Function<FluentList<T>, FluentList<T>> finisher() {
                return i -> i;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return CH_ID;
            }
        };
    }

    public FluentList<T> add(T t) {
        inner.add(t);
        return this;
    }

    public FluentList<T> addAll(FluentList<T> fl) {
        inner.addAll(fl.inner);
        return this;
    }

    public List<T> list() {
        return this.inner;
    }


}
