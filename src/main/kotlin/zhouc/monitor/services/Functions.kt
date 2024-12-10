package zhouc.monitor.services

import oshi.SystemInfo

data class AllData(
    val systemData: SystemData,
    val processorData: ProcessorData,
    val ramData: RamData,
)

class Functions {
    val systemInfo = SystemInfo()
    val os = systemInfo.operatingSystem
    val processor = systemInfo.hardware.processor
    val memory = systemInfo.hardware.memory
    var networks=systemInfo.hardware.networkIFs

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
        )
    }

    fun getNetworkData(): ArrayList<NetworkData>{

        val ls=ArrayList<NetworkData>()

        for (networkIF in networks){
            networkIF.updateAttributes()
            val rxBytes = networkIF.bytesRecv
            val txBytes = networkIF.bytesSent
            if(rxBytes==0L && txBytes==0L){
                ls.add(NetworkData(
                    name = networkIF.name,
                    downloadSpeed = 0,
                    uploadSpeedt = 0
                ))
                continue
            }
            try {
                Thread.sleep(1000)
            }catch(_: ArithmeticException){}
            networkIF.updateAttributes()
            val rxBytes1 = networkIF.bytesRecv
            val txBytes1 = networkIF.bytesSent
            val rxSpeed = ((rxBytes1 - rxBytes) * 8 / 1024).toInt()
            val txSpeed = ((txBytes1 - txBytes) * 8 / 1024).toInt()
            ls.add(NetworkData(
                name = networkIF.name,
                downloadSpeed = rxSpeed,
                uploadSpeedt = txSpeed
            ))
        }

        return ls
    }
}