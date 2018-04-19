using Sodium;
using System.Windows.Controls;
using SWidgets;
using System;
using System.Windows;

namespace SSpinner
{
    /// <summary>
    /// Sspinner.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class Sspinner : Grid, IDisposable
    {
        public Sspinner() : this(0) { }

        public Sspinner(int initVal)
        {
            Transaction.RunVoid(() =>
            {
                StreamLoop<int> sSetValue = new StreamLoop<int>();
                STextBox textBox = new STextBox(
                    sSetValue.Map(v => v.ToString()),
                    initVal.ToString(),
                    Cell.Constant(true));

                this.Value = textBox.CText.Map(txt => int.TryParse(txt, out int val) ? val : 0);

                //initializeComponent
                InitializeComponent();
                textBox.HorizontalContentAlignment = HorizontalAlignment.Center;
                textBox.VerticalAlignment = VerticalAlignment.Center;
                this.valueTextBoxPlaceHolder.Children.Add(textBox);

                Stream<int> sPlusDelta = this.plusButton.SClicked.Map(u => 1);
                Stream<int> sMinusDelta = this.minusButton.SClicked.Map(u => -1);
                Stream<int> sDelta = sPlusDelta.OrElse(sMinusDelta);
                sSetValue.Loop(
                    sDelta.Snapshot(
                        this.Value,
                        (delta, value) => delta + value));
            });
        }

        public Cell<int> Value { get; private set; }

        public void Dispose()
        {
        }
    }
}
