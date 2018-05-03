package com.codemaker.frp_sample.swidgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import nz.sodium.Cell
import nz.sodium.StreamLoop
import nz.sodium.Transaction


/**
 * Created by CodeMaker
 */
class SSpinner : LinearLayout {

    lateinit var value: Cell<Int>

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, initValue: Int) : super(context) {
        init(initValue)
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        init()
    }

    fun init(initValue: Int = 0) {
        Transaction.runVoid {
            val sSetValue = StreamLoop<Int>()
            val textField = STextField(context,
                    sSetValue.map<String> { v -> Integer.toString(v) },
                    Integer.toString(initValue)
            )
            this.value = textField.text.map { txt -> txt.toInt() }

            val plus = SButton(context, "+")
            val minus = SButton(context, "-")
            addView(textField)
            addView(plus)
            addView(minus)
            val sPlusDelta = plus.sClicked.map<Int> { u -> 1 }
            val sMinusDelta = minus.sClicked.map<Int> { u -> -1 }
            val sDelta = sPlusDelta.orElse(sMinusDelta)
            sSetValue.loop(sDelta.snapshot<Int, Int>(this.value) { delta, value_ -> (delta + value_) })
        }
    }
}