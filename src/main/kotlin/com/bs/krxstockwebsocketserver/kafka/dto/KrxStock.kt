package com.bs.krxstockwebsocketserver.kafka.dto

data class KrxStock(
    val ticker:String,
    val name:String,
    val openPrice:String,
    val highestPrice:String,
    val lowestPrice:String,
    val closePrice:String,
    val volume:String,
    val fluctuationRange:String,
    val fluctuationRate:String,
    var fluctuationSign:String?="",
    val tradingValue:String,
    val marketCap:String,
){
    fun setFluctuationSign(){
        if (this.fluctuationRange.toInt() = 0) return
        if (this.fluctuationRange.toInt() > 0 ) {
            this.fluctuationSign = "+"
            return
        }
        this.fluctuationSign = "-"
    }
}
