import entity.MahasiswaItem
import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import io.quarkus.runtime.annotations.QuarkusMain
import utility.ConfigUtility
import utility.JsonUtility
import java.time.LocalDateTime
import java.util.*
import java.util.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.print.attribute.standard.Media
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@QuarkusMain
class MainApp : QuarkusApplication {
    companion object{
        @JvmStatic
        fun main(args: Array<String>){
            Quarkus.run(MainApp::class.java, *args)
        }
    }

    override fun run(vararg args: String?): Int {
        Quarkus.waitForExit()
        return 0
    }
}

@Suppress("unused")
@ApplicationScoped
class LifecycleBean {
    fun onStart(@Observes event: StartupEvent){
        Logger.getLogger("LifecycleBean").
        info{ "Application just starting..." }
    }

    fun onStop(@Observes event: ShutdownEvent){
        Logger.getLogger("LifecycleBean").info { "Application just shutdown!" }
    }
}

@Path("/mahasiswa/service")
class MahasiswaDataService{
    @Inject
    lateinit var entityManager: EntityManager

    @Path("/testing") @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun testing(): String{
        return "Test: Mahasiswa data service!"
    }

    @Transactional @POST
    @Path("synch/createMahasiswa/{nim}/{nama}/{alamat}/{jurusan}")
    fun createMahasiswaItem(@PathParam("nim") nim: String,
                            @PathParam("nama") nama: String,
                            @PathParam("alamat") alamat: String,
                            @PathParam("jurusan") jurusan: String) : Response{
        val query = MahasiswaItem().apply {
            this.nim = nim
            this.nama = nama
            this.alamat = alamat
            this.jurusan = jurusan
        }

        entityManager.persist(query)
//        return query
        return Response.ok().entity(JsonUtility.toJson(query)).build()
    }

    @GET @Path("synch/listMahasiswa/list")
    fun findAllMahasiswa() : List<MahasiswaItem>{
        val query = entityManager.createNamedQuery("MahasiswaItems.findAll", MahasiswaItem::class.java)
        return query.resultList
    }

    @PUT @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Path("synch/updateMahasiswa/{id}/{nim}/{nama}/{alamat}")
    fun updateMahasiswa(@PathParam("id") id: Long,
                        @QueryParam("nim") nim: String,
                        @QueryParam("nama") nama: String,
                        @QueryParam("alamat") alamat:String,
                        @QueryParam("jurusan") jurusan: String) : Response {
        val query = entityManager.createNamedQuery("MahasiswaItems.findById", MahasiswaItem::class.java)
        query.setParameter("mhsId", id)
        query.resultStream.findFirst().get().apply {
            this.nim = nim
            this.nama = nama
            this.jurusan = jurusan
            this.alamat = alamat
            this.update += 1
            this.updatedAt = LocalDateTime.now()
            entityManager.persist(this)
        }
//        entityManager.persist(query)
        return Response.ok().entity(JsonUtility.toJson(query)).build()
    }

}

