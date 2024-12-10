package zhouc.monitor.services

data class ProcessorData(
    val name: String,
    val pysicalCount: Int,
    val logicalCount: Int,
    val baseFreq: Int,
    val usage: Double
)