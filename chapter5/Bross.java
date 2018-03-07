
public class Chapter5 {

    private static final String URL = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=";
    private static final int LAST_LOTTO_COUNT = 6;

    class Lotto {
        private int bnusNo;
        private long firstAccumamnt;
        private long firstWinamnt;
        private String returnValue;
        private long totSellamnt;
        private int drwtNo3;
        private int drwtNo2;
        private int drwtNo1;
        private int drwtNo6;
        private int drwtNo5;
        private int drwtNo4;
        private String drwNoDate;
        private int drwNo;
        private int firstPrzwnerCo;

        @Override
        public String toString() {
            return "[" + drwNo + "] ";
        }
    }

    public static void requestLastestLottoNumber() {

        Gson gson = new GsonBuilder().create();

        Observable
                .just(URL)
                .map(OkHttpHelper::getWithLog)
                .subscribe(it -> {
                    Lotto lastestLotto = gson.fromJson(it, Lotto.class);
                    requestAllLottoNumber(lastestLotto);
                });

    }

    public static void requestAllLottoNumber(Lotto lotto) {

        int[] array = new int[45];
        Gson gson = new GsonBuilder().create();
        Observable
                .range(1, lotto.drwNo)
                .map((Function<Integer, Object>) integer -> OkHttpHelper.getWithLog(URL + integer))
                .subscribeOn(Schedulers.newThread())
                .blockingSubscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                        Lotto lastestLotto = gson.fromJson(o.toString(), Lotto.class);

                        int count = lastestLotto.firstPrzwnerCo > 0 ? lastestLotto.firstPrzwnerCo : 1;
                        array[lastestLotto.drwtNo1 - 1] += count;
                        array[lastestLotto.drwtNo2 - 1] += count;
                        array[lastestLotto.drwtNo3 - 1] += count;
                        array[lastestLotto.drwtNo4 - 1] += count;
                        array[lastestLotto.drwtNo5 - 1] += count;
                        array[lastestLotto.drwtNo6 - 1] += count;
                        array[lastestLotto.bnusNo - 1] += count;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        HashMap<Integer, Integer> map = new HashMap<>();

                        for (int i = 0; i < array.length; i++) {
                            map.put(i, array[i]);
                        }

                        List<Integer> list = sortByMinValue(map);

                        LinkedList<Integer> minList = new LinkedList<>(list.subList(0, LAST_LOTTO_COUNT-1));

                        List<Integer> lastIndexList = new ArrayList<>();
                        lastIndexList.add(list.get(LAST_LOTTO_COUNT));

                        int lastNumber = lastIndexList.get(0);

                        for (Integer i : list.subList(LAST_LOTTO_COUNT+1, list.size() - 1)) {
                            if (lastNumber == i) {
                                lastIndexList.add(i);
                            }
                        }


                        System.out.println("-- MIN --");

                        lastIndexList.forEach(it->{
                            List tempList = new ArrayList();
                            tempList.addAll(minList);
                            tempList.add(it);
                            System.out.println(tempList.toString());
                        });




                        list = sortByMaxValue(map);

                        LinkedList<Integer> maxList = new LinkedList<>(list.subList(0, LAST_LOTTO_COUNT-1));

                        lastIndexList = new ArrayList<>();
                        lastIndexList.add(list.get(LAST_LOTTO_COUNT));

                        lastNumber = lastIndexList.get(0);

                        for (Integer i : list.subList(LAST_LOTTO_COUNT+1, list.size() - 1)) {
                            if (lastNumber == i) {
                                lastIndexList.add(i);
                            }
                        }


                        System.out.println("\n-- MAX --");

                        lastIndexList.forEach(it->{
                            List tempList = new ArrayList();
                            tempList.addAll(maxList);
                            tempList.add(it);
                            System.out.println(tempList.toString());
                        });

                    }
                });
    }

    public static List<Integer> sortByMinValue(final Map map) {
        ArrayList list = new ArrayList(map.keySet());

        list.sort((o1, o2) -> {
            Object v1 = map.get(o1);
            Object v2 = map.get(o2);

            return ((Comparable) v1).compareTo(v2);
        });
        return list;
    }

    public static List<Integer> sortByMaxValue(final Map map) {
        ArrayList list = new ArrayList(map.keySet());

        list.sort((o1, o2) -> {
            Object v1 = map.get(o1);
            Object v2 = map.get(o2);

            return ((Comparable) v1).compareTo(v2);
        });
        Collections.reverse(list); // 주석시 오름차순
        return list;
    }

    public static void main(String[] args) {

        requestLastestLottoNumber();

//        int[] array = {915,
//                806,
//                786,
//                920,
//                824,
//                880,
//                892,
//                888,
//                649,
//                910,
//                849,
//                1013,
//                837,
//                759,
//                729,
//                772,
//                929,
//                890,
//                836,
//                933,
//                792,
//                620,
//                669,
//                820,
//                733,
//                782,
//                918,
//                658,
//                675,
//                712,
//                812,
//                716,
//                919,
//                947,
//                749,
//                763,
//                783,
//                789,
//                789,
//                802,
//                727,
//                695,
//                942,
//                870,
//                813};
//
//        HashMap<Integer, Integer> map = new HashMap<>();
//
//        for (int i = 0; i < array.length; i++) {
//            map.put(i, array[i]);
//        }
//
//        List<Integer> list = sortByMinValue(map);
//
//        LinkedList<Integer> minList = new LinkedList<>(list.subList(0, LAST_LOTTO_COUNT-1));
//
//        List<Integer> lastIndexList = new ArrayList<>();
//        lastIndexList.add(list.get(LAST_LOTTO_COUNT));
//
//        int lastNumber = lastIndexList.get(0);
//
//        for (Integer i : list.subList(LAST_LOTTO_COUNT+1, list.size() - 1)) {
//            if (lastNumber == i) {
//                lastIndexList.add(i);
//            }
//        }
//
//
//        System.out.println("-- MIN --");
//
//        lastIndexList.forEach(it->{
//            List tempList = new ArrayList();
//            tempList.addAll(minList);
//            tempList.add(it);
//            System.out.println(tempList.toString());
//        });
//
//
//
//
//        list = sortByMaxValue(map);
//
//        LinkedList<Integer> maxList = new LinkedList<>(list.subList(0, LAST_LOTTO_COUNT-1));
//
//        lastIndexList = new ArrayList<>();
//        lastIndexList.add(list.get(LAST_LOTTO_COUNT));
//
//        lastNumber = lastIndexList.get(0);
//
//        for (Integer i : list.subList(LAST_LOTTO_COUNT+1, list.size() - 1)) {
//            if (lastNumber == i) {
//                lastIndexList.add(i);
//            }
//        }
//
//
//        System.out.println("\n-- MAX --");
//
//        lastIndexList.forEach(it->{
//            List tempList = new ArrayList();
//            tempList.addAll(maxList);
//            tempList.add(it);
//            System.out.println(tempList.toString());
//        });

    }
}

/*
-- MIN --
[21, 8, 27, 22, 28, 29]

-- MAX --
[11, 33, 42, 19, 16, 3]

*/
