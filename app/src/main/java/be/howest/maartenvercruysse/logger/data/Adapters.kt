package be.howest.maartenvercruysse.logger.data

import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

fun TextView.applyWithDisabledTextWatcher(textWatcher: TextWatcher, codeBlock: TextView.() -> Unit) {
    this.removeTextChangedListener(textWatcher)
    codeBlock()
    this.addTextChangedListener(textWatcher)
}

@BindingAdapter("android:onLongClick")
fun setOnLongClickListener(view: View, block : () -> Unit) {
    view.setOnLongClickListener {
        block()
        return@setOnLongClickListener true
    }
}