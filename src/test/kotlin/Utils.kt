import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.json.*
import service.*

@KtorExperimentalAPI
fun withServer(block: TestApplicationEngine.() -> Unit) = withTestApplication({ module() }) {
    DatabaseFactory.TestDb.clean()
    DatabaseFactory.TestDb.create()
    block()
}



