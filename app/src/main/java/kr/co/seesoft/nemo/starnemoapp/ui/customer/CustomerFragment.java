package kr.co.seesoft.nemo.starnemoapp.ui.customer;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.PaginationScrollListener;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.TransactionViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class CustomerFragment extends Fragment {


    private CustomerViewModel customerViewModel;


    //리스트뷰 관련
    private RecyclerView rvPlanAddList;
    private VisitPlanAddAdapter rvPlanAddAdapter;
    private RecyclerView.LayoutManager rvPlanAddLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type visitPlanListType = new TypeToken<ArrayList<NemoHospitalSearchRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //셀렉박스 관련
    private Spinner spSearchOption;

    // 검색 액션 구분
    // tab = 상단 탭 클릭
    // search = 검색 버튼 클릭
    private String searchMode = "tab";

    /**
     * 검색 방법 종류 (고객명, 고객 코드, 담당자)
     */
    private int searchType;
    //셀렉박스 관련 끝

    private Button btnMyHospital, btnBranchHospital, btnRNDHospital, btnSearch;
    private EditText etSearchKeyWord;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    Gson gson = new Gson();

    private int currentPage = 1; // 초기 페이지

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        customerViewModel =
                ViewModelProviders.of(getActivity()).get(CustomerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_customer, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {


        btnMyHospital = (Button) view.findViewById(R.id.btnMyHospital);
        btnBranchHospital = (Button) view.findViewById(R.id.btnBranchHospital);
        btnRNDHospital = (Button) view.findViewById(R.id.btnRNDHospital);
        btnSearch = (Button) view.findViewById(R.id.btnVisitPlanAddSearch);

        rvPlanAddList = (RecyclerView) view.findViewById(R.id.rvVisitPlanAddList);
        rvPlanAddList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvPlanAddLayoutManager = new LinearLayoutManager(getActivity());
        rvPlanAddList.setLayoutManager(rvPlanAddLayoutManager);


        spSearchOption = (Spinner) view.findViewById(R.id.spVisitPlanAddSearchOption);
        etSearchKeyWord = (EditText) view.findViewById(R.id.etVisitPlanAddSearchWord);


        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

    }

    private void init() {

        Bundle receivedData = getArguments();
        if (receivedData != null) {
            String value = receivedData.getString("fromMenu");

            AndroidUtil.log("receivedData : " + value);

            if( "true".equals( value ))
            {
                customerViewModel.setClear();
            }

            getArguments().clear();
        }

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        btnBack.setOnClickListener(click);

        btnMyHospital.setOnClickListener(click);
        btnBranchHospital.setOnClickListener(click);
        btnRNDHospital.setOnClickListener(click);
        btnSearch.setOnClickListener(click);

        progressDialog = new CustomProgressDialog(getContext());

        customerViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }

            }
        });


        rvPlanAddAdapter = new VisitPlanAddAdapter(new ArrayList<>());

        rvPlanAddList.setAdapter(rvPlanAddAdapter);

        customerViewModel.getVisitPlanHospitalList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoHospitalSearchRO>>() {
            @Override
            public void onChanged(ArrayList<NemoHospitalSearchRO> visitPlanROS) {
                rvPlanAddAdapter.setPlanLists(visitPlanROS);

                if( currentPage == 1)
                {
                    rvPlanAddList.scrollToPosition(0);


                    rvPlanAddList.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager)rvPlanAddLayoutManager) {
                        @Override
                        public void loadNextPage(int page) {

                            currentPage++;
                            AndroidUtil.log("loadNextPage : " + currentPage);
                            customerViewModel.setCurrentPage(currentPage);

                            if( "tab".equals( searchMode ))
                            {
                                customerViewModel.setHospitals();
                            }
                            else
                            {
                                String searchKeyWord = etSearchKeyWord.getText().toString().trim();

                                if( StringUtils.isEmpty(searchKeyWord) )
                                {
                                    searchKeyWord = "";
                                }

                                customerViewModel.searchHospitals(searchType, searchKeyWord);
                            }
                        }

                    });

                }
            }
        });


        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(getContext(), R.array.search_options, R.layout.adapter_spinner_row_layout);
        spAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout);

        spSearchOption.setAdapter(spAdapter);
        spSearchOption.setOnItemSelectedListener(new SpinnerSelected());

        customerViewModel.getSelectType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectType) {
                setDispTab(selectType);
            }
        });

        customerViewModel.getChoiceType().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer selectType) {
                spSearchOption.setSelection(selectType);
            }
        });

        customerViewModel.getChoiceText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectType) {
                etSearchKeyWord.setText(selectType);
            }
        });

        etSearchKeyWord.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                searchHospital();

                return true;
            }
        });

    }

    public ArrayList<NemoHospitalSearchRO> visitPlanListCopy(ArrayList<NemoHospitalSearchRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoHospitalSearchRO> toList = gson.fromJson(copyJSON, visitPlanListType);
        return toList;
    }

    private void setDispTab(String inStr)
    {
        if( "my".equals( inStr ))
        {
            btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
            btnMyHospital.setTextColor(Color.parseColor("#E50018"));
            btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
            btnBranchHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
            btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
            btnRNDHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
        }
        else if( "br".equals( inStr ))
        {
            btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
            btnMyHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
            btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
            btnBranchHospital.setTextColor(Color.parseColor("#E50018"));
            btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
            btnRNDHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
        }
        else if( "rnd".equals( inStr ))
        {
            btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
            btnMyHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
            btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
            btnBranchHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
            btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
            btnRNDHospital.setTextColor(Color.parseColor("#E50018"));
        }
    }

    protected void searchHospital() {

        String searchKeyWord = etSearchKeyWord.getText().toString().trim();

        if( StringUtils.isEmpty(searchKeyWord) )
        {
            searchKeyWord = "";
        }

        rvPlanAddList.scrollToPosition(0);

        searchMode = "search";

        currentPage = 1;

        customerViewModel.setCurrentPage(currentPage);
        customerViewModel.searchHospitals(searchType, searchKeyWord);

    }

    protected void moveTask(NemoHospitalSearchRO t){

        Bundle bundle = new Bundle();

        bundle.putSerializable("visit_hospital_cd", t.getCustCd());
        bundle.putSerializable("visit_hospital_nm", t.getCustNm());

        Navigation.findNavController(getView()).navigate(R.id.action_customerDetailFragment, bundle);

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnVisitPlanAddSearch:

                    searchHospital();
                    break;
                case R.id.btnMyHospital:

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    customerViewModel.setSelectType("my");
                    customerViewModel.setCurrentPage(1);
                    customerViewModel.setHospitals();

                    break;
                case R.id.btnBranchHospital:

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    customerViewModel.setSelectType("br");
                    customerViewModel.setCurrentPage(1);
                    customerViewModel.setHospitals();

                    break;

                case R.id.btnRNDHospital:

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    customerViewModel.setSelectType("rnd");
                    customerViewModel.setCurrentPage(1);
                    customerViewModel.setHospitals();

                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class SpinnerSelected implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            searchType = i;

            etSearchKeyWord.setText("");

            if( i == 1 )
            {
                etSearchKeyWord.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else
            {
                etSearchKeyWord.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            searchType = Const.SEARCH_NAME;
        }

    }

    public class VisitPlanAddAdapter extends RecyclerView.Adapter<VisitPlanAddAdapter.VisitPlanAddViewHolder> {

        private ArrayList<NemoHospitalSearchRO> planLists;
        private SelectionTracker<Long> selectionTracker;

        public VisitPlanAddAdapter(ArrayList<NemoHospitalSearchRO> planLists) {
            this.planLists = visitPlanListCopy(planLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoHospitalSearchRO getItem(int position){
            return planLists.get(position);
        }

        public void setPlanLists(ArrayList<NemoHospitalSearchRO> planLists) {
            this.planLists = visitPlanListCopy(planLists);

            if (planLists.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                rvPlanAddList.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                rvPlanAddList.setVisibility(View.VISIBLE);
            }

            notifyDataSetChanged();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public VisitPlanAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_visit_plan_add_layout, parent, false);

            VisitPlanAddViewHolder holder = new VisitPlanAddViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull VisitPlanAddViewHolder holder, int position) {
            NemoHospitalSearchRO t = this.planLists.get(position);

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
            return this.planLists.size();
        }

        public void setClear()
        {
            this.planLists.clear();
            notifyDataSetChanged();
        }

        public class VisitPlanAddViewHolder extends RecyclerView.ViewHolder {

            public TextView tvCode, tvName;

            public View v;

            public NemoHospitalSearchRO item = null;

            public VisitPlanAddViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvCode = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListCode);
                tvName = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListName);

                itemView.setOnClickListener(view -> {
                    moveTask(item);
                });
            }

            public void bindItem(NemoHospitalSearchRO t) {
                tvCode.setText(AndroidUtil.nullToString(t.getCustCd(),""));
                tvName.setText(AndroidUtil.nullToString(t.getCustNm(),""));
                this.item = t;
            }

        }
    }

}