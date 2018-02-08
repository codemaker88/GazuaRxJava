using System;
using System.Diagnostics;
using System.Linq;
using System.Reactive.Linq;
using System.Reactive.Subjects;

namespace ConsoleApp2
{
    class Program
    {
        static void Main(string[] args)
        {
            const int SIZE_BOUND = 29;
            bool[] isPrime = Enumerable.Repeat(true, SIZE_BOUND + 2).ToArray();
            ReplaySubject<int> primeNumberSubject = new ReplaySubject<int>();

            Observable.Range(2, SIZE_BOUND)
                .SelectMany(i =>
                {
                    if (isPrime[i])
                    {
                        primeNumberSubject.OnNext(i);
                        return Observable.Empty<int>();
                    }
                    return Observable.Generate(
                           i * i,
                           x => x <= Math.Sqrt(SIZE_BOUND),
                           x => x + i,
                           x => x);
                })
                .Subscribe(onNext: v => isPrime[v] = false,
                           onCompleted: primeNumberSubject.OnCompleted);

            primeNumberSubject
                .Select(primeNumber => new NumProvider(primeNumber))
                .Where(np => IsNotPrimeFactor(np.PrimeNumberOfMersenne))
                // .Where(np =>
                // {
                //     Debug.WriteLine("소수 ->  " + np.PrimeNumber);
                //     Debug.WriteLine("메르센 소수 ->  " + np.PrimeNumberOfMersenne);
                //     Debug.WriteLine("도출된 완전수 ->  " + np.PerfectNumber + "\n");
                //     return true;
                // })
                .Select(np => np.PerfectNumber + np.PrimeNumber)
                .Sum()
                .Subscribe(result => Debug.WriteLine(result));
        }

        private static bool IsNotPrimeFactor(Int64 num)
        {
            for (int i = 2; i <= Math.Sqrt(num); i++)
                if (num % i == 0) return false;
            return true;
        }
    }

    public struct NumProvider
    {
        public int PrimeNumber { get; }
        public Int64 PrimeNumberOfMersenne { get; }
        public Int64 PerfectNumber { get; }

        public NumProvider(int primeNumber) : this()
        {
            Int64 shiftNum = (1L << primeNumber);
            PrimeNumber = primeNumber;
            PrimeNumberOfMersenne = shiftNum - 1;
            PerfectNumber = shiftNum * PrimeNumberOfMersenne / 2;
        }
    };
}
