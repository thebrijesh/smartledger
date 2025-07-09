# 🐳 Docker Command Reference for SmartLedger

## 📚 Essential Docker Commands

### 🖼️ Image Management
```bash
# List all images
docker images

# Build an image from Dockerfile
docker build -t smartledger:latest .

# Remove an image
docker rmi smartledger:latest

# Pull an image from registry
docker pull mysql:8.0

# Push image to registry
docker push your-registry/smartledger:latest
```

### 📦 Container Management
```bash
# List running containers
docker ps

# List all containers (including stopped)
docker ps -a

# Run a container
docker run -d --name smartledger-app -p 5000:5000 smartledger:latest

# Stop a container
docker stop smartledger-app

# Start a stopped container
docker start smartledger-app

# Remove a container
docker rm smartledger-app

# Execute command in running container
docker exec -it smartledger-app bash

# View container logs
docker logs smartledger-app

# Follow logs in real-time
docker logs -f smartledger-app
```

### 🧹 Cleanup Commands
```bash
# Remove stopped containers
docker container prune

# Remove unused images
docker image prune

# Remove unused volumes
docker volume prune

# Remove everything unused (CAREFUL!)
docker system prune -a

# See disk usage
docker system df
```

## 🎭 Docker Compose Commands

### 🚀 Service Management
```bash
# Start all services in background
docker-compose up -d

# Start specific service
docker-compose up -d mysql

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Restart services
docker-compose restart

# View service status
docker-compose ps

# View logs
docker-compose logs

# Follow logs for specific service
docker-compose logs -f app

# Build and start
docker-compose up --build

# Scale a service
docker-compose up --scale app=3
```

### 🔧 Development Commands
```bash
# Start development environment
docker-compose -f docker-compose.dev.yml up -d

# Rebuild a specific service
docker-compose build app

# Execute command in service
docker-compose exec app bash

# Run one-off command
docker-compose run app npm install
```

## 🛠️ SmartLedger Specific Commands

### 📊 Database Operations
```bash
# Connect to MySQL
docker exec -it smartledger-mysql mysql -u smartledger -p

# Run SQL script
docker exec -i smartledger-mysql mysql -u smartledger -p < backup.sql

# Create database backup
docker exec smartledger-mysql mysqldump -u smartledger -p khatabook > backup.sql
```

### 🔴 Redis Operations
```bash
# Connect to Redis
docker exec -it smartledger-redis redis-cli

# Monitor Redis commands
docker exec smartledger-redis redis-cli monitor

# Get Redis info
docker exec smartledger-redis redis-cli info
```

### 🐛 Debugging Commands
```bash
# Check container resource usage
docker stats

# Inspect container details
docker inspect smartledger-app

# Check network connections
docker network ls

# Inspect volumes
docker volume ls
```

## 🚨 Common Issues & Solutions

### ❌ Port Already in Use
```bash
# Find process using port
netstat -ano | findstr :5000
# Kill process
taskkill /PID <PID> /F
```

### 💾 Out of Disk Space
```bash
# Clean up everything
docker system prune -a --volumes
```

### 🔄 Container Won't Start
```bash
# Check logs
docker logs container-name

# Run in interactive mode
docker run -it --rm image-name bash
```

### 🌐 Can't Connect to Services
```bash
# Check if container is running
docker ps

# Check container IP
docker inspect container-name | grep IPAddress

# Test network connectivity
docker exec app ping mysql
```

## 🏗️ Building Your Own Images

### 📝 Dockerfile Best Practices
- Use specific image tags (not `latest`)
- Order instructions by change frequency
- Use `.dockerignore` to exclude files
- Run containers as non-root user
- Use multi-stage builds for smaller images

### 🔒 Security Tips
- Don't store secrets in images
- Use minimal base images
- Scan images for vulnerabilities
- Keep images updated

## 🎯 Environment-Specific Commands

### 🛠️ Development
```bash
# Start dev services only
docker-compose -f docker-compose.dev.yml up -d

# View dev logs
docker-compose -f docker-compose.dev.yml logs -f
```

### 🚀 Production
```bash
# Production deployment
docker-compose -f docker-compose.yml up -d

# Health check
docker-compose exec app curl http://localhost:5000/actuator/health
```

## 📈 Monitoring & Performance

### 📊 Resource Monitoring
```bash
# Real-time stats
docker stats

# Container processes
docker exec app top

# Disk usage
docker system df
```

### 🔍 Log Management
```bash
# Limit log output
docker logs --tail 50 app

# Search logs
docker logs app | grep ERROR

# Export logs
docker logs app > app.log
```

## 🎓 Learning Resources

- **Official Docs**: https://docs.docker.com/
- **Docker Hub**: https://hub.docker.com/
- **Best Practices**: https://docs.docker.com/develop/dev-best-practices/
- **Security**: https://docs.docker.com/engine/security/
