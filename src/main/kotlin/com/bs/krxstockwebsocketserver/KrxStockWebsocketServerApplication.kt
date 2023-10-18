package com.bs.krxstockwebsocketserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KrxStockWebsocketServerApplication

fun main(args: Array<String>) {
	runApplication<KrxStockWebsocketServerApplication>(*args)
}
