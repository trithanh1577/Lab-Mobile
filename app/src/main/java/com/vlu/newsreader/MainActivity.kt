package com.vlu.newsreader

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.JsonParseException
import com.vlu.newsreader.databinding.ActivityMainBinding
import com.vlu.newsreader.model.NewsItem
import com.vlu.newsreader.network.HttpClient
import com.vlu.newsreader.parser.NewsJsonParser
import com.vlu.newsreader.ui.NewsAdapter
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val newsAdapter = NewsAdapter()
    private val httpClient = HttpClient()
    private var lastParserMode = ParserMode.GSON

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvNews.adapter = newsAdapter
        binding.btnLoadGson.setOnClickListener {
            loadNews(ParserMode.GSON)
        }
        binding.btnLoadManual.setOnClickListener {
            loadNews(ParserMode.MANUAL)
        }
        binding.btnRetry.setOnClickListener {
            loadNews(lastParserMode)
        }

        loadNews(ParserMode.GSON)
    }

    private fun loadNews(parserMode: ParserMode) {
        lastParserMode = parserMode

        lifecycleScope.launch {
            showLoading()

            try {
                val jsonString = withContext(Dispatchers.IO) {
                    httpClient.get(POSTS_URL)
                }

                val newsItems = withContext(Dispatchers.Default) {
                    when (parserMode) {
                        ParserMode.GSON -> NewsJsonParser.parseWithGson(jsonString)
                        ParserMode.MANUAL -> NewsJsonParser.parseManually(jsonString)
                    }
                }

                showContent(newsItems, parserMode)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                showError(error)
            }
        }
    }

    private fun showLoading() = with(binding) {
        setLoadButtonsEnabled(false)
        tvStatus.text = getString(R.string.loading_message)
        tvStatus.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        rvNews.visibility = View.GONE
        errorState.visibility = View.GONE
    }

    private fun showContent(items: List<NewsItem>, parserMode: ParserMode) = with(binding) {
        newsAdapter.submitList(items)
        val parserName = when (parserMode) {
            ParserMode.GSON -> getString(R.string.gson_parser_name)
            ParserMode.MANUAL -> getString(R.string.manual_parser_name)
        }
        tvStatus.text = resources.getQuantityString(
            R.plurals.loaded_message,
            items.size,
            items.size,
            parserName
        )
        tvStatus.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        errorState.visibility = View.GONE
        rvNews.visibility = View.VISIBLE
        setLoadButtonsEnabled(true)
    }

    private fun showError(error: Exception) = with(binding) {
        tvError.text = when (error) {
            is IOException -> getString(R.string.error_no_network)
            is JSONException, is JsonParseException ->
                getString(R.string.error_invalid_data)
            else -> getString(R.string.error_generic)
        }
        tvStatus.visibility = View.GONE
        progressBar.visibility = View.GONE
        rvNews.visibility = View.GONE
        errorState.visibility = View.VISIBLE
        setLoadButtonsEnabled(true)
    }

    private fun setLoadButtonsEnabled(enabled: Boolean) = with(binding) {
        btnLoadGson.isEnabled = enabled
        btnLoadManual.isEnabled = enabled
    }

    private enum class ParserMode {
        GSON,
        MANUAL
    }

    private companion object {
        const val POSTS_URL = "https://jsonplaceholder.typicode.com/posts"
    }
}
