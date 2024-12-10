@file:Suppress("MemberVisibilityCanBePrivate")

package zhouc.monitor.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import oshi.SystemInfo


data class SystemData(
    val family: String,
    val manufacturer: String,
    val version: String,
)

data class ProcessorData(
    val name: String,
    val pysicalCount: Int,
    val logicalCount: Int,
    val baseFreq: Int,
    val usage: Double
)

data class RamData(
    val total: Int,
    val available: Int,
)

data class AllData(
    val systemData: SystemData,
    val processorData: ProcessorData,
    val ramData: RamData,
)

fun getAllData(): AllData{
    val systemInfo = SystemInfo()
    val os = systemInfo.operatingSystem
    val processor = systemInfo.hardware.processor
    val memory = systemInfo.hardware.memory
    return AllData(
        SystemData(
            family = os.family,
            manufacturer = os.manufacturer,
            version = os.versionInfo.version,
        ),
        ProcessorData(
            name = processor.getProcessorIdentifier().getName(),
            pysicalCount = processor.getPhysicalProcessorCount(),
            logicalCount=processor.getLogicalProcessorCount(),
            baseFreq = (processor.getProcessorIdentifier().getVendorFreq()/1000000).toInt(),
            usage = processor.getSystemCpuLoad(1000)
        ),
        RamData(
            total=(memory.total/1024/1024).toInt(),
            available=(memory.available/1024/1024).toInt()
        )
    )
}

@RestController
@RequestMapping("/api")
class ApiControllers {

    @RequestMapping("/all")
    fun getdata(): AllData{
        return getAllData()
    }
}