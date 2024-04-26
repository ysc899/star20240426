package kr.co.seesoft.nemo.starnemoapp.ui.visitplanadd;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.po.NemoScheduleAddListPO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoScheduleListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.PaginationScrollListener;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.visitplan.VisitPlanViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class VisitPlanAddFragment extends Fragment {


    private VisitPlanViewModel visitPlanViewModel;
    private VisitPlanAddViewModel visitPlanAddViewModel;


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

    private Button btnMyHospital, btnBranchHospital, btnRNDHospital , btnCencel, btnSave, btnSearch;
    private EditText etSearchKeyWord;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    Gson gson = new Gson();

    private int currentPage = 1; // 초기 페이지

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        visitPlanViewModel =
                ViewModelProviders.of(getActivity()).get(VisitPlanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_visit_plan_add, container, false);

        visitPlanAddViewModel = ViewModelProviders.of(this).get(VisitPlanAddViewModel.class);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {


        btnCencel = (Button) view.findViewById(R.id.btnVisitPlanAddCancel);
        btnSave = (Button) view.findViewById(R.id.btnVisitPlanAddSave);
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

        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));

        Click click = new Click();

        btnBack.setOnClickListener(click);

        btnCencel.setOnClickListener(click);
        btnSave.setOnClickListener(click);
        btnMyHospital.setOnClickListener(click);
        btnBranchHospital.setOnClickListener(click);
        btnRNDHospital.setOnClickListener(click);
        btnSearch.setOnClickListener(click);

        progressDialog = new CustomProgressDialog(getContext());
        visitPlanAddViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        visitPlanAddViewModel.getVisitPlanHospitalList().observe(getViewLifecycleOwner(), new Observer<ArrayList<NemoHospitalSearchRO>>() {
            @Override
            public void onChanged(ArrayList<NemoHospitalSearchRO> visitPlanROS) {
                rvPlanAddAdapter.setPlanLists(visitPlanROS);
                selection.clearSelection();

                if( currentPage == 1)
                {
                    rvPlanAddList.scrollToPosition(0);


                    rvPlanAddList.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager)rvPlanAddLayoutManager) {
                        @Override
                        public void loadNextPage(int page) {

                            currentPage++;
                            AndroidUtil.log("loadNextPage : " + currentPage);
                            visitPlanAddViewModel.setCurrentPage(currentPage);

                            if( "tab".equals( searchMode ))
                            {
                                visitPlanAddViewModel.setHospitals();
                            }
                            else
                            {
                                String searchKeyWord = etSearchKeyWord.getText().toString().trim();

                                if( StringUtils.isEmpty(searchKeyWord) )
                                {
                                    searchKeyWord = "";
                                }

                                visitPlanAddViewModel.searchHospitals(searchType, searchKeyWord);
                            }
                        }

                    });

                }

            }
        });

        selection = new SelectionTracker.Builder<>(
                "selection_id",
                rvPlanAddList,
                new StableIdKeyProvider(rvPlanAddList),
                new VisitPlanAddLookUp(rvPlanAddList),
                StorageStrategy.createLongStorage()).withSelectionPredicate(SelectionPredicates.<Long>createSelectAnything())
                .build();


        rvPlanAddAdapter.setSelectionTracker(selection);

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(getContext(), R.array.search_options, R.layout.adapter_spinner_row_layout);
        spAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout);

        spSearchOption.setAdapter(spAdapter);
        spSearchOption.setOnItemSelectedListener(new SpinnerSelected());


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

    protected void searchHospital() {

        String searchKeyWord = etSearchKeyWord.getText().toString().trim();

        if( StringUtils.isEmpty(searchKeyWord) )
        {
            searchKeyWord = "";
        }

        searchMode = "search";

        currentPage = 1;

        visitPlanAddViewModel.setCurrentPage(currentPage);
        visitPlanAddViewModel.searchHospitals(searchType, searchKeyWord);

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnVisitPlanAddSearch:

                    searchHospital();
                    break;
                case R.id.btnMyHospital:

                    //rvPlanAddList.scrollToPosition(0);

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    visitPlanAddViewModel.setSelectType("my");
                    visitPlanAddViewModel.setCurrentPage(1);
                    visitPlanAddViewModel.setHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
                    btnMyHospital.setTextColor(Color.parseColor("#E50018"));
                    btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnBranchHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnRNDHospital.setTextColor(Color.parseColor("#FF9BA5B7"));

                    break;
                case R.id.btnBranchHospital:
                    //rvPlanAddList.scrollToPosition(0);

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    visitPlanAddViewModel.setSelectType("br");
                    visitPlanAddViewModel.setCurrentPage(1);
                    visitPlanAddViewModel.setHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnMyHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
                    btnBranchHospital.setTextColor(Color.parseColor("#E50018"));
                    btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnRNDHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    break;

                case R.id.btnRNDHospital:
                    //rvPlanAddList.scrollToPosition(0);

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    visitPlanAddViewModel.setSelectType("rnd");
                    visitPlanAddViewModel.setCurrentPage(1);
                    visitPlanAddViewModel.setHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnMyHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnBranchHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
                    btnRNDHospital.setTextColor(Color.parseColor("#E50018"));
                    break;

                case R.id.btnVisitPlanAddSave:
                    Selection<Long> selects = selection.getSelection();

                    AndroidUtil.log("selects : " + selects);

                    ArrayList<NemoScheduleAddListPO> addList = new ArrayList<>();

                    ArrayList<NemoScheduleListRO> oldList = visitPlanViewModel.getVisitPlanList().getValue();

                    selects.forEach(t->{
                        NemoHospitalSearchRO target = rvPlanAddAdapter.getItem(t.intValue());

                        NemoScheduleListRO obj = null;

                        try {
                            obj = oldList.stream().filter(h->h.getCustCd().equals(target.getCustCd()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException());
                        }
                        catch (Exception ex)
                        {

                        }

                        // 중복 고객이 아니면 추가
                        if( obj == null )
                        {

                            addList.add(new NemoScheduleAddListPO( target.getCustCd(), target.getCustNm(), target.getPrjtCd() ));
                        }


                    });


                    if( addList.size() > 0 )
                    {
                        visitPlanViewModel.addVisPlanListData(addList);
                    }

                    Navigation.findNavController(getView()).popBackStack();

                    break;

                case R.id.btnVisitPlanAddCancel:
                    //이전 화면으로 가기
                    Navigation.findNavController(getView()).popBackStack();

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

            holder.tvCode.setText(t.getCustCd());
            holder.tvName.setText(t.getCustNm());
            holder.tvCount.setText("0");

            holder.setSelectiontracker(selectionTracker);

        }

        @Override
        public int getItemCount() {
            return this.planLists.size();
        }

        public class VisitPlanAddViewHolder extends RecyclerView.ViewHolder {

            public TextView tvCode, tvName, tvCount;

            public View v;

            public VisitPlanAddViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvCode = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListCode);
                tvName = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListName);
                tvCount = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListCount);
            }

            public VisitPlanAddLookUp.ItemDetails<Long> getItemDetails() {
                return new ItemDetailsLookup.ItemDetails<Long>() {
                    @Override
                    public int getPosition() {
                        return getAdapterPosition();
                    }

                    @Nullable
                    @Override
                    public Long getSelectionKey() {
                        return getItemId();
                    }
                };
            }

            public void setSelectiontracker(SelectionTracker<Long> selectionTracker){

                if(selectionTracker != null && selectionTracker.isSelected((long) getAdapterPosition())){
                    v.setBackgroundResource(android.R.color.darker_gray);
                    tvCode.setTextColor(Color.parseColor("#ffffff"));
                    tvName.setTextColor(Color.parseColor("#ffffff"));
                    tvCount.setTextColor(Color.parseColor("#ffffff"));
                    //AndroidUtil.toast(getContext(),"선택됨");
                }else{

                    if ((long) getAdapterPosition() % 2 == 0) {
                        v.setBackgroundColor(ContextCompat.getColor(v.getContext(), android.R.color.transparent));
                    }
                    else
                    {
                        v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.even_row_color));
                    }

                    //v.setBackgroundResource(android.R.color.white);
                    tvCode.setTextColor(Color.parseColor("#000000"));
                    tvName.setTextColor(Color.parseColor("#000000"));
                    tvCount.setTextColor(Color.parseColor("#000000"));
                    //AndroidUtil.toast(getContext(),"선택 안됨");
                }

            }

        }
    }

    public class VisitPlanAddLookUp extends ItemDetailsLookup<Long> {

        private RecyclerView view;

        public VisitPlanAddLookUp(RecyclerView view) {
            this.view = view;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
            View v = view.findChildViewUnder(e.getX(), e.getY());
            if (v != null) {
                VisitPlanAddAdapter.VisitPlanAddViewHolder holder = (VisitPlanAddAdapter.VisitPlanAddViewHolder) view.getChildViewHolder(v);
                return holder.getItemDetails();
            }
            return null;
        }
    }
}