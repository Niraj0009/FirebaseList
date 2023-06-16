package com.example.firebasedemo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.R
import com.example.firebasedemo.adapter.UserListAdapter
import com.example.firebasedemo.model.chatPojo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File


class MainActivity : AppCompatActivity() {
    private val storage = FirebaseStorage.getInstance();
    private var delPosition: Int = -1
    private lateinit var rvView: RecyclerView
    private lateinit var userListAdapter: UserListAdapter
    val firebaseDataSnapshot = MutableLiveData<DataSnapshot>()
    private var mFirebaseDatabaseReferenceone2one: DatabaseReference? = null
    var ivSend: Button? = null
    var image: Button? = null
    var floating_button: FloatingActionButton? = null
    var url: TextView? = null
    var etMessage: EditText? = null
    private val keyset: List<String> = ArrayList()
    var str_imgTypeClick = ""
    val REQUEST_CODE_PERMISSION_MULTIPLE = 123
    val REQUEST_CODE_Profile_Gallery = 100


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       /* ivSend = findViewById(R.id.ivSend)
        image = findViewById(R.id.image)
        url = findViewById(R.id.url)
        etMessage = findViewById(R.id.etMessage)*/
        rvView = findViewById(R.id.rvView)
        floating_button = findViewById(R.id.floating_button)
        FirebaseApp.initializeApp(this)
        userListAdapter = UserListAdapter(ArrayList(),this)

        initChat("")


        firebaseDataSnapshot.observe(this) { dataSnapshot ->
            if (dataSnapshot.value != null) {
                userListAdapter.clearList()
                Log.e("Firebase1", "initChat: " + dataSnapshot.value)

                for (snapShot in dataSnapshot.children) {
                    val chatPojo = snapShot.getValue(chatPojo::class.java)
                    Log.e("Firebase", "initChat: " + snapShot)
                    userListAdapter.updateList(chatPojo)
                }

                // Scroll to Last
                rvView.scrollToPosition(userListAdapter.itemCount - 1)
            }
        }

      /*  ivSend?.setOnClickListener(View.OnClickListener {
            if (etMessage?.text.toString()
                    .isEmpty() || etMessage?.text.toString() == "" || etMessage?.text.toString()
                    .replace(" ", "").isEmpty()
            ) {
                Toast.makeText(this, "Type Message.", Toast.LENGTH_SHORT).show()
//                showSmallLengthToast(resources.getString(R.string.type_message))
            } else {
                sendChat()

                *//*if (Helper.isNetworkConnected(this)) {
                    if (binding.etMessage.text.toString() != "") {
                        sendChat()
                    }
                } else {
                    Helper.showInternetToast(this)
                }*//*
            }
        })*/

      /*  image?.setOnClickListener(View.OnClickListener {
        })*/

        floating_button?.setOnClickListener(View.OnClickListener {
            showDialog()
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
                    userListAdapter.deleteItem(viewHolder.adapterPosition,mFirebaseDatabaseReferenceone2one)
                    delPosition = viewHolder.adapterPosition

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

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        etMessage = dialog.findViewById(R.id.etMessage) as EditText
        url = dialog.findViewById(R.id.url) as TextView
        val image = dialog.findViewById(R.id.image) as Button
        val ivSend = dialog.findViewById(R.id.ivSend) as Button

        ivSend.setOnClickListener {
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
            dialog.dismiss()
        }

        image.setOnClickListener {
           checkStoragePermission()
        }
        dialog.show()

    }

    private fun checkStoragePermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    imgClick()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }


    private fun imgClick() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(
            options
        ) { dialog: DialogInterface, item: Int ->
            if (options[item] == "Take Photo") {
                try {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val f: File = File(
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "temp_image.jpg"
                    )
                    val photoURI: Uri
                    photoURI = if (Build.VERSION.SDK_INT >= 24) {
                        FileProvider.getUriForFile(
                            this, "com.example.firebasedemo" +".provider", f
                        )
                    } else {
                        Uri.fromFile(f)
                    }
                    str_imgTypeClick = "PhotoCameraRequest"
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, 10000)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    //DebugLogger.Write("Error in Main Activity Profile Take Photo Button " +e);
                }
            } else if (options[item] == "Choose from Gallery") {
                val intent = Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                val f: File = File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "temp_gallery.jpg"
                )

                val photoURI: Uri
                photoURI = if (Build.VERSION.SDK_INT >= 24) {
                    FileProvider.getUriForFile(
                        this, "com.example.firebasedemo"+".provider", f
                    )

                } else {
                    Uri.fromFile(f)
                }
                str_imgTypeClick = "PhotoGalleryRequest"
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, 20000)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_Profile_Gallery) {
//            profileIV!!.visibility = View.VISIBLE
//            profileIV!!.setImageURI(data?.data) // handle chosen image
        }

        // Camera
        if (requestCode == 10000 && resultCode == Activity.RESULT_OK) {
            try {
                var f: File =
                    File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString())
                for (temp in f.listFiles()) {
                    if (temp.name == "temp_image.jpg") {
                        f = temp
                        break
                    }
                }
                val photoURI: Uri
                photoURI = if (Build.VERSION.SDK_INT >= 24) {
                    FileProvider.getUriForFile(
                        this, "com.example.firebasedemo"+".provider", f
                    )
                } else {
                    Uri.fromFile(f)
                }

                url?.text = photoURI.toString()
              //  uploadImageInfirebase(photoURI.toString())

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

        }

        //Gallery
        if (requestCode == 20000 && resultCode == Activity.RESULT_OK) {

            var image_path = ""
            if (str_imgTypeClick.equals("PhotoCameraRequest", ignoreCase = true)) {
                // val result = CropImage.getActivityResult(data)
                val resultUri = data?.data

                url?.text = resultUri.toString()
            } else if (str_imgTypeClick.equals("PhotoGalleryRequest", ignoreCase = true)) {
                /*val result = CropImage.getActivityResult(data)
                val resultUri = result.uri*/

                val resultUri = data?.data
                url?.text = resultUri.toString()
            }
        }

    }

    private fun uploadImageInfirebase(toString: String) {
        val storageRef: StorageReference = storage.reference

        val mountainsRef = storageRef.child(toString)
        // Create a reference to 'images/mountains.jpg'

        val mountainImagesRef = storageRef.child("images/$toString")

//        // While the file names are the same, the references point to different files
//        mountainsRef.name == mountainImagesRef.name // true
//
//        mountainsRef.path == mountainImagesRef.path
//
        storage.reference.putFile(Uri.parse(toString))
    }


    private fun sendChat() {


        val message = chatPojo(
            "101",
            etMessage?.getText().toString(),
            "niraj",
            System.currentTimeMillis().toString(),
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