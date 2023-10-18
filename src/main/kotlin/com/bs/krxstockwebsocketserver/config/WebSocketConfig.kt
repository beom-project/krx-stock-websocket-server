package com.bs.krxstockwebsocketserver.config

import com.bs.krxstockwebsocketserver.handler.KrxStockWebSocketHandler
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
class WebSocketConfig(
    private val krxStockWebSocketHandler: KrxStockWebSocketHandler
): WebSocketConfigurer{
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(krxStockWebSocketHandler, "stock").setAllowedOrigins("*")
    }
}