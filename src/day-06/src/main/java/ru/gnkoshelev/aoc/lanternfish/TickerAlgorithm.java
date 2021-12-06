package ru.gnkoshelev.aoc.lanternfish;

import java.util.Arrays;

/**
 * @author Gregory Koshelev
 */
public class TickerAlgorithm implements Algorithm {
    @Override
    public long compute(int[] counters, int days) {
        for (int d = 0; d < days; d++) {
            int newLanternfish = 0;
            for (int c : counters) {
                if (c == 0) {
                    newLanternfish++;
                }
            }
            int[] nextCounters = new int[counters.length + newLanternfish];
            int i = 0;
            while (i < counters.length) {
                int counter = counters[i] - 1;
                nextCounters[i] = counter < 0 ? Lanternfish.REPRODUCING_INTERVAL_DAYS - 1 : counter;
                i++;
            }
            Arrays.fill(nextCounters, i, nextCounters.length, Lanternfish.REPRODUCTIVE_AGE_DAYS - 1);

            counters = nextCounters;
        }

        return counters.length;
    }
}
