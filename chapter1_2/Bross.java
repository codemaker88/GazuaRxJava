@CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromMap(Map source) {
        return RxJavaPlugins.onAssembly(new ObservableFromIterable<T>(source.entrySet()));
    }


    public static void main(String[] args) {

        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            map.put(i, String.valueOf(i));
        }
        Observable source = fromMap(map);

        source.subscribe(System.out::println);

        map.replace(0, "-1");

        System.out.println("\n\n\n");

        source.subscribe(System.out::println);

    }
