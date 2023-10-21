// Generated by view binder compiler. Do not edit!
package com.yakulab.feature.profile.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.yakulab.feature.profile.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemMedalBinding implements ViewBinding {
  @NonNull
  private final MaterialCardView rootView;

  @NonNull
  public final ShapeableImageView ivIcon;

  @NonNull
  public final MaterialTextView tvName;

  @NonNull
  public final MaterialTextView tvTotal;

  private ItemMedalBinding(@NonNull MaterialCardView rootView, @NonNull ShapeableImageView ivIcon,
      @NonNull MaterialTextView tvName, @NonNull MaterialTextView tvTotal) {
    this.rootView = rootView;
    this.ivIcon = ivIcon;
    this.tvName = tvName;
    this.tvTotal = tvTotal;
  }

  @Override
  @NonNull
  public MaterialCardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemMedalBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemMedalBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_medal, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemMedalBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.iv_icon;
      ShapeableImageView ivIcon = ViewBindings.findChildViewById(rootView, id);
      if (ivIcon == null) {
        break missingId;
      }

      id = R.id.tv_name;
      MaterialTextView tvName = ViewBindings.findChildViewById(rootView, id);
      if (tvName == null) {
        break missingId;
      }

      id = R.id.tv_total;
      MaterialTextView tvTotal = ViewBindings.findChildViewById(rootView, id);
      if (tvTotal == null) {
        break missingId;
      }

      return new ItemMedalBinding((MaterialCardView) rootView, ivIcon, tvName, tvTotal);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}