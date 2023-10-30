package com.bs.krxstockwebsocketserver.kafka

import com.bs.krxstockwebsocketserver.handler.KrxStockWebSocketHandler
import com.bs.krxstockwebsocketserver.kafka.dto.KrxStock
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.Collections
import java.util.Comparator

@Service
class KrxStockConsumer(
    private val krxStockWebSocketHandler: KrxStockWebSocketHandler,
    private val objectMapper: ObjectMapper = ObjectMapper(),
) {
    private var payload:String = ""

    @KafkaListener(topics = ["krx-stocks"], groupId = "krx-stock-service1")
    fun listenKrxStock(record:ConsumerRecord<String, String> ){
        var krxStockPrice = convertStockListToPayload(convertMessageToKrxStockList(record.value()))
        println("----------------------------------------------------------------------------------------------------------------")
        this.payload = krxStockPrice
        println(krxStockPrice)
        println("----------------------------------------------------------------------------------------------------------------")
        krxStockWebSocketHandler.sendKrxStockPrice(krxStockPrice)
    }

    private fun convertMessageToKrxStockList(message:String): List<KrxStock>{
        val stockList = objectMapper.readValue(message, Array<KrxStock>::class.java)
        return stockList.toList().stream().parallel().map {
            it.setFluctuationSign()
            it
        }.sorted(
            Collections.reverseOrder(
                Comparator.comparing { it.marketCap.toLong() }
            )
        ).limit(20L).toList()
    }

    private fun convertStockListToPayload(stockList:List<KrxStock>):String{
        return objectMapper.writeValueAsString(stockList)
    }

    /**
     * get payload
     */
    fun getPayload(): String {
        return this.payload
    }
}