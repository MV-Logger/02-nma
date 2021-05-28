package be.howest.maartenvercruysse.logger.ui.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import be.howest.maartenvercruysse.logger.database.DatabaseEntry

class BookFragment(id: Int): Fragment() {

    private val viewModel: BookViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, BookViewModelFactory(activity.application, id))
            .get(BookViewModel::class.java)
    }

    private var viewModelAdapter: BooksAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.entries.observe(viewLifecycleOwner, Observer<List<DatabaseEntry>> { videos ->
            videos?.apply {
                viewModelAdapter?.entries = videos
            }
        })
    }

}