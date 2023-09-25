import com.example.hotelapplication.model.TokenType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import java.util.*

data class Token(
    var id: String? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var type: TokenType? = null,
    var emailToken: String? = null,
    var valid: Boolean? = null,
    var expiration: Date? = null,
    var clientId: String? = null
) {

}
