package kr.co.seesoft.nemo.starnemo.ui.visitplanadd;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import kr.co.seesoft.nemo.starnemo.R;
import kr.co.seesoft.nemo.starnemo.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemo.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemo.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemo.ui.visitplan.VisitPlanViewModel;
import kr.co.seesoft.nemo.starnemo.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemo.util.Const;

public class VisitPlanAddFragment extends Fragment {


    private VisitPlanViewModel visitPlanViewModel;
    private VisitPlanAddViewModel visitPlanAddViewModel;


    //리스트뷰 관련
    private RecyclerView rvPlanAddList;
    private VisitPlanAddAdapter rvPlanAddAdapter;
    private RecyclerView.LayoutManager rvPlanAddLayoutManager;
    private SelectionTracker<Long> selection;

    Type visitPlanListType = new TypeToken<ArrayList<NemoHospitalSearchRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //셀렉박스 관련
    private Spinner spSearchOption;

    /**
     * 검색 방법 종류 (병원명, 병원 코드)
     */
    private int searchType;
    //셀렉박스 관련 끝

    private Button btnMyHospital, btnAllHospital, btnCencel, btnSave, btnSearch;
    private EditText etSearchKeyWord;

    private CustomProgressDialog progressDialog;

    Gson gson = new Gson();

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
        btnMyHospital = (Button) view.findViewById(R.id.btnVisitPlanAddMyHospital);
        btnAllHospital = (Button) view.findViewById(R.id.btnVisitPlanAddAllHospital);
        btnSearch = (Button) view.findViewById(R.id.btnVisitPlanAddSearch);

        rvPlanAddList = (RecyclerView) view.findViewById(R.id.rvVisitPlanAddList);
        rvPlanAddList.setHasFixedSize(true);

        rvPlanAddLayoutManager = new LinearLayoutManager(getActivity());
        rvPlanAddList.setLayoutManager(rvPlanAddLayoutManager);


        spSearchOption = (Spinner) view.findViewById(R.id.spVisitPlanAddSearchOption);
        etSearchKeyWord = (EditText) view.findViewById(R.id.etVisitPlanAddSearchWord);


    }

    private void init() {

        Click click = new Click();

        btnCencel.setOnClickListener(click);
        btnSave.setOnClickListener(click);
        btnMyHospital.setOnClickListener(click);
        btnAllHospital.setOnClickListener(click);
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
            }
        });

        selection = new SelectionTracker.Builder<>(
                "seelection_id",
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

        etSearchKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    searchHospital();
                }
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

        String searchKeyWord = etSearchKeyWord.getText().toString();

        visitPlanAddViewModel.searchHospitals(searchType, searchKeyWord);

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnVisitPlanAddSearch:
                    searchHospital();
                    break;
                case R.id.btnVisitPlanAddMyHospital:
                    etSearchKeyWord.setText("");
                    visitPlanAddViewModel.setMyHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.btn_04));
                    btnMyHospital.setTextColor(Color.parseColor("#863049"));
                    btnAllHospital.setBackground(getActivity().getDrawable(R.drawable.btn_05));
                    btnAllHospital.setTextColor(Color.parseColor("#333333"));

                    break;
                case R.id.btnVisitPlanAddAllHospital:
                    etSearchKeyWord.setText("");
                    visitPlanAddViewModel.setAllHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.btn_05));
                    btnMyHospital.setTextColor(Color.parseColor("#333333"));
                    btnAllHospital.setBackground(getActivity().getDrawable(R.drawable.btn_04));
                    btnAllHospital.setTextColor(Color.parseColor("#863049"));
                    break;

                case R.id.btnVisitPlanAddSave:
                    Selection<Long> selects = selection.getSelection();

                    ArrayList<NemoVisitListRO> addList = new ArrayList<>();

                    AtomicInteger idx = new AtomicInteger(1);


                    selects.forEach(t->{
                        NemoHospitalSearchRO target = rvPlanAddAdapter.getItem(t.intValue());
                        addList.add(new NemoVisitListRO(idx.getAndIncrement(), target.getHospitalName(), target.getHospitalCode()));
                    });

                    visitPlanViewModel.addVisPlanListData(addList);

                case R.id.btnVisitPlanAddCancel:
                    //이전 화면으로 가기
                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class SpinnerSelected implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            searchType = i;
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

            holder.tvCode.setText(t.getHospitalCode());
            holder.tvName.setText(t.getHospitalName());
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
//                    AndroidUtil.toast(getContext(),"선택됨");
                }else{
                    v.setBackgroundResource(android.R.color.white);
                    tvCode.setTextColor(Color.parseColor("#999999"));
                    tvName.setTextColor(Color.parseColor("#999999"));
                    tvCount.setTextColor(Color.parseColor("#999999"));
//                    AndroidUtil.toast(getContext(),"선택 안됨");
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