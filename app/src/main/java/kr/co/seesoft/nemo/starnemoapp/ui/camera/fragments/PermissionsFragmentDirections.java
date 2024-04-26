package kr.co.seesoft.nemo.starnemoapp.ui.camera.fragments;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

import kr.co.seesoft.nemo.starnemoapp.R;


public class PermissionsFragmentDirections {
  private PermissionsFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionPermissionsToCamera() {
    return new ActionOnlyNavDirections(R.id.action_permissions_to_camera);
  }
}
