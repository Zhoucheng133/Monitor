package zhouc.monitor.services

import oshi.SystemInfo
import oshi.software.os.OSFileStore

data class AllData(
    val systemData: SystemData,
    val processorData: ProcessorData,
    val ramData: RamData,
    val diskData: ArrayList<DiskData>
)

data class WsData(
    val ramData: RamData,
    val processorData: ProcessorData,
)

class Functions {
    val systemInfo = SystemInfo()
    val os = systemInfo.operatingSystem
    val processor = systemInfo.hardware.processor
    val memory = systemInfo.hardware.memory
    val fileStores: List<OSFileStore> = os.fileSystem.fileStores

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
            ArrayList<DiskData>(
                fileStores.map {
                    DiskData(
                        name = it.name,
                        total = (it.totalSpace/(1024*1024)).toInt(),
                        used = (it.usableSpace/(1024*1024)).toInt(),
                    )
                }
            )
        )
    }

    fun getWsData(): WsData{
        return WsData(
            ramData = RamData(
                total=(memory.total/1024/1024).toInt(),
                available=(memory.available/1024/1024).toInt()
            ),
            processorData = ProcessorData(
                name = processor.getProcessorIdentifier().getName(),
                pysicalCount = processor.getPhysicalProcessorCount(),
                logicalCount=processor.getLogicalProcessorCount(),
                baseFreq = (processor.getProcessorIdentifier().getVendorFreq()/1000000).toInt(),
                usage = processor.getSystemCpuLoad(1000)
            ),
        )
    }
}