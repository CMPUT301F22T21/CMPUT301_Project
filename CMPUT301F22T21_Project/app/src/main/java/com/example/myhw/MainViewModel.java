package com.example.myhw;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.helper.FirebaseUtil;
import com.example.myhw.plan.Plan;
import com.example.myhw.recipes.Recipes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;


public class MainViewModel extends ViewModel {
    //仓库 LiveData对象
    private MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>(new ArrayList<>());
    //菜谱 LiveData对象
    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>(new ArrayList<>());
    //购物车LiveData对象
    private MutableLiveData<List<Ingredient>> shoppingList = new MutableLiveData<>(new ArrayList<>());
    //计划LiveData对象
    private MutableLiveData<List<Plan>> plans = new MutableLiveData<>(new ArrayList<>());
    //当前的计划LiveDate对象
    private MutableLiveData<Plan> currentPlan = new MutableLiveData<>();
    //当前购物车排序于
    private String shoppingListOrderBy = "description";
    //当前菜谱排序于
    private String recipesOrderBy = "title";
    //当前仓库排序于
    private String ingredientOrderBy = "description";

    /**
     * This decide the order of recipes.
     * @param recipesOrderBy This is the order of the recipes
     */
    public void setRecipesOrderBy(String recipesOrderBy) {
        this.recipesOrderBy = recipesOrderBy;
        refreshRecipe();
    }

    /**
     * This returns a list of ingredients
     * @return
     *      Return the ingredients
     */
    public LiveData<List<Ingredient>> observerIngredients() {
        return ingredients;
    }

    /**
     * This returns the shopping list.
     * @return
     *      Return the shopping list
     */
    public LiveData<List<Ingredient>> observerShoppingList() {
        return shoppingList;
    }

    /**
     * This returns the meal plan.
     * @return
     *      Return the meal plan
     */
    public LiveData<List<Plan>> observerPlans() {
        return plans;
    }

    /**
     * This returns the recipes.
     * @return
     *      Return the recipes
     */
    public LiveData<List<Recipes>> observerRecipes() {
        return recipes;
    }


