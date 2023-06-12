package com.example.firebasedemo



import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class UserListAdapter(private val data: ArrayList<chatPojo>?, /*private var itemClickListener : ItemClickListener*/) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var pos: Int? = 0
   // var buttonClicked: OnClicked? = null

   /* interface onButtonClicked {
        fun onDoubtSubjectClicked()
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.custom_doubt_sub,parent,false)
        return ViewHolder(v)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text= data!!.get(position).message
        /*if(pos==position) {
            holder.title.setBackgroundResource(R.drawable.background_bg_next)
            holder.title.setTextColor(Color.parseColor("#FFFFFF"))
        }else {
            holder.title.setBackgroundResource(R.drawable.background_mcq)
            holder.title.setTextColor(Color.parseColor("#545454"))
        }*/
        holder.title.setOnClickListener {
            pos=position
            notifyDataSetChanged()
//            itemClickListener?.onClick(data.get(position).id)

        }

    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title : TextView = view.findViewById(R.id.doubt_subject_name)

    }

    fun updateList(dataList: chatPojo?) {
        data?.add(dataList!!)
        notifyDataSetChanged()
    }

    fun clearList() {
        data?.clear()
    }

}