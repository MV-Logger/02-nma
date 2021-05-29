package be.howest.maartenvercruysse.logger.ui.books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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

    private lateinit var binding: BooksViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("book", "Book fragment onviewcreated")
        viewModel.entries.observe(viewLifecycleOwner, { entries ->
            entries?.apply {
                viewModelAdapter?.entries = entries
            }
            scrollToEnd(binding.recyclerView)
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.books_view,
            container,
            false
        )

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModelAdapter = BooksAdapter()


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

    private fun scrollToEnd(recyclerView: RecyclerView) {
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                viewModelAdapter?.itemCount?.takeIf { it > 0 }?.let {
                    recyclerView.scrollToPosition(it - 1)
                    Log.d("book", "scrolled $it")
                }
            }
        })
    }

}