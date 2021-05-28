package be.howest.maartenvercruysse.logger.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import be.howest.maartenvercruysse.logger.R
import be.howest.maartenvercruysse.logger.databinding.DialogAddBookBinding
import be.howest.maartenvercruysse.logger.network.Book
import be.howest.maartenvercruysse.logger.repository.LoggerRepository
import kotlinx.coroutines.launch


class BookDialogFragment(val repo: LoggerRepository) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_book,  null)

            val binding: DialogAddBookBinding = DialogAddBookBinding.bind(view);

            builder.setView(view)
                .setTitle(R.string.dialog_book)
                .setPositiveButton(R.string.confirm_book) { _, _ ->
                    Log.d("book", "fired")
                    requireActivity().lifecycleScope.launch { // need lifecycle of activity bc this fragment lifecycle ends before request is done
                        repo.addBook(Book(0, binding.bookName.text.toString()))
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }

            val dialog = builder.create()

            dialog.setOnShowListener {
                val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                btn.isEnabled = false

                binding.bookName.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                    override fun afterTextChanged(s: Editable?) {
                        btn.isEnabled = !TextUtils.isEmpty(s)
                    }
                })
            }
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}