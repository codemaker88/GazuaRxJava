package com.codemaker.frp_sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.codemaker.frp_sample.swidgets.SButton
import com.codemaker.frp_sample.swidgets.SLabel
import com.codemaker.frp_sample.swidgets.SSpinner
import com.codemaker.frp_sample.swidgets.STextField
import nz.sodium.Cell
import nz.sodium.Transaction


/**
 * Created by CodeMaker
 */
class FormValidationView : LinearLayout {

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        init()
    }

    fun init() {
        orientation = LinearLayout.VERTICAL
        Transaction.runVoid {
            val maxEmails = 4

            val labels = arrayOfNulls<TextView>(maxEmails + 2)
            val fields = arrayOfNulls<View>(maxEmails + 2)
            val valids = arrayOfNulls<Cell<String>>(maxEmails + 2)
            var row = 0

            labels[row] = SLabel(context, Cell("Name"))
            val name = STextField(context, initText = "")
            fields[row] = name
            valids[row] = name.text.map { t ->
                if (t.trim { it <= ' ' } == "")
                    "<-- enter something"
                else if (t.trim { it <= ' ' }.indexOf(' ') < 0)
                    "<-- must contain space"
                else
                    ""
            }
            row++

            labels[row] = SLabel(context, Cell("No of email addresses"))
            val number = SSpinner(context, 1)
            fields[row] = number
            valids[row] = number.value.map({ n ->
                if (n < 1 || n > maxEmails)
                    "<-- must be 1 to " + maxEmails
                else
                    ""
            })
            row++


            val emails = arrayOfNulls<STextField>(maxEmails)
            run {
                var i = 0
                while (i < maxEmails) {
                    labels[row] = SLabel(context, Cell("Email #" + (i + 1)))
                    val ii = i
                    val enabled = number.value.map({ n -> ii < n })
                    val email = STextField(context, initText = "", enabled = enabled)
                    fields[row] = email
                    valids[row] = email.text.lift(number.value) { e, n ->
                        if (ii >= n)
                            ""
                        else if (e.trim { it <= ' ' } == "")
                            "<-- enter something"
                        else if (e.indexOf('@') < 0)
                            "<-- must contain @"
                        else
                            ""
                    }
                    i++
                    row++
                }
            }

            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL
            addView(layout)
            var allValid = Cell(true)
            for (i in 0 until row) {
                layout.addView(labels[i])
                layout.addView(fields[i])
                valids[i]?.let {
                    val validLabel = SLabel(context, it)
                    layout.addView(validLabel)
                }
                val thisValid = valids[i]?.map { t -> t == "" }
                allValid = allValid.lift(thisValid) { a, b -> a!! && b!! }
            }
            val ok = SButton(context, "OK", allValid)
            layout.addView(ok)
        }
    }
}