public static boolean isPrime(long n) {
        if (n <= 1)
            return false;
        if ((n & 1) == 0)
            return (n == 2);

        for (int i = 3; i * i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        Observable
                .range(1, 29)
                .map(it -> new Pair<>(it, (long) Math.pow(2, it) - 1))
                .filter(it -> isPrime(it.getValue()))
                .map(it -> new Pair<>(it.getKey(), it.getValue() * (it.getValue() + 1) / 2))
                .map(it->it.getValue()+it.getKey())
                .reduce((value1,value2)->value1+value2)
                .subscribe(System.out::println);
    }
