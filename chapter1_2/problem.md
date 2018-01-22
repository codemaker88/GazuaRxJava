# RXjava 1,2챕터 #

## 1 ##

많은 분들이 예상하신 57페이지 문제입니다.

fromMap 함수를 구현한 Observable클래스의 상속 클래스 ObservableForMap 클래스를 만드시오.

- Observable<V> fromMap(Comparator<K> comparator, Map<K,V> map)함수
- Observable<Map.Entry<K,V>> fromMap(Comparator<K> comparator, Map<K,V> map)함수
- 레퍼런스 comparator 순서대로 Key가 정렬되어 V가 나오는 콜드 옵저버블이 나오면 됩니다.
- C#에는 확장 메소드 문법이 있습니다. 메소드 하나만 추가되므로 확장메소드 구현도 상관 없습니다.
