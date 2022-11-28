package com.example.myhw.recipes;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

<<<<<<< HEAD
import com.example.myhw.Ingredient.Ingredient;
=======
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
import com.example.myhw.helper.FirebaseUtil;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.ActivityRecipesDetailBinding;
import com.example.myhw.databinding.ItemRecipeIngredient1Binding;
<<<<<<< HEAD
=======
import com.example.myhw.plan.AnotherIngredient;
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad

public class RecipesDetailActivity extends BaseBindingActivity<ActivityRecipesDetailBinding> {
    private BindAdapter<ItemRecipeIngredient1Binding, Ingredient> adapter = new BindAdapter<ItemRecipeIngredient1Binding, Ingredient>() {
        @Override
        public ItemRecipeIngredient1Binding createHolder(ViewGroup parent) {
            return ItemRecipeIngredient1Binding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemRecipeIngredient1Binding itemRecipeIngredientBinding, Ingredient ingredient, int position) {
            if (position == 0) {
                itemRecipeIngredientBinding.tvDescription.setText("description");
                itemRecipeIngredientBinding.tvCount.setText("count");
                itemRecipeIngredientBinding.tvCategory.setText("category");
            } else {
                itemRecipeIngredientBinding.tvDescription.setText(ingredient.description);
                itemRecipeIngredientBinding.tvCount.setText(ingredient.count + "("+ingredient.unit+")");

                itemRecipeIngredientBinding.tvCategory.setText(ingredient.category);
            }


        }
    };

    @Override
    protected void initListener() {
        viewBinder.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Recipes recipes;
    private int type;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        setTitle("RECIPES DETAIL");
        type = getIntent().getIntExtra("type", 0);
        recipes = (Recipes) getIntent().getSerializableExtra("recipes");
        adapter.getData().addAll(recipes.ingredients);
        adapter.getData().add(0, new Ingredient());
        viewBinder.rvIngredient.setAdapter(adapter);
        viewBinder.tvTitle.setText("Title:" + recipes.title);
        viewBinder.tvPreparationTime.setText("Preparation time:" + recipes.preparationTime);
        viewBinder.tvNumber.setText("Number of serving:" + recipes.numberOfServings);
        viewBinder.tvComments.setText("Common:" + recipes.comments);
        viewBinder.tvCategory.setText("Recipe category:" + recipes.category);
        FirebaseUtil.loadImage(recipes.photo, viewBinder.ivImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (type==0){
            getMenuInflater().inflate(R.menu.menu_recipes_detial, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, AddRecipeActivity.class).putExtra("recipes", recipes));
        finish();
        return super.onOptionsItemSelected(item);
    }
}