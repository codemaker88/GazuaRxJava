using System;
using System.Reactive.Linq;
using System.Collections.Generic;
using System.Reactive.Subjects;
using System.Reactive.Concurrency;

namespace ConsoleApp1
{
    class Program
    {
        static void Main(string[] args)
        {
            var MaxVal = 29;
            var root = (int)Math.Sqrt(MaxVal);

            //소수 체
            var isPrime = new bool[MaxVal + 1];
            Observable.Range(2, MaxVal - 1, Scheduler.Immediate)
                .Subscribe(_ => isPrime[_] = true);

            //체 걸러내어 MaxVal안의 소수인 수만 뽑기.
            Observable.Range(2, root, Scheduler.Immediate)
                .Subscribe(_1 =>
                {
                    if (isPrime[_1])
                        Observable.Range(_1, MaxVal)
                            .Select(_2 => _2 * _1)
                            .TakeWhile(_2 => _2 <= MaxVal)
                            .Subscribe(_2 => isPrime[_2] = false);
                });

            Subject<int> subject = new Subject<int>();

            //테스트 출력
            subject.Aggregate(0L, (sum, _) =>
            {
                Console.Write(string.Format("n : {0}({1})", _, GetMersenn(_)));
                if (isPrime[_] && IsPrimeMercenn(_))
                {
                    Console.WriteLine(string.Format(" true {0} + {1} = {2}", _, GetPerfectNumber(_), _ + GetPerfectNumber(_)));
                    sum += _ + GetPerfectNumber(_);
                }
                else
                    Console.WriteLine(" false");

                return sum;
            })
            .Subscribe(Console.WriteLine);
            //

            //실 문제풀이 
            subject.Where(_ => isPrime[_])              //idx가 소수인지 검증하는게 훨씬 빠름 (위에서 구한 체 사용)
                .Where(IsPrimeMercenn)                  //역은 참이 아니므로 검증단계 필요.
                .Sum(_ => _ + GetPerfectNumber(_))      //idx와 완전수 합친수 더하면 됨.
                .Subscribe(Console.WriteLine);          //출력

            Observable.Range(1, MaxVal)
                .Subscribe(subject);

            Console.ReadLine();
        }

        static long GetMersenn(int n) => (int)Math.Pow(2, n) - 1;

        static long GetPerfectNumber(int n) => GetMersenn(n) * (GetMersenn(n) + 1) / 2;

        static void PrintProblem(int n)
        {
            Console.WriteLine(string.Format("{0}+{1}= {2}", n, GetPerfectNumber(n), n + GetPerfectNumber(n)));
        }

        /// <summary>
        ///     메르센 소수의 index인지 확인.
        /// </summary>
        /// <param name="n"></param>
        /// <returns></returns>
        static bool IsPrimeMercenn(int n)
        {
            var mercenn = GetMersenn(n);
            var maxFactor = (int)Math.Sqrt(mercenn);
            bool isPrime = true;
            Observable.Range(2, maxFactor, Scheduler.Immediate)
                .TakeWhile(_ => _ <= maxFactor)
                .Any(_ => mercenn % _ == 0)
                .Subscribe(_ => isPrime = !_);
            return isPrime;
        }
    }
}
/*출력
n(3) : 2 true 2 + 6 = 8
n(7) : 3 true 3 + 28 = 31
n(15) : 4 false
n(31) : 5 true 5 + 496 = 501
n(63) : 6 false
n(127) : 7 true 7 + 8128 = 8135
n(255) : 8 false
n(511) : 9 false
n(1023) : 10 false
n(2047) : 11 false
n(4095) : 12 false
n(8191) : 13 true 13 + 33550336 = 33550349
n(16383) : 14 false
n(32767) : 15 false
n(65535) : 16 false
n(131071) : 17 true 17 + 8589869056 = 8589869073
n(262143) : 18 false
n(524287) : 19 true 19 + 137438691328 = 137438691347
n(1048575) : 20 false
n(2097151) : 21 false
n(4194303) : 22 false
n(8388607) : 23 false
n(16777215) : 24 false
n(33554431) : 25 false
n(67108863) : 26 false
n(134217727) : 27 false
n(268435455) : 28 false
146062119444
146062119444
*/
