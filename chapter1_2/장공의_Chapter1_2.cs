using Chapter1_2.ExtensionMethods;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Linq;
using static System.Diagnostics.Debug;

namespace Chapter1_2
{
    public class ObservableForDictionary
    {
        public ObservableForDictionary()
        {
            // ExampleObservableFromSortedDictionary();
            // ExampleObservableFromDictionary();
            // ExampleObservableFromList();

            ObservableFromDictionaryLikeOrder();
        }

        private void ObservableFromDictionaryLikeOrder()
        {
            var source = new Dictionary<int, string>
            {
                [1] = "A",
                [2] = "B",
                [3] = "C"
            };

            source.ToObservableLikeOrder(Comparer<int>.Default)
                .Subscribe(pair => {
                    WriteLine("key " + pair.Key);
                    WriteLine("Value " + pair.Value);
                }
            );
        }

        private void ExampleObservableFromSortedDictionary()
        {
            var source = new SortedDictionary<int, string>
            {
                [3] = "C",
                [1] = "A",
                [2] = "B"
            };
            SourceToObserable(source);
        }

        private void ExampleObservableFromDictionary()
        {
            var source = new Dictionary<int, string>
            {
                [1] = "A",
                [2] = "B",
                [3] = "C"
            };
            SourceToObserable(source);
        }

        private void ExampleObservableFromList()
        {
            var source = new List<string> { "A", "B", "C" };
            SourceToObserable(source);
        }

        private void SourceToObserable<T>(IEnumerable<T> source)
        {
            Observable.ToObservable(source).Subscribe(x => WriteLine(x));
            source.ToObservable().Subscribe(x => WriteLine(x));
        }
    }

    namespace ExtensionMethods
    {
        public static class ObservableFromDictionaryLikeOrderExtension
        {
            public static IObservable<KeyValuePair<K, V>> ToObservableLikeOrder<K, V>(this Dictionary<K, V> source, Comparer<K> comparer)
                => source.OrderBy((pair) => pair.Key, comparer).ToObservable();
        }
    }
}