
public class HelloRx {

    public static void problem2() {
        Observable.range(1, 29)
                .map(it -> new Pair<>(it, numberMersenne(it)))
                .filter(it -> isPrime(it.getValue()))
                .map(it -> it.getKey() + numberPerfect(it.getValue()))
                .reduce((number1, number2) -> number1 + number2)
                .subscribe(System.out::println);

    }

    public static int numberMersenne(int number) {
        return (int)(Math.pow(2, number) - 1);
    }

    public static boolean isPrime(int number) {
        return !IntStream.rangeClosed(2, number/2).anyMatch(i -> number%i == 0);
    }

    public static int numberPerfect(int number) {
        return (number * (number + 1) / 2);
    }

    public static void main(String[] args) {
        // problem1();
        problem2(); // 33231382
    }
}
