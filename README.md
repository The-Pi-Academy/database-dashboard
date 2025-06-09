# Database Dashboard - SQL Learning Application

A Java-based REST API server that provides a learning environment for SQL operations with an H2 in-memory database. This application serves as an educational tool for students to practice SQL queries through a web-based interface.

## 🚀 Features

- **REST API Server**: Built-in HTTP server running on port 8080
- **SQL Query Execution**: Execute custom SQL queries through API endpoints
- **Health Monitoring**: Database connection health checks
- **CORS Support**: Cross-origin resource sharing for web frontend integration
- **H2 Database Integration**: In-memory database for safe learning environment
- **JSON API Responses**: Structured API responses with proper error handling

## 🏗️ Architecture

### Core Components

- **SqlController**: Main REST API controller handling HTTP requests
- **DataRepo**: Database repository for SQL operations and connection management
- **WebHandler**: HTTP request handler for web interface
- **Models**: API response models and data structures

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/sql/health` | Check database connection health |
| `POST` | `/sql/query` | Execute custom SQL queries |
| `GET` | `/` | Web interface (handled by WebHandler) |

## 📋 Prerequisites

- **Java 11+**: Required for running the application
- **Maven**: For dependency management and building
- **H2 Database**: Included as dependency for in-memory database

## 🛠️ Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/The-Pi-Academy/database-dashboard.git
   cd database-dashboard
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="org.academy.pi.sql.SqlController"
   ```

   Or compile and run directly:
   ```bash
   javac -cp ".:lib/*" src/main/java/org/academy/pi/sql/SqlController.java
   java -cp ".:lib/*" org.academy.pi.sql.SqlController
   ```

## 🚦 Usage

### Starting the Server

When you run the application, you'll see:
```
🌐 SQL Learning API started on http://localhost:8080
📋 Available endpoints:
 GET /sql/health ==> SQL Server Health
 POST /sql/query ==> SQL Custom Query
```

### API Usage Examples

#### Health Check
```bash
curl -X GET http://localhost:8080/sql/health
```

#### Execute SQL Query
```bash
curl -X POST http://localhost:8080/sql/query \
  -H "Content-Type: application/json" \
  -d '{"sql": "SELECT * FROM users LIMIT 10"}'
```

#### API Response Format
```json
{
  "success": true,
  "type": "TABLE",
  "data": {
    "columns": ["id", "name", "email"],
    "rows": [
      [1, "John Doe", "john@example.com"],
      [2, "Jane Smith", "jane@example.com"]
    ],
    "rowCount": 2
  },
  "message": null
}
```

## 🔧 Configuration

### Default Settings
- **Port**: 8080
- **Database**: H2 in-memory database
- **CORS**: Enabled for all origins (`*`)

### Customization
To modify the server port, update the `API_PORT` constant in `SqlController.java`:
```java
private static final int API_PORT = 8080; // Change to desired port
```

## 📊 Database Schema

The application uses an H2 in-memory database. The schema is managed by the `DataRepo` class, which handles:
- Database connection initialization
- Sample data population
- Query execution and result formatting

## 🛡️ Security Features

- **CORS Headers**: Properly configured for web frontend integration
- **Input Validation**: SQL query validation before execution
- **Error Handling**: Comprehensive error responses with appropriate HTTP status codes
- **Safe Environment**: H2 in-memory database prevents data persistence issues

## 🧪 Development

### Project Structure
```
src/
├── main/java/org/academy/pi/sql/
│   ├── SqlController.java       # Main API controller
│   ├── data/
│   │   └── DataRepo.java        # Database operations
│   ├── handler/
│   │   └── WebHandler.java      # Web interface handler
│   └── models/                  # Data models
│       ├── ApiResponse.java
│       ├── ApiResponseType.java
│       ├── SqlHealthResult.java
│       └── SqlQueryResult.java
```

### Dependencies
- **Jackson**: JSON serialization/deserialization
- **H2 Database**: In-memory SQL database
- **Java HTTP Server**: Built-in HTTP server (com.sun.net.httpserver)

### Building for Production
```bash
mvn clean package
java -jar target/database-dashboard-1.0.jar
```

## 📚 Educational Use

This application is designed for educational purposes and provides:
- Safe SQL practice environment
- Real-time query execution
- Structured error messages for learning
- Web-based interface for interactive learning

### Learning Objectives
- SQL query syntax and operations
- Database connection management
- REST API interaction
- JSON data handling

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## 📝 License

This project is part of The Pi Academy educational resources. Please refer to the repository for specific license information.

## 🐛 Troubleshooting

### Common Issues

**Port Already in Use**
```
Error: Address already in use
```
Solution: Change the `API_PORT` or stop the process using port 8080:
```bash
lsof -ti:8080 | xargs kill -9
```

**Database Connection Issues**
Check the H2 database configuration in `DataRepo.java` and ensure proper initialization.

**CORS Errors**
The application includes CORS headers, but if you encounter issues, verify the frontend is making requests to the correct endpoint.

## 📞 Support

For educational support and questions about this SQL learning tool, please contact The Pi Academy or create an issue in the GitHub repository.

---

**Happy Learning! 🎓**