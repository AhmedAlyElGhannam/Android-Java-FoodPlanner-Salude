package com.example.salude.contracts;

import com.example.salude.model.pojo.Category;
import com.example.salude.model.pojo.Meal;

import java.util.List;

public interface HomeScreenContract {
    public interface View {
        public void showMealOfTheDay(Meal meal);
        public void showMealCategories(List<Category> categories);
    }

    public interface Presenter {
        public void getMealOfTheDay();
        public void getAllCategories();
        }

    public interface Model {

    }
}
