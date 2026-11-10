package vn.edu.vlu.lab09

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import vn.edu.vlu.lab09.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (auth.currentUser == null) {
            openAuth()
            return
        }

        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val safeArea = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(safeArea.left, safeArea.top, safeArea.right, safeArea.bottom)
            insets
        }

        binding.rvPhotos.layoutManager = LinearLayoutManager(this)
        binding.rvPhotos.adapter = PhotoAdapter(PhotoDataSource.getPhotos())

        val logoutItem = binding.toolbar.menu.add("Logout")
        logoutItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        logoutItem.setOnMenuItemClickListener {
            auth.signOut()
            openAuth()
            true
        }
    }

    private fun openAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}