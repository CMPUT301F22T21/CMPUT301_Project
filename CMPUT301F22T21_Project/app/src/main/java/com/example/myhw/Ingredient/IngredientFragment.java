package com.example.myhw.Ingredient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhw.MainViewModel;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingFragment;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.FragmentIngredientBinding;
import com.example.myhw.databinding.ItemIngredientBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class IngredientFragment extends BaseBindingFragment<FragmentIngredientBinding> {
    private MainViewModel viewModel;
    private BindAdapter<ItemIngredientBinding, Ingredient> adapter = new BindAdapter<ItemIngredientBinding, Ingredient>() {
        @Override
        public ItemIngredientBinding createHolder(ViewGroup parent) {
            return ItemIngredientBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemIngredientBinding itemIngredientBinding, Ingredient ingredient, int position) {
<<<<<<< HEAD
            itemIngredientBinding.tvCategory.setText(ingredient.category);
            itemIngredientBinding.tvBastBeforeDate.setText(ingredient.time);
            itemIngredientBinding.tvCount.setText((Math.max(ingredient.count, 0)) + "("+ingredient.unit+")");
            itemIngredientBinding.tvUnitCost.setText(ingredient.unit);
            itemIngredientBinding.tvLocation.setText(ingredient.location);
            itemIngredientBinding.tvDescription.setText(ingredient.description);
=======
            itemIngredientBinding.tvCategory.setText( ingredient.category);
            itemIngredientBinding.tvBastBeforeDate.setText( ingredient.time);
            itemIngredientBinding.tvCount.setText((Math.max(ingredient.count, 0))+"");
            itemIngredientBinding.tvUnitCost.setText( ingredient.unit);
            itemIngredientBinding.tvLocation.setText( ingredient.location);
            itemIngredientBinding.tvDescription.setText( ingredient.description);
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
            itemIngredientBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), AddIngredientActivity.class).putExtra("ingredient", ingredient));
                }
            });
        }
    };

    @Override
    protected void initData() {
        viewBinder.rvData.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        showLoading();
        viewModel.observerIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                dismissLoading();
                adapter.getData().clear();
                adapter.getData().addAll(ingredients);
                adapter.notifyDataSetChanged();
            }
        });
        viewBinder.add.setOnClickListener(v -> startActivity(AddIngredientActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sort_ingredient) {
            showSortDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        new AlertDialog.Builder(getContext()).setItems(new CharSequence[]{
                "Sort by description",
                "Sort by base before date",
                "Sort by location",
                "Sort by category"
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        viewModel.setIngredientOrderBy("description");
                        break;
                    case 1:
                        viewModel.setIngredientOrderBy("time");
                        break;
                    case 2:
                        viewModel.setIngredientOrderBy("location");
                        break;
                    case 3:
                        viewModel.setIngredientOrderBy("category");
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshIngredients();
    }


    @Override
    protected void initListener() {
    }
}
