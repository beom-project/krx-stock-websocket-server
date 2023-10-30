package com.bs.krxstockwebsocketserver.kafka

import com.bs.krxstockwebsocketserver.kafka.dto.KrxStock
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.util.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 1, topics = ["krx-stocks"], brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092" ])
internal class KrxStockConsumerTest {


    @Autowired
    private lateinit var krxStockConsumer: KrxStockConsumer
    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    private val objectMapper: ObjectMapper = ObjectMapper()

    // Kafka 프로듀서 구성 설정
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

    val message = objectMapper.writeValueAsString(listOf<KrxStock>(stock1, stock2))

    val expect = convertStockListToPayload(convertMessageToKrxStockList(message))
    @BeforeEach
    fun setup() {
        kafkaTemplate.send("krx-stocks", message)
        kafkaTemplate.flush()
    }

    @Test
    @DisplayName("카프카로 받은 메시지 시가총액순으로 정렬하여 클라이언트에게 보내며 fluctuationRange 값이 0보다 작으면 '-', 크면 '+' 이다. ")
    fun listenKrxStockTest(){
        //given
        Thread.sleep(1000)

        kafkaTemplate.send("krx-stocks", message)
        kafkaTemplate.flush()

        Thread.sleep(1000)

        val result = krxStockConsumer.getPayload()
        val resultObject = objectMapper.readValue(result, Array<KrxStock>::class.java )
        //then
        Assertions.assertEquals(expect,result)
        Assertions.assertEquals("삼성전자", resultObject[0].name)
        Assertions.assertEquals("+", resultObject[0].fluctuationSign)
        Assertions.assertEquals("LG에너지솔루션", resultObject[1].name)
        Assertions.assertEquals("-", resultObject[1].fluctuationSign)
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


}