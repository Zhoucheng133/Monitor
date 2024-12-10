package zhouc.monitor.services

import oshi.SystemInfo

data class AllData(
    val systemData: SystemData,
    val processorData: ProcessorData,
    val ramData: RamData,
    val networkData: ArrayList<NetworkData>
)

class Functions {
    val systemInfo = SystemInfo()
    val os = systemInfo.operatingSystem
    val processor = systemInfo.hardware.processor
    val memory = systemInfo.hardware.memory
    var networks=ArrayList<NetworkData>()

    fun getAllData(): AllData {
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
            ),
            networks
        )
    }
}