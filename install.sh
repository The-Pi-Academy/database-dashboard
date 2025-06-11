#!/bin/bash

# ==============================================================================
# Raspberry Pi 4 Java Installation Script
# For The-Pi-Academy/database-dashboard project
# ==============================================================================
# Simple installation script for Java 17 + Maven + project dependencies
# ==============================================================================

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
JAVA_VERSION="17"
MAVEN_VERSION="3.9.6"
LOG_FILE="/tmp/database-dashboard-install.log"

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1" | tee -a "$LOG_FILE"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1" | tee -a "$LOG_FILE"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1" | tee -a "$LOG_FILE"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1" | tee -a "$LOG_FILE"
}

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check system requirements
check_system() {
    print_status "Checking system requirements..."

    # Check if running on Raspberry Pi
    if ! grep -q "Raspberry Pi" /proc/device-tree/model 2>/dev/null; then
        print_warning "This script is optimized for Raspberry Pi. Continuing anyway..."
    fi

    # Check available disk space (need at least 1GB)
    AVAILABLE_SPACE=$(df / | awk 'NR==2 {print $4}')
    if [ "$AVAILABLE_SPACE" -lt 1000000 ]; then
        print_error "Insufficient disk space. Need at least 1GB available."
        exit 1
    fi

    print_success "System requirements check completed"
}

# Function to update system
update_system() {
    print_status "Updating system packages..."

    sudo apt-get update -y || {
        print_error "Failed to update package lists"
        exit 1
    }

    print_success "System update completed"
}

# Function to install essential tools
install_essential_tools() {
    print_status "Installing essential tools..."

    sudo apt-get install -y \
        curl \
        wget \
        git \
        unzip \
        ca-certificates || {
        print_error "Failed to install essential tools"
        exit 1
    }

    print_success "Essential tools installed successfully"
}

# Function to install Java 17
install_java() {
    print_status "Installing OpenJDK $JAVA_VERSION..."

    if command_exists java; then
        CURRENT_JAVA=$(java -version 2>&1 | head -n 1 | grep -o '"[^"]*"' | tr -d '"' | cut -d'.' -f1)
        if [ "$CURRENT_JAVA" = "$JAVA_VERSION" ]; then
            print_success "Java $JAVA_VERSION is already installed"
            java -version
            return
        fi
    fi

    sudo apt-get install -y openjdk-${JAVA_VERSION}-jdk || {
        print_error "Failed to install Java $JAVA_VERSION"
        exit 1
    }

    # Set JAVA_HOME for current session
    export JAVA_HOME="/usr/lib/jvm/java-${JAVA_VERSION}-openjdk-$(dpkg --print-architecture)"

    # Add to .bashrc for persistence
    if ! grep -q "JAVA_HOME" ~/.bashrc; then
        echo "export JAVA_HOME=$JAVA_HOME" >> ~/.bashrc
        echo "export PATH=\$PATH:\$JAVA_HOME/bin" >> ~/.bashrc
    fi

    # Verify installation
    java -version
    javac -version

    print_success "Java $JAVA_VERSION installed successfully"
}

# Function to install Maven
install_maven() {
    print_status "Installing Apache Maven $MAVEN_VERSION..."

    if command_exists mvn; then
        CURRENT_MAVEN=$(mvn -version 2>/dev/null | head -n 1 | grep -o '[0-9]\+\.[0-9]\+\.[0-9]\+' | head -n 1)
        if [ "$CURRENT_MAVEN" = "$MAVEN_VERSION" ]; then
            print_success "Maven $MAVEN_VERSION is already installed"
            mvn -version
            return
        fi
    fi

    # Download and install Maven
    cd /tmp
    wget https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz || {
        print_error "Failed to download Maven"
        exit 1
    }

    sudo tar -xzf apache-maven-$MAVEN_VERSION-bin.tar.gz -C /opt/
    sudo ln -sf /opt/apache-maven-$MAVEN_VERSION /opt/maven

    # Set Maven environment variables for current session
    export MAVEN_HOME=/opt/maven
    export PATH=$PATH:$MAVEN_HOME/bin

    # Add to .bashrc for persistence
    if ! grep -q "MAVEN_HOME" ~/.bashrc; then
        echo "export MAVEN_HOME=/opt/maven" >> ~/.bashrc
        echo "export PATH=\$PATH:\$MAVEN_HOME/bin" >> ~/.bashrc
    fi

    # Create symlink for easier access
    sudo ln -sf /opt/maven/bin/mvn /usr/local/bin/mvn

    # Verify installation
    mvn -version

    print_success "Maven $MAVEN_VERSION installed successfully"
}

# Function to build the project
build_project() {
    print_status "Building the project..."

    # Check if pom.xml exists
    if [ -f "pom.xml" ]; then
        print_status "Found pom.xml, building with Maven..."

        # Clean and compile
        mvn clean compile || {
            print_warning "Maven compile failed. Continuing to try downloading dependencies..."
        }

        # Download dependencies
        mvn dependency:resolve || {
            print_warning "Failed to resolve some dependencies"
        }

        # Create the fat JAR
        mvn clean package || {
            print_warning "Maven package failed. You may need to check the source code."
        }

        if [ -f "target/db-learning-app-1.0.0.jar" ]; then
            print_success "Build completed successfully! JAR file created."
        else
            print_warning "Build may have issues. Check the Maven output above."
        fi

    else
        print_warning "No pom.xml found. Make sure the repository was cloned correctly."
    fi
}

