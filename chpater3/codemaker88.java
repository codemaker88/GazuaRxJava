package chapter3;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.IntStream;

/**
 * Created by codemaker88 on 2018-02-05.
 */

public class Chapter3 {
    public void solve() {
        final int target = 29;

        Maybe<Integer> source = Observable.range(1, target)
                .map(index -> Pair.of(index, (1 << index) - 1)) //Pair(index, mersenneNumber)
                .filter(pair -> isPrime(pair.getValue()))
                .map(pair -> pair.getLeft() + makePerfect(pair.getRight()))
                .reduce((integer, integer2) -> integer + integer2);
        source.subscribe(System.out::println);
    }

    private boolean isPrime(int number) {
        return number > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(number)).noneMatch(i -> number % i == 0);
    }

    private int makePerfect(int number) {
        return number * (number + 1) / 2;
    }
}
