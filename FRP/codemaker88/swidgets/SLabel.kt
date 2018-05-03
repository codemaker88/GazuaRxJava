package com.codemaker.frp_sample.swidgets

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import nz.sodium.Cell
import nz.sodium.Listener
import nz.sodium.Operational
import nz.sodium.Transaction


/**
 * Created by CodeMaker
 */
class SLabel(context: Context, text: Cell<String>) : AppCompatTextView(context) {
    private val l: Listener

    init {
        l = Operational.updates(text).listen { t -> setText(t) }

        // Set the text at the end of the transaction so SLabel works
        // with CellLoops.
        Transaction.post { setText(text.sample()) }
    }

    fun release() {
        l.unlisten()
    }
}