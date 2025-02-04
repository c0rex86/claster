# Cluster
Go web server 

Example:
```yaml
server:
  port: 443
  ssl:
    enabled: true
    cert_file: "cert.pem"
    key_file: "key.pem"
  domains:
    - name: "example.com"
      enabled: true
    - name: "api.example.com"
      enabled: true
``` 