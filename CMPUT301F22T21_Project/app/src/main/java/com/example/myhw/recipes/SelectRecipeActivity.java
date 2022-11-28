package com.example.myhw.recipes;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

<<<<<<< HEAD
import com.example.myhw.Ingredient.Ingredient;
=======
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
import com.example.myhw.helper.FirebaseUtil;
import com.example.myhw.MainViewModel;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.ActivitySelectRecipeBinding;
import com.example.myhw.databinding.ItemSelectRecipesBinding;

import java.util.List;

public class SelectRecipeActivity extends BaseBindingActivity<ActivitySelectRecipeBinding> {
    private MainViewModel viewModel;
    private BindAdapter<ItemSelectRecipesBinding, Recipes> adapter = new BindAdapter<ItemSelectRecipesBinding, Recipes>() {
        @Override
        public ItemSelectRecipesBinding createHolder(ViewGroup parent) {
            return ItemSelectRecipesBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemSelectRecipesBinding itemSelectRecipesBinding, Recipes recipes, int position) {
            FirebaseUtil.loadImage(recipes.photo, itemSelectRecipesBinding.ivImage);
            StringBuilder builder = new StringBuilder();
            for (Ingredient ingredient : recipes.ingredients) {
                builder.append(ingredient.description).append("\t\t\t\tX").append(ingredient.count).append("\n");
            }
            builder.append("Number Of Servings:").append(recipes.numberOfServings);
            itemSelectRecipesBinding.tvIngredient.setText(builder.toString());
            itemSelectRecipesBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK, new Intent().putExtra("recipes", recipes));
                    finish();
                }
            });
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

        viewModel.observerRecipes().observe(this, new Observer<List<Recipes>>() {
            @Override
            public void onChanged(List<Recipes> recipes) {
                adapter.getData().clear();
                adapter.getData().addAll(recipes);
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.refreshRecipe();
    }

    @Override
    protected void initData() {
        setTitle("CHOOSE BY RECIPE");
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewBinder.rvData.setAdapter(adapter);

    }
}