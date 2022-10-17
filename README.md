# Android Coding Challenge Raheel Masood

#The Brief
A simple app starts with Listing screen for Users, a detail screen with user's posts.

#Architecture and design.
Single Activity Architecture using:
1) MVVM + Clean
2) DI Using HILT.
3) Coroutine with flow and Channels.
4) 2 way dataBinding.
5) Unit test of repository, view model and room DB tests are included.
6) Navigation graph.
7) Error Handling, in case of no internet or error from network. Once the internet resumes, user can hit the request again.
8) Room Database in case no internet. (Module is added)

#General Logic for the code
Steps...
Views will call view model.
View model is injected with use case.
Use case makes a call to repository.
API for users: in case of success insert data in local db table of users.
API for posts: in case of success insert data in local db table of posts.
In case both APIs are successful, then data is fetch from local DB using Relation of users and their posts.
Repository returns the data to the view model.
View model returns the response to the view I.e fragment.
Local DB is updated every time a new record is fetch. Old is deleted. 

#Test:
#Unit Tests are included in test folder:
FetchUsersAndTheirPostTest: This will use mock responses.
UserViewModelTest: Checking all the Channel's events using mocks.

#AndroidTest:
EntityRoomDBTest: Simple CRUD operations are tested in this folder. 
To run these test a device are required, since no Activity is attached, It will run quick.