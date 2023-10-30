package com.bs.krxstockwebsocketserver.kafka.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class KrxStock @JsonCreator constructor(
    @JsonProperty("ticker") val ticker: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("openPrice") val openPrice: String,
    @JsonProperty("highestPrice") val highestPrice: String,
    @JsonProperty("lowestPrice") val lowestPrice: String,
    @JsonProperty("closePrice") val closePrice: String,
    @JsonProperty("volume") val volume: String,
    @JsonProperty("fluctuationRange") val fluctuationRange: String,
    @JsonProperty("fluctuationRate") val fluctuationRate: String,
    @JsonProperty("fluctuationSign") var fluctuationSign: String? = "",
    @JsonProperty("tradingValue") val tradingValue: String,
    @JsonProperty("marketCap") val marketCap: String,
){
    fun setFluctuationSign(){
        if (this.fluctuationRange.toInt() == 0) return
        if (this.fluctuationRange.toInt() > 0 ) {
            this.fluctuationSign = "+"
            return
        }
        this.fluctuationSign = "-"
    }
}
