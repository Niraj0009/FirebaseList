package com.example.firebasedemo.adapter



import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.R
import com.example.firebasedemo.activity.MainActivity
import com.example.firebasedemo.activity.UserDetails
import com.example.firebasedemo.model.chatPojo
import com.google.firebase.database.*
import java.util.*


class UserListAdapter(
    private val data: ArrayList<chatPojo>? /*private var itemClickListener : ItemClickListener*/,
    private val context: MainActivity
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var pos: Int? = 0
    var state: Boolean = false
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
        holder.message.setOnClickListener {
          /*  pos=position
            notifyDataSetChanged()*/

//            itemClickListener?.onClick(data.get(position).id)


        }
        holder.itemView.setOnClickListener {
            val i = Intent(context, UserDetails::class.java)
            i.putExtra("message", data!!.get(position).message)
            i.putExtra("name", data!!.get(position).name)
            context.startActivity(i)
        }

        holder.checkBox.setOnClickListener {
            pos = position
            if (!state) {

                holder.user_name.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = data!!.get(position).name
                }
                state=true
            } else {
                holder.user_name.text = data!!.get(position).name
              //  notifyDataSetChanged()
                holder.user_name.setPaintFlags(holder.user_name.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                state=false
            }

        }

    }





    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var message : TextView = view.findViewById(R.id.doubt_subject_name)
        var user_name : TextView = view.findViewById(R.id.user_name)
        var user_image : ImageView = view.findViewById(R.id.user_image)
        var checkBox : CheckBox = view.findViewById(R.id.checkbox)

    }

    fun updateList(dataList: chatPojo?) {
        data?.add(dataList!!)
        notifyDataSetChanged()
    }

    fun clearList() {
        data?.clear()
    }

    fun deleteItem(position: Int, firebaseDatabase: DatabaseReference?) {
        delete(data?.get(position)?.date.toString(),firebaseDatabase)
       // snapshots.getSnapshot(position).reference.delete()
    }


    private fun delete(date: String, firebaseDatabase: DatabaseReference?) {

        firebaseDatabase?.ref?.orderByChild("date")?.equalTo(date)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.e("Childers", "onDataChange: " + dataSnapshot.children)
                    Log.e("Childers", "onDataChange: " + dataSnapshot.key)
                    for (ds in dataSnapshot.children) {
                        Log.e("DS", "onDataChange: " + ds.value)
                        ds.ref.removeValue()
//                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "onCancelled: "+error.message)
                    TODO("Not yet implemented")
                }

            })
    }

}