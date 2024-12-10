@file:Suppress("MemberVisibilityCanBePrivate")

package zhouc.monitor.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zhouc.monitor.services.*

@RestController
@RequestMapping("/api")
class ApiControllers {

    val funcs=Functions()

    @RequestMapping("/all")
    fun allData(): AllData {
        return funcs.getAllData()
    }

    @RequestMapping("/network")
    fun networkData(): ArrayList<NetworkData>{
        return funcs.getNetworkData()
    }
}