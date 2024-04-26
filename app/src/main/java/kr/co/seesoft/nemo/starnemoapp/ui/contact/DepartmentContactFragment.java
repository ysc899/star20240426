package kr.co.seesoft.nemo.starnemoapp.ui.contact;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDepartmentContactListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.PaginationScrollListener;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class DepartmentContactFragment extends Fragment {


    private DepartmentContactViewModel departmentContactViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type adapterListType = new TypeToken<ArrayList<NemoDepartmentContactListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    private Button btnSearch;
    private EditText etDeptNm, etUserNm;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    private String searchDeptNm, searchUserNm;

    Gson gson = new Gson();

    private int currentPage = 1; // 초기 페이지


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        departmentContactViewModel =
                ViewModelProviders.of(getActivity()).get(DepartmentContactViewModel.class);
        View root = inflater.inflate(R.layout.fragment_department_contact, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        rvViewList = (RecyclerView) view.findViewById(R.id.rvViewList);
        rvViewList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvViewLayoutManager = new LinearLayoutManager(getActivity());
        rvViewList.setLayoutManager(rvViewLayoutManager);

        etDeptNm = (EditText) view.findViewById(R.id.etDeptNm);
        etUserNm = (EditText) view.findViewById(R.id.etUserNm);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {

        departmentContactViewModel.setClear();


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        btnBack.setOnClickListener(click);
        btnSearch.setOnClickListener(click);


        progressDialog = new CustomProgressDialog(getContext());
        departmentContactViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });


        rvViewAdapter = new ListViewAdapter(new ArrayList<>());

        rvViewList.setAdapter(rvViewAdapter);

        departmentContactViewModel.getDepartmentContactList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoDepartmentContactListRO>>() {
            @Override
            public void onChanged(ArrayList<NemoDepartmentContactListRO> adapterPlanROS) {
                rvViewAdapter.setadapterLists(adapterPlanROS);
            }
        });

        rvViewList.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager)rvViewLayoutManager) {
            @Override
            public void loadNextPage(int page) {

                currentPage++;
                AndroidUtil.log("loadNextPage : " + currentPage);
                departmentContactViewModel.setCurrentPage(currentPage);

                departmentContactViewModel.apiDepartmentContact(searchDeptNm,searchUserNm);
            }
        });

        etDeptNm.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                searchContact();

                return true;
            }
        });

        etUserNm.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                searchContact();

                return true;
            }
        });

    }

    public ArrayList<NemoDepartmentContactListRO> adapterListCopy(ArrayList<NemoDepartmentContactListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoDepartmentContactListRO> toList = gson.fromJson(copyJSON, adapterListType);
        return toList;
    }

    private void searchContact()
    {
        String searchDeptNm = etDeptNm.getText().toString().trim();
        String searchUserNm = etUserNm.getText().toString().trim();

        if( StringUtils.isEmpty(searchDeptNm) && StringUtils.isEmpty(searchUserNm) )
        {
            AndroidUtil.toast(getContext(), "검색어를 입력 하세요.");
            return;
        }

        currentPage = 1;
        departmentContactViewModel.setCurrentPage(currentPage);

        this.searchDeptNm = searchDeptNm;
        this.searchUserNm = searchUserNm;

        departmentContactViewModel.apiDepartmentContact(searchDeptNm,searchUserNm);
    }

    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnSearch:

                    searchContact();

                    break;


                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    private void callPhone(NemoDepartmentContactListRO t)
    {
        String moblPhno = AndroidUtil.nullToString(t.getBzMoblPhno(),"");

        if( moblPhno.length() > 0 )
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+moblPhno));
            startActivity(callIntent);
        }

    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoDepartmentContactListRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        public ListViewAdapter(ArrayList<NemoDepartmentContactListRO> adapterLists) {
            this.adapterLists = adapterListCopy(adapterLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoDepartmentContactListRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoDepartmentContactListRO> adapterLists) {
            this.adapterLists = adapterListCopy(adapterLists);

            if (adapterLists.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                rvViewList.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                rvViewList.setVisibility(View.VISIBLE);
            }

            notifyDataSetChanged();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_department_contact_layout, parent, false);

            ListViewHolder holder = new ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            NemoDepartmentContactListRO t = this.adapterLists.get(position);

            holder.bindItem(t);

            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
            }
            else
            {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.even_row_color));
            }

        }

        @Override
        public int getItemCount() {
            return this.adapterLists.size();
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {

            public TextView tvDeptNm, tvUserNm, tvBzMoblPhno, tvOffmTelno;

            public View v;

            public NemoDepartmentContactListRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvDeptNm = (TextView) itemView.findViewById(R.id.tvDeptNm);
                tvUserNm = (TextView) itemView.findViewById(R.id.tvUserNm);
                tvBzMoblPhno = (TextView) itemView.findViewById(R.id.tvBzMoblPhno);
                tvOffmTelno = (TextView) itemView.findViewById(R.id.tvOffmTelno);

                tvBzMoblPhno.setOnClickListener(view -> {
                    callPhone(item);
                });
            }

            public void bindItem(NemoDepartmentContactListRO t) {
                tvDeptNm.setText(AndroidUtil.nullToString(t.getDeptNm(),""));
                tvUserNm.setText(AndroidUtil.nullToString(t.getUserNm(),""));
                tvBzMoblPhno.setText(AndroidUtil.nullToString(t.getBzMoblPhno(),""));
                tvOffmTelno.setText(AndroidUtil.nullToString(t.getOffmTelno(),""));
                this.item = t;
            }

        }
    }

}