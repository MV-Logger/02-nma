package be.howest.maartenvercruysse.logger.ui.books

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import be.howest.maartenvercruysse.logger.database.DatabaseEntry
import be.howest.maartenvercruysse.logger.databinding.BooksViewItemBinding

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class BooksAdapter() : RecyclerView.Adapter<BookViewHolder>() {

    /**
     * The videos that our Adapter will show
     */
    var entries: List<DatabaseEntry> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val withDataBinding: BooksViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            BookViewHolder.LAYOUT,
            parent,
            false)

        return BookViewHolder(withDataBinding)
    }

    override fun getItemCount() = entries.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.entry = entries[position]
        }
    }

}

