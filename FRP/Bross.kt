  fun spinnerExample(layout: LinearLayout) {
        Transaction.runVoid({
            val sSetValue = StreamLoop<Int>()
            val sEditText = SEditText(this, sSetValue.map({ it.toString() }), "0")

            val value = sEditText.text.map({ it.toInt() })

            val plus = SButton(this, "+")
            val minus = SButton(this, "-")

            val sPlusDelta = plus.sClicked.map({ 1 })
            val sMiunsDelta = minus.sClicked.map({ -1 })
            val sDelta = sPlusDelta.orElse(sMiunsDelta)

            sSetValue.loop(
                    sDelta.snapshot(value, { v: Int, i: Int -> v + i })
            )

            layout.addView(plus)
            layout.addView(minus)
            layout.addView(sEditText)
        })

    }


    fun colorExample(layout: LinearLayout) {

        val red = SButton(this, "red")
        val green = SButton(this, "green")

        val sRed = red.sClicked.map({ "red" })
        val sGreen = green.sClicked.map({ "green" })

        val sColor = sRed.orElse(sGreen)

        val color = sColor.hold("")

        val sLabel = STextView(this, color)

        layout.addView(red)
        layout.addView(green)
        layout.addView(sLabel)
    }
    
    
    public class STextView extends android.support.v7.widget.AppCompatTextView{

    private final Listener l;

    public STextView(Context context){
        super(context);
        l = null;
    }

    public STextView(final Context context, Cell<String> text) {
        super(context);

        l = Operational.updates(text).listen(t -> {

            post(() -> setText(t));

        });
        // Set the text at the end of the transaction so SLabel works
        // with CellLoops.
        Transaction.post(
                () -> post(()->setText(text.sample())));
    }



    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        l.unlisten();
    }

}



public class SEditText extends AppCompatEditText {


    private StreamSink<Integer> sDecrement = new StreamSink<>();
    private Cell<Boolean> allow;
    private Listener l;
    public Cell<String> text;
    public Stream<String> sUserChanges;

    public SEditText(Context context) {
        super(context);
        init(new Stream<String>(), "", new Cell<>(true));
    }
    public SEditText(Context context, Stream<String> sText, String initText) {
        super(context);
        init(sText, initText, new Cell<>(true));
    }

    private void init(Stream<String> sText, String initText, Cell<Boolean> enabled) {
        allow = sText.map(u -> 1)  // Block local changes until remote change has
                // been completed in the GUI
                .orElse(sDecrement)
                .accum(0, (d, b) -> b + d).map(b -> b == 0);

        final StreamSink<String> sUserChangesSnk = new StreamSink<String>();
        this.sUserChanges = sUserChangesSnk;
        this.text = sUserChangesSnk.gate(allow).orElse(sText).hold(initText);


        OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                update(textView.getText().toString());
                return false;
            }

            public void update(final String text) {
                post(() -> {
                    if (text != null) {
                        sUserChangesSnk.send(text);
                    }
                });
            }
        };

        setOnEditorActionListener(onEditorActionListener);

        // Do it at the end of the transaction so it works with looped cells
        Transaction.post(() -> setEnabled(enabled.sample()));
        l = sText.listen(text -> post(() -> {
            setOnEditorActionListener(null);
            setText(text);
            setOnEditorActionListener(onEditorActionListener);
            sDecrement.send(-1);  // Re-allow blocked remote changes

        })).append(
                Operational.updates(enabled).listen(
                        ena -> post(() -> setEnabled(ena)))
        );
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        l.unlisten();
    }
}


public class SButton extends android.support.v7.widget.AppCompatButton{

    private Listener l;
    public Stream<Unit> sClicked;

    public SButton(Context context){
        super(context);
        init(new Cell<>(true));
    }
    public SButton(Context context, String text){
        super(context);
        setText(text);
        init(new Cell<>(true));
    }

    public SButton(final Context context, Cell<Boolean> enabled) {
        super(context);
        init(enabled);
    }

    private void init(Cell<Boolean> enabled){
        StreamSink<Unit> sClickedSink = new StreamSink<>();
        this.sClicked = sClickedSink;

        setOnClickListener(view -> {
            sClickedSink.send(Unit.UNIT);
        });

        Transaction.post(() -> setEnabled(enabled.sample()));
        l = Operational.updates(enabled).listen(
                ena -> {

                    post(() -> {
                        setEnabled(ena);
                    });
                }
        );
    }



    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        l.unlisten();
    }

}
