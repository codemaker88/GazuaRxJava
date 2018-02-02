# GazuaRxJava
RxJava 가즈아~~!

---
## Problem 1 (chap 1, 2)

많은 분들이 예상하신 57페이지 문제입니다.

fromMap 함수를 구현한 Observable클래스의 상속 클래스 ObservableForMap 클래스를 만드시오.

- Observable<V> fromMap(Comparator<K> comparator, Map<K,V> map)함수
- Observable<Map.Entry<K,V>> fromMap(Comparator<K> comparator, Map<K,V> map)함수
- 레퍼런스 comparator 순서대로 Key가 정렬되어 V가 나오는 콜드 옵저버블이 나오면 됩니다.
- C#에는 확장 메소드 문법이 있습니다. 메소드 하나만 추가되므로 확장메소드 구현도 상관 없습니다.
- C#문법은 from 이 아닌 to 방식입니다. C# rx의 형태에 맞게 IObservable<V> ToObservable<K, V>(this Dictionary<K, V> map, IComparer<K> comparer) 및 
IObservable<KeyValuePair<K, V>> ToObservablePair<K, V>(this Dictionary<K, V> map, IComparer<K> comparer) 함수 구현해주심 됩니당.

# RxJava 3챕터 #
## 3 ##
- 2의 거듭제곱에서 1이 모자란수를 메르센수라고 합니다.
- 메르센수중에 소수인것을 통해 완전수를 도출할 수 있습니다. 
- 메르센수 29개를 발행하고 메르센소수를 도출하여 완전수를 구하고 몇번째 메르센소수인지와 완전수들의 합을 도출합니다.
<pre>
ex) 메르센수 3개를 발행했을때 메르센수는 1,3,7이고 그중 소수는 3,7입니다.
    메르센소수를 가지고 구한 완전수는 6, 28 이고 메르센수의 인덱스와 완전수의 합은 2+3+6+28 = 39입니다.
</pre>
링크)https://ko.wikipedia.org/wiki/%EB%A9%94%EB%A5%B4%EC%84%BC_%EC%86%8C%EC%88%98
