package main

import (
	"monitor/utils"

	"github.com/gin-gonic/gin"
)

func ping(c *gin.Context) {
	c.JSON(200, gin.H{
		"message": "pong",
	})
}

func main() {
	r := gin.Default()
	r.GET("/ping", ping)
	r.GET("/ip", utils.GetIp)
	r.Run(":8080")
}
