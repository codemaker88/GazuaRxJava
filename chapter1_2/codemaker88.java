package chapter2;

import io.reactivex.Observable;
import io.reactivex.Observer;

import java.util.*;

/**
 * Created by codemaker88 on 2018-01-28.
 */

public class Chapter2 {
    public void solve() {
        Map<String, String> stringMap = new HashMap<String, String>() {
            {
                put("key2", "value2");
                put("key1", "value1");
                put("key3", "value3");
            }
        };

        Map<Integer, String> intMap = new HashMap<Integer, String>() {
            {
                put(2, "value3");
                put(1, "value2");
                put(3, "value1");
            }
        };

        Observable<Map.Entry<String, String>> mapObservable2 = ObservableForMap.fromMap(Comparator.comparing(Map.Entry::getValue), stringMap);
        mapObservable2.subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("onComplete"));


        Observable<Map.Entry<Integer, String>> mapObservable3 = ObservableForMap.fromMap(Comparator.comparing(Map.Entry::getKey), intMap);
        mapObservable3.subscribe(
                System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("onComplete"));
    }

    static class ObservableForMap extends Observable {

        static <K, V> Observable<Map.Entry<K, V>> fromMap(Comparator<Map.Entry<K, V>> comparator, Map<K, V> map) {
            return Observable.create(e -> {
                try {
                    List<Map.Entry<K, V>> entryList = new ArrayList<>(map.entrySet());
                    entryList.sort(comparator);

                    for (Map.Entry<K, V> entry : entryList) {
                        e.onNext(entry);
                    }
                    e.onComplete();
                } catch (Exception ex) {
                    e.onError(ex);
                }
            });
        }

        @Override
        protected void subscribeActual(Observer observer) {
            System.out.println("call subscribeActual");
        }
    }
}
