package com.example.shoppinglist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.shoppinglist.data.Item
import com.example.shoppinglist.data.MoneyResult
import com.example.shoppinglist.databinding.ActivityMainBinding.inflate

import com.example.shoppinglist.databinding.ItemrowBinding.inflate

import com.example.shoppinglist.databinding.AdditemDialogBinding
import com.example.shoppinglist.network.MoneyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val dialogBinding =AdditemDialogBinding.inflate(layoutInflater)

            etItemName = dialogBinding.etItemName
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
                    try {
                        var price = etPrice.text.toString().toFloat()
                        handleItemEdit(price)
                        dialog!!.dismiss()
                    }
                    catch (e:NumberFormatException)
                    {
                        etPrice.error = "This should be a number"
                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show()
                    }

                } else {
                    try {
                       var price = etPrice.text.toString().toFloat()
                        handleItemAdd(price)
                        dialog!!.dismiss()
                    }
                    catch (e:NumberFormatException)
                    {
                        etPrice.error = "This should be a number"
                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show()
                    }

                }

            }
            else{
                etItemName.error = "This field cannot be empty"
            }

        }
    }

    private fun handleItemAdd(price:Float)
    {
        val currencies = getCurrencies()
        itemHandler.itemAdded(
            Item(null,
                etItemName.text.toString(),
                false,
                etItemDesc.text.toString(),
                spinnerCategory.selectedItemPosition,
                price,
                currencies
               )
        )
    }
    private fun handleItemEdit(price:Float){
        val currencies = getCurrencies()
        val itemEdit = arguments?.getSerializable(MainActivity.KEY_EDIT) as Item
        itemEdit.name = etItemName.text.toString()
        itemEdit.done = cbItemStatus.isChecked
        itemEdit.category = spinnerCategory.selectedItemPosition
        itemEdit.price = price
        itemEdit.currencies = currencies
        itemHandler.itemUpdated(itemEdit)

    }

    private fun getCurrencies() :List<Float>
    {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.frankfurter.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val currencyAPI = retrofit.create(MoneyAPI::class.java)

        val moneyCall  = currencyAPI.getMoney("HUF")
        var USD:Float? = null
        var INR:Float? =null
        var RUB:Float? = null
        moneyCall.enqueue(object:Callback<MoneyResult>{
            override fun onFailure(call:Call<MoneyResult>,t:Throwable){
                Toast.makeText(context,t.message.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MoneyResult>, response: Response<MoneyResult>) {
                var moneyResult = response.body()
                USD = moneyResult?.rates?.USD?.toFloat()
                INR = moneyResult?.rates?.INR?.toFloat()
                RUB = moneyResult?.rates?.RUB?.toFloat()
            }

        })
        return  listOf(USD!!,INR!!,RUB!!)
    }

}