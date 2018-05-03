package com.codemaker.frp_sample.swidgets

import android.content.Context
import android.widget.Button
import nz.sodium.*
import nz.sodium.Unit


/**
 * Created by CodeMaker
 */
class SButton(context: Context, label: String, enabled: Cell<Boolean> = Cell(true)) : Button(context) {

    private val l: Listener
    val sClicked: Stream<Unit>

    init {
        text = label
        val sClickedSink = StreamSink<Unit>()
        sClicked = sClickedSink

        setOnClickListener({ sClickedSink.send(Unit.UNIT) })

        // Do it at the end of the transaction so it works with looped cells
        Transaction.post { isEnabled = enabled.sample() }
        l = Operational.updates(enabled).listen { enabled -> this.isEnabled = enabled }
    }

    fun release() {
        l.unlisten()
    }
}