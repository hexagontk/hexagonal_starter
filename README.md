
# Hexagonal Starter Application
This is a Hexagon service created from a template.

## Software Requirements
To build the application you will need:
* JDK 11+ for compiling the sources.
* An Internet connection to download the dependencies.

To run the application:
* For the Gradle distribution: JRE 11+ is required (JDK is not required at runtime).
* For the jpackage bundle: any major OS will run it (Alpine Linux causes problems).
* To run the native executable there is no specific requirements.

## Development
* Build: `./gradlew build`
* Rebuild: `./gradlew clean build`
* Run: `./gradlew run`
* Test (*\*Test*): `./gradlew test`
* Integration Test (*\*IT*): `./gradlew verify`
* Test Coverage: `./gradlew jacocoTestReport`

The reports are located in the `build/reports` directory after building the project.

## Gradle Wrapper Setup
You can change the Gradle version in `gradle/wrapper/gradle-wrapper.properties`.

## Usage
After building the project (`./gradlew build`), archives with the application's distributions are
stored in `build/distributions`.

To install the application you just need to unpack one distribution file.

After installing the application, you can run the application executing the `bin/hexagonal_starter`
script.

Once the application is running, you can send a request using [httpie] executing:
`http :9090/api/appointments id='a' userIds:='["mike", "jena"]' start=$(isodate) end=$(isodate)`
assuming: `alias isodate='date +"%Y-%m-%dT%H:%M:%S"'`

## Native Image
```bash
./gradlew -P agent test
./gradlew nativeCompile

# Executable
build/native/nativeCompile/gradle_starter

# Memory
ps -o rss -C gradle_starter
```
