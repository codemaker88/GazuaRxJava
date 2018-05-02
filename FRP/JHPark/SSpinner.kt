package examples

import javax.swing.*
import java.awt.*
import nz.sodium.*
import swidgets.src.swidgets.SButton
import swidgets.src.swidgets.STextField

class SSpinner(initialValue: Int) : JPanel() {
    val value: Cell<Int>

    private val sSetValue = StreamLoop<Int>()
    private val plus = SButton("+")
    private val minus = SButton("-")
    private val textField: STextField


    init {
        textField = STextField(sSetValue.map { v -> Integer.toString(v!!) }, Integer.toString(initialValue), 5)
        initLayout()

        this.value = textField.text.map { Integer.parseInt(it) }

        val sPlusDelta = this.plus.sClicked.map { 1 }
        val sMinusDelta = this.minus.sClicked.map { -1 }
        val sDelta = sPlusDelta.orElse(sMinusDelta)

        sSetValue.loop(sDelta.snapshot(this.value) { delta, value -> delta!! + value!! })
    }

    private fun initLayout() {
        layout = GridBagLayout()
        val c = GridBagConstraints()
        add(textField, c)
        add(plus, c)
        add(minus, c)
    }
}
