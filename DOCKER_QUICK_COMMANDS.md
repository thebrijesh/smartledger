# 🎯 Quick Docker Commands for SmartLedger

## 🚀 **Daily Operations**

### Start Everything
```bash
# Full stack (production-like)
docker-compose up -d

# Development mode (just DB + Redis)
docker-compose -f docker-compose.dev.yml up -d

# With logs visible
docker-compose up
```

### Stop Services
```bash
# Graceful stop
docker-compose down

# Stop and remove volumes (fresh start)
docker-compose down -v

# Stop specific service
docker-compose stop app
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f app
docker-compose logs -f mysql

# Last 50 lines
docker-compose logs --tail=50 app
```

## 🔍 **Monitoring & Debugging**

### Check Status
```bash
# Service status
docker-compose ps

# Resource usage
docker stats

# Inspect container
docker inspect smartledger-app
```

### Database Operations
```bash
# Connect to MySQL
docker exec -it smartledger-mysql mysql -u smartledger -p

# MySQL shell
docker exec -it smartledger-mysql bash

# Backup database
docker exec smartledger-mysql mysqldump -u smartledger -p khatabook > backup.sql
```

### Redis Operations
```bash
# Connect to Redis
docker exec -it smartledger-redis redis-cli

# Monitor Redis
docker exec -it smartledger-redis redis-cli monitor
```

## 🔄 **Development Workflow**

### Rebuild After Code Changes
```bash
# Rebuild application image
docker-compose build app

# Rebuild and restart
docker-compose up -d --build app

# Force rebuild (no cache)
docker-compose build --no-cache app
```

### Update Dependencies
```bash
# Update images
docker-compose pull

# Restart with new images
docker-compose down && docker-compose up -d
```

## 🧹 **Cleanup**

### Remove Unused Resources
```bash
# Remove stopped containers
docker container prune

# Remove unused images
docker image prune

# Remove unused volumes
docker volume prune

# Remove everything unused
docker system prune -a
```

### Reset Everything
```bash
# Stop and remove all containers, networks, volumes
docker-compose down -v
docker system prune -a --volumes

# Then restart fresh
docker-compose up -d
```

## 🎯 **Testing & Validation**

### Health Checks
```bash
# Test application
curl http://localhost:5000

# Test via Nginx
curl http://localhost:80

# Test MySQL connection
docker exec smartledger-mysql mysqladmin ping

# Test Redis
docker exec smartledger-redis redis-cli ping
```

### Performance Testing
```bash
# Resource usage
docker stats --no-stream

# Container processes
docker exec smartledger-app ps aux

# Application logs
docker logs smartledger-app --follow
```

## 🔧 **Troubleshooting**

### Common Issues
```bash
# Container won't start
docker-compose logs [service-name]

# Database connection issues
docker exec -it smartledger-mysql mysql -u root -p

# Port conflicts
docker-compose down && docker-compose up -d

# Permission issues
docker exec -it smartledger-app ls -la /app
```

### Debug Mode
```bash
# Run container interactively
docker run -it --rm smartledger-app sh

# Execute shell in running container
docker exec -it smartledger-app sh

# Check environment variables
docker exec smartledger-app env
```

## 🎨 **Development Tips**

### Live Development
```bash
# Start just databases for local development
docker-compose -f docker-compose.dev.yml up -d

# Run Spring Boot locally with profile
java -jar target/smartledger.jar --spring.profiles.active=local

# Watch for changes
docker-compose up --build
```

### Profile Management
```bash
# Development profile
SPRING_PROFILES_ACTIVE=local docker-compose up

# Production profile  
SPRING_PROFILES_ACTIVE=production docker-compose up

# Custom environment
docker-compose --env-file .env.custom up
```

## 📊 **Monitoring**

### Resource Monitoring
```bash
# Real-time stats
docker stats

# Memory usage
docker exec smartledger-app cat /proc/meminfo

# Disk usage
docker system df

# Network info
docker network ls
```

### Log Management
```bash
# Rotate logs
docker-compose logs --tail=1000 app > app.log

# Clear logs
docker-compose down && docker-compose up -d

# Set log limits in docker-compose.yml
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

## 🔗 **Useful URLs**

- **Application**: http://localhost:5000
- **Nginx**: http://localhost:80  
- **MySQL**: localhost:3307
- **Redis**: localhost:6380

## 🎉 **Happy Coding!**

Your Docker setup is now complete and ready for development!
