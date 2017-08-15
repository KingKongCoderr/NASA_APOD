Android application that retrieves images from NASA Sources daily which is called APOD(Astronomical picture of the Day)

Components used in this application:
Recycler View
Card View
Glide image Library for inserting images into Recycler-Card View.
Retrofit REST Client for handling background network tasks like fetching data from NASA API in JSON Format.
Realm for database
CalendarView
Explicit Intents(onActivityforResult)

Constraints used for application:
1.Should make only 1 API call for the day
2.Information should be stored so that user can retreive particular days info by selecting date on CalendarView
3.List button should give all the information presented in database in decreasing order of Date.
4.Should be able to click the image to show more info and click image again to hide information.


https://user-images.githubusercontent.com/10462780/29320902-5d16746e-819e-11e7-8c95-c840eb91e491.png


https://user-images.githubusercontent.com/10462780/29320919-67e89a34-819e-11e7-8b19-e723a5b8b502.png

https://user-images.githubusercontent.com/10462780/29320931-721f0a56-819e-11e7-9400-5045bb0918e8.png

https://user-images.githubusercontent.com/10462780/29320941-76ca3e54-819e-11e7-9373-5c1f83036e91.png

https://user-images.githubusercontent.com/10462780/29320945-7bddfcc8-819e-11e7-913c-95052ed88e17.png
