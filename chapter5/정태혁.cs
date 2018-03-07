using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Reactive;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using System.Reactive.Concurrency;
using System.Reactive.PlatformServices;
using System.Collections;

namespace ConsoleApp1
{
    class Program
    {
        public static void Main()
        {
            var network = new GetLottoData();

            var 데이터 = new PrintLotto();

            //api 옵저버블 생성
            var pub = new Subject<Dictionary<string, string>>();
            pub.Subscribe(맵 => 당첨자_추가(맵, 데이터),
                          ()=> 프린트(데이터));

            Observable.Range(1, 1000)
                .SubscribeOn(ThreadPoolScheduler.Instance)
                .ObserveOn(ThreadPoolScheduler.Instance)
                .TakeWhile(_ => network.ResponseLottoJson(_, pub.OnNext))
                .Subscribe();

            Console.ReadLine();
        }

        public static void 당첨자_추가(Dictionary<string,string> 맵, PrintLotto 데이터)
        {
            var 당첨수 = int.Parse(맵["firstPrzwnerCo"]);
            당첨수 = 당첨수 == 0 ? 1 : 당첨수;

            Observable.Range(1, 6)
               .Subscribe(_ => 데이터.NumbersCnt[int.Parse(맵["drwtNo" + _])] += 당첨수);

            데이터.NumbersCnt[int.Parse(맵["bnusNo"])] += 당첨수;
        }

        public static void 프린트(PrintLotto 데이터)
        {
            Console.WriteLine("최다노출");
            데이터.PrintMax();

            Console.WriteLine("최다노출");
            데이터.PrintMin();
        }
    }

    class PrintLotto
    {
        public int[] NumbersCnt = new int[45+1];
        ArrayList NumbersRank;

        public void PrintMax()
        {
            NumbersRank = new ArrayList(Enumerable.Range(1,45).ToList());
            NumbersRank.Sort(1, 45, Comparer<int>.Create((x, y) => NumbersCnt[x].CompareTo(NumbersCnt[y])));

            Observable.Range(0, 45)
                .TakeWhile(_ => NumbersCnt[(int)NumbersRank[_]] >= NumbersCnt[(int)NumbersRank[6]])
                .Subscribe(_=> Console.Write(_+" "));
        }

        public void PrintMin()
        {
            NumbersRank = new ArrayList(Enumerable.Range(1, 45).ToList());
            NumbersRank.Sort(1, 45, Comparer<int>.Create((x, y) => NumbersCnt[y].CompareTo(NumbersCnt[x])));

            Observable.Range(0, 45)
                .TakeWhile(_ => NumbersCnt[(int)NumbersRank[_]] <= NumbersCnt[(int)NumbersRank[6]])
                .Subscribe(_ => Console.Write(_ + " "));
        }
    }

    class GetLottoData
    {
        string lottoUri = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=";

        public bool ResponseLottoJson(int drwNo, Action<Dictionary<string,string>> responseAction)
        {
            var wrGETURL = WebRequest.Create(string.Format("{0}{1}",lottoUri,drwNo));

            try
            {
                using (var reader = new StreamReader(wrGETURL.GetResponse().GetResponseStream(), Encoding.UTF8, true))
                {
                    var map = JsonConvert.DeserializeObject<Dictionary<string, string>>(reader.ReadToEnd());
                    if (map["returnValue"].Equals("fail"))
                        return false;
                    responseAction(map);
                }
            }
            catch
            {
                return false;
            }

            return true;
        }
    }
}