# Function to create run scripts
create_run_scripts() {
    print_status "Creating run scripts..."

    # Create run script for the application
    cat > "run.sh" << 'EOF'
#!/bin/bash
# SQL Learning App runner script

cd "$(dirname "$0")"

echo "Starting SQL Learning Application..."
echo "=================================="

# Check if JAR exists
if [ -f "target/db-learning-app-1.0.0.jar" ]; then
    echo "Running from built JAR..."
    java -jar target/db-learning-app-1.0.0.jar
elif [ -f "pom.xml" ]; then
    echo "JAR not found, building and running with Maven..."
    mvn clean package -DskipTests
    if [ -f "target/db-learning-app-1.0.0.jar" ]; then
        java -jar target/db-learning-app-1.0.0.jar
    else
        echo "Build failed. Running with Maven exec plugin..."
        mvn exec:java
    fi
else
    echo "Error: No pom.xml found. Please check the project setup."
    exit 1
fi
EOF

    chmod +x "run.sh"

    # Create development script for easy Maven commands
    cat > "dev.sh" << 'EOF'
#!/bin/bash
# Development helper script

cd "$(dirname "$0")"

echo "SQL Learning App - Development Helper"
echo "====================================="
echo
echo "Available commands:"
echo "1. clean   - Clean the project"
echo "2. compile - Compile the project"
echo "3. test    - Run tests"
echo "4. package - Create JAR file"
echo "5. run     - Run the application"
echo "6. deps    - Download dependencies"
echo

if [ $# -eq 0 ]; then
    echo "Usage: ./dev.sh [command]"
    echo "Example: ./dev.sh run"
    exit 0
fi

case "$1" in
    clean)
        mvn clean
        ;;
    compile)
        mvn compile
        ;;
    test)
        mvn test
        ;;
    package)
        mvn clean package
        ;;
    run)
        mvn exec:java
        ;;
    deps)
        mvn dependency:resolve
        ;;
    *)
        echo "Unknown command: $1"
        echo "Available: clean, compile, test, package, run, deps"
        exit 1
        ;;
esac
EOF

    chmod +x "dev.sh"

    print_success "Run scripts created successfully"
}

# Function to test the installation
test_installation() {
    print_status "Testing the installation..."

    # Test Java
    if command_exists java; then
        print_success "Java is working: $(java -version 2>&1 | head -n 1)"
    else
        print_error "Java installation failed"
        return 1
    fi

    # Test Maven
    if command_exists mvn; then
        print_success "Maven is working: $(mvn -version 2>&1 | head -n 1)"
    else
        print_error "Maven installation failed"
        return 1
    fi

    # Test project structure
    if [ -f "pom.xml" ]; then
        print_success "Project structure looks good"
    else
        print_warning "Project may not be set up correctly"
    fi

    print_success "Installation test completed"
}

# Function to display final instructions
display_final_instructions() {
    print_success "Installation completed successfully!"
    echo ""
    echo "============================================="
    echo "Quick Start Instructions:"
    echo "============================================="
    echo ""
    echo "1. Navigate to the project directory:"
    echo "   cd $PROJECT_DIR"
    echo ""
    echo "2. Run the application:"
    echo "   ./run.sh"
    echo ""
    echo "3. For development, use the helper script:"
    echo "   ./dev.sh run        # Run with Maven"
    echo "   ./dev.sh package    # Build JAR file"
    echo "   ./dev.sh test       # Run tests"
    echo ""
    echo "4. Or use Maven directly:"
    echo "   mvn exec:java                    # Run the app"
    echo "   mvn clean package                # Build JAR"
    echo "   java -jar target/db-learning-app-1.0.0.jar  # Run JAR"
    echo ""
    echo "============================================="
    echo "Installed Software:"
    echo "============================================="
    java -version 2>&1 | head -n 1
    mvn -version 2>&1 | head -n 1
    echo ""
    echo "Project Directory: $PROJECT_DIR"
    echo "Main Class: org.academy.pi.sql.SqlLearningApp"
    echo ""
    echo "============================================="
    echo "Dependencies (will be downloaded by Maven):"
    echo "============================================="
    echo "- H2 Database (in-memory)"
    echo "- Jackson (JSON processing)"
    echo "- Lombok (code generation)"
    echo "- JUnit (testing)"
    echo ""
    echo "No external database setup required!"
    echo "============================================="
}

# Main installation function
main() {
    print_status "Starting Raspberry Pi 4 Java Setup for SQL Learning App"
    print_status "Log file: $LOG_FILE"
    echo "Installation started at $(date)" > "$LOG_FILE"

    check_system
    update_system
    install_essential_tools
    install_java
    install_maven
    build_project
    create_run_scripts
    test_installation
    display_final_instructions

    print_success "Setup completed! Run './run.sh' in $PROJECT_DIR to start the app."
    echo "Setup completed at $(date)" >> "$LOG_FILE"
}

# Check if running as root
if [ "$EUID" -eq 0 ]; then
    print_error "Please do not run this script as root. Run as the 'pi' user instead."
    exit 1
fi

# Run main function
main "$@"