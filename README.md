# USCRecApp - Team 37, Syuen See, Kelly Ma, Elizabeth Szabo

## Overview
We are running our app on a Pixel 5 API Tiramisu. We are storing our data using Google Firebase and wrote our app using Java in Android Studio. Our Firebase database has 3 main classes: Users, Timeslots, and Gyms. For the purposes of this project, we have 4 users: Syuen See, Kelly Ma, Elizabeth Szabo, and Tommy Trojan. We also have 3 gyms: Lyon, Village, and Uytengsu. Each gym has a timeslots for Monday, Tuesday and Wednesday from 8-10am, 10am-12pm, 12-2pm, 2-4pm, and 4-6pm. Each time slot has a capacity of 10 students maximum. 

## Login Page
The first page of the app is the user login page. Here, USC students in the database can sign into the app using their full name in upper case. For example, Tommy Trojan can sign into the app by typing “Tommy Trojan” into the login text box. Other possible login names include “Syuen See”, “Kelly Ma”, and “Elizabeth Szabo”. Once done typing the user’s full name, press the Login button to continue.

## Summary Page
The first page of the app is the user summary page. Here, the user is able to see their name, student ID, and photo. Below, the user can see their upcoming and previous reservations in a Scroll View. Each box of upcoming reservations lists the time, day, and location of their gym reservation, along with a “cancel” button that deletes the reservation from the database and decrements the number of reservations for that time slot by 1. In addition, the Previous Bookings box shows the location, day and time of the user’s previous bookings. The Users can scroll up and down in both of the reservation boxes. If the current date and time is later than the start of the gym timeslot, the time slot will display in the previous bookings box.

## Gym Reservation Slots Page
In the Gym Reservation Slots page you can see time slots that span from 8am to 6pm, and each time slot is about 2 hours. Underneath the time you can see a capacity showing how many people signed up and the max amount of users that can sign up. If the time slot is at capacity, it will show “Capacity: FULL.” For now, all the time slots have a capacity of 10. Each time slot has a reserve button which allows the user to reserve that time slot if the capacity is not full. There is another button called “Remind Me” which allows the user to sign up for the waitlist for that time slot. You can also toggle between days with the drop down menu at the top of the list of reservations. The days you can toggle between are Monday, Tuesday, and Wednesday. 

## Home Page
The home page features a map of the locations of the Village Gym, Lyon Center, and Uytengsu Pool. The star at each location is a button you can click to view each gym’s reservation slots. For example, if you click the star located at the village, it will take you to the Village gym reservation page and you’ll be able to see the time slots and capacities for that gym on different days. This is the same for if you click the star located at Lyon Center and Uytengsu Pool.

## Nav-bar at the bottom of the screen
On each page of our app, except for the log-in page, there is a navigation bar at the bottom of the screen. The house icon saying “Home” will always take you to the home page with the map. The person icon saying “Person” will always take you to the summary page that shows all the reservations for that profile.

## How to Test our App
To test the login feature, you can login to the app using the name “Tommy Trojan”. Other acceptable names include “Syuen See”, “Kelly Ma”, and “Elizabeth Szabo”, but these names were used for testing and have already signed up for gym slots. 

The app will bring you to the Summary Page. You should be able to see your name, student ID, and photo. 

At the bottom navigation bar, you can click the “Home” button to see the map. Click on the star buttons on the map to view different gyms across campus. 

After clicking on one of the star icons, you are taken to the gym page. Click the drop down to select a day for the gym (Monday, Tuesday, or Wednesday). Then, you can sign up for a timeslot using the “Reserve” button, or join a waitlist using the “Remind Me” button. You should see the currently reserved buttons on your user Summary Page.

To test the waitlist feature, you can sign up for the waitlist for Lyon Gym - Monday from 8:00am-10:00am. This is because this slot’s capacity is purposely set to “FULL”, so users are only allowed to join the waitlist. 

To test upcoming bookings, you can reserve Lyon on Wednesday 10am-12pm, Uytengsu on Wednesday 10am-12pm, and Village on Tuesday from 8am-10am. These time slots are purposely configured in the database to occur in 2023 to ensure that when viewing our app, these time slots will show up in the “upcoming reservations” section of your profile. The rest of the time slots are set to occur from Monday, March 28 to Wednesday, March 30, 2022, so reserving these gym slots beyond those dates will show up in your Previous Bookings.

Once you reserve the upcoming bookings, you can test the “Cancel” feature by clicking on the “Cancel” button beside each gym reservation in the Upcoming Bookings.

## Additional features we added beyond the scope of the project
The log-in page which was described above.

We integrated Google Firebase into our app so that we could have a database to store user and gym reservation information. In our app, we would then read and write to firebase.

For the summary page, we implemented a dynamic scrolling view for upcoming bookings and previous reservations. If there are multiple reservations, you can scroll through the list to see all reservations/bookings.

On the gym slots reservation page, we included pop up confirmations for when you sign up for a time slot or for when you join the waitlist. There is also a pop up for when you try to sign up for a time slot that is full.

## Additional features added in Project 2.5
A new feature we added beyond the scope of the project was notifying the user 10 minutes before their registered gym time slot. A pop-up notification shows up when the time is 10 minutes before their upcoming gym appointment.

Another feature added is viewing time slots for a week (Sunday-Saturday) rather than for three days (Monday-Wednesday). For each gym you can now click on Sunday-Saturday.

In addition, we added a window on the maps page that displays all of the user's upcoming bookings. If there are none, the window is empty. You can click on the window to navigate to the user summary page and view the bookings in more detail. The map is connected to the backend through another firebase connection. 