package c14220041.room

import android.os.Bundle
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnCloseListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import c14220041.room.database.daftarBelanja
import c14220041.room.database.daftarBelanjaDB

class TambahDaftar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(

        )
        setContentView(R.layout.activity_tambah_daftar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        CoroutineScope(Dispatchers.IO).async {
            DB.fundaftaBelanjaDAO().insert(
                daftarBelanja(
                    tanggal = tanggal,
                    item = _etItem.text.toString(),
                    jumlah = _etJumlah.text.toString()
                )
            )
        }
    }
    var DB = daftarBelanjaDB.getDatabase(this)
    var tanggal = getCurrentDate()
}