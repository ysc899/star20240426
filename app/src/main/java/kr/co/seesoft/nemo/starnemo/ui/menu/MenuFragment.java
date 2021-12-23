package kr.co.seesoft.nemo.starnemo.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import kr.co.seesoft.nemo.starnemo.R;

public class MenuFragment extends Fragment {

    private MenuViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        initUI(root);
        return root;
    }

    private void initUI(View view){
        view.findViewById(R.id.btnMenuVisitPlan).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_visitPlanFragment));
//        view.findViewById(R.id.btnMenuAccount).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_accountFragment));
//        view.findViewById(R.id.btnStatis).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_statisFragment));


    }
}