package com.codemaker.frp_sample.swidgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import nz.sodium.*


/**
 * Created by CodeMaker
 */
class STextField(context: Context, sText: Stream<String> = Stream(),
                 initText: String, enabled: Cell<Boolean> = Cell(true)) : EditText(context) {

    private val sDecrement = StreamSink<Int>()
    private val allow: Cell<Boolean>
    private val l: Listener
    val text: Cell<String>
    val sUserChanges: Stream<String>

    init {
        allow = sText.map { u -> 1 }  // Block local changes until remote change has
                // been completed in the GUI
                .orElse(sDecrement)
                .accum(0) { d, b -> b + d }.map { b -> b == 0 }

        val sUserChangesSnk = StreamSink<String>()
        this.sUserChanges = sUserChangesSnk
        this.text = sUserChangesSnk.gate(allow).orElse(sText).hold(initText)
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                sUserChangesSnk.send(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        // Do it at the end of the transaction so it works with looped cells
        Transaction.post { isEnabled = enabled.sample() }
        l = sText.listen { text ->
            setText(text)
        }.append(
                Operational.updates(enabled).listen { enabled -> this.isEnabled = enabled }
        )
    }

    fun release() {
        l.unlisten()
    }
}