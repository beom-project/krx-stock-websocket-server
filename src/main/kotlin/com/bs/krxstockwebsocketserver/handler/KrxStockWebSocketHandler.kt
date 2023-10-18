package com.bs.krxstockwebsocketserver.handler

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException


@Component
class KrxStockWebSocketHandler : TextWebSocketHandler() {
    private val sessionMap: HashMap<String, WebSocketSession> = HashMap()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessionMap[session.id] = session
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionMap.remove(session.id)
    }

    fun sendKrxStockPrice(stockInfo: String) {
        sessionMap.entries.stream().forEach { s ->
            try {
                s.value.sendMessage(TextMessage(stockInfo))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}