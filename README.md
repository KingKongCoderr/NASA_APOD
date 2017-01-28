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

