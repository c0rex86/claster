package main

import (
	"fmt"
	"log"
	"os"
	"strings"

	"github.com/gin-gonic/gin"
	"gopkg.in/yaml.v3"
)

type Config struct {
	Server struct {
		Port    int      `yaml:"port"`
		SSL     SSL      `yaml:"ssl"`
		Domains []Domain `yaml:"domains"`
	} `yaml:"server"`
}

type SSL struct {
	Enabled  bool   `yaml:"enabled"`
	CertFile string `yaml:"cert_file"`
	KeyFile  string `yaml:"key_file"`
}

type Domain struct {
	Name    string `yaml:"name"`
	Enabled bool   `yaml:"enabled"`
}

func loadConfig() (*Config, error) {
	f, err := os.ReadFile("config.yaml")
	if err != nil {
		return nil, err
	}

	var cfg Config
	if err := yaml.Unmarshal(f, &cfg); err != nil {
		return nil, err
	}

	return &cfg, nil
}

func main() {
	cfg, err := loadConfig()
	if err != nil {
		log.Fatalf("Error loading config: %v", err)
	}

	r := gin.Default()

	r.Use(func(c *gin.Context) {
		host := strings.Split(c.Request.Host, ":")[0]
		allowed := false
		for _, domain := range cfg.Server.Domains {
			if domain.Name == host && domain.Enabled {
				allowed = true
				break
			}
		}
		if !allowed {
			c.AbortWithStatus(403)
			return
		}
		c.Next()
	})

	r.GET("/", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "Welcome to Cluster",
			"domain":  c.Request.Host,
		})
	})

	addr := fmt.Sprintf(":%d", cfg.Server.Port)

	if cfg.Server.SSL.Enabled {
		log.Printf("Starting HTTPS server on port %d", cfg.Server.Port)
		log.Fatal(r.RunTLS(addr, cfg.Server.SSL.CertFile, cfg.Server.SSL.KeyFile))
	} else {
		log.Printf("Starting HTTP server on port %d", cfg.Server.Port)
		log.Fatal(r.Run(addr))
	}
}
