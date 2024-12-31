package zhouc.monitor.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import zhouc.monitor.services.AllData
import zhouc.monitor.services.Functions

@RestController
@RequestMapping("/api")
@CrossOrigin
class StaticController {
    val funcs= Functions()

    @RequestMapping("/get")
    fun allData(): AllData {
        return funcs.getAllData()
    }
}