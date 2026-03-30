# QR Scanner Pro

A modern JavaFX desktop app to scan QR codes from:
- **Live webcam feed**
- **Image files** (`.png`, `.jpg`, `.jpeg`)

It is fast, offline-friendly, and built with `ZXing` + `webcam-capture`.

---

## Features

- Real-time QR scanning from webcam
- Scan QR codes from local image files
- One-click **Open Link** action for URL QR results
- Clean JavaFX UI with status feedback

---

## Tech Stack

- **Java:** 17+
- **UI:** JavaFX 21
- **Build Tool:** Maven Wrapper (`mvnw`)
- **QR Decoder:** ZXing
- **Camera:** `com.github.sarxos:webcam-capture`

---

## Prerequisites

Install these first:
- **JDK 17 or newer** (full JDK, not JRE)
- **Git**

Check your environment:

```bash
java -version
javac -version
```

---

## Clone Project

```bash
git clone https://github.com/fromearth03/QR-Scanner-Pro.git
cd QR-Scanner-Pro
chmod +x mvnw
```

---

## Build Commands

Clean and compile:

```bash
./mvnw clean compile
```

Run tests:

```bash
./mvnw test
```

Create JAR package:

```bash
./mvnw clean package
```

---

## Run the App (Recommended)

Use Maven JavaFX plugin (this correctly sets module path):

```bash
./mvnw javafx:run
```

Main class:
- `block.qrscanner.QRScannerApplication`

---

## Quick Workflow

```bash
./mvnw clean compile
./mvnw javafx:run
```

## Project Structure

```text
src/
	main/
		java/
			module-info.java
			block/qrscanner/
				QRScannerApplication.java
				QRScannerController.java
		resources/
			block/qrscanner/
				hello-view.fxml
				styles.css
```

