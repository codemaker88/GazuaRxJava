package examples

import nz.sodium.Cell
import swidgets.src.swidgets.SButton
import swidgets.src.swidgets.SLabel
import swidgets.src.swidgets.STextField

import javax.swing.*
import java.awt.*
import java.lang.reflect.Array

class FormValidator : Runnable {

    private var row = 0
    
    private val labels = arrayOfNulls<JLabel>(maxEmails + 2)
    private val fields = arrayOfNulls<JComponent>(maxEmails + 2)
    private val valids = arrayOfNulls<Cell<String>>(maxEmails + 2)

    private var number: SSpinner? = null
    private var allValid = Cell(true)
    private val ok = SButton("OK", allValid)
    private val c = GridBagConstraints()

    override fun run() {
        number = SSpinner(0)

        initNameFileds()

        initEmailCountFields()

        initEmailDetails()

        initLayout()

        initValidation()
    }

    private fun initValidation() {
        (0 until row).forEach { i ->
            view.add(SLabel(valids[i]), c)
            allValid = allValid.lift<Boolean, Boolean>(valids[i]!!.map({ t -> t == "" })) { a, b -> a!! && b!! }
        }
    }


    private fun initEmailDetails() {
        var i = 0
        while (i < maxEmails) {
            labels[row] = JLabel("Email #" + (i + 1))

            val ii = i
            val enabled = number!!.value.map { n -> ii < n }

            val email = STextField("", 30, enabled)
            fields[row] = email
            valids[row] = email.text.lift(number!!.value
            ) { e, n ->
                if (ii >= n)
                    ""
                else if (e.trim { it <= ' ' } == "")
                    "<-- enter something"
                else if (e.indexOf('@') < 0) "<-- must contain @" else ""
            }
            i++
            row++
        }
    }

    private fun initEmailCountFields() {
        labels[row] = JLabel("No of email addresses")

        fields[row] = number
        valids[row++] = number!!.value.map { n -> if (n < 1 || n > maxEmails) "<-- must be 1 to " + maxEmails else "" }
    }

    private fun initNameFileds() {
        labels[row] = JLabel("Name")
        val name = STextField("", 30)
        fields[row] = name
        valids[row++] = name.text.map { t ->
            if (t.trim { it <= ' ' } == "")
                "<-- enter something"
            else if (t.trim { it <= ' ' }.indexOf(' ') < 0) "<-- must contain space" else ""
        }
    }

    private fun initLayout() {
        for (i in 0 until row) {
            view.add(labels[i], c)
            view.add(fields[i], c)
        }

        view.add(ok, c)
    }

    companion object {
        private val maxEmails = 4

        var view = JFrame("formvalidation")
    }
}
