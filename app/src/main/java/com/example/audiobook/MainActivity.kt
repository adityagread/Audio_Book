package com.example.audiobook

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.audiobook.MyAdapter.onItemClickListner
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseFirestore : FirebaseFirestore
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<Book>
    private lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userRecyclerview = findViewById(R.id.recycler_view)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<Book>()
        myAdapter = MyAdapter(userArrayList)
        userRecyclerview.adapter = myAdapter
        myAdapter.setOnItemClickListner(object : MyAdapter.onItemClickListner{
            override fun onItemClick(position: Int) {
                var intent = Intent(this@MainActivity,AudioPlayer::class.java)
                intent.putExtra("title",userArrayList[position].title)
                intent.putExtra("writer",userArrayList[position].writer)
                intent.putExtra("image",userArrayList[position].image)
                intent.putExtra("audio",userArrayList[position].audio)
                startActivity(intent)
            }

        })
        getBookData()

    }

    private fun getBookData() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseFirestore
            .collection("Book")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {

                        if(error != null){
                            Log.d(TAG, "onEvent: "+ error.message.toString())
                        }
                    for (dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(Book::class.java))
                        }
                    }
                        myAdapter.notifyDataSetChanged()
                }

            })

        Log.d(TAG, "getBookData: "+ userArrayList.size)
    }
}