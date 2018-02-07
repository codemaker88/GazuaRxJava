// ObservableForMap.java

import io.reactivex.Observable;
import io.reactivex.Observer;

import java.util.Comparator;
import java.util.Map;


public class ObservableForMap<T> extends Observable<T> {

    public static <K, V> Observable<Map.Entry<K,V>> fromMap(Comparator<Map.Entry<K,V>> comparator, Map<K,V> map) {
        return Observable.create(s -> {
            try {
                map.entrySet().stream().sorted(comparator).forEach(i -> s.onNext(i));
                s.onComplete();
            } catch (Exception e) {
                s.onError(e);
            }

        });
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
    }
}


// HelloRx.java
import java.util.HashMap;
import java.util.Map;


public class HelloRx {

    public static void main(String[] args) {

        Map<String, Integer> src = new HashMap<>();
        src.put("B", 4);
        src.put("A", 5);
        src.put("C", 3);
        src.put("E", 1);
        src.put("D", 2);

        System.out.println("Key order");
        ObservableForMap.fromMap(Map.Entry.comparingByKey(), src).subscribe(System.out::println);
        System.out.println("Value order");
        ObservableForMap.fromMap(Map.Entry.comparingByValue(), src).subscribe(System.out::println);
    }
}

