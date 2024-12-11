package zhouc.monitor.controllers

import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

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
    private val logger = LoggerFactory.getLogger(MyWebSocketHandler::class.java)
    private val executor = Executors.newSingleThreadScheduledExecutor()  // 更高效的定时任务调度器
    private var scheduledFuture: java.util.concurrent.ScheduledFuture<*>? = null

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        // 如果定时任务已经存在，取消之前的任务并重新调度
        scheduledFuture?.cancel(false)

        // 重新调度定时任务
        scheduledFuture = executor.scheduleAtFixedRate({
            try {
                println("定时执行的内容")
                session.sendMessage(TextMessage("这是定时执行的内容，间隔时间为2秒"))
            } catch (e: Exception) {
                logger.error("Error sending WebSocket message", e)
            }
        }, 0, 2, TimeUnit.SECONDS)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("New connection established: ${session.id}")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        scheduledFuture?.cancel(false)  // 取消定时任务
        executor.shutdown()  // 关闭定时任务调度器
        println("Connection closed: ${session.id}")
    }
}
