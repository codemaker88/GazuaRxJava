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

---
## Problem 2 (chap 3)

- 2의 거듭제곱에서 1이 모자란수를 메르센수라고 합니다.
- 오일러의 정리에 따르면 모든짝수 완전수는 메르센소수를 통하여 도출할 수 있다고 증명했습니다.
- 메르센수 29개를 발행하고 메르센소수를 도출하여 완전수를 구하고 몇번째 메르센소수인지와 완전수들의 합을 도출합니다.
<pre>
ex) 메르센수 3개를 발행했을때 메르센수는 1,3,7이고 그중 소수는 3,7입니다.
    메르센소수를 가지고 구한 완전수는 6, 28 이고 메르센수의 인덱스와 완전수의 합은 2+3+6+28 = 39입니다.
</pre>
링크)https://ko.wikipedia.org/wiki/%EB%A9%94%EB%A5%B4%EC%84%BC_%EC%86%8C%EC%88%98

---
## Problem 3 (chap 5)

5장을 읽다보니 옛 코틀린의 추억이 떠오르네요.
추억의 문제를 꺼내겠습니다. [문제바로가기](https://github.com/potea/m_kotlin/blob/master/problem3/problem.md)
(Kotlin도 3번 문제군요!)

### 로또 수열

로또 번호들을 보던도중 재밌는 수열이 생각났습니다.<br>
매회 당첨번호에 당첨자들의 수를 곱한 누적값을 기준으로 정렬합니다.<br>
단, 당첨자가 0일 경우 1을 더해줍니다.<br>
이를 기준으로 통계값이 가장 높은 수열과 가장 낮은 수열을 구합니다.<br>
만약, 7번째로 등장한 숫자의 통계값이 8-10번째와 같다면 모두 출력합니다.

```text
예를 들어 당첨번호가 1,2,3,4,5,6,7 일때 1등 수가 8이라면,
lottoCnt[1]+=8
lottoCnt[2]+=8
lottoCnt[3]+=8
lottoCnt[4]+=8
lottoCnt[5]+=8
lottoCnt[6]+=8
lottoCnt[7]+=8

1등수가 0 이라면,
lottoCnt[1]++
lottoCnt[2]++
lottoCnt[3]++
lottoCnt[4]++
lottoCnt[5]++
lottoCnt[6]++
lottoCnt[7]++

API 문서 
API LINK : http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=

example : http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=644

output : {"bnusNo":8,"firstWinamnt":1831451204,"totSellamnt":61846599000,"returnValue":"success","drwtNo3":17,"drwtNo2":13,"drwtNo1":5,"drwtNo6":36,"drwtNo5":28,"drwtNo4":23,"drwNoDate":"2015-04-04","drwNo":644,"firstPrzwnerCo":8}
```

#### ~~제한~~ 권장사항

* 가장 간결한 람다형식으로 변경합니다.
* 외부 라이브러리는 사용하지 않습니다.
* flatmap을 사용합니다.
* 요청한 URL의 json 형식은 파싱을 통해 map 형태로 변환합니다.

#### 메인

Kotlin의 내용이지만 목표는 로또수열을 최다노출/최소노출 모두 출력하는 것입니다.

```kotlin

fun main(args: Array<String>) {

    val lottoNumberSequence : lottoNumberSequence = LottoNumberSequence()
    lottoNumberSequence print MAX_EXPOSE // 최다 노출 숫자형
    lottoNumberSequence print MIN_EXPOSE // 최소 노출 숫자형
}

```


#### 출력
```text
로또 수열 출력 ~ [최다 노출 기준]
1. 1, 2, 3, 4, 5, 6, 7 
          .
          .
          .
          
=> 마지막 자릿수의 숫자 7과 숫자 10 노출횟수가 200번으로 같을경우 출력
1. 1, 2, 3, 4, 5, 6, 7 
2. 1, 2, 3, 4, 5, 6, 10
          
 
로또 수열 출력 ~ [최소 노출 기준]
1. 15, 16, 17, 18, 19, 20, 21 
         .
         .
         .
 
=> 마지막 자릿수의 숫자 21과 숫자 22 노출횟수가 63번으로 같을경우 출력
1. 15, 16, 17, 18, 19, 20, 21 
2. 15, 16, 17, 18, 19, 20, 22
         .
         .
         .

```

#### 선택사항
```text
API 요청 횟수가 많아 속도가 느립니다.
이를 개선해보세요.
```



