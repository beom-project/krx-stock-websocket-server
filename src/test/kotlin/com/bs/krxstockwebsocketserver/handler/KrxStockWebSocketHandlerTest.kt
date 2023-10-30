package com.bs.krxstockwebsocketserver.handler

import com.bs.krxstockwebsocketserver.kafka.dto.KrxStock
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles


@WebMvcTest
@ActiveProfiles("test")
internal class KrxStockWebSocketHandlerTest(){
    
    @MockBean
    private lateinit var krxStockWebSocketHandler: KrxStockWebSocketHandler

    private val objectMapper:ObjectMapper= ObjectMapper()

    @Test
    fun sendStockMessageTest() {
        //given
        val stock1 = KrxStock(
            ticker = "373220",
            name = "LG에너지솔루션",
            openPrice = "1111",
            highestPrice = "2222",
            lowestPrice = "1111",
            closePrice = "1333",
            volume = "11111111",
            fluctuationRange = "-1000",
            fluctuationRate = "-1.00",
            tradingValue = "1111111",
            marketCap = "1111111111111",
        )
        val stock2 = KrxStock(
            ticker = "005930",
            name = "삼성전자",
            openPrice = "1234",
            highestPrice = "2222",
            lowestPrice = "1111",
            closePrice = "1333",
            volume = "11111111",
            fluctuationRange = "1000",
            fluctuationRate = "1.00",
            tradingValue = "111111",
            marketCap = "1111111122222",
        )
        val res = listOf(stock1, stock2)

        val message = objectMapper.writeValueAsString(res)
        //when//then
        krxStockWebSocketHandler.sendKrxStockPrice(message)
    }
}