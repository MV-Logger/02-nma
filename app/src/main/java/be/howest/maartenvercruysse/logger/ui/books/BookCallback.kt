package be.howest.maartenvercruysse.logger.ui.books

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import be.howest.maartenvercruysse.logger.database.DatabaseEntry

class BookCallback(private val block: (DatabaseEntry) -> Unit) {
    fun onClick(entry: DatabaseEntry) = block(entry)
}


@BindingAdapter("android:onLongClick")
fun setOnLongClickListener(view: View, block : () -> Unit) {
    Log.d("book", "long press from adapter")
    view.setOnLongClickListener {
        block()
        return@setOnLongClickListener true
    }
}