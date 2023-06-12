package com.example.firebasedemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    val firebaseDataSnapshot = MutableLiveData<DataSnapshot>()
    private var mFirebaseDatabaseReferenceone2one: DatabaseReference? = null
    var ivSend: ImageView? = null
    var etMessage: EditText? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivSend = findViewById(R.id.ivSend)
        etMessage = findViewById(R.id.etMessage)


//        ivSend.setOnClickListener {
//            if (Helper.isConnected(this@MainActivity)) {
//                if (etMessage.getText().toString().trim { it <= ' ' } != "") {
//
//                    val message = chatPojo(
//                        "101",
//                        etMessage.getText().toString(),
//                        "niraj",
//                        System.currentTimeMillis(),
//                        "1",
//                        "Image url",
//                    )
//                    mFirebaseDatabaseReferenceone2one!!.push().setValue(message)
//                    etMessage.setText("")
//                    // Helper.hideKeyboard(this@MainActivity)
//
//                } else {
//                    Helper.showSnackBar(ivSend, "Please enter your query first.")
//                }
//            } else {
//                Helper .showSnackBar(ivSend, "Please connect internet first.")
//            }
//        }

        initChat("")


        firebaseDataSnapshot.observe(this) { dataSnapshot ->
            if (dataSnapshot.value != null) {
//                chatAdapter.clearList()

                for (snapShot in dataSnapshot.children) {
                    val chatPojo = snapShot.getValue(chatPojo::class.java)
//                    chatAdapter.updateList(chatPojo)
                }

                // Scroll to Last
//                binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
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
        // Helper.hideKeyboard(this@MainActivity)
        //Hide Keyboard
//        Helper.hideKeyboard(this)
    }


    fun initChat(chatNode: String) {
        try {
            mFirebaseDatabaseReferenceone2one =
                FirebaseDatabase.getInstance("https://fir-list-29c11-default-rtdb.firebaseio.com/")
                    .reference
                    .child("/chat_master/" + "1TO1/")

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
//            Helper.logPrinter(Configuration.TAG, "e", e.localizedMessage, "")
        }
    }


 /*   private fun fireBaseOperation() {
        mFirebaseDatabaseReferenceone2one =
            FirebaseDatabase.getInstance("https://fir-list-29c11-default-rtdb.firebaseio.com/")
                .getReference()
                .child("/chat_master/" + "/1TO1/")
        if (isPublicChatEnabled) {
            onetomanyvalueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value == null) {
                        checkstatus = "1"
                        onetomanygetupdatedchatdata(MakeMyExam.getTime_server())
                    } else {
                        try {
                            for (childSnapshot in dataSnapshot.children) {
                                val message: chatPojo? =
                                    childSnapshot.getValue(chatPojo::class.java)
                                if (!message.getType()
                                        .equalsIgnoreCase("poll") && !message.getType()
                                        .equalsIgnoreCase("is_chat_locked")
                                ) {
                                    arrChat.add(message)
                                    pollarr.add(message)
                                    keyset.add(childSnapshot.key)
                                } else {
                                    if (message.getType().equalsIgnoreCase("is_chat_locked")) {
                                        lockarr.add(message)
                                    }
                                    pollarr.add(message)
                                }
                                chatAdapter.notifyDataSetChanged()
                            }
                            recyclerChat.smoothScrollToPosition(arrChat.size)
                            checkstatus = "1"
                            onetomanygetupdatedchatdata(pollarr.get(pollarr.size - 1).getDate())
                            if (lockarr.size > 0) {
                                val message: String = lockarr.get(lockarr.size - 1).getMessage()
                                if (message.equals("0", ignoreCase = true)) {
                                    islocked = "1"
                                    showchat()
                                } else {
                                    islocked = "2"
                                    hidechat()
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@Liveawsactivity,
                                "null pointer exception",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            mFirebaseDatabaseReferenceone2many.addListenerForSingleValueEvent(
                onetomanyvalueEventListener
            )
            mFirebaseDatabaseReferenceone2many.limitToLast(500)
        } else {
            valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value == null) {
                        checkstatus = "1"
                        getupdatedchatdata(MakeMyExam.getTime_server())
                    } else {
                        try {
                            for (childSnapshot in dataSnapshot.children) {
                                val message: chatPojo? =
                                    childSnapshot.getValue(chatPojo::class.java)


//                            android.util.Log.e("xcfghjk", "onDataChange: " + message.getType());
                                if (!message.getType()
                                        .equalsIgnoreCase("poll") && !message.getType()
                                        .equalsIgnoreCase("is_chat_locked")
                                ) {
                                    arrChat.add(message)
                                    keyset.add(childSnapshot.key)
                                    pollarr.add(message)
                                } else {
                                    if (message.getType().equalsIgnoreCase("is_chat_locked")) {
                                        lockarr.add(message)
                                    }
                                    pollarr.add(message)
                                }
                                chatAdapter.notifyDataSetChanged()
                            }
                            recyclerChat.smoothScrollToPosition(arrChat.size)
                            checkstatus = "1"
                            getupdatedchatdata(pollarr.get(pollarr.size - 1).getDate())
                            if (lockarr.size > 0) {
                                val message: String = lockarr.get(lockarr.size - 1).getMessage()
                                if (message.equals("0", ignoreCase = true)) {
                                    islocked = "1"
                                    showchat()
                                } else {
                                    islocked = "2"
                                    hidechat()
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@Liveawsactivity,  *//*"null pointer exception"*//*
                                resources.getString(R.string.null_pointer_exception),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            mFirebaseDatabaseReferenceone2one.addListenerForSingleValueEvent(valueEventListener)
            mFirebaseDatabaseReferenceone2one.limitToLast(500)
        }
        ivSend.setOnClickListener(View.OnClickListener {
            if (Helper.isConnected(this@Liveawsactivity)) {
                if (etMessage.getText().toString().trim { it <= ' ' } != "") {
                    if (!isintractavailable) {
                        checkintract()
                    }
                    if (Helper.IsValidUrl(etMessage.getText().toString().trim { it <= ' ' })) {
                        val message = chatPojo(
                            MakeMyExam.userId,
                            etMessage.getText().toString(),
                            SharedPreference.getInstance().getLoggedInUser().getName(),
                            System.currentTimeMillis(),
                            "1",
                            SharedPreference.getInstance().getLoggedInUser().getProfilePicture(),
                            Const.DEVICE_TYPE_ANDROID,
                            "url",
                            courseid
                        )
                        mFirebaseDatabaseReferenceone2one.push().setValue(message)
                        mFirebaseDatabaseReferenceone2many.push().setValue(message)
                        etMessage.setText("")
                        Helper.hideKeyboard(this@Liveawsactivity)
                    } else {
                        val message = chatPojo(
                            MakeMyExam.userId,
                            etMessage.getText().toString(),
                            SharedPreference.getInstance().getLoggedInUser().getName(),
                            System.currentTimeMillis(),
                            "1",
                            SharedPreference.getInstance().getLoggedInUser().getProfilePicture(),
                            Const.DEVICE_TYPE_ANDROID,
                            "text",
                            courseid
                        )
                        mFirebaseDatabaseReferenceone2one.push().setValue(message)
                        mFirebaseDatabaseReferenceone2many.push().setValue(message)
                        etMessage.setText("")
                        Helper.hideKeyboard(this@Liveawsactivity)
                    }
                } else {
                    Helper.showSnackBar(ivSend, "Please enter your query first.")
                }
            } else {
                Helper.showSnackBar(ivSend, "Please connect internet first.")
            }
        })
        if (chatAdapter != null) chatAdapter.pauseAudio()
        arrChat.clear()
        chatAdapter = ChatAdapter(this, "", arrChat)
        val llm = LinearLayoutManager(this)
        llm.isAutoMeasureEnabled = false
        recyclerChat.setLayoutManager(llm)
        recyclerChat.setAdapter(chatAdapter)
        resumePlayer()
    }*/
}