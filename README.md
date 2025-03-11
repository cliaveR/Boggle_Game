# Boggle Game

## Overview
This project is a multiplayer Boggle game implemented using the fundamentals of Java Sockets and CORBA programming. The game consists of a server-client architecture where players can connect, generate a random Boggle board, and compete to find words within a time limit. Additionally, Python was used for another component of the game to demonstrate interoperability between different programming languages.

## Technologies Used
- **Java**: Used for implementing the core server and client logic using Java Sockets.
- **CORBA (Common Object Request Broker Architecture)**: Enables communication between distributed objects across different platforms.
- **Python**: Used for another component of the project, showcasing inter-language communication.

## Features
- 🎮 Multiplayer support through Java Socket programming.
- 🌐 CORBA for distributed communication.
- 🔀 Random Boggle board generation.
- ✅ Word validation and scoring system.
- 🖥️ Cross-language integration with Python.

## Setup Instructions
### Prerequisites
- ☕ Java Development Kit (JDK) installed.
- 📡 CORBA ORB (e.g., Java IDL or JacORB) configured.
- 🐍 Python installed.

### Running the Game
```bash
# Compile Java Files
javac *.java

# Start the CORBA Naming Service
orbd -ORBInitialPort 1050 &

# Run the Server
java BoggleServer

# Run the Client
java BoggleClient

# Execute Python Component (if applicable)
python boggle_python.py
```

## Future Enhancements
- 🎨 Improve GUI using JavaFX or Swing.
- 🌍 Implement a web-based version using WebSockets.
- 📖 Enhance word validation with an external dictionary API.

## Contributors
👤 **[Your Name]** - Developer

## License
📜 This project is licensed under the MIT License.

