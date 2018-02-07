package rxjavachapter3;

import io.reactivex.Observable;

public class 백병민 {
    public static void main(String[] args) {
        Observable.range(1, 29)
                .map(인덱스 -> new int[]{인덱스, 메르센수계산(인덱스)})
                .filter(인덱스와_메르센수 -> 메르센소수냐(인덱스와_메르센수[1]))
                .map(인덱스와_메르센수 -> 인덱스와_메르센수[0] + 완전수냐(인덱스와_메르센수[1]))
                .reduce((a, b) -> a + b)
                .subscribe(System.out::println);
    }

    private static int 완전수냐(Integer 메르센소수) {
        return (메르센소수 * (메르센소수 + 1)) / 2;
    }

    private static int 메르센수계산(Integer 인덱스) {
        return (int) (Math.pow(2, 인덱스) - 1);
    }

    private static boolean 메르센소수냐(Integer 메르센수) {
        for (int i = 2; i < 메르센수; i++) {
            if (메르센수 % i == 0) {
                return false;
            }
        }
        return true;
    }

}
