package com.example.myhw.recipes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
<<<<<<< HEAD
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhw.Ingredient.AddIngredientActivity;
=======
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
import com.example.myhw.helper.FirebaseUtil;
import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.Ingredient.SelectIngredientActivity;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.ActivityAddRecipeBinding;
import com.example.myhw.databinding.ItemRecipeIngredientBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class AddRecipeActivity extends BaseBindingActivity<ActivityAddRecipeBinding> {
    private String imageDocId = "";
<<<<<<< HEAD
    private Uri photoUri;
    private BindAdapter<ItemRecipeIngredientBinding, Ingredient> adapter = new BindAdapter<ItemRecipeIngredientBinding, Ingredient>() {
=======

    private BindAdapter<ItemRecipeIngredientBinding, AnotherIngredient> adapter = new BindAdapter<ItemRecipeIngredientBinding, AnotherIngredient>() {
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
        @Override
        public ItemRecipeIngredientBinding createHolder(ViewGroup parent) {
            return ItemRecipeIngredientBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemRecipeIngredientBinding itemRecipeIngredientBinding, Ingredient recipesIngredient, int position) {
            itemRecipeIngredientBinding.getRoot().setText(recipesIngredient.description + "," + recipesIngredient.count + "," + recipesIngredient.unit + "," + recipesIngredient.category);
        }
    };

    @Override
    protected void initListener() {
        viewBinder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                FirebaseUtil.getRecipesCollection().document(recipes.id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dismissLoading();
                        toast("delete success");
                        finish();
                    }
                });
            }
        });
        viewBinder.btnSave.setOnClickListener(v -> {
            String title = viewBinder.etTitle.getText().toString();
            String category = viewBinder.etCategory.getText().toString();
            String comments = viewBinder.etComments.getText().toString();
            String preparationTime = viewBinder.etPreparationTime.getText().toString();
            String number = viewBinder.etNumber.getText().toString();
            if (title.isEmpty() || category.isEmpty() || comments.isEmpty() || preparationTime.isEmpty() || number.isEmpty()) {
                toast("Please complete everything");
                return;
            }
            if (adapter.getData().isEmpty()) {
                toast("Please upload at least one ingredient");
                return;
            }
            Map<String, Object> recipeMap = new HashMap<>();
            recipeMap.put("title", title);
            recipeMap.put("numberOfServings", Integer.parseInt(number));
            recipeMap.put("preparationTime", preparationTime);
            recipeMap.put("category", category);
            recipeMap.put("comments", comments);
            recipeMap.put("ingredients", adapter.getData());
            recipeMap.put("photo", imageDocId);
            showLoading();
            if (recipes != null) {
                FirebaseUtil
                        .getRecipesCollection()
                        .document(recipes.id)
                        .update(recipeMap)
                        .addOnSuccessListener(unused -> {
                            dismissLoading();
                            toast("Update successfully");
                            finish();
                        });
            } else {
                FirebaseUtil.getRecipesCollection().add(recipeMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        dismissLoading();
                        toast("Added successfully");
                        finish();
                    }
                });
            }

        });
        viewBinder.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinder.btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRecipeActivity.this, AddIngredientActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 100);
            }
        });
        viewBinder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddRecipeActivity.this).setItems(new CharSequence[]{"Album", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 101);
                        } else {
                            if (ContextCompat.checkSelfPermission(AddRecipeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                                ActivityCompat.requestPermissions(AddRecipeActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                                return;
                            }
                            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File localPhotoFile = new File(storageDir, System.currentTimeMillis() + ".jpg");
                            photoUri = FileProvider.getUriForFile(AddRecipeActivity.this, "com.example.myhw", localPhotoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, 102);
                        }
                    }
                }).show();
            }
        });
    }

    private Recipes recipes;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        setTitle("ADD RECIPES");
        viewBinder.rvIngredient.setAdapter(adapter);
        recipes = (Recipes) getIntent().getSerializableExtra("recipes");
        if (recipes != null) {
            setTitle("EDIT RECIPES");
            viewBinder.etTitle.setText(recipes.title);
            viewBinder.etPreparationTime.setText(recipes.preparationTime);
            viewBinder.etNumber.setText(recipes.numberOfServings + "");
            viewBinder.etCategory.setText(recipes.category);
            viewBinder.etComments.setText(recipes.comments);
            FirebaseUtil.loadImage(imageDocId = recipes.photo, viewBinder.ivImage);
            adapter.getData().addAll(recipes.ingredients);
            adapter.notifyDataSetChanged();
            viewBinder.btnDelete.setVisibility(View.VISIBLE);
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(AddRecipeActivity.this)
                        .setTitle("Notice")
                        .setMessage("Are you sure you want to delete this data")
<<<<<<< HEAD
                        .setNegativeButton("CONFIRM", (dialog, which) -> {
=======
                        .setNegativeButton("SURE", (dialog, which) -> {
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
                            adapter.getData().remove(viewHolder.getAdapterPosition());
                            adapter.notifyDataSetChanged();
                        }).setCancelable(false)
                        .setPositiveButton("CANCEL", (dialog, which) -> adapter.notifyDataSetChanged()).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(viewBinder.rvIngredient);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100: {
                    Ingredient ingredient = (Ingredient) data.getSerializableExtra("ingredient");
                    Ingredient oldIngredient = null;
                    for (Ingredient datum : adapter.getData()) {
                        if (datum.description.equals(ingredient.description)) {
                            oldIngredient = datum;
                        }
                    }
                    if (oldIngredient != null) {
                        oldIngredient.count += ingredient.count;
                    } else {
                        adapter.getData().add(ingredient);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
                case 101:
                    try {
                        uploadImage(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 102:
                    try {
                        uploadImage(photoUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void uploadImage(Uri data) throws FileNotFoundException {
        viewBinder.ivImage.setImageResource(R.drawable.ic_baseline_loop_24);
        String preImageDocId = System.currentTimeMillis() + "";
        StorageReference reference = FirebaseUtil.getImagesReference();
        StorageReference child = reference.child(preImageDocId);
        child.putStream(getContentResolver().openInputStream(data)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                toast("The picture was uploaded successfully.");
                imageDocId = preImageDocId;
                viewBinder.ivImage.setImageURI(data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewBinder.ivImage.setImageResource(R.drawable.ic_baseline_image_24);
                toast("Upload failed->" + e.getLocalizedMessage());
            }
        });
    }

}