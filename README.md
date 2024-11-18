# :oncoming_automobile: Vehicular Cloud Real Time System (VCRTS)
## Project Description
Capstone project for CUS 1165 Software Engineering. _A Vehicular Cloud Real-Time System_ (VCRTS) manages and organizes computatation resources and jobs in a vehicular cloud. Users can register their vehicles by providing their vehicle information. The computation power from their vehicle will be distributed to jobs created by others users for completition. 

There are three types of users in the system:
1. **Car Owners** : Rent out their vehicle computation power
2. **Job Owners** : Submit jobs to be executed by the VCRTS using the computational resources available 
3. **Vehicular Cloud Controller** : Has the highest access and able to access all user data

## Directory
- \client\src : Contains the classes for the client-side of the system
- \database : Contains the .CSV files that provides storage management
- \lib : Contains the neccessary libraries for the system 
- \server\src : Contains the classes for the server-side of the system
- \test : Contains all J-Unit classes for testing

## How to Run Program
1. Run the Server.java located in the \server\src
2. Run the VCRTSGUI.java located in the \client\src
