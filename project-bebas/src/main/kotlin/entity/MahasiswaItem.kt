package entity

import io.quarkus.runtime.annotations.RegisterForReflection
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "master_mahasiswa", uniqueConstraints = [UniqueConstraint(name = "uq_mahasiswa_nim", columnNames = ["mahasiswa_nim", "status", "update"])])
@NamedQuery(name = "MahasiswaItems.findById", query = "SELECT mhs FROM MahasiswaItem mhs WHERE mhs.id = :mhsId AND status = true")
@NamedQuery(name = "MahasiswaItems.findAll", query = "SELECT mhs FROM MahasiswaItem mhs WHERE mhs.status = true ")
@NamedQuery(name = "MahasiswaItems.findByNim", query = "SELECT mhs FROM MahasiswaItem mhs WHERE mhs.nim = :mhsNim AND status = true")
@RegisterForReflection
class MahasiswaItem {
    @SequenceGenerator(name = "mahasiswaSeq", sequenceName = "mahasiswa_id_seq", allocationSize = 1, initialValue = 1 )
    @GeneratedValue(generator = "mahasiswaSeq")
    @Id
    var id: Long = 0

    @Column(name = "mahasiswa_nim", length = 15)
    var nim: String = ""

    @Column(name = "mahasiswa_nama", length = 25)
    var nama: String = ""

    @Column(name = "mahasiswa_alamat", length = 25)
    var alamat: String = ""

    @Column(name = "mahasiswa_jurusan", length = 25)
    var jurusan: String = ""

    var status: Boolean = true

    @Column(name = "update")
    var update: Long = 0

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_by")
    var updatedBy: Long = 0

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "created_by")
    var createdBy: Long = 0

//    fun update(nim: String, nama: String, U)
}