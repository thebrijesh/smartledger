#!/bin/bash
# ════════════════════════════════════════════════════════════════════════════════════
# Docker Helper Scripts for SmartLedger
# ════════════════════════════════════════════════════════════════════════════════════
# Collection of useful Docker commands for managing your application

echo "🐳 SmartLedger Docker Helper"
echo "════════════════════════════"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker is not running. Please start Docker Desktop."
        exit 1
    fi
    print_status "Docker is running ✓"
}

# Build the application
build_app() {
    print_status "Building SmartLedger application..."
    docker-compose build --no-cache
    if [ $? -eq 0 ]; then
        print_status "Build completed successfully ✓"
    else
        print_error "Build failed ✗"
        exit 1
    fi
}

# Start all services
start_services() {
    print_status "Starting all services..."
    docker-compose up -d
    print_status "Services started. Checking health..."
    sleep 10
    docker-compose ps
}

# Start development services only
start_dev() {
    print_status "Starting development services (MySQL + Redis only)..."
    docker-compose -f docker-compose.dev.yml up -d
    print_status "Development services started ✓"
    print_warning "Run your Spring Boot app locally to connect to these services"
}

# Stop all services
stop_services() {
    print_status "Stopping all services..."
    docker-compose down
    print_status "Services stopped ✓"
}

# View logs
view_logs() {
    if [ -z "$1" ]; then
        print_status "Showing logs for all services..."
        docker-compose logs -f
    else
        print_status "Showing logs for $1..."
        docker-compose logs -f "$1"
    fi
}

# Clean up everything
cleanup() {
    print_warning "This will remove all containers, images, and volumes. Continue? (y/N)"
    read -r response
    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        print_status "Cleaning up..."
        docker-compose down -v --remove-orphans
        docker system prune -f
        print_status "Cleanup completed ✓"
    else
        print_status "Cleanup cancelled"
    fi
}

# Show application status
status() {
    print_status "Application Status:"
    echo "─────────────────────"
    docker-compose ps
    
    echo ""
    print_status "Container Health:"
    echo "─────────────────"
    for container in smartledger-mysql smartledger-redis smartledger-app; do
        if docker ps --format "table {{.Names}}" | grep -q "$container"; then
            health=$(docker inspect --format='{{.State.Health.Status}}' "$container" 2>/dev/null || echo "no health check")
            echo "  $container: $health"
        else
            echo "  $container: not running"
        fi
    done
}

# Show useful URLs and commands
info() {
    print_status "SmartLedger Application URLs:"
    echo "─────────────────────────────"
    echo "  🌐 Application: http://localhost:5000"
    echo "  📊 Health Check: http://localhost:5000/actuator/health"
    echo "  🔧 Nginx: http://localhost (if enabled)"
    echo ""
    print_status "Database Connections:"
    echo "─────────────────────"
    echo "  🗄️  MySQL: localhost:3307"
    echo "     Database: khatabook"
    echo "     Username: smartledger"
    echo "     Password: smartledger123"
    echo ""
    echo "  🔴 Redis: localhost:6380"
    echo ""
    print_status "Useful Commands:"
    echo "────────────────"
    echo "  View app logs: docker-compose logs -f app"
    echo "  Enter MySQL: docker exec -it smartledger-mysql mysql -u smartledger -p khatabook"
    echo "  Enter Redis: docker exec -it smartledger-redis redis-cli"
    echo "  Rebuild app: docker-compose build app && docker-compose up -d app"
}

# Main menu
show_menu() {
    echo ""
    echo "Available commands:"
    echo "  1. build     - Build the application"
    echo "  2. start     - Start all services"
    echo "  3. dev       - Start development services only"
    echo "  4. stop      - Stop all services"
    echo "  5. logs      - View logs (specify service name or leave empty for all)"
    echo "  6. status    - Show application status"
    echo "  7. info      - Show URLs and connection info"
    echo "  8. cleanup   - Clean up all containers and volumes"
    echo "  9. help      - Show this menu"
    echo ""
}

# Handle command line arguments
case "$1" in
    "build")
        check_docker
        build_app
        ;;
    "start")
        check_docker
        start_services
        ;;
    "dev")
        check_docker
        start_dev
        ;;
    "stop")
        stop_services
        ;;
    "logs")
        view_logs "$2"
        ;;
    "status")
        status
        ;;
    "info")
        info
        ;;
    "cleanup")
        cleanup
        ;;
    "help"|"")
        show_menu
        ;;
    *)
        print_error "Unknown command: $1"
        show_menu
        exit 1
        ;;
esac
