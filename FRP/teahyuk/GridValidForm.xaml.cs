using System.Windows;
using System.Windows.Controls;
using Sodium;
using SWidgets;
using SSpinner;
using System;
using System.Collections.Generic;
using System.Linq;


namespace formValidation
{
    /// <summary>
    /// MainWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class GridValidForm : Grid
    {
        readonly int maxEmails = 4;

        public GridValidForm()
        {
            InitializeComponent();

            Transaction.RunVoid(() =>
            {
                STextBox nameBox = new STextBox();
                Cell<string> nameLabel = nameBox.CText.Map(t =>
                {
                    if (string.IsNullOrWhiteSpace(t))
                        return "입력해라";
                    else if (t.Trim().IndexOf(" ") < 0)
                        return "성씨가 뭐냐 스페이스바를 넣어라.";
                    else
                        return string.Empty;
                });
                SLabel nameCheckLabel = new SLabel(nameLabel);

                this.nameBox.Children.Add(nameBox);
                this.nameValidateLabel.Children.Add(nameCheckLabel);

                Sspinner emailCounter = new Sspinner(1);
                Cell<string> emailCountLabel = emailCounter.Value.Map(v =>
                    v < 1 || v > maxEmails ?
                        "이메일은 1개부터 4개다잉~ 잘해라잉" :
                        string.Empty);
                SLabel CounterCheckLabel = new SLabel(emailCountLabel);

                this.spinner.Children.Add(emailCounter);
                this.numberValidateLabel.Children.Add(CounterCheckLabel);


                var emailLists = new List<Tuple<Grid, Grid>>()
                {
                    new Tuple<Grid, Grid>(this.eMailBox1, this.eMailValidateLabel1),
                    new Tuple<Grid, Grid>(this.eMailBox2, this.eMailValidateLabel2),
                    new Tuple<Grid, Grid>(this.eMailBox3, this.eMailValidateLabel3),
                    new Tuple<Grid, Grid>(this.eMailBox4, this.eMailValidateLabel4),
                };

                var emailValids = emailLists.Select((t, i) =>
                {
                    Cell<bool> enabled = emailCounter.Value.Map(v => v > i);
                    var emailBox = new STextBox(string.Empty, enabled);
                    Cell<string> emailLabel = enabled.Lift(emailBox.CText.Map(t_ =>
                    {
                        if (string.IsNullOrWhiteSpace(t_))
                            return "입력해라";
                        else if (t_.Trim().IndexOf("@") < 0)
                            return "골뱅이 어따 팔아먹었냐~ 넣지??";
                        else
                            return string.Empty;
                    }),
                    (_1, _2) => _1 ? _2 : string.Empty);
                    var emailCheckLabel = new SLabel(emailLabel);

                    t.Item1.Children.Add(emailBox);
                    t.Item2.Children.Add(emailCheckLabel);
                    return emailLabel;
                });

                var allOk = emailValids.Concat(new []{ nameLabel, emailCountLabel }).Lift(v=>v.All(string.IsNullOrWhiteSpace));

                var OkButton = new SButton(allOk)
                {
                    Content = "확인"
                };
                this.okButton.Children.Add(OkButton);
            });
        }
    }
}
