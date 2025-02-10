# Spring Boot E-commerce API Docker Image

Docker image for a RESTful API built with Spring Boot that implements an e-commerce system with user authentication, product management, and purchase tracking.

## Quick Start

```bash
# Create .env file with required variables
cat << EOF > .env
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_DATABASE=your_database_name
MYSQL_USER=your_database_user
MYSQL_PASSWORD=your_database_password
MYSQL_CHARSET=utf8mb4
MYSQL_COLLATION=utf8mb4_unicode_ci
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=your_admin_password
ADMIN_NAME=Administrator
CORS_ORIGINS=http://localhost:3000
EOF

# Start the application
docker compose up -d
```

The API will be available at `http://localhost:8080`

## Environment Variables

Required environment variables for proper operation:

| Variable | Description | Example |
|----------|-------------|---------|
| MYSQL_ROOT_PASSWORD | MySQL root password | `mysecretpassword` |
| MYSQL_DATABASE | Database name | `shopping_db` |
| MYSQL_USER | Database user | `app_user` |
| MYSQL_PASSWORD | Database password | `app_password` |
| MYSQL_CHARSET | Database charset | `utf8mb4` |
| MYSQL_COLLATION | Database collation | `utf8mb4_unicode_ci` |
| ADMIN_EMAIL | Admin user email | `admin@example.com` |
| ADMIN_PASSWORD | Admin user password | `admin_password` |
| ADMIN_NAME | Admin user name | `Administrator` |
| CORS_ORIGINS | Allowed CORS origins | `http://localhost:3000` |

## Architecture

The application consists of two containers:

1. **Spring Boot Application (`app`)**
   - Java 17
   - Spring Boot 3.4.1
   - Spring Security
   - Spring Data JPA
   - OpenAPI/Swagger documentation

2. **MySQL Database (`mysql`)**
   - MySQL 9
   - Includes initial schema setup
   - Persistent data storage

## API Documentation

Once running, access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Container Management

```bash
# Start services
docker compose up -d

# View logs
docker compose logs -f

# Stop services
docker compose down

# Stop and remove volumes
docker compose down -v

# Rebuild after changes
docker compose up --build -d
```

## Troubleshooting

1. **Database Connection Issues**
   - Check if MySQL container is healthy: `docker compose ps`
   - Verify environment variables in `.env`
   - Check logs: `docker compose logs mysql`

2. **Application Startup Issues**
   - Check application logs: `docker compose logs app`
   - Ensure database is ready before application starts
   - Verify database credentials

3. **Performance Issues**
   - Check container resources: `docker stats`
   - Monitor MySQL connections: `docker compose exec mysql mysqladmin processlist`

## Security Notes

- Change default admin credentials
- Use strong passwords
- Configure CORS_ORIGINS appropriately
- Keep containers updated
- Use secrets management in production

## Support

For issues and feature requests, please visit the [GitHub repository](https://github.com/gussttaav/springboot-projects/tree/gestion-tienda).
