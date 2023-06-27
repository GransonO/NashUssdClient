[![](https://jitpack.io/v/GransonO/NashUssdClient.svg)](https://jitpack.io/#GransonO/NashUssdClient)

# Nash USSD Client Android Library
### This library will enable you to interact with the USSD service without active human interaction.


## Installation
Please ensure AndroidX is enabled in your project in the android/gradle.properties file

### Add the JitPack repository to your build file
This is necessary if the library will be queried from Jipack:

```
allprojects {
  repositories {
    ...
		maven { url 'https://jitpack.io' }
	}
}
```

### Add the NashClient Dependency
```
dependencies {
  implementation 'com.github.GransonO:NashUssdClient:vx.x.x'
}
```
Ensure you target the latest release
Alternatively, you can manually add the Lib jar file to your project using Android Studio

## Usage

#### 1. Initialize the library in your Activity/Fragment passing the Context
#### 2. Request the user permissions ( The library cannot run without all permissions granted )
#### 3. Call the `runUssdUtil` function passing the `USSDPayload` object.

```
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initNashClient(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssd(view: View){

        runUssdUtil(
            USSDPayload(
                id = "1",
                code = "*544#",
                promptFlow = "2*6*2*1*PIN",
                hasPin = true
            ),
            object : UssdCallback{
                override fun onSuccess(value: String) {
                    binding.ussdtext.text = value
                }

                override fun onFailure(value: String) {
                    binding.ussdtext.text = value
                }
            }
        )
    }
    fun requestPhonePermissions(view: View){
        requestPhonePermissionsUtil(this@MainActivity)
    }

    fun requestPhoneStatePermissions(view: View){
        requestPhoneStatePermissionsUtil(this@MainActivity)
    }

    fun requestAccessibilityService(view: View){
        requestAccessibilityServiceUtil(this@MainActivity)
    }

    fun getSimProviders(view: View){
        val simList = nashSimProviders(this@MainActivity)
    }
}
```

Based on the agreed criteria of the passed string e.g `"2*6*2*1*PIN"` the library will work as required.

### Happy development
