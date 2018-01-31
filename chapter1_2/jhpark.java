import io.reactivex.Observable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.observable.ObservableFromIterable;
import io.reactivex.plugins.RxJavaPlugins;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j.h.park on 2018. 1. 18..
 */
public class Main {

    private static abstract class ObservableEx<T> extends Observable<T> {
        static <K, V> Observable<Map.Entry<K, V>> fromMap(Map<K, V> map) {
            ObjectHelper.requireNonNull(map, "map is null");

            return RxJavaPlugins.onAssembly(new ObservableFromIterable<>(() -> map.entrySet().iterator()));
        }
    }

    public static void main(String[] args) {
        final Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        Observable<Map.Entry<String, Integer>> source = ObservableEx.fromMap(map);
        source.subscribe(System.out::println);
    }

}
