package com.codemaker.frp_sample.swidgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import nz.sodium.CellLoop
import nz.sodium.Transaction

/**
 * Created by CodeMaker
 */
class SSpinner1 : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    init {
        Transaction.runVoid {
            val value = CellLoop<Int>()
            val lblValue = SLabel(context!!,
                    value.map({ i -> Integer.toString(i) }))
            val plus = SButton(context, "+")
            val minus = SButton(context, "-")
            addView(lblValue)
            addView(plus)
            addView(minus)
            val sPlusDelta = plus.sClicked.map<Int> { u -> 1 }
            val sMinusDelta = minus.sClicked.map<Int> { u -> -1 }
            val sDelta = sPlusDelta.orElse(sMinusDelta)
            val sUpdate = sDelta.snapshot(value) { delta, value_ -> (delta + value_) }
            value.loop(sUpdate.hold(0))
        }
    }
}