package c14220041.room

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import c14220041.room.database.daftarBelanja
import c14220041.room.database.daftarBelanjaDB
import c14220041.room.helper.DateHelper.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TambahDaftar : AppCompatActivity() {

    private lateinit var _btnTambah: Button
    private lateinit var _btnUpdate: Button
    private lateinit var _etItem: EditText
    private lateinit var _etJumlah: EditText
    var DB = daftarBelanjaDB.getDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_daftar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _btnTambah = findViewById(R.id.btnTambah)
        _btnUpdate = findViewById(R.id.btnUpdate)
        _etItem = findViewById(R.id.etItem)
        _etJumlah = findViewById(R.id.etJumlah)

        var tanggal = getCurrentDate()

        _btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.fundaftaBelanjaDAO().insert(
                    daftarBelanja(
                        tanggal = tanggal,
                        item = _etItem.text.toString(),
                        jumlah = _etJumlah.text.toString()
                    )
                )
            }
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }

        var iID: Int = 0
        var iAddEdit: Int = 0

        iID = intent.getIntExtra("id", 0)
        iAddEdit = intent.getIntExtra("addEdit", 0)

        if (iAddEdit == 0) {
            _btnTambah.visibility = View.VISIBLE
            _btnUpdate.visibility = View.GONE
            _etItem.isEnabled = true
        } else {
            _btnTambah.visibility = View.GONE
            _btnUpdate.visibility = View.VISIBLE
            _etItem.isEnabled = false

            CoroutineScope(Dispatchers.IO).async {
                val item = DB.fundaftaBelanjaDAO().getItem(iID)
                _etItem.setText(item.item)
                _etJumlah.setText(item.jumlah)
            }
        }

        _btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.fundaftaBelanjaDAO().update(
                    isi_tanggal = tanggal,
                    isi_item = _etItem.text.toString(),
                    isi_jumlah = _etJumlah.text.toString(),
                    isi_status = 0,
                    pilihid = iID
                )
            }
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }
}