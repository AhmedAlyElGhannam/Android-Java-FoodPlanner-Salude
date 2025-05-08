# Android-Java-FoodPlanner-Salude

## Introduction
This is the final project of the Android Mobile App Development Using Java & Android Architectural Design Patterns courses taught in Information Technology Institute (ITI)'s 9-Month Professional Training Program -- Embedded Systems Track as a part of the intensive Android Automotive subfield under the supervision of Eng. Ahmed Mazen & Eng. Heba Ismail from Java Education & Technology Services department. Additionally, since I wanted something more challenging, I took a quick detour and compiled AOSP 15 from source for my Raspberry Pi 4B and tested the application on it (more on that later). As a reference, until I talk more about it later, here is a quick overview on the app's features:

1. Colourful splashscreen.
2. User login/registration.
3. Guest mode.
4. Meal of the day: a random meal is suggested to the user every day.
5. Search meals by Area/Category/Ingredient/Name/First letter.
6. Schedule meals for up to one week. Scheduled meals are inscribed into the phone's calendar.
7. Add meals to favourites to easily access them anytime.

## App Showcase
Here are some screenshots of the app. :)

![](./Assets/app.png)

## Work Breakdown Structure
The figure below showcases how this big project got dissected into smaller parts and in what order they were handled. You can see the project's [Trello board here](https://trello.com/b/Qjx9fW7i/salude) for more details.

![](./Diagrams/WBS/WBS.png)

## Used Architecture
The app was built based on Model-View-Presenter (MVP) architecture: separating UI updates from database logic by a middle-layer which contains all of the non-UI logic and acts as the middleman when it comes to dealing with databases. The project's file structure follows it as seen below, and I will briefly explain the purpose of each:
```bash
.
├── contracts
│   ├── HomeScreenContract.java
│   ├── ListOfFavouriteMealsContract.java
│   ├── ListOfPlannedMealsContract.java
│   ├── LoginContract.java
│   ├── MainScreenContract.java
│   ├── MealDetailsContract.java
│   ├── ProfileScreenContract.java
│   ├── RegistrationContract.java
│   ├── SearchScreenContract.java
│   └── SplashScreenContract.java
├── features
│   ├── auth_firebase
│   │   ├── login
│   │   │   ├── presenter
│   │   │   │   └── LoginAuthFirebasePresenter.java
│   │   │   └── view
│   │   │       └── LoginAuthFirebaseActivity.java
│   │   └── register
│   │       ├── presenter
│   │       │   └── RegisterAuthFirebasePresenter.java
│   │       └── view
│   │           └── RegisterAuthFirebaseActivity.java
│   ├── list_Fav
│   │   ├── presenter
│   │   │   └── ListOfFavouriteMealsPresenter.java
│   │   └── view
│   │       ├── ListOfFavouriteMealsAdapter.java
│   │       └── ListOfFavouriteMealsFragment.java
│   ├── list_plan
│   │   ├── presenter
│   │   │   └── ListOfPlannedMealsPresenter.java
│   │   └── view
│   │       ├── ListOfPlannedMealsAdapter.java
│   │       └── ListOfPlannedMealsFragment.java
│   ├── main_screen
│   │   ├── fragments
│   │   │   ├── connection
│   │   │   │   ├── ConnectionLostFragment.java
│   │   │   │   └── ConnectionRestoredFragment.java
│   │   │   ├── home
│   │   │   │   ├── presenter
│   │   │   │   │   └── HomeScreenPresenter.java
│   │   │   │   └── view
│   │   │   │       └── HomeScreenFragment.java
│   │   │   ├── profile
│   │   │   │   ├── presenter
│   │   │   │   │   └── ProfileScreenPresenter.java
│   │   │   │   └── view
│   │   │   │       └── ProfileScreenFragment.java
│   │   │   └── search
│   │   │       ├── presenter
│   │   │       │   └── SearchScreenPresenter.java
│   │   │       └── view
│   │   │           ├── ListOfFilteredMealsAdapter.java
│   │   │           ├── MealAreaAdapter.java
│   │   │           ├── MealCategoryAdapter.java
│   │   │           ├── MealIngredientsAdapter.java
│   │   │           ├── MealSearchResultsAdapter.java
│   │   │           └── SearchScreenFragment.java
│   │   ├── presenter
│   │   │   └── MainScreenPresenter.java
│   │   └── view
│   │       └── MainScreenActivity.java
│   ├── mealdetails
│   │   ├── presenter
│   │   │   └── MealDetailsPresenter.java
│   │   └── view
│   │       ├── IngredientsAdapter.java
│   │       └── MealDetailsFragment.java
│   └── splash_screen
│       ├── presenter
│       │   └── SplashScreenPresenter.java
│       └── view
│           └── SplashScreenActivity.java
├── model
│   ├── local
│   │   ├── dao
│   │   │   ├── MealDAO.java
│   │   │   └── RoomLocalDB.java
│   │   └── datasource
│   │       └── LocalDataSource.java
│   ├── pojo
│   │   ├── Area.java
│   │   ├── Category.java
│   │   ├── FilteredMeal.java
│   │   ├── Ingredient.java
│   │   └── Meal.java
│   ├── remote
│   │   ├── retrofit
│   │   │   ├── callback
│   │   │   │   └── RemoteRetrofitCallback.java
│   │   │   ├── client
│   │   │   │   └── RemoteRetrofitClient.java
│   │   │   ├── datasource
│   │   │   │   └── RemoteDataSource.java
│   │   │   ├── response
│   │   │   │   ├── AreaResponse.java
│   │   │   │   ├── CategoryResponse.java
│   │   │   │   ├── FilteredMealResponse.java
│   │   │   │   ├── IngredientResponse.java
│   │   │   │   └── MealResponse.java
│   │   │   └── service
│   │   │       └── RemoteRetrofitService.java
│   │   └── user
│   │       └── datasource
│   │           └── UserRegAndAuthDataSource.java
│   └── repository
│       └── SaludRepository.java
└── utils
    ├── clicklistener
    │   ├── OnAreaClickListener.java
    │   ├── OnCategoryClickListener.java
    │   ├── OnConnectionRestoredListener.java
    │   ├── OnFavouriteClickListener.java
    │   ├── OnFilteredMealItemClickListener.java
    │   ├── OnIngredientClickListener.java
    │   ├── OnMealItemClickListener.java
    │   └── OnPlannedClickListener.java
    ├── guest
    │   └── GuestMode.java
    ├── mealarea
    │   └── CountryFlagsUtil.java
    ├── network
    │   ├── ConnectivityUtil.java
    │   └── NetworkChangeReceiver.java
    └── plannedmeal
        └── DatePickerDialogManager.java
```

