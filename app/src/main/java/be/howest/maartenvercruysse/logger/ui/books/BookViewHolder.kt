package be.howest.maartenvercruysse.logger.ui.books

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.databinding.BooksViewItemBinding

/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class BookViewHolder(val viewDataBinding: BooksViewItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.books_view_item
    }
}