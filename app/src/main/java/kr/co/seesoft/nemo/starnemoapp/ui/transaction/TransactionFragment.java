package kr.co.seesoft.nemo.starnemoapp.ui.transaction;

import android.content.Intent;
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
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class TransactionFragment extends Fragment {


    private TransactionViewModel transactionViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
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
        transactionViewModel =
                ViewModelProviders.of(getActivity()).get(TransactionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        btnMyHospital = (Button) view.findViewById(R.id.btnMyHospital);
        btnBranchHospital = (Button) view.findViewById(R.id.btnBranchHospital);
        btnRNDHospital = (Button) view.findViewById(R.id.btnRNDHospital);

        btnSearch = (Button) view.findViewById(R.id.btnVisitPlanAddSearch);

        rvViewList = (RecyclerView) view.findViewById(R.id.rvViewList);
        rvViewList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvViewLayoutManager = new LinearLayoutManager(getActivity());
        rvViewList.setLayoutManager(rvViewLayoutManager);


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
                transactionViewModel.setClear();
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
        transactionViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        transactionViewModel.getVisitPlanHospitalList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoHospitalSearchRO>>() {
            @Override
            public void onChanged(ArrayList<NemoHospitalSearchRO> visitPlanROS) {
                rvViewAdapter.setadapterLists(visitPlanROS);

                if( currentPage == 1)
                {
                    rvViewList.scrollToPosition(0);


                    rvViewList.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager)rvViewLayoutManager) {
                        @Override
                        public void loadNextPage(int page) {

                            currentPage++;
                            AndroidUtil.log("loadNextPage : " + currentPage);
                            transactionViewModel.setCurrentPage(currentPage);

                            if( "tab".equals( searchMode ))
                            {
                                transactionViewModel.setHospitals();
                            }
                            else
                            {
                                String searchKeyWord = etSearchKeyWord.getText().toString().trim();

                                if( StringUtils.isEmpty(searchKeyWord) )
                                {
                                    searchKeyWord = "";
                                }

                                transactionViewModel.searchHospitals(searchType, searchKeyWord);
                            }
                        }

                    });

                }
            }
        });

        rvViewAdapter.setSelectionTracker(selection);

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(getContext(), R.array.search_options, R.layout.adapter_spinner_row_layout);
        spAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout);

        spSearchOption.setAdapter(spAdapter);
        spSearchOption.setOnItemSelectedListener(new SpinnerSelected());

        etSearchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    searchHospital();
                }
                return true;
            }
        });

        transactionViewModel.getSelectType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectType) {
                setDispTab(selectType);
            }
        });

        transactionViewModel.getChoiceType().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer selectType) {
                spSearchOption.setSelection(selectType);
            }
        });

        transactionViewModel.getChoiceText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

    public ArrayList<NemoHospitalSearchRO> adapterListCopy(ArrayList<NemoHospitalSearchRO> fromList) {
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

        String searchKeyWord = etSearchKeyWord.getText().toString();

        if( StringUtils.isEmpty(searchKeyWord) )
        {
            searchKeyWord = "";
        }

        rvViewList.scrollToPosition(0);

        searchMode = "search";

        currentPage = 1;

        transactionViewModel.setCurrentPage(currentPage);

        transactionViewModel.searchHospitals(searchType, searchKeyWord);

    }

    protected void moveTask(NemoHospitalSearchRO t){

        AndroidUtil.log("moveTask : " + t);

        Bundle bundle = new Bundle();

        bundle.putSerializable("visit_hospital", t);

        String type = transactionViewModel.getSelectType().getValue();

        bundle.putSerializable("visit_hospital_type", type);

        Navigation.findNavController(getView()).navigate(R.id.action_visitPlanFragment_to_transactionDetailFragment, bundle);

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

                    transactionViewModel.setSelectType("my");
                    transactionViewModel.setCurrentPage(1);
                    transactionViewModel.setHospitals();

                    break;
                case R.id.btnBranchHospital:

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    transactionViewModel.setSelectType("br");
                    transactionViewModel.setCurrentPage(1);
                    transactionViewModel.setHospitals();

                    break;

                case R.id.btnRNDHospital:

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    transactionViewModel.setSelectType("rnd");
                    transactionViewModel.setCurrentPage(1);
                    transactionViewModel.setHospitals();

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

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoHospitalSearchRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        public ListViewAdapter(ArrayList<NemoHospitalSearchRO> adapterLists) {
            this.adapterLists = adapterListCopy(adapterLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoHospitalSearchRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoHospitalSearchRO> adapterLists) {
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

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction_layout, parent, false);

            ListViewHolder holder = new ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            NemoHospitalSearchRO t = this.adapterLists.get(position);

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

            public TextView tvCode, tvName, tvDate;

            public View v;

            public NemoHospitalSearchRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvCode = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListCode);
                tvName = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListName);
                tvDate = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListyymm);

                itemView.setOnClickListener(view -> {

                    moveTask(item);
                });
            }

            public void bindItem(NemoHospitalSearchRO t) {

                this.item = t;

                tvCode.setText(AndroidUtil.nullToString(t.getCustCd(),""));
                tvName.setText(AndroidUtil.nullToString(t.getCustNm(),""));

//                String csntDtm = t.getCsntDtm();
//
//                if( csntDtm == null )
//                {
//                    csntDtm = "";
//                }
//                else
//                {
//                    csntDtm = csntDtm.substring(0,7);
//                }

                tvDate.setText(AndroidUtil.dispDate(t.getCsntDtm()));

            }

        }
    }

}