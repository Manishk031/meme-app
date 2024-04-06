package com.zoro.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    var CurrentImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        loadMessage()
    }

    fun loadMessage() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"
        val memeImageView: ImageView = findViewById(R.id.memeImageView)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        // Show progress bar
        progressBar.visibility = View.VISIBLE

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    CurrentImageUrl = response.getString("url")
                    // Now you can use the 'url' variable as needed
                    // Toast.makeText(this, "URL: $url", Toast.LENGTH_SHORT).show()
                    Glide.with(this@MainActivity)
                        .load(CurrentImageUrl)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility = View.GONE
                                return false
                            }

                        })
                        .into(memeImageView)
                } catch (e: JSONException) {
                    //   Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show()
                }
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        )
        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest)
    }

    fun nextMeme(view: View) {
        loadMessage()
    }


    fun shareMeme(view: View) {

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TIME, "Check out this meme!$CurrentImageUrl")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "share Meme"))

    }


}