### Contracts
> Contracts are interfaces that explains the interactions between presenter & view, view & presenter, and presenter and model: for each feature, it lets the developer or anyone reviewing the code understand how each part interacts with the other. It is a clear and concise way to limit interactions instead of making a separate interface for each part.

### Features
> Features directory contains the view & presenter components of each feature in the app: like Activities/Fragments, Adapters if recycler views were used, and presenters; all of which have to implement their respective part of their Contract for consistency.

### Model
> Model directory contains all-things related to databases/information access: be it remote or local. There are multiple datasources in the project: remote, like the api that fetches the meal data (POJOs) via Retrofit or Firebase shenanigans that help with registration/login; or local, like the local Room database for storing planned and favourite meals. All of these data sources are abstracted via a repository that facilitates these operations and acts as the Model for all features; implementing the Model section of all features' contracts.

### Utils
> Utils directory contains helper classes/interfaces that does not fit the architecture but are essential for the business logic like: click listener interfaces, guest mode helper class, helper class generating urls for countries' flags, helper classes for network sensing (extending BroadcastReceiver), and a class for showing date-picker dialog when scheduling meals.

## App Features
In this section, I will go over each feature: explaining it briefly and showcasing its class diagram and sequence diagrams.

### Login Feature
After the 5-second splash screen, the user is greeted with the login screen. They can either login with their account, login via Google, or, if they have neither, they can create an account and head to registration page.

![](./Diagrams/Class_Sequence_Diagrams/LoginFeature/ClassDiagram/LoginContractClassDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/LoginFeature/SequenceDiagram/LoginSequenceDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/LoginFeature/SequenceDiagram/GoogleLoginSequenceDiagram.png)

