package com.example.shoppinglist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.shoppinglist.adapter.ItemAdapter
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item
import com.example.shoppinglist.databinding.ActivityMainBinding
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_EDIT = "KEY_EDIT"

        const val PREF_NAME = "PREFTODO"
        const val KEY_STARTED = "KEY_STARTED"
        const val KEY_LAST_USED = "KEY_LAST_USED"
    }


    private lateinit var binding: ActivityMainBinding
    lateinit var itemListAdapter:  ListAdapter

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
                itemListAdapter = ItemAdapter(this, itemListAdapter)
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
    var editIndex: Int = -1

    public fun showEditItemDialog(itemEdit:Item,index:Int)
    {
        editIndex = index
        val editIdemDialog:ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT,itemEdit)

    }
}