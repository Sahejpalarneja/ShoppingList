package com.example.shoppinglist

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.shoppinglist.adapter.ItemAdapter
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item
import com.example.shoppinglist.data.ItemDao
import com.example.shoppinglist.databinding.ActivityMainBinding
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import java.util.*


class MainActivity : AppCompatActivity(),ItemDialog.ItemHandler{

    companion object {
        const val KEY_EDIT = "KEY_EDIT"
        const val PREF_NAME = "PREFITEM"
        const val KEY_STARTED = "KEY_STARTED"
        const val KEY_LAST_USED = "KEY_LAST_USED"
    }


    private lateinit var binding: ActivityMainBinding
    lateinit var itemListAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title= title
        binding.fab.setOnClickListener {
            view->showItemDialog()
        }
        if(!wasStartedBefore())
        {
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setPrimaryText("Add Item")
                .setSecondaryText("Click here to add a new item")
                .show()
        }
        Thread{
            var items = AppDatabase.getInstance(this).itemDao().getAllItems()
            runOnUiThread {
                itemListAdapter = ItemAdapter(this, items)
                binding.recyclerItem.adapter = itemListAdapter

                val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                binding.recyclerItem.addItemDecoration(itemDecoration)

            }
        }.start()
        saveStartInfo()

    }
    fun saveStartInfo(){
        var sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        var editor = sharedPref.edit()
        editor.putBoolean(KEY_STARTED,true)
        editor.putString(KEY_LAST_USED, Date(System.currentTimeMillis()).toString())
        editor.apply()
    }
    fun wasStartedBefore():Boolean{
        var sharedPref = getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

        return sharedPref.getBoolean(KEY_STARTED,false)
    }
    fun showItemDialog(){
        ItemDialog().show(supportFragmentManager,"Dialog")
    }
    var editIndex: Int = -1

     fun detailsclicked(position:Int)
    {
        var detailsIntent = Intent()
        detailsIntent.setClass(this,DetailsActivity::class.java)
        detailsIntent.putExtra("Name",itemListAdapter.get(position).name)
        detailsIntent.putExtra("Price",itemListAdapter.get(position).price)
        detailsIntent.putExtra("Desc",itemListAdapter.get(position).desc)
        detailsIntent.putExtra("Image",itemListAdapter.get(position).category)
        detailsIntent.putExtra("USD",itemListAdapter.get(position).USD)
        detailsIntent.putExtra("INR",itemListAdapter.get(position).INR)
        detailsIntent.putExtra("RUB",itemListAdapter.get(position).RUB)
        detailsIntent.putExtra("Status",itemListAdapter.get(position).done)

        startActivity(detailsIntent)
    }
    public fun showEditItemDialog(itemEdit:Item,index:Int)
    {
        editIndex = index

        val editIdemDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT,itemEdit)
        editIdemDialog.arguments = bundle

        editIdemDialog.show(supportFragmentManager,"EDIT Dialog")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId){
            R.id.action_settings -> true
            R.id.action_deleteall -> deleteall()

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun itemAdded(item: Item) {
        saveItem(item)
    }
    private fun saveItem(item:Item){
        Thread{
            item.ItemId = AppDatabase.getInstance(this).itemDao().addItem(item)
            runOnUiThread{
                itemListAdapter.addItem(item)
            }
        }.start()
    }

    override fun itemUpdated(item: Item) {
        Thread{
            AppDatabase.getInstance(this).itemDao().updateItem(item)
            runOnUiThread{
                itemListAdapter.updateItem(item,editIndex)
            }
        }.start()
    }

    private fun deleteall() :Boolean
    {
        for(i in 0 until itemListAdapter.itemCount)
        {
            itemListAdapter.deleteall()
        }
        return true
    }

}