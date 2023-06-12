package com.example.firebasedemo.adapter



import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.R
import com.example.firebasedemo.model.chatPojo
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



    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.message.text= data!!.get(position).message
        holder.user_name.text= data!!.get(position).name
        /*if(pos==position) {
            holder.title.setBackgroundResource(R.drawable.background_bg_next)
            holder.title.setTextColor(Color.parseColor("#FFFFFF"))
        }else {
            holder.title.setBackgroundResource(R.drawable.background_mcq)
            holder.title.setTextColor(Color.parseColor("#545454"))
        }*/
        holder.message.setOnClickListener {
            pos=position
            notifyDataSetChanged()
//            itemClickListener?.onClick(data.get(position).id)

        }

    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var message : TextView = view.findViewById(R.id.doubt_subject_name)
        var user_name : TextView = view.findViewById(R.id.user_name)
        var user_image : ImageView = view.findViewById(R.id.user_image)

    }

    fun updateList(dataList: chatPojo?) {
        data?.add(dataList!!)
        notifyDataSetChanged()
    }

    fun clearList() {
        data?.clear()
    }

}