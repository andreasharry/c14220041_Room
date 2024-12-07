package c14220041.room.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface historyBarangDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: HistoryBarang)

    @Query("UPDATE HistoryBarang SET tanggal=:isi_tanggal, status=:isi_status, item=:isi_item, jumlah=:isi_jumlah WHERE id=:pilihid")
    fun update(isi_tanggal: String, isi_item: String, isi_jumlah: String, isi_status: Int, pilihid: Int)

    @Delete
    fun delete(history: HistoryBarang)

    @Query("SELECT * FROM historyBarang ORDER BY id asc")
    fun selectAll(): MutableList<HistoryBarang>

    @Query("SELECT * FROM historyBarang WHERE id=:isi_id")
    suspend fun getItem(isi_id: Int): HistoryBarang
}