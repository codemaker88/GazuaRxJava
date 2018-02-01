package com.yudong80.reactivejava.chapter01;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class FirstExample {

	@SuppressWarnings("rawtypes")
	static class ObservableForMap extends Observable {
		public static <K, V> Observable<Map.Entry<K, V>> fromMap(Comparator<K> comparator, Map<K, V> map) {
			Map<K, V> sortedMap = sortByValue(comparator, map);
			return Observable.create(e -> {
				for (Map.Entry<K, V> entry : sortedMap.entrySet()) {
					e.onNext(entry);
				}
				e.onComplete();
			});
		}

		@Override
		protected void subscribeActual(Observer observer) {
			// TODO Auto-generated method stub
		}
	}

	public static <K, V> Map<K, V> sortByValue(Comparator<K> comparator, Map<K, V> map) {
		return map.entrySet().stream().sorted(Map.Entry.comparingByKey(comparator))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public static <K, V> Observable<V> fromMap(Comparator<K> comparator, Map<K, V> map) {
		Map<K, V> sortedMap = sortByValue(comparator, map);
		return Observable.fromIterable(sortedMap.values());
	}

	public static void main(String args[]) {
		Map<String, String> testMap = new HashMap<>();
		testMap.put("1", "One");
		testMap.put("2", "Two");
		testMap.put("3", "Three");
		testMap.put("A", "A");
		testMap.put("C", "C");

		Observable<String> source = ObservableForMap.fromIterable(testMap.values());
		source.subscribe(System.out::println);

		System.out.println("-------");

		Observable<Map.Entry<String, String>> converted_source = ObservableForMap.fromMap(Collections.reverseOrder(),
				testMap);
		converted_source.subscribe(System.out::println);

		System.out.println("-------");

		Observable<String> converted_fun_source = fromMap(Collections.reverseOrder(), testMap);
		converted_fun_source.subscribe(System.out::println);
	}

}