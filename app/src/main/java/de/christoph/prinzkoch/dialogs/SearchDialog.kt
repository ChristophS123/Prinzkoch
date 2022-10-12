package de.christoph.prinzkoch.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import de.christoph.prinzkoch.R
import kotlinx.android.synthetic.main.search_dialog.*

class SearchDialog() : AppCompatDialogFragment() {

    private lateinit var listener:SearchDialogListener
    private lateinit var etSearchDialogMain:EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater:LayoutInflater = requireActivity().layoutInflater
        val view:View = inflater.inflate(R.layout.search_dialog, null)
        mDialog.setView(view)
            .setTitle("Suchen")
            .setNegativeButton("Abbrechen", DialogInterface.OnClickListener { _, _ -> })
            .setPositiveButton("Suchen", DialogInterface.OnClickListener { _, _ ->
                var searchWord:String = etSearchDialogMain.text.toString()
                if(searchWord.isEmpty()) {
                    listener.reset()
                    return@OnClickListener
                }
                listener.search(searchWord)
            })
        etSearchDialogMain = view.findViewById(R.id.et_search_dialog_main)
        return mDialog.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as SearchDialogListener
        } catch (exeption:Exception) {
            throw exeption
        }
    }
    interface SearchDialogListener {
        fun search(searchWord:String)
        fun reset()
    }

}