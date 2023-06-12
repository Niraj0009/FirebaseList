package com.example.firebasedemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.R
import com.example.firebasedemo.adapter.UserListAdapter
import com.example.firebasedemo.model.chatPojo
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private lateinit var rvView: RecyclerView
    private lateinit var userListAdapter: UserListAdapter
    val firebaseDataSnapshot = MutableLiveData<DataSnapshot>()
    private var mFirebaseDatabaseReferenceone2one: DatabaseReference? = null
    var ivSend: Button? = null
    var etMessage: EditText? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivSend = findViewById(R.id.ivSend)
        etMessage = findViewById(R.id.etMessage)
        rvView = findViewById(R.id.rvView)
        FirebaseApp.initializeApp(this)
        userListAdapter= UserListAdapter(ArrayList())

        initChat("")


        firebaseDataSnapshot.observe(this) { dataSnapshot ->
            if (dataSnapshot.value != null) {
                userListAdapter.clearList()
                Log.e("Firebase", "initChat: " + dataSnapshot.value)

                for (snapShot in dataSnapshot.children) {
                    val chatPojo = snapShot.getValue(chatPojo::class.java)
                    Log.e("Firebase", "initChat: " + snapShot)
                    userListAdapter.updateList(chatPojo)
                }

                // Scroll to Last
                rvView.scrollToPosition(userListAdapter.itemCount - 1)
            }
        }

        ivSend?.setOnClickListener(View.OnClickListener {
            if (etMessage?.text.toString()
                    .isEmpty() || etMessage?.text.toString() == "" || etMessage?.text.toString()
                    .replace(" ", "").isEmpty()
            ) {
                Toast.makeText(this, "Type Message.", Toast.LENGTH_SHORT).show()
//                showSmallLengthToast(resources.getString(R.string.type_message))
            } else {
                sendChat()

                /*if (Helper.isNetworkConnected(this)) {
                    if (binding.etMessage.text.toString() != "") {
                        sendChat()
                    }
                } else {
                    Helper.showInternetToast(this)
                }*/
            }
        })

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //  noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.adapterPosition))
                    Toast.makeText(
                        this@MainActivity,
                        "Item Delete",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvView)

    }

    private fun sendChat() {


        val message = chatPojo(
            "101",
            etMessage?.getText().toString(),
            "niraj",
            System.currentTimeMillis(),
            "1",
            "Image url",
        )
        mFirebaseDatabaseReferenceone2one!!.push().setValue(message)

        etMessage?.setText("")
    }


    fun initChat(chatNode: String) {
        try {
            rvView.adapter = userListAdapter
            mFirebaseDatabaseReferenceone2one =
                FirebaseDatabase.getInstance("https://fir-list-29c11-default-rtdb.firebaseio.com/")
                    .reference
                    .child("/chat_master/")

            mFirebaseDatabaseReferenceone2one?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    firebaseDataSnapshot.value = dataSnapshot
                }

                override fun onCancelled(databaseError: DatabaseError) {
//                    Helper.logPrinter(Configuration.TAG, "e", databaseError.message, "")
                }
            })

            mFirebaseDatabaseReferenceone2one?.limitToLast(500)
        } catch (e: Exception) {
            Log.e("ERROR", "initChat: " + e.localizedMessage)
            Log.e("ERROR", "initChat: " + e.localizedMessage)
//            Helper.logPrinter(Configuration.TAG, "e", e.localizedMessage, "")
        }
    }
}