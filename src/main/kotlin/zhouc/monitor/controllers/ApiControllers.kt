package zhouc.monitor.controllers

import lombok.Data
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Data
class DataInfo{

}

@RestController
@RequestMapping("/api")
class ApiControllers {

    @RequestMapping("/get")
    fun getdata(): DataInfo{
        return DataInfo()
    }

    @RequestMapping("/test")
    fun test(): String{
        return "Hello world!"
    }
}