package com.example.myhw.plan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhw.Ingredient.AddIngredientActivity;
import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.Ingredient.SelectIngredientActivity;
import com.example.myhw.MainViewModel;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingFragment;
import com.example.myhw.base.PlanAdapter;
import com.example.myhw.databinding.FragmentPlanBinding;
import com.example.myhw.recipes.Recipes;
import com.example.myhw.recipes.SelectRecipeActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class PlanFragment extends BaseBindingFragment<FragmentPlanBinding> {

    private MainViewModel viewModel;
    private PlanAdapter adapter = new PlanAdapter();
    private EditText scale_factor;
    @Override
    protected void initData() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewBinder.rvData.setAdapter(adapter);
    }

    private String time = "";
    private Calendar instance = Calendar.getInstance();

    @Override
    protected void initListener() {
<<<<<<< HEAD
        viewBinder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("Notice")
                        .setMessage("Are you sure you want to delete this plan? It cannot be restored after deletion!")
                        .setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.deletePlanByTime(time);
                            }
                        })
                        .setPositiveButton("CANCEL", null).show();

            }
        });
        viewModel.getLivePlan().observe(getViewLifecycleOwner(), plan -> {
            adapter.getData().clear();
            if (plan != null) {
                adapter.getData().addAll(plan.recipes);
                adapter.getData().addAll(plan.ingredients);
                viewBinder.ivDelete.setVisibility(View.VISIBLE);
//                viewBinder.ivCopy.setVisibility(View.VISIBLE);
            } else {
                viewBinder.ivDelete.setVisibility(View.GONE);
//                viewBinder.ivCopy.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        });
        viewBinder.add.setOnClickListener(v -> {
            if (time.isEmpty()) {
                toast("Please select the date first");
                return;
            }
            showAddFunctionDialog();
        });
        viewBinder.ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCopyDialog(input -> {
                    viewModel.copyPlan(input);
                });
            }
        });
        viewBinder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        time = year + "-" + (month + 1) + "-" + dayOfMonth;
                        viewBinder.title.setText(time);
                        viewModel.getPlanByDate(time);
                    }
                }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

=======
        viewBinder.add.setOnClickListener(v -> showAddFunctionDialog());
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
    }


    private void showAddFunctionDialog() {
        new AlertDialog.Builder(requireActivity()).setItems(
                new CharSequence[]{
                        "Add from ingredient",
                        "Add from recipe",
                }, (dialog, which) -> {
                    if (which == 0) {
                        startActivityForResult(new Intent(getActivity(), SelectIngredientActivity.class), 100);
                    } else {
                        startActivityForResult(new Intent(getActivity(), SelectRecipeActivity.class), 101);
                    }
                }).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
<<<<<<< HEAD
                case 100: {
                    showAdd2PlanDialog(new OnInputListener() {
                        @Override
                        public void result(int input) {
                            Ingredient ingredient = (Ingredient) data.getSerializableExtra("ingredient");
                            scale_factor = getActivity().findViewById(R.id.edit_scale_factor);
                            int factor = Integer.parseInt(scale_factor.getText().toString());

                            ingredient.count = input*factor;

                            Plan plan = viewModel.getCurrentPlan();
                            if (plan == null) {
                                plan = new Plan();
                                plan.ingredients = new ArrayList<>();
                                plan.ingredients.add(ingredient);
                                plan.recipes = new ArrayList<>();
                                plan.time = time;
=======
                case 100:
                    Ingredient ingredient = (Ingredient) data.getSerializableExtra("ingredient");
                    if (currentPlanIndex == -1) {
                        showAdd2PlanDialog(count -> {
                            viewModel.addPlan(ingredient, count);
                        });
                    } else {
                        showAdd2PlanDialog(count -> {
                            Plan plan = adapter.getData().get(currentPlanIndex);
                            viewModel.updatePlan(ingredient, plan, count);
                        });
                    }
                    break;
                case 101: {
                    List<AnotherIngredient> ingredients = (List<AnotherIngredient>) data.getSerializableExtra("ingredients");
                    int number = data.getIntExtra("number", 0);
                    showAdd2PlanDialog(new OnInputListener() {
                        @Override
                        public void result(int count) {
                            for (AnotherIngredient item : ingredients) {
                                item.count = item.count * number * count;
                            }
                            if (currentPlanIndex == -1) {
                                Plan plan = new Plan();
                                plan.list = new ArrayList<>();
                                plan.list.addAll(ingredients);
                                viewModel.addPlan(plan);
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
                            } else {
                                int index = plan.ingredients.indexOf(ingredient);
                                if (index != -1) {
                                    Ingredient oldIngredient = plan.ingredients.get(index);
                                    oldIngredient.count += ingredient.count;
                                } else {
                                    plan.ingredients.add(ingredient);
                                }

                            }
                            viewModel.addPlan(plan);
                        }
                    });
                }
                break;
                case 101: {
                    Recipes recipes = (Recipes) data.getSerializableExtra("recipes");
                    Plan plan = viewModel.getCurrentPlan();
                    if (plan == null) {
                        plan = new Plan();
                        plan.ingredients = new ArrayList<>();
                        plan.recipes = new ArrayList<>();
                        scale_factor = getActivity().findViewById(R.id.edit_scale_factor);
                        int factor = Integer.parseInt(scale_factor.getText().toString());
                        for (int i = 0; i < factor; i++){
                            plan.recipes.add(recipes);
                        }
                        plan.time = time;
                    } else {
                        scale_factor = getActivity().findViewById(R.id.edit_scale_factor);
                        int factor = Integer.parseInt(scale_factor.getText().toString());
                        for (int i = 0; i < factor; i++){
                            plan.recipes.add(recipes);
                        }
                    }
                    viewModel.addPlan(plan);
                }
                break;
            }
        }
    }

    private void showAdd2PlanDialog(OnInputListener listener) {
        EditText editText = new EditText(getContext());
        editText.setHint("Input Count (each serving): ");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(getContext())
                .setTitle("Please enter the count")
                .setView(editText)
                .setPositiveButton("CANCEL", null)
                .setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String countStr = editText.getText().toString();
                        if (countStr.isEmpty()) return;
                        int count = Integer.parseInt(countStr);
                        if (count == 0) return;
                        listener.result(count);
                    }
                })
                .show();
    }

    private void showCopyDialog(OnInputListener listener) {
        EditText editText = new EditText(getContext());
        editText.setHint("Input Days");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.setAutofillHints("10");
        }
        new AlertDialog.Builder(getContext())
                .setTitle("Please enter the days")
                .setView(editText)
                .setPositiveButton("CANCEL", null)
                .setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String countStr = editText.getText().toString();
                        if (countStr.isEmpty()) return;
                        int count = Integer.parseInt(countStr);
                        if (count == 0) return;
                        listener.result(count);
                    }
                })
                .show();
    }

    private interface OnInputListener {
        void result(int input);
    }

}
