package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Test "Bug in remove else" as discussed in Issue #352
 * @author Tomer Dragucki
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class issue0352 {
  @Test public void a() {
    trimmingOf("@Override    Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {      if (restriction.isEmpty())"
        + "        return Iterators.emptyIterator();      final Iterator<Range<C>> completeRangeItr;"
        + "      if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound))        return Iterators.emptyIterator();    else"
        + "        completeRangeItr = ((lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound)"
        + "                ? rangesByUpperBound.tailMap(restriction.lowerBound, false)"
        + "                : rangesByLowerBound.tailMap(lowerBoundWindow.lowerBound.endpoint(),"
        + "                        lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values()).iterator();"
        + "      final Cut<Cut<C>> upperBoundOnLowerBounds =          Ordering.natural()"
        + "              .min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));"
        + "      return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {        @Override"
        + "        protected Entry<Cut<C>, Range<C>> computeNext() {            if (!completeRangeItr.hasNext())"
        + "                return endOfData();            Range<C> nextRange = completeRangeItr.next();"
        + "            if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound))                return endOfData();"
        + "            nextRange = nextRange.intersection(restriction);            return Maps.immutableEntry(nextRange.lowerBound, nextRange);"
        + "        }      };")
            .gives("@Override    Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {      if (restriction.isEmpty())"
                + "        return Iterators.emptyIterator();      final Iterator<Range<C>> $;"
                + "      if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound))        return Iterators.emptyIterator();    else"
                + "        $ = ((lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound)"
                + "                ? rangesByUpperBound.tailMap(restriction.lowerBound, false)"
                + "                : rangesByLowerBound.tailMap(lowerBoundWindow.lowerBound.endpoint(),"
                + "                        lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values()).iterator();"
                + "      final Cut<Cut<C>> upperBoundOnLowerBounds =          Ordering.natural()"
                + "              .min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));"
                + "      return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {        @Override"
                + "        protected Entry<Cut<C>, Range<C>> computeNext() {            if (!$.hasNext())                return endOfData();"
                + "            Range<C> nextRange = $.next();            if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound))"
                + "                return endOfData();            nextRange = nextRange.intersection(restriction);"
                + "            return Maps.immutableEntry(nextRange.lowerBound, nextRange);        }      };")
            .gives("@Override    Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {      if (restriction.isEmpty())"
                + "        return Iterators.emptyIterator();      final Iterator<Range<C>> $;"
                + "      if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound))        return Iterators.emptyIterator();    "
                + "        $ = ((lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound)"
                + "                ? rangesByUpperBound.tailMap(restriction.lowerBound, false)"
                + "                : rangesByLowerBound.tailMap(lowerBoundWindow.lowerBound.endpoint(),"
                + "                        lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values()).iterator();"
                + "      final Cut<Cut<C>> upperBoundOnLowerBounds =          Ordering.natural()"
                + "              .min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));"
                + "      return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {        @Override"
                + "        protected Entry<Cut<C>, Range<C>> computeNext() {            if (!$.hasNext())"
                + "                return endOfData();            Range<C> nextRange = $.next();"
                + "            if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound))                return endOfData();"
                + "            nextRange = nextRange.intersection(restriction);"
                + "            return Maps.immutableEntry(nextRange.lowerBound, nextRange);        }      };")
            .gives("@Override    Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {      if (restriction.isEmpty())"
                + "        return Iterators.emptyIterator();      final Iterator<Range<C>> $;"
                + "      if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound))        return Iterators.emptyIterator();    "
                + "        $ = (lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound)"
                + "                ? rangesByUpperBound.tailMap(restriction.lowerBound, false)"
                + "                : rangesByLowerBound.tailMap(lowerBoundWindow.lowerBound.endpoint(),"
                + "                        lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values().iterator();"
                + "      final Cut<Cut<C>> upperBoundOnLowerBounds =          Ordering.natural()"
                + "              .min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));"
                + "      return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {        @Override"
                + "        protected Entry<Cut<C>, Range<C>> computeNext() {            if (!$.hasNext())"
                + "                return endOfData();            Range<C> nextRange = $.next();"
                + "            if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound))                return endOfData();"
                + "            nextRange = nextRange.intersection(restriction);"
                + "            return Maps.immutableEntry(nextRange.lowerBound, nextRange);        }      };")
            .stays();
  }
  @Test public void b() {
    trimmingOf("if (a())     return 0;   if (a())     return 1;   else {     int b = c(5) + 7;     return c(b+b+b);}")
        .gives("if (a())   return 0; if (a())   return 1;int b=c(5) + 7;return c(b + b + b);");
  }
}
