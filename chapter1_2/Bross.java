
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromMap(Map source) {
        return RxJavaPlugins.onAssembly(new ObservableFromIterable<T>(source.entrySet()));
    }


    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static <T> Observable<T> fromMap(Comparator comparator, Map source) {
        return Observable.create(emitter -> {
            try {
                source.keySet().stream().sorted(comparator).forEach(it -> emitter.onNext((T) (it + " :" + source.get(it))));
                emitter.onComplete();
            } catch (Throwable t) {
                emitter.onError(t);
            }
        });
    }


    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();

        for (int i = 10; i >= 0; i--) {
            map.put(String.valueOf(i), String.valueOf(i));
        }

        map.put("a", "a");
        map.put("a1", "a");
        map.put("aa", "a");
        map.put("aaaa", "a");
        map.put("0a", "a");
        map.put("1a", "a");

        Observable source = fromMap(map);
        source.subscribe(System.out::println);

        map.replace("0", "-1");

        System.out.println("\n\n\n");
        source = fromMap(Comparator.comparing(String::length), map);

        source.subscribe(System.out::println);

    }
