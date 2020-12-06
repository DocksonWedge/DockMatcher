# DockMatcher

## Setup


### Maven
To pull in with Maven:
```
    <repositories>
        <repository>
            <id>jcenter</id>
            <url>https://jcenter.bintray.com</url>
        </repository>
    </repositories>

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
    jcenter()
}


implementation 'org.docksonwedge.dockmatcher:matcher:1.0'

```
## Usage

## For development
 
### Deploying

`mvn clean source:jar javadoc:jar deploy`