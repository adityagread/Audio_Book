package com.example.audiobook

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import java.io.IOException

class AudioPlayer : AppCompatActivity() {
    lateinit var book_title: String
    lateinit var book_writer : String
    lateinit var book_image: String
    lateinit var  book_audio: String
    private lateinit var playBtn: Button
    private lateinit var revindBtn: Button
    private lateinit var forwardBtn: Button
    private lateinit var seek_bar: SeekBar
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private lateinit var seekStart: TextView
    private lateinit var seekEnd: TextView
    private var pause:Boolean = false
    var mediaPlayer : MediaPlayer? = null

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        book_title = intent.getStringExtra("title").toString()
        book_writer = intent.getStringExtra("writer").toString()
        book_image = intent.getStringExtra("image").toString()
        book_audio = intent.getStringExtra("audio").toString()

        var bookTitle = findViewById<TextView>(R.id.book_title)
        var bookTitleB = findViewById<TextView>(R.id.audio_name)
        var bookWriter = findViewById<TextView>(R.id.audio_author)
        var bookImage = findViewById<ImageView>(R.id.audio_image)
        var bookmark = findViewById<Button>(R.id.btn_bookmark)
        var chapter = findViewById<Button>(R.id.btn_next_chapter)
        var download = findViewById<Button>(R.id.btn_download)
        var upload = findViewById<Button>(R.id.btn_upload)
        playBtn = findViewById(R.id.playbtn)
        revindBtn = findViewById(R.id.btnfr)
        forwardBtn = findViewById(R.id.btnff)
        seek_bar = findViewById(R.id.seekbar)
        seekStart = findViewById(R.id.txtsstart)
        seekEnd = findViewById(R.id.txtsstop)

        bookTitle.text = book_title
        bookTitleB.text = book_title
        bookWriter.text = book_writer

        Glide.with(this).load(book_image).into(bookImage)
        Log.d(TAG, "onCreate: "+book_audio)
        playAudio()
        initializeSeekBar()

        playBtn.setOnClickListener {
            if(mediaPlayer?.isPlaying == true)
                pauseAudio()
            else
                playAudio()
        }
        revindBtn.setOnClickListener {
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition-10000)
            }
        }
        forwardBtn.setOnClickListener {
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition+10000)
            }
        }
        upload.setOnClickListener {
            Toast.makeText(this,"Not Available",Toast.LENGTH_SHORT).show()
        }
        download.setOnClickListener {
            Toast.makeText(this,"Not Available",Toast.LENGTH_SHORT).show()
        }
        bookmark.setOnClickListener {
            Toast.makeText(this,"Not Available",Toast.LENGTH_SHORT).show()
        }
        chapter.setOnClickListener {
            Toast.makeText(this,"Only 1 chapter Available",Toast.LENGTH_SHORT).show()
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer?.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    private fun playAudio() {
        val audioURL = book_audio
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer!!.setDataSource(audioURL)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        }catch (e: IOException){
            e.printStackTrace()
        }
        Toast.makeText(this,"Audio Started",Toast.LENGTH_SHORT).show()
    }

    private fun pauseAudio() {
        if(mediaPlayer?.isPlaying == true){
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
        }else{
            Toast.makeText(this,"Audio has not Played",Toast.LENGTH_SHORT).show()
        }
    }
    private fun initializeSeekBar() {
        seek_bar.max = mediaPlayer!!.seconds

        runnable = Runnable {
            seek_bar.progress = mediaPlayer!!.currentSeconds

            seekStart.text = "${mediaPlayer!!.currentSeconds} sec"
            val diff = mediaPlayer!!.seconds - mediaPlayer!!.currentSeconds
            seekEnd.text = "$diff sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}
val MediaPlayer.seconds:Int
    get() {
        return this.duration / 1000
    }
// Creating an extension property to get media player current position in seconds
val MediaPlayer.currentSeconds:Int
    get() {
        return this.currentPosition/1000
    }