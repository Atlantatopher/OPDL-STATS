package com.opdl.api.stats.comparator;

import com.opdl.api.stats.model.HighInOutReport;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@Component
public class HighInOutReportComparator implements Comparator<HighInOutReport> {

    public int compareHighIns(HighInOutReport o1, HighInOutReport o2) {
        int result = Integer.compare(o2.getNumHighIns(), o1.getNumHighIns());
        if ( result == 0 ) {
            result = Integer.compare(o2.getHighestIn(), o1.getHighestIn());
        }
        return result;
    }

    public int compareHighOuts(HighInOutReport o1, HighInOutReport o2) {
        int result = Integer.compare(o2.getNumHighOuts(), o1.getNumHighOuts());
        if ( result == 0 ) {
            result = Integer.compare(o2.getHighestOut(), o1.getHighestOut());
        }
        return result;
    }

    /**
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(HighInOutReport o1, HighInOutReport o2) {
        return 0;
    }

    /**
     * @return
     */
    @Override
    public Comparator<HighInOutReport> reversed() {
        return Comparator.super.reversed();
    }

    /**
     * @param other the other comparator to be used when this comparator
     *              compares two objects that are equal.
     * @return
     */
    @Override
    public Comparator<HighInOutReport> thenComparing(Comparator<? super HighInOutReport> other) {
        return Comparator.super.thenComparing(other);
    }

    /**
     * @param keyExtractor  the function used to extract the sort key
     * @param keyComparator the {@code Comparator} used to compare the sort key
     * @param <U>
     * @return
     */
    @Override
    public <U> Comparator<HighInOutReport> thenComparing(Function<? super HighInOutReport, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return Comparator.super.thenComparing(keyExtractor, keyComparator);
    }

    /**
     * @param keyExtractor the function used to extract the {@link
     *                     Comparable} sort key
     * @param <U>
     * @return
     */
    @Override
    public <U extends Comparable<? super U>> Comparator<HighInOutReport> thenComparing(Function<? super HighInOutReport, ? extends U> keyExtractor) {
        return Comparator.super.thenComparing(keyExtractor);
    }

    /**
     * @param keyExtractor the function used to extract the integer sort key
     * @return
     */
    @Override
    public Comparator<HighInOutReport> thenComparingInt(ToIntFunction<? super HighInOutReport> keyExtractor) {
        return Comparator.super.thenComparingInt(keyExtractor);
    }

    /**
     * @param keyExtractor the function used to extract the long sort key
     * @return
     */
    @Override
    public Comparator<HighInOutReport> thenComparingLong(ToLongFunction<? super HighInOutReport> keyExtractor) {
        return Comparator.super.thenComparingLong(keyExtractor);
    }

    /**
     * @param keyExtractor the function used to extract the double sort key
     * @return
     */
    @Override
    public Comparator<HighInOutReport> thenComparingDouble(ToDoubleFunction<? super HighInOutReport> keyExtractor) {
        return Comparator.super.thenComparingDouble(keyExtractor);
    }
}
