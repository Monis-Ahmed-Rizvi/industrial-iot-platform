# Industrial IoT Platform for Natural Gas Production

## Project Overview
This Industrial IoT Platform is designed for processing and analyzing natural gas production data. It demonstrates proficiency in Java, Spring Boot, MongoDB, and related technologies, showcasing skills relevant for a Software Engineering Specialist role.

## Technologies Used
- Java 17
- Spring Boot 3.1.x
- MongoDB
- Spring Data MongoDB
- Spring Security
- Lombok
- Maven 3.8+
- Git for version control

## Features
- CSV data import for natural gas production data
- RESTful API for querying production data
- Date range queries for production data
- Top production days analysis
- Total production by state calculation
- Error handling and input validation

## Getting Started

### Prerequisites
- Java 17 JDK
- MongoDB 6.0+
- Maven 3.8+
- Git

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/your-username/industrial-iot-platform.git
   ```
2. Navigate to the project directory:
   ```
   cd industrial-iot-platform
   ```
3. Install dependencies:
   ```
   mvn clean install
   ```

### Configuration
1. Update MongoDB connection details in `src/main/resources/application.properties`
2. Ensure the CSV data file is placed in the `data` directory

### Running the Application
```
mvn spring-boot:run
```

## API Endpoints

- `POST /api/production/import` - Import CSV data
  - Parameter: `filePath` (string) - Path to the CSV file
- `GET /api/production/date-range` - Get production data for a date range
  - Parameters: `startDate`, `endDate` (ISO date format)
- `GET /api/production/date` - Get production data for a specific date
  - Parameter: `date` (ISO date format)
- `GET /api/production/top-production` - Get top production days
  - Parameters: `startDate`, `endDate` (ISO date format), `limit` (optional, default 5)
- `GET /api/production/total-by-state` - Get total production by state

## Code Structure
- `com.example.iotplatform.model` - Data models
- `com.example.iotplatform.repository` - MongoDB repositories
- `com.example.iotplatform.service` - Business logic and data processing
- `com.example.iotplatform.controller` - REST API controllers
- `com.example.iotplatform.exception` - Custom exceptions and error handling

## Future Enhancements
- Implement JWT authentication for API security
- Add comprehensive unit and integration tests
- Implement data visualization features
- Set up CI/CD pipeline with GitHub Actions
- Dockerize the application for easy deployment
- Integrate with cloud services (AWS, Azure, or GCP)

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
Monis Ahmed Rizvi - monisahmedrizvi@gmail.com
Project Link: https://github.com/Monis-Ahmed-Rizvi/industrial-iot-platform

## Acknowledgements
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MongoDB](https://www.mongodb.com/)
- [Lombok](https://projectlombok.org/)
- [Maven](https://maven.apache.org/)



