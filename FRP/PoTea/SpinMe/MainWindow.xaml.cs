using SWidgets;

namespace SpinMe
{
    public partial class MainWindow
    {
        public MainWindow()
        {
            this.InitializeComponent();

            SSpinner spinner = SSpinner.Create(0);
            // 생성된 SSpinner를 생성하고 컨테이너에 추가하는 것으로 끝!
            spinner.Width = 75;
            this.Container.Children.Add(spinner);
        }
    }
}