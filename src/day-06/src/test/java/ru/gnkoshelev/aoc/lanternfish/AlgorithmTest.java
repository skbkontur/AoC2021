package ru.gnkoshelev.aoc.lanternfish;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Gregory Koshelev
 */
public class AlgorithmTest {
    private static final Algorithm algorithm = new TickerAlgorithm();

    private static Stream<Arguments> args() {
        return Stream.of(
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{3, 4, 3, 1, 2}, 0),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{2, 3, 2, 0, 1}, 1),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{1, 2, 1, 6, 0, 8}, 2),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{0, 1, 0, 5, 6, 7, 8}, 3),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{6, 0, 6, 4, 5, 6, 7, 8, 8}, 4),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{5, 6, 5, 3, 4, 5, 6, 7, 7, 8}, 5),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{4, 5, 4, 2, 3, 4, 5, 6, 6, 7}, 6),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{3, 4, 3, 1, 2, 3, 4, 5, 5, 6}, 7),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{2, 3, 2, 0, 1, 2, 3, 4, 4, 5}, 8),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{1, 2, 1, 6, 0, 1, 2, 3, 3, 4, 8}, 9),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{0, 1, 0, 5, 6, 0, 1, 2, 2, 3, 7, 8}, 10),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{6, 0, 6, 4, 5, 6, 0, 1, 1, 2, 6, 7, 8, 8, 8}, 11),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{5, 6, 5, 3, 4, 5, 6, 0, 0, 1, 5, 6, 7, 7, 7, 8, 8}, 12),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{4, 5, 4, 2, 3, 4, 5, 6, 6, 0, 4, 5, 6, 6, 6, 7, 7, 8, 8}, 13),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{3, 4, 3, 1, 2, 3, 4, 5, 5, 6, 3, 4, 5, 5, 5, 6, 6, 7, 7, 8}, 14),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{2, 3, 2, 0, 1, 2, 3, 4, 4, 5, 2, 3, 4, 4, 4, 5, 5, 6, 6, 7}, 15),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{1, 2, 1, 6, 0, 1, 2, 3, 3, 4, 1, 2, 3, 3, 3, 4, 4, 5, 5, 6, 8}, 16),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{0, 1, 0, 5, 6, 0, 1, 2, 2, 3, 0, 1, 2, 2, 2, 3, 3, 4, 4, 5, 7, 8}, 17),
                Arguments.of(new int[]{3, 4, 3, 1, 2}, new int[]{6, 0, 6, 4, 5, 6, 0, 1, 1, 2, 6, 0, 1, 1, 1, 2, 2, 3, 3, 4, 6, 7, 8, 8, 8, 8}, 18)
        );
    }

    @ParameterizedTest
    @MethodSource("args")
    public void test(int[] source, int[] result, int days) {
        assertEquals(result.length, algorithm.compute(source, days));
    }
}
