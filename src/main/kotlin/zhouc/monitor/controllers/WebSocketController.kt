package zhouc.monitor.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import zhouc.monitor.services.Functions
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@Configuration
@EnableWebSocket
class WebSocketController : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(myWebSocketHandler(), "/ws")
            .setAllowedOrigins("*")
    }

    fun myWebSocketHandler(): WebSocketHandler {
        return MyWebSocketHandler()
    }
}

class MyWebSocketHandler : TextWebSocketHandler() {
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var scheduledFuture: java.util.concurrent.ScheduledFuture<*>? = null
    val funcs: Functions= Functions()
    var objectMapper: ObjectMapper = ObjectMapper()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            if(message.payload=="ws_request"){
                scheduledFuture?.cancel(false)
                scheduledFuture = executor.scheduleAtFixedRate({
                    val jsonData = objectMapper.writeValueAsString(funcs.getWsData())
                    session.sendMessage(TextMessage(jsonData))
                }, 0, 1, TimeUnit.SECONDS)
            }

        }catch (_: Exception){}
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        scheduledFuture?.cancel(false)
    }
}
