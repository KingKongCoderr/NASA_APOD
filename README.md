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

All the NASA APOD images in a list:

![alt text](https://user-images.githubusercontent.com/10462780/29322336-c9ae5f02-81a2-11e7-9306-7ab748680abf.gif)

Select image of specific day from calendar: 

![alt text](https://user-images.githubusercontent.com/10462780/29322422-0b5daaac-81a3-11e7-8095-319e2719b753.gif)

Capture Image from within this application which is saved to gallery:

![alt text](https://user-images.githubusercontent.com/10462780/29322437-18499384-81a3-11e7-9f85-af1ffd1a554c.gif)

Submit the image to NASA moderators through email with moderators email address provided by the application.

![alt text](https://user-images.githubusercontent.com/10462780/29322459-2c1c548c-81a3-11e7-8c48-643e6304a04e.gif)


![alt text](https://user-images.githubusercontent.com/10462780/29322467-3437a9d2-81a3-11e7-960f-a09481c0caae.gif)
