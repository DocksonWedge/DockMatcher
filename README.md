# DockMatcher

## Setup

### Maven
To pull in with Maven:
```
    <repository>
        <id>bintray-docksonwedge-DockMatcher</id>
        <name>bintray</name>
        <url>https://dl.bintray.com/docksonwedge/DockMatcher</url>
    </repository>

    <dependencies>
        <dependency>
          <groupId>org.docksonwedge.dockmatcher</groupId>
          <artifactId>matcher</artifactId>
          <version>1.0.2</version>
        </dependency>
    </dependencies>

```

### Gradle

To pull in with Gradle:
```

repositories {
    maven {
        setUrl("https://dl.bintray.com/docksonwedge/DockMatcher")
    }
}


implementation 'org.docksonwedge.dockmatcher:matcher:1.0.2'

```
## Usage

To use default settings, it's three small steps
    
1. Initialize DockMatcher with a KClass
2. Define assertions using .check() or .checkBool()
3. Pass the JSON to validate to .onBody

At the end, the format looks like this:
```
DockMatcher(MyClass::class)
    .check {
        <Assertions on MyClass properties>
    }.onBody(responseBody)
    
```
and that's it! AssertJ is a good assertion framework to use, 
but anything that throws assertion errors will work. 
`responseBody` can be a JSON string, 
a REST-Assured response object, or an instance of MyClass.

For full examples, see tests in this repo.
* [KotlinxSerializerTest](src/test/kotlin/org/docksonwedge/dockmatcher/KotlinxSerializerTest.kt)
    * The default for classes using @Serializable from Kotlinx. Shows both `.check()` and `.checkBool()`
* [JacksonSerializerTest](src/test/kotlin/org/docksonwedge/dockmatcher/JacksonSerializerTest.kt)
    * Shows using Jackson ObjectMatcher and changing the underlying ObjectMapper's configuration.
* [GsonSerializerTest](src/test/kotlin/org/docksonwedge/dockmatcher/GsonSerializerTest.kt)
    * If no serialization annotations are present, DockMatcher tries to use Gson.
* [OverrideSerializerTest](src/test/kotlin/org/docksonwedge/dockmatcher/OverrideSerializerTest.kt)
    * Shows using your own serializer rather than any of the defaults.

### Configuring Jackson ObjectMapper

If you need to change the settings for Jackson ObjectMapper you can access `DockMatcher.getObjectMapper()` to configure 
the underlying object mapper object. See [JacksonSerializerTest](src/test/kotlin/org/docksonwedge/dockmatcher/JacksonSerializerTest.kt)
for an example.

### Configuring Gson deserializer

Similar to Jackson, you can access `DockMatcher.getGsonBuilder()` to configure the underlying Gson deserializer. See
[GsonSerializerTest](src/test/kotlin/org/docksonwedge/dockmatcher/GsonSerializerTest.kt) for an example.

### Overriding Deserializers

If none of the default serializers will work, you can override it. `DockMatcher()` Has an optional parameter: a 
function that takes a string and returns an instance of the class in the first parameter: 

`DockerMatcher(MyClass::class) { json: String -> <return MyClass based on json> }`

See [OverrideSerializerTest](src/test/kotlin/org/docksonwedge/dockmatcher/OverrideSerializerTest.kt) for a real example

## For development
 
### Deploying

`mvn clean source:jar javadoc:jar deploy`