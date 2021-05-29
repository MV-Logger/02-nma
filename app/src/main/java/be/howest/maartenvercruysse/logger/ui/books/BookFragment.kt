package be.howest.maartenvercruysse.logger.ui.books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.databinding.BooksViewBinding

class BookFragment : Fragment() {

    private val viewModel: BookViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val safeArgs: BookFragmentArgs by navArgs()

        ViewModelProvider(this, BookViewModelFactory(activity.application, safeArgs.id))
            .get(BookViewModel::class.java)
    }

    private var viewModelAdapter: BooksAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("book", "Book fragment onviewcreated")
        viewModel.entries.observe(viewLifecycleOwner, { entries ->
            Log.d("book", "entries changed")
            entries?.apply {
                viewModelAdapter?.entries = entries
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding: BooksViewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.books_view,
            container,
            false)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModelAdapter = BooksAdapter()

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }




    init {
        Log.d("book", "Book fragment init")
    }

}