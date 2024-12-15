# CryptoSim: A Cryptography Simulation Application
CryptoSim is a Java-based application that offers various encryption algorithms and user authentication through X.509 certificates. This project includes the implementation of encryption algorithms, user registration, and authentication using certificates, and managing simulation history.

## Features
* **Encryption Algorithms**: Rail Fence, Myszkowski, and Playfair ciphers.
* **User Authentication**: Register and authenticate users using X.509 certificates.
* **Certificate Authority (CA)**: Generate CA and user certificates.
* **Simulation History**: Save and read encrypted simulation history for each user.

## Installation
1. **Clone the repository**:

```bash
  git clone https://github.com/yourusername/cryptosim.git
  cd cryptosim
```

2. **Build the project**:

  Ensure you have Java Development Kit (JDK) installed, preferably JDK 8 or higher.

```bash
  javac -cp ".:path/to/bouncycastle.jar" com/cryptosim/*.java
```

3. **Run the application**:

```bash
  java -cp ".:path/to/bouncycastle.jar" com.cryptosim.Main
```

## Usage
1. **Launch the application**:

  Run the `Main` class which will guide you through registration or login.
  

2. **Register a new user**:

  Choose the registration option, enter your desired username and password. The application will generate a certificate and save your credentials.
  

3. **Login as an existing user**:

  Choose the login option, provide the path to your certificate, your username, and password.
  

4. **Encryption Menu**:

  Once logged in, you can choose from the provided encryption algorithms, enter text and keys to encrypt the text.
  

5. **Simulation History**:

  View your past encryption simulations saved in an encrypted file.
  

## Encryption Algorithms

### Rail Fence Cipher
The Rail Fence cipher is a form of transposition cipher that derives its name from the way in which it's encoded. The characters are arranged in a zigzag pattern and then read row by row.

**Method Signature**:

```java
public static String RailFence(String text, int key)
```

### Myszkowski Transposition
Myszkowski Transposition is a variant of columnar transposition where repeated keywords are handled in a specific manner.

**Method Signature**:

```java
public static String Myszkowski(String text, String key)
```

### Playfair Cipher
The Playfair cipher is a digraph substitution cipher, encrypting pairs of letters instead of single letters.

**Method Signature**:

```java
public static String Playfair(String text, String key)
```

## User Authentication
User authentication is handled using X.509 certificates. Users must provide their certificate, username, and password to authenticate.

**Method Signature**:

```java
public boolean authenticate(String certPath, String username, String password) throws Exception
```

## Certificate Generation
The application can generate a Certificate Authority (CA) and user certificates for secure authentication.

**Method Signature**:

```java
public static void generateCA() throws Exception
```

## Simulation History
Simulation history is saved encrypted and can be retrieved to review past simulations.

**Method Signature**:

```java
public static void saveSimulation(String username, String text, String algorithm, String key, String cipher) throws Exception
public static void readSimulations(String username) throws Exception
```

## Testing
The project includes JUnit tests for the encryption algorithms and authentication methods.

**Run Tests**:

```bash
java -cp ".:path/to/junit.jar:path/to/bouncycastle.jar" org.junit.runner.JUnitCore com.cryptosim.AlgorithmsTest
java -cp ".:path/to/junit.jar:path/to/bouncycastle.jar" org.junit.runner.JUnitCore com.cryptosim.AuthentificationTest
```

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes.
