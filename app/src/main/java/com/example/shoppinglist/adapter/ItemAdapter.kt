package com.example.shoppinglist.adapter

import android.content.Context

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

import com.example.shoppinglist.MainActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item
import com.example.shoppinglist.databinding.ItemrowBinding


class ItemAdapter(private val context: Context, listItems: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var items = mutableListOf<Item>()

    init {
        items.addAll(listItems)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemrowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentItem = items[position]

        holder.binding.tvName.text= currentItem.name
        holder.binding.cbItemStatus.isChecked = currentItem.done
        holder.binding.tvPrice.text = currentItem.price.toString()
        holder.binding.tvDesc?.text = currentItem.desc

        holder.binding.btnDelete.setOnClickListener{
            deleteItem(holder.adapterPosition)
        }
        holder.binding.btnDetails.setOnClickListener {
            (context as MainActivity).detailsclicked(position)
        }

        holder.binding.btnEdit.setOnClickListener{
            (context as MainActivity).showEditItemDialog(items[holder.adapterPosition],holder.adapterPosition)
        }

        holder.binding.cbItemStatus.setOnClickListener{
            items[holder.adapterPosition].done = holder.binding.cbItemStatus.isChecked
            Thread{
                AppDatabase.getInstance(context).itemDao().updateItem(items[holder.adapterPosition])

            }.start()
        }

        if(items[holder.adapterPosition].category == 0){
            holder.binding.ivIcon.setImageResource(R.mipmap.ic_foodicon)
        }
        else if(items[holder.adapterPosition].category == 1){
            holder.binding.ivIcon.setImageResource(R.mipmap.ic_homeicon)
        }
        else if(items[holder.adapterPosition].category == 2){
            holder.binding.ivIcon.setImageResource(R.mipmap.ic_personalicon)
        }

    }
    fun get(position: Int):Item
    {
        return items[position]
    }
    private fun  deleteItem(position: Int){
        Thread{
            AppDatabase.getInstance(context).itemDao().deleteItem(items[position])

            (context as MainActivity).runOnUiThread{
                items.removeAt(position)

                //moveItems(position)s
                notifyItemRemoved(position)
            }
        }.start()
    }

    fun addItem(item: Item)
    {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

     fun updateItem(item: Item,editIndex:Int)
    {
        items[editIndex] = item
        notifyItemChanged(editIndex)
    }


    inner class  ViewHolder(val binding : ItemrowBinding):RecyclerView.ViewHolder(binding.root){

    }
    fun deleteall()
    {
        Thread{
            AppDatabase.getInstance(context).itemDao().deleteall()
            (context as MainActivity).runOnUiThread{
                for(i in 0 until itemCount)
                {
                    items.removeAt(0)

                }
                notifyDataSetChanged()
            }

        }.start()

    }
/*
    override fun onDismmissed(position: Int){
        deleteItem(position)
    }

    fun moveItems(position)
    {
        for(item:Item in items)
        {
            Collections.
        }
        Collections.swap(items,fromPosition,toPosition)

    } */


}