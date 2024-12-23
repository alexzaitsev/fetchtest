# Fetch Test
This is a test app that demonstrates my approach to modern Android development. 
While working on this project, I have made several notes:  
- Project structure is similar to real-world app with many screens (to illustrate my approach for bigger projects). It's important to note that many things are still omitted here for simplicity.      
- I use Koin DI as I would do on real project. Can be easily replaced with Hilt / another DI tool.  
- MVI is implemented without additional libraries (to reduce amount of boilerplate code and still concentrate on solution), on bigger project I would use OrbitMVI or similar library.  
- I don't create any navigation system to keep myself focused on the task.  
- I use [kittinunf/Result](https://github.com/kittinunf/Result) library to propagate data between layers, on bigger project I would use more mature library such as [Arrow](https://arrow-kt.io/).  
- `MainViewModel` is covered by unit tests in [MainViewModelTest.kt](app/src/test/java/com/alexzaitsev/fetchtest/ui/screen/main/MainViewModelTest.kt) to demonstrate my approach. Depending on the project more things can be covered (I would cover at least all business logic).  

# Demo
### Happy Path
Demonstrates the flow when user starts the app and has internet connection. Later they rotate the app.  
[happy-path.mov](readme/happy-path.mov)
### Error flow
Demonstrates the flow when user starts the app without internet connection. Later they enable the connection, load data, and rotate the app.  
[error-flow.mov](readme/error-flow.mov)
