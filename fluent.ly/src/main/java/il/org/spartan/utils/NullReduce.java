package il.org.spartan.utils;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Provides null value for {@link #reduce()}
 *
 * @param <T>
 * @since 2017
 */
public abstract class NullReduce<T> extends Reduce<T> {
	@SuppressWarnings("null")
	@Override
	public final T reduce() {
		return null;
	}
}