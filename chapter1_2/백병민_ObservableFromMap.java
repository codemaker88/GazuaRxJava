package rxjavachapter1and2;

import io.reactivex.Observable;
import io.reactivex.Observer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class 백병민_ObservableFromMap<K, V> extends Observable {
    private final Map<K, V> source;
    private final Comparator comparator;

    private 백병민_ObservableFromMap(Comparator comparator, Map<K, V> source) {
        this.source = source;
        this.comparator = comparator;
    }

    public static <SK, SV> Observable<Map.Entry<SK, SV>> fromMap(Comparator comparator, Map<SK, SV> map) {
        return new 백병민_ObservableFromMap(comparator, map);
    }

    @Override
    protected void subscribeActual(Observer s) {
        try {
            this.source.entrySet().parallelStream().sorted(comparator).forEachOrdered(e -> s.onNext(e));
            s.onComplete();
        } catch (Exception e) {
            s.onError(e);
        }
    }

    public static void main(String[] args) {
        Map<String, Object> a = new HashMap<>();
        a.put("1", "3");
        a.put("2", "1");
        a.put("3", "2");

        백병민_ObservableFromMap.fromMap(Map.Entry.comparingByKey(), a).subscribe(System.out::println, System.err::println, () -> System.out.println("Key order complete"));
        백병민_ObservableFromMap.fromMap(Map.Entry.comparingByValue(), a).subscribe(System.out::println, System.err::println, () -> System.out.println("Value order complete"));
    }
}