### Registration Feature
Here, the user is prompted to enter their username, email, and a password. Upon inserting valid entries, the user goes back to the login screen to login.

![](./Diagrams/Class_Sequence_Diagrams/RegistrationFeature/ClassDiagrams/RegistrationContractClassDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/RegistrationFeature/SequenceDiagrams/RegistrationSequenceDiagram.png)

### Main Screen Feature
Main screen is basically the main activity in the app: it manages all other features' fragments and their transactions, **listens to network changes** in order to go to offline mode or refetch data when needed, and gets permission from the user to read/write from/into the device's calendar.

![](./Diagrams/Class_Sequence_Diagrams/MainScreenFeature/ClassDiagram/ClassDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/MainScreenFeature/SequenceDiagram/PermissionCheckSequenceDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/MainScreenFeature/SequenceDiagram/NetworkLossSequenceDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/MainScreenFeature/SequenceDiagram/NetworkRestorationSequenceDiagram.png)

### Home Screen Feature
Home screen has a simple, yet multi-layered purpose: display meal of the day (either fetch it or use data stored in shared preferences); which comes with multiple things that need to be handled like add to favourite button, add to planned button (also writes in device calendar), and item click to show its details.

So, how does the app writes meal data into the device's calendar app? It uses Content Provider's content resolver to enquire about the default calendar's id, then formulates the event before inserting it into the calendar. Pretty cool stuff :).

![](./Diagrams/Class_Sequence_Diagrams/HomeScreenFeature/ClassDiagram/HomeScreenClassDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/HomeScreenFeature/SequenceDiagrams/FetchMealOfTheDay.png)
![](./Diagrams/Class_Sequence_Diagrams/HomeScreenFeature/SequenceDiagrams/ToggleFavorite.png)
![](./Diagrams/Class_Sequence_Diagrams/HomeScreenFeature/SequenceDiagrams/TogglePlanned.png)


### Meal Details Feature
Here, upon clicking on any meal in the system, this page will open: displaying meal details like title, video, ingredients, and so on.

![](./Diagrams/Class_Sequence_Diagrams/MealDetailsFeature/ClassDiagram/MealDetailsClassDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/MealDetailsFeature/SequenceDiagram/CheckPlannedAndFavouriteStatusSequenceDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/MealDetailsFeature/SequenceDiagram/ToggleFavouriteStatusSequenceDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/MealDetailsFeature/SequenceDiagram/TogglePlannedStatusAndCheckCalendarSequenceDiagram.png)


### Search Screen Feature
In search screen, the user can filter meal library by either area, category, or ingredient. Additionally, they can search by meal name or by letter.

![](./Diagrams/Class_Sequence_Diagrams/SearchScreenFeature/ClassDiagram/SearchScreenClassDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/SearchScreenFeature/SequenceDiagrams/SearchByNameSequenceDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/SearchScreenFeature/SequenceDiagrams/FilteredMealDetailsSequenceDiagram.png)


### List of Favourite Meals
In the profile screen, the user can choose to view their meals marked as favourite at anytime even without network or to remove any of them.

![](./Diagrams/Class_Sequence_Diagrams/ListOfFavouriteMeals/ClassDiagram/ListOfFavouriteMealsClassDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/ListOfFavouriteMeals/SequenceDiagrams/GetFavouriteMeals.png)

![](./Diagrams/Class_Sequence_Diagrams/ListOfFavouriteMeals/SequenceDiagrams/RemoveFromFavourites.png)


### List of Planned Meals
In the profile screen, the user can choose to view their meals marked as planned at anytime even without network or to remove any of them.

![](./Diagrams/Class_Sequence_Diagrams/ListOfPlannedMeals/ClassDiagram/ListOfPlannedMealsClassDiagram.png)

![](./Diagrams/Class_Sequence_Diagrams/ListOfPlannedMeals/SequenceDiagrams/SequenceDiagram1.png)

![](./Diagrams/Class_Sequence_Diagrams/ListOfPlannedMeals/SequenceDiagrams/SequenceDiagram2.png)

![](./Diagrams/Class_Sequence_Diagrams/ListOfPlannedMeals/SequenceDiagrams/SequenceDiagram3.png)


## Demo
![](./Assets/Demo.mp4)
