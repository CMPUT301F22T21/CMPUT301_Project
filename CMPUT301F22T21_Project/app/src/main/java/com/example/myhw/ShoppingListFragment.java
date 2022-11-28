package com.example.myhw;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhw.Ingredient.AddIngredientActivity;
import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.base.BaseBindingFragment;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.FragmentShoppingListBinding;
import com.example.myhw.databinding.ItemIngredientBinding;
import com.example.myhw.helper.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ShoppingListFragment extends BaseBindingFragment<FragmentShoppingListBinding> {
    private MainViewModel viewModel;
    private int currentIndex = -1;
    private BindAdapter<ItemIngredientBinding, Ingredient> adapter = new BindAdapter<ItemIngredientBinding, Ingredient>() {
        @Override
        public ItemIngredientBinding createHolder(ViewGroup parent) {
            return ItemIngredientBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemIngredientBinding itemIngredientBinding, Ingredient ingredient, int position) {
            itemIngredientBinding.tvCategory.setText("Category:" + ingredient.category);
            itemIngredientBinding.tvCount.setText("Count:" + (Math.abs(ingredient.count)));
            itemIngredientBinding.tvUnitCost.setText("Unit:" + ingredient.unit);
            itemIngredientBinding.tvDescription.setText("Description:" + ingredient.description);
            itemIngredientBinding.tvLocation.setText("Location:" + ingredient.location);
            itemIngredientBinding.tvLocation.setVisibility(View.GONE);
            itemIngredientBinding.tvBastBeforeDate.setText("Best Before Date:" + ingredient.time);
            itemIngredientBinding.tvBastBeforeDate.setVisibility(View.GONE);
            if (currentIndex == position) {
                itemIngredientBinding.getRoot().setBackgroundColor(Color.GRAY);
            } else {
                itemIngredientBinding.getRoot().setBackgroundColor(Color.WHITE);
            }
            itemIngredientBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == currentIndex) {
                        currentIndex = -1;
                    } else {
                        currentIndex = position;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    protected void initData() {
        viewBinder.rvData.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewBinder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == -1) {
                    return;
                }
<<<<<<< HEAD
                showLoading();
                Ingredient ingredient = adapter.getData().get(currentIndex);
                FirebaseUtil.getIngredientCollection().document(ingredient.description).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        dismissLoading();
                        Intent intent = new Intent(getActivity(), AddIngredientActivity.class);
                        if (documentSnapshot.exists()) {
                            intent.putExtra("oldIngredient", documentSnapshot.toObject(Ingredient.class));
                        }
                        intent.putExtra("ingredient", ingredient);
                        startActivity(intent);
                    }
                });
=======
                startActivity(new Intent(getActivity(), AddIngredientActivity.class)
                        .putExtra("ingredient", adapter.getData().get(currentIndex))
                        .putExtra("type", 1)
                );
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
            }
        });
    }

    @Override
    protected void initListener() {
        viewModel.observerShoppingList().observe(this, new Observer<List<Ingredient>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                currentIndex = -1;
                Log.d("TAG", "onChanged: "+ingredients.size());
                adapter.getData().clear();
                adapter.getData().addAll(ingredients);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_shopping: {
                showShoppingListSort();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showShoppingListSort() {
        String[] items = getResources().getStringArray(R.array.orderBy);
        new AlertDialog.Builder(requireActivity()).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.changeShoppingListOrderBy(items[which]);
            }
        }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
<<<<<<< HEAD
        if (!hidden) {
=======
        if (!hidden){
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
            viewModel.calculateShoppingCart();
        }
    }
}