    /**
     * This decide the order of shopping list.
     * @param orderBy This is the order of the shopping list.
     */
    public void changeShoppingListOrderBy(String orderBy) {
        shoppingListOrderBy = orderBy;
        List<Ingredient> value = shoppingList.getValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            value.sort((o1, o2) -> {
                if (shoppingListOrderBy.equals("description")) {
                    return Collator.getInstance(Locale.CHINESE).compare(o1.description, o2.description);
                } else {
                    return Collator.getInstance(Locale.CHINESE).compare(o1.category, o2.category);
                }
            });
        }
        shoppingList.setValue(value);
    }

    /**
     * This update the recipe order.
     */
    public void refreshRecipe() {
        FirebaseUtil.getRecipesCollection().orderBy(recipesOrderBy, Query.Direction.ASCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Recipes> recipesList = new ArrayList<>();
            Log.d("TAG", "->: onSuccess");
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot document : documents) {
                Recipes recipes = document.toObject(Recipes.class);
                Log.d("TAG", "->: " + recipes.id);
                recipesList.add(recipes);
            }
            recipes.setValue(recipesList);
        });
    }


    /**
     * calculate the shopping cart
     */
    public void calculateShoppingCart() {
        FirebaseUtil.getPlanCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, Ingredient> shopCartMap = new HashMap<>();
                List<Plan> planList = queryDocumentSnapshots.toObjects(Plan.class);
                for (Plan plan : planList) {
                    for (Recipes recipe : plan.recipes) {
                        for (Ingredient ingredient : recipe.ingredients) {
                            Ingredient preIngredient = shopCartMap.get(ingredient.description);
                            if (preIngredient == null) {
                                preIngredient = ingredient;
                                preIngredient.count = preIngredient.count * recipe.numberOfServings;
                                shopCartMap.put(ingredient.description, preIngredient);
                            } else {
                                preIngredient.count += ingredient.count * recipe.numberOfServings;
                            }
                        }
                    }
                }

                for (Plan plan : planList) {
                    for (Ingredient ingredient : plan.ingredients) {
                        Ingredient preIngredient = shopCartMap.get(ingredient.description);
                        if (preIngredient == null) {
                            preIngredient = ingredient;
                            shopCartMap.put(ingredient.description, preIngredient);
                        } else {
                            preIngredient.count += ingredient.count;
                        }
                    }
                }
                for (Map.Entry<String, Ingredient> stringIngredientEntry : shopCartMap.entrySet()) {
                    Log.d("MAPMAP", "MAP: " + stringIngredientEntry.getKey() + "--" + stringIngredientEntry.getValue().count);
                }

                FirebaseUtil.getIngredientCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Ingredient> shoppingListPreDate = new ArrayList<>();
                        List<Ingredient> IngredientList = queryDocumentSnapshots.toObjects(Ingredient.class);
                        for (Ingredient ingredient : IngredientList) {
                            //如果获取出的食物不为null 说明计划中存在 则要进行购买数量计算
                            //如果仓库中的数量大于计划中的数量则说明不需要购买 否则计算数量

                            Ingredient preIngredient = shopCartMap.get(ingredient.description);
                            if (preIngredient != null) {
                                Log.d("TAG", "onSuccess: " + ingredient.description);
                                if (ingredient.count < preIngredient.count) {
                                    ingredient.count = preIngredient.count - ingredient.count;
                                    shoppingListPreDate.add(ingredient);
                                }
                                shopCartMap.remove(ingredient.description);
                            }
                        }
                        for (Map.Entry<String, Ingredient> remaining : shopCartMap.entrySet()) {
                            Log.d("TAG", "remaining: " + remaining.getValue().description);
                            shoppingListPreDate.add(remaining.getValue());
                        }
                        shoppingList.setValue(shoppingListPreDate);
                    }
                });

            }
        });


    }

    /**
     * set the ingredient storage order
     *
     * @param ingredientOrderBy
     */
    public void setIngredientOrderBy(String ingredientOrderBy) {
        this.ingredientOrderBy = ingredientOrderBy;
        refreshIngredients();
    }

    /**
     * update the ingredient storage list
     */
    public void refreshIngredients() {
        FirebaseUtil.getIngredientCollection().orderBy(ingredientOrderBy).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Ingredient> preData = new ArrayList<>();
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Ingredient ingredient = document.toObject(Ingredient.class);
                    preData.add(ingredient);
                }
                Log.d("TAG", "onSuccess: " + preData.size());
                ingredients.setValue(preData);

            }
        });
    }


    /**
     * add or update one plan
     *
     * @param plan
     */
    public void addPlan(Plan plan) {
        FirebaseUtil.getPlanCollection()
                .document(plan.time)
                .set(plan).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        currentPlan.setValue(plan);
                    }
                });
    }


    /**
     * get the plan according to time
     *
     * @param time
     */
    public void getPlanByDate(String time) {
        FirebaseUtil.getPlanCollection().document(time).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentPlan.setValue(documentSnapshot.toObject(Plan.class));
            }
        });
    }

    /**
     * get current plane
     *
     * @return plan
     */
    public Plan getCurrentPlan() {
        return currentPlan.getValue();
    }

    /**
     * get the current plan's livedata
     *
     * @return MutableLiveData
     */
    public MutableLiveData<Plan> getLivePlan() {
        return currentPlan;
    }

    /**
     * According to selected time to delete plan
     *
     * @param time
     */
    public void deletePlanByTime(String time) {
        FirebaseUtil.getPlanCollection().document(time).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                currentPlan.setValue(null);
            }
        });
    }

    /**
     * Copy plan by given input
     *
     * @param input
     */
    public void copyPlan(int input) {
        Plan value = currentPlan.getValue();
        for (int i = 1; i < input; i++) {
            FirebaseUtil.getPlanCollection().document(getDay(i)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                    }
                }
            });

        }
    }

    /**
     * get the selected day
     *
     * @return String
     */
    private String getDay(int count) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, count);
        return instance.get(Calendar.YEAR) + "-" + (instance.get(Calendar.MONTH) + 1) + "-" + instance.get(Calendar.DAY_OF_MONTH);
    }
}
