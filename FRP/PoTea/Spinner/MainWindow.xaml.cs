using System.Windows;
using Sodium;
using SWidgets;

namespace Spinner
{
    public partial class MainWindow
    {
        public MainWindow()
        {
            this.InitializeComponent();

            Transaction.RunVoid(() =>
            {
                // 책의 자바 예제와 다른점은 CellLoop를 사용
                CellLoop<int> value = new CellLoop<int>();

                // UI요소 생성 및 등록 부분
                SLabel lblValue = new SLabel(value.Map(i => i.ToString()));
                SButton plus = new SButton { Content = "+", Width = 25, Margin = new Thickness(5, 0, 0, 0) };
                SButton minus = new SButton { Content = "-", Width = 25, Margin = new Thickness(5, 0, 0, 0) };

                this.Container.Children.Add(lblValue);
                this.Container.Children.Add(plus);
                this.Container.Children.Add(minus);

                // 각 버튼의 기능 스트림과 표시에 대한 스냅샷 연결
                Stream<int> sPlusDelta = plus.SClicked.Map(_ => 1);
                Stream<int> sMinusDelta = minus.SClicked.Map(_ => -1);
                Stream<int> sDelta = sPlusDelta.OrElse(sMinusDelta);
                Stream<int> sUpdate = sDelta.Snapshot(value, (d, v) => v + d);
                value.Loop(sUpdate.Hold(0));
            });
        }
    }
}