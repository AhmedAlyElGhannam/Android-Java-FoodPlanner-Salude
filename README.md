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

## Work Breakdown Structure
The figure below showcases how this big project got dissected into smaller parts and in what order they were handled. You can see the project's [Trello board here](https://trello.com/b/Qjx9fW7i/salude) for more details.

[WBS](./Diagrams/WBS/WBS.png)

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

### 

## Demo
