package c14220041.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import c14220041.room.database.daftarBelanjaDB
import c14220041.room.database.adapterDaftar
import c14220041.room.database.daftarBelanja
import c14220041.room.database.HistoryBarang
import c14220041.room.database.historyBarangDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var DB: daftarBelanjaDB
    private lateinit var DBhistoryBarang: historyBarangDB
    private lateinit var adapterDaftar: adapterDaftar
    private var arrDaftar: MutableList<daftarBelanja> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DB = daftarBelanjaDB.getDatabase(this)
        DBhistoryBarang = historyBarangDB.getDatabase(this)
        adapterDaftar = adapterDaftar(arrDaftar)

        var _fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        _fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahDaftar::class.java))
        }

        var _rvNotes = findViewById<RecyclerView>(R.id.rvNotes)
        _rvNotes.layoutManager = LinearLayoutManager(this)
        _rvNotes.adapter = adapterDaftar

        adapterDaftar.setOnItemClickCallback(
            object : adapterDaftar.OnItemClickCallback {
                override fun delData(dtBelanja: daftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        DB.fundaftaBelanjaDAO().delete(dtBelanja)
                        val daftar = DB.fundaftaBelanjaDAO().selectAll()
                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(daftar)
                        }
                    }
                }

                override fun selesaiData(dtBelanja: daftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        val history = HistoryBarang(
                            tanggal = dtBelanja.tanggal,
                            item = dtBelanja.item,
                            jumlah = dtBelanja.jumlah,
                            status = 1
                        )

                        DBhistoryBarang.funhistoryBarangDAO().insert(history)

                        DB.fundaftaBelanjaDAO().delete(dtBelanja)

                        val daftar = DB.fundaftaBelanjaDAO().selectAll()

                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(daftar)
                        }
                    }
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val daftarBelanja = DB.fundaftaBelanjaDAO().selectAll()
            adapterDaftar.isiData(daftarBelanja)
            Log.d("data ROOM", daftarBelanja.toString())
        }
    }
}

