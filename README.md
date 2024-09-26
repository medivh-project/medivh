<p align="center">
  <a href="https://medivh.tech/en/" target="_blank" rel="noopener noreferrer">
    <img width="200" src="https://github.com/user-attachments/assets/697cf38e-83aa-4e88-8280-2bee79a83c2f" alt="logo" />
  </a>
</p>
<br/>

## 🔥 Medivh

> Medivh is a convenient and easy-to-use monitoring component. You only need one annotation to get a function execution report.

See [web site](https://medivh.tech) for details on the project.

<br/>

## build from source

Gradle Plugin is Pending Approval，Currently only available through source code build

Require:
>  JDK 17+
>
>  kotlin 2+

run the following command

```shell
git clone https://github.com/medivh-project/medivh.git

cd medivh

./gradlew build publishToMavenLocal

```

## import gradle plugin in your project

```kts
id("tech.medivh.plugin.gradle") version "0.0.1"
```


## config Medivh

You can use Medivh in your Gradle file after build

```kts
medivh {
    include("com.example") // your package name here
}
```

## add annotation to your code
```kotlin
package tech.medivh.demo.kotlin
//  package name must be the same as the package name you configured in the gradle file
import tech.medivh.api.DebugTime

class DemoClass {
    @DebugTime
    fun helloWorld(){
        println("Hello World")
    }

}
```

## write and run test case

```kotlin
import org.junit.jupiter.api.Test

class DemoClassTest{
    
    @Test
    fun testDemo(){
        val demoClass = DemoClass()
        repeat(10){
            demoClass.helloWorld()
        }
    }
}

```

## see the console output


you will see output like this:

> you can open  file:///xxx/report.html to see the report

click the link above, you will see your Medivh report



## Complete Example

this is a complete example, you can refer to this [example](https://github.com/medivh-project/medivh-demo-kotlin) to use Medivh
