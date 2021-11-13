package com.example.shoppinglist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.shoppinglist.data.Item

class ItemDialog :DialogFragment() {
    interface  ItemHandler{
        fun itemAdded(item: Item)
        fun itemUpdated(item: Item)
    }

    lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ItemHandler){
            itemHandler = context
        }
        else{
            throw RuntimeException("The Activity is not implementing the handler")
        }
    }

    lateinit var etItemName : EditText
    lateinit var cbItemStatus : CheckBox
    lateinit var spinnerCategory: Spinner
    lateinit var etItemDesc:EditText
    lateinit var etPrice:EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Add Item")
        val dialogBinding =  ItemDialogBinding.inflate(layoutInflater)

        etItemName = dialogBinding.etItemDesc
        cbItemStatus = dialogBinding.cbItemStatus
        spinnerCategory = dialogBinding.spinnerCategory
        etItemDesc = dialogBinding.etItemDesc
        etPrice = dialogBinding.etPrice

        var categoryAdapter = ArrayAdapter.createFromResource(requireContext()!!, R.array.categories,android.R.layout.simple_spinner_dropdown_item)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        dialogBuilder.setView(dialogBinding.root)

        val arguments = this.arguments

        if(arguments != null && arguments.containsKey(MainActivity.KEY_EDIT)){
            val item = arguments.getSerializable(MainActivity.KEY_EDIT) as Item
            etItemName.setText(item.name)
            cbItemStatus.isChecked = item.done
            etItemDesc.setText(item.desc)
            etPrice.setText(item.price.toString())
            dialogBuilder.setTitle("Edit Item")
        }

        dialogBuilder.setPositiveButton("Ok"){
            dialog,which ->
        }
        dialogBuilder.setNegativeButton("Cancel"){
            dialog,which ->
        }
        return dialogBuilder.create()


    }

    override fun onResume() {
        super.onResume()
        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if(etItemName.text.isNotEmpty()) {
                val arguments = this.arguments

                if (arguments != null && arguments.containsKey(MainActivity.KEY_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemAdd()
                }

                dialog!!.dismiss()
            }
            else{
                etItemName.error = "This field cannot be empty"
            }

        }
    }

    private fun handleItemAdd()
    {
        itemHandler.itemAdded(
            Item(null,
                etItemName.text.toString(),
                false,etItemDesc.text.toString(),
                spinnerCategory.selectedItemPosition,
                etPrice.text.toString().toFloat())
        )
    }
    private fun handleItemEdit(){
        val itemEdit = arguments?.getSerializable(MainActivity.KEY_EDIT) as Item
        itemEdit.name = etItemName.text.toString()
        itemEdit.done = cbItemStatus.isChecked
        itemEdit.category = spinnerCategory.selectedItemPosition
        itemEdit.price = etPrice.text.toString().toFloat()
        itemHandler.itemUpdated(itemEdit)

    }

}