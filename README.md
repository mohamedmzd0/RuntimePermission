# RuntimePermission
permission helper 
## Easily requet Rutime permission 


#### Step1
create new instance from PermissionHandler Class
```
private val permissionHandler = PermissionHandler( [ activity or fragment = this ] , onResultReceived = this)
```
### Overrive **Invoke** method inside your class

```
 override fun invoke(p1: PermissionCallback) {
        when (p1) {
            PermissionCallback.DENIED -> toast("user denied")
            PermissionCallback.GENERATED -> toast("generated")
                // show dialog if needed
            PermissionCallback.RATIONAL -> {
                permissionHandler.showRationale(
                    android.Manifest.permission.RECORD_AUDIO,
                    "this message explain why i need this permission"
                )
            }
        }
    }
```

#### You can check permission availability and request it easy
```
      if (!permissionHandler.isPermissionGranted(android.Manifest.permission.RECORD_AUDIO))
            permissionHandler.requestPermission(android.Manifest.permission.RECORD_AUDIO)
```

