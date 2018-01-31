using System;
using System.Reactive.Linq;
using System.Collections.Generic;

namespace ConsoleApp1
{
    class Program
    {
        static void Main(string[] args)
        {
            var map = new Dictionary<int, string>()
            {
                {1,"일" },
                {3,"삼" },
                {2,"이" },
                {13,"오" },
                {8,"사" }
            };
            var reverse = Comparer<int>.Create((x, y) => x < y ? 1 : -1);
            var forward = Comparer<int>.Create((x, y) => x > y ? 1 : -1);
                

            map.ToObservable(forward)
                .Subscribe(Console.WriteLine);

            Console.WriteLine();

            map.ToObservable(reverse)
                .Subscribe(Console.WriteLine);

            Console.WriteLine();

            map.ToObservablePair(forward)
                .Subscribe( x =>
                {
                    Console.WriteLine(string.Format("{0} : {1}", x.Key, x.Value));
                });

            Console.WriteLine();

            map.ToObservablePair(reverse)
                .Subscribe(x =>
                {
                    Console.WriteLine(string.Format("{0} : {1}", x.Key, x.Value));
                });

            Console.WriteLine();

            //함수형 입력.
            map.ToObservable((x, y) => x > y ? 1 : -1)
                .Subscribe(Console.WriteLine);

            Console.ReadLine();
        }

    }

    static class DictionaryToObservable
    {
        public static IObservable<V> ToObservable<K, V>(this Dictionary<K, V> map, IComparer<K> comparer)
        {
            return new SortedDictionary<K, V>(map, comparer).Values.ToObservable();
        }

        public static IObservable<KeyValuePair<K, V>> ToObservablePair<K, V>(this Dictionary<K, V> map, IComparer<K> comparer)
        {
            return new SortedDictionary<K, V>(map, comparer).ToObservable();
        }

        public static IObservable<V> ToObservable<K, V>(this Dictionary<K, V> map, Comparison<K> comparer)
        {
            return new SortedDictionary<K, V>(map, Comparer<K>.Create(comparer)).Values.ToObservable();
        }

        public static IObservable<KeyValuePair<K, V>> ToObservablePair<K, V>(this Dictionary<K, V> map, Comparison<K> comparer)
        {
            return new SortedDictionary<K, V>(map, Comparer<K>.Create(comparer)).ToObservable();
        }
    }
}

/* 출력
일
이
삼
사
오

오
사
삼
이
일

1 : 일
2 : 이
3 : 삼
8 : 사
13 : 오

13 : 오
8 : 사
3 : 삼
2 : 이
1 : 일

일
이
삼
사
오

*/
