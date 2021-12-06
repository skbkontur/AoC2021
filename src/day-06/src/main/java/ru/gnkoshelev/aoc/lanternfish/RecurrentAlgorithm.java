package ru.gnkoshelev.aoc.lanternfish;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Koshelev
 */
public class RecurrentAlgorithm implements Algorithm {
    private final List<Long> reproducedByDays = new ArrayList<>(257);

    {
        reproducedByDays.add(0L);
        reproducedByDays.add(1L);
        reproducedByDays.add(1L);
        reproducedByDays.add(1L);
        reproducedByDays.add(1L);
        reproducedByDays.add(1L);
        reproducedByDays.add(1L);
        reproducedByDays.add(1L);
        reproducedByDays.add(2L);
    }

    @Override
    public long compute(int[] counters, int days) {
        long count = counters.length;
        for (int c : counters) {
            if (c > days) {
                continue;
            }
            count += reproducedForDays(days - c);
        }
        return count;
    }

    private long reproducedForDays(int days) {
        if (reproducedByDays.size() <= days) {
            int start = reproducedByDays.size();
            for (int i = start; i <= days; i++) {
                reproducedByDays.add(1 +
                        reproducedByDays.get(i - Lanternfish.REPRODUCING_INTERVAL_DAYS) +
                        reproducedByDays.get(i - Lanternfish.REPRODUCTIVE_AGE_DAYS));
            }
        }

        return reproducedByDays.get(days);
    }
}
