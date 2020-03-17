import io.ktor.server.testing.*


fun withServer(block: TestApplicationEngine.() -> Unit) {
    withTestApplication({ module() }, block)
}


