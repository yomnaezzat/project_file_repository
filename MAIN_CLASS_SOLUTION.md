# Solving the Main Class Problem in Multi-Module Spring Boot Projects

## The Problem

When running a multi-module Spring Boot project directly from the parent directory, you may encounter this error:

```
Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:3.2.5:run (default-cli) on project project-file-repositoryEntity: Unable to find a suitable main class, please add a 'mainClass' property
```

This happens because the parent POM doesn't have a main class - it's just a container for the modules.

## Solution: Using Maven Profiles

I've added Maven profiles to your parent POM that define the main class for each microservice. This allows you to run any service directly from the parent directory.

### How to Use the Profiles

You can start each service by specifying its profile:

```bash
# Start Eureka Server
mvn spring-boot:run -P eureka

# Start User Service
mvn spring-boot:run -P user

# Start API Gateway
mvn spring-boot:run -P gateway

# Start File Service
mvn spring-boot:run -P file

# Start Repository Service
mvn spring-boot:run -P repository
```

### Using the New Start Script

For convenience, I've created a new start script that uses these profiles:

```
C:\Users\yumenna ezzat\Desktop\project_file_repository\start-with-profiles.bat
```

This script:
- Starts all services in the correct order using Maven profiles
- Creates log files for troubleshooting
- Provides status messages

## How It Works

The profiles in the parent POM specify:
1. The main class for each service
2. The required configuration for the Spring Boot Maven plugin
3. The folder location of each module

For example, the Eureka Server profile looks like this:

```xml
<profile>
    <id>eureka</id>
    <properties>
        <spring.boot.mainclass>com.filerepository.eureka.EurekaServerApplication</spring.boot.mainclass>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>${spring.boot.mainclass}</mainClass>
                    <folders>
                        <folder>eureka-server</folder>
                    </folders>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

## Benefits of This Approach

1. **Simpler Command Line**: Run services directly from the parent directory
2. **Consistent Configuration**: All service configurations are in one place
3. **Easier CI/CD**: Simplifies automated deployment scripts
4. **Better Dependency Management**: Uses the parent POM's dependency management

## Troubleshooting

If you still encounter issues:

1. **Check Class Names**: Ensure the main class names in the profiles match your actual class names
2. **Verify Module Names**: Make sure the folder names match your actual module names
3. **Maven Version**: Use Maven 3.5+ for best compatibility
4. **Module Dependencies**: Some modules may need to be built first with `mvn clean install`

Run `mvn clean install` on the parent project before starting services to ensure all modules are built correctly.
