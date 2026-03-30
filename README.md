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

---

## IDE Setup (Important)

If you get `module not found` errors in IDE:

1. Reimport Maven project
2. Set Project SDK to **JDK 17+**
3. Run using **Maven goal** `javafx:run` (not plain "Run class")

---

## Troubleshooting

### 1) `java: module not found: javafx.controls` (and similar)

Cause: class run config is not using Maven module path.

Fix:

```bash
./mvnw clean compile
./mvnw javafx:run
```

### 2) `release version 21/17 not supported`

Cause: JRE or incomplete Java installation.

Fix:
- Install a full JDK
- Confirm `javac -version` works
- Reopen terminal/IDE and rebuild

### 3) Linux webcam / GTK warnings

Some systems print webcam GTK/GDK warnings. If app still opens and scans, it is usually non-fatal.

---

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

---

## License

This project currently has no explicit license file. Add one if you plan to distribute it publicly.
