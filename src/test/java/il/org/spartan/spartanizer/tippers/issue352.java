package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Tomer Dragucki
 * @since 2016 [[SuppressWarningsSpartan]] */
@SuppressWarnings({ "static-method", "javadoc" })
public class issue352 {
  @Test public void a() {
    trimmingOf(
  "" + //
  "@Override" + //
  "    Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {" + //
  "      if (restriction.isEmpty())" + //
  "        return Iterators.emptyIterator();" + //
  "      final Iterator<Range<C>> completeRangeItr;" + //
  "      if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound))" + //
  "        return Iterators.emptyIterator();" + //
  "    else" + //
  "        completeRangeItr = ((lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound)" + //
  "                ? rangesByUpperBound.tailMap(restriction.lowerBound, false)" + //
  "                : rangesByLowerBound.tailMap(lowerBoundWindow.lowerBound.endpoint()," + //
  "                        lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values()).iterator();" + //
  "      final Cut<Cut<C>> upperBoundOnLowerBounds =" + //
  "          Ordering.natural()" + //
  "              .min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));" + //
  "      return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {" + //
  "        @Override" + //
  "        protected Entry<Cut<C>, Range<C>> computeNext() {" + //
  "            if (!completeRangeItr.hasNext())" + //
  "                return endOfData();" + //
  "            Range<C> nextRange = completeRangeItr.next();" + //
  "            if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound))" + //
  "                return endOfData();" + //
  "            nextRange = nextRange.intersection(restriction);" + //
  "            return Maps.immutableEntry(nextRange.lowerBound, nextRange);" + //
  "        }" + //
  "      };"//

  ).gives("" + //
      "@Override" + //
      "    Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {" + //
      "      if (restriction.isEmpty())" + //
      "        return Iterators.emptyIterator();" + //
      "      final Iterator<Range<C>> $;" + //
      "      if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound))" + //
      "        return Iterators.emptyIterator();" + //
      "    else" + //
      "        $ = ((lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound)" + //
      "                ? rangesByUpperBound.tailMap(restriction.lowerBound, false)" + //
      "                : rangesByLowerBound.tailMap(lowerBoundWindow.lowerBound.endpoint()," + //
      "                        lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values()).iterator();" + //
      "      final Cut<Cut<C>> upperBoundOnLowerBounds =" + //
      "          Ordering.natural()" + //
      "              .min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));" + //
      "      return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {" + //
      "        @Override" + //
      "        protected Entry<Cut<C>, Range<C>> computeNext() {" + //
      "            if (!$.hasNext())" + //
      "                return endOfData();" + //
      "            Range<C> nextRange = $.next();" + //
      "            if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound))" + //
      "                return endOfData();" + //
      "            nextRange = nextRange.intersection(restriction);" + //
      "            return Maps.immutableEntry(nextRange.lowerBound, nextRange);" + //
      "        }" + //
      "      };"//
      ).gives("" + //
          "@Override" + //
          "    Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {" + //
          "      if (restriction.isEmpty())" + //
          "        return Iterators.emptyIterator();" + //
          "      final Iterator<Range<C>> $;" + //
          "      if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound))" + //
          "        return Iterators.emptyIterator();" + //
          "    " + //
          "        $ = ((lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound)" + //
          "                ? rangesByUpperBound.tailMap(restriction.lowerBound, false)" + //
          "                : rangesByLowerBound.tailMap(lowerBoundWindow.lowerBound.endpoint()," + //
          "                        lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values()).iterator();" + //
          "      final Cut<Cut<C>> upperBoundOnLowerBounds =" + //
          "          Ordering.natural()" + //
          "              .min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));" + //
          "      return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {" + //
          "        @Override" + //
          "        protected Entry<Cut<C>, Range<C>> computeNext() {" + //
          "            if (!$.hasNext())" + //
          "                return endOfData();" + //
          "            Range<C> nextRange = $.next();" + //
          "            if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound))" + //
          "                return endOfData();" + //
          "            nextRange = nextRange.intersection(restriction);" + //
          "            return Maps.immutableEntry(nextRange.lowerBound, nextRange);" + //
          "        }" + //
          "      };"//
          ).stays();
  }

}
