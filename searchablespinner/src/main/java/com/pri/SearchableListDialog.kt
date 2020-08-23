package com.pri

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pri.searchablespinnerlibrary.R
import com.pri.searchablespinnerlibrary.databinding.SearchableListDialogBinding

class SearchableListDialog internal constructor() : BottomSheetDialogFragment() {


    private var itemClickListener: OnItemClickListener? = null

    private var _strTitle: String? = null

    private var mBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //set to adjust screen height automatically, when soft keyboard appears on screen
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Crash on orientation change #7
        // Change Start
        // Description: As the instance was re initializing to null on rotating the device,
        // getting the instance from the saved instance
        if (null != savedInstanceState) {
            itemClickListener = savedInstanceState.getSerializable("item") as OnItemClickListener
        }
        // Change End

        val binding: SearchableListDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.searchable_list_dialog, null, false)
        setData(binding)
        dialog.setContentView(binding.root)

        val strTitle = if (_strTitle == null) "Select Item" else _strTitle
        dialog.setTitle(strTitle)
        binding.tvTitle.text = strTitle

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_ADJUST_RESIZE)
        mBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        mBehavior?.peekHeight = 500
        KeyboardUtil(activity, binding.root)
        /* mBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
             override fun onSlide(bottomSheet: View, offset: Float) {
                 binding.rvItems.setPadding(0, 0, 0, (bottomSheet.height * offset).toInt())
             }

             override fun onStateChanged(bottomSheet: View, newState: Int){}
         })*/
        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    // Crash on orientation change #7
    // Change Start
    // Description: Saving the instance of searchable item instance.
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("item", itemClickListener)
        super.onSaveInstanceState(outState)
    }
    // Change End

    fun setTitle(strTitle: String) {
        _strTitle = strTitle
    }

    fun setOnSearchableItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    var adapter: SearchListAdapter? = null


    private fun setData(binding: SearchableListDialogBinding) {
        if (items.isEmpty()) {
            binding.group.visibility = View.GONE
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
        } else {
            binding.group.visibility = View.VISIBLE
            binding.rvItems.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            //create the adapter by passing your ArrayList data
            //attach the adapter to the list
            if (adapter == null) adapter = SearchListAdapter()

            binding.rvItems.adapter = adapter
            adapter?.setData(items)
            adapter?.setItemClickListener(object : OnItemClickListener {
                override fun onItemClick(parent: SearchableSpinner?, view: View?, position: Int, id: Long) {
                    itemClickListener?.onItemClick(view = view, position = position, id = id)
                    dismiss()
                }

            })

            if (items.size > 10) {
                binding.tilSearch.visibility = View.VISIBLE
                binding.etSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        adapter?.filter?.filter(editable.toString())
                    }
                })
            }
        }
    }

    var items = ArrayList<Any>()
    fun setData(data: List<Any>) {
        items.clear()
        items.addAll(data)
    }


    companion object {

        private val ITEMS = "items"

        fun newInstance(): SearchableListDialog {
            return SearchableListDialog()
        }
    }
}

open class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.ViewHolder>(), Filterable {
    private var dataFiltered: List<Any> = ArrayList()
    private var data: List<Any> = ArrayList()
    fun setData(data: List<Any>) {
        this.data = data
        this.dataFiltered = data
    }

    protected open fun getLayoutId(): Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId()
                ?: android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataFiltered[position]
        if (getLayoutId() == null) {
            val textView: TextView = holder.itemView.findViewById(android.R.id.text1)
            textView.text = item.toString()
        } else {
            setItemData(holder.itemView, item, position)
        }
    }

    protected open fun setItemData(itemView: View, item: Any, position: Int) {
    }

    override fun getItemCount(): Int {
        return dataFiltered.size
    }

    internal fun getItem(position: Int): Any {
        return dataFiltered[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                dataFiltered = if (charString.isEmpty()) {
                    data
                } else {
                    val filteredList = ArrayList<Any>()
                    for (row in data) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.toString().contains(charString, true)) {
                            filteredList.add(row)
                        }
                    }

                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = dataFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                dataFiltered = filterResults.values as List<Any>
                notifyDataSetChanged()
            }
        }
    }

    private var itemClickListener: OnItemClickListener? = null
    fun setItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                data.indexOf(getItem(adapterPosition)).let { it1 ->
                    itemClickListener?.onItemClick(view = itemView, position = it1, id = itemId)
                }
            }
        }
    }
}
