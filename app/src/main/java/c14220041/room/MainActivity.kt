package c14220041.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import c14220041.room.database.daftarBelanjaDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        private lateinit var DB : daftarBelanjaDB
        DB = daftarBelanjaDB.getDatabase(this)

        _fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahDaftar::class.java))
        }
        super.onStart()
        CoroutineScope(Dispatchers.Main).async{
            val daftarBelanja = DB.fundaftaBelanjaDAO().selectAll()
            Log.d("data ROOM", daftarBelanja.toString())
        }
    }
}