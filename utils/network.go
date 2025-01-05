package utils

import (
	"io"
	"net/http"

	"github.com/gin-gonic/gin"
)

func getIpv4() string {
	resp, err := http.Get("https://4.ipw.cn")
	if err != nil {
		return ""
	}
	defer resp.Body.Close()
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return ""
	}
	return string(body)
}

func getIpv6() string {
	resp, err := http.Get("https://6.ipw.cn")
	if err != nil {
		return ""
	}
	defer resp.Body.Close()
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return ""
	}
	return string(body)
}

func GetIp(c *gin.Context) {
	c.JSON(200, gin.H{
		"ok": true,
		"msg": gin.H{
			"ipv4": getIpv4(),
			"ipv6": getIpv6(),
		},
	})
}
