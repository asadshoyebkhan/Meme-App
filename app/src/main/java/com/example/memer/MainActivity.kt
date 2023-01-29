package com.example.memer

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity()
{
    var currentImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    // Instantiate the RequestQueue.
    private fun loadMeme()
    {
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        progress.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentImageUrl = response.getString("url")
                val meeme = findViewById<ImageView>(R.id.memeimageView)
                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>
                {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility = View.GONE
                        return false

                    }
                }).into(meeme)

            },

            {
                // TODO: Handle error
                Toast.makeText(this,"Something Wrong", Toast.LENGTH_LONG).show()

            }
        )

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

    }


    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Check this Out! $currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share it on...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}