#### -- Picasso --
 -dontwarn com.squareup.picasso.**

 #### -- OkHttp --

 -dontwarn com.squareup.okhttp.internal.**

 #### -- Apache Commons --


 -dontwarn org.apache.commons.logging.**
 -ignorewarnings
 -keep class * {
     public private *;
     }