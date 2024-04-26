package kr.co.seesoft.nemo.starnemoapp.ui.account;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.api.ro.VisitPlanRO;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class AccountFragment extends Fragment {


    private AccountViewModel accountViewModel;


    //리스트뷰 관련
    private RecyclerView rvAccountList;
    private AccountAdapter rvAccountAdapter;
    private RecyclerView.LayoutManager rvAccountLayoutManager;

//    Type visitPlanListType = new TypeToken<ArrayList<VisitPlanRO>>() {
//    }.getType();
    //리스트뷰 관련 끝

    //셀렉박스 관련
    private Spinner spSearchOption;


    /**
     * 검색 모드
     */
    private int searchMode = Const.SEARCH_MODE_MY;
    /**
     * 검색 방법 종류 (병원명, 병원 코드)
     */
    private int searchType;
    //셀렉박스 관련 끝

    private Button btnMyHospital, btnAllHospital, btnSearch;
    private EditText etSearchKeyWord;


    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {


        btnMyHospital = (Button) view.findViewById(R.id.btnAccountMyHospital);
        btnAllHospital = (Button) view.findViewById(R.id.btnAccountAllHospital);
        btnSearch = (Button) view.findViewById(R.id.btnAccountSearch);

        rvAccountList = (RecyclerView) view.findViewById(R.id.rvAccountList);
        rvAccountList.setHasFixedSize(true);

        rvAccountLayoutManager = new LinearLayoutManager(getActivity());
        rvAccountList.setLayoutManager(rvAccountLayoutManager);


        spSearchOption = (Spinner) view.findViewById(R.id.spAccountSearchOption);
        etSearchKeyWord = (EditText) view.findViewById(R.id.etAccountSearchWord);


    }

    private void init() {

        Click click = new Click();

        btnMyHospital.setOnClickListener(click);
        btnAllHospital.setOnClickListener(click);
        btnSearch.setOnClickListener(click);

        rvAccountAdapter = new AccountAdapter(new ArrayList<>());

        rvAccountList.setAdapter(rvAccountAdapter);

        accountViewModel.getVisitPlanHospitalList().observe(getViewLifecycleOwner(), new Observer<ArrayList<VisitPlanRO>>() {
            @Override
            public void onChanged(ArrayList<VisitPlanRO> visitPlanROS) {
                rvAccountAdapter.setPlanLists(visitPlanROS);
            }
        });

//        selection = new SelectionTracker.Builder<>(
//                "seelection_id",
//                rvAccountList,
//                new StableIdKeyProvider(rvAccountList),
//                new AccountLookUp(rvAccountList),
//                StorageStrategy.createLongStorage()).withSelectionPredicate(SelectionPredicates.<Long>createSelectAnything())
//                .build();
//
//
//        rvAccountAdapter.setSelectionTracker(selection);

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(getContext(), R.array.search_options, R.layout.adapter_spinner_row_layout);
        spAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout);

        spSearchOption.setAdapter(spAdapter);

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

    protected void searchHospital() {

        String searchKeyWord = etSearchKeyWord.getText().toString();

        accountViewModel.searchHospitals(searchMode, searchType, searchKeyWord);

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnAccountSearch:
                    searchHospital();
                    break;
                case R.id.btnAccountMyHospital:
                    searchMode = Const.SEARCH_MODE_MY;
                    accountViewModel.setMyHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.btn_04));
                    btnMyHospital.setTextColor(Color.parseColor("#863049"));
                    btnAllHospital.setBackground(getActivity().getDrawable(R.drawable.btn_05));
                    btnAllHospital.setTextColor(Color.parseColor("#333333"));

                    break;
                case R.id.btnAccountAllHospital:
                    searchMode = Const.SEARCH_MODE_ALL;
                    accountViewModel.setAllHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.btn_05));
                    btnMyHospital.setTextColor(Color.parseColor("#333333"));
                    btnAllHospital.setBackground(getActivity().getDrawable(R.drawable.btn_04));
                    btnAllHospital.setTextColor(Color.parseColor("#863049"));
                    break;

            }
        }
    }


    public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

        private ArrayList<VisitPlanRO> accountLists;
        private SelectionTracker<Long> selectionTracker;

        public AccountAdapter(ArrayList<VisitPlanRO> accountLists) {
            this.accountLists = accountLists;

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

//        public ArrayList<VisitPlanRO> getSelectPlanLists(List<Long> positions) {
//            return planLists;
//        }

        public VisitPlanRO getItem(int position) {
            return accountLists.get(position);
        }

        public void setPlanLists(ArrayList<VisitPlanRO> accountLists) {
            this.accountLists = accountLists;
            notifyDataSetChanged();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_account_layout, parent, false);

            AccountViewHolder holder = new AccountViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
            VisitPlanRO t = this.accountLists.get(position);
//            holder.tvOrder.setText(String.valueOf(t.order));
            holder.tvCode.setText(t.hospitalCode);
            holder.tvName.setText(t.hospitalName);
            holder.tvCount.setText(String.valueOf(t.count));


        }

        @Override
        public int getItemCount() {
            return this.accountLists.size();
        }

        public class AccountViewHolder extends RecyclerView.ViewHolder {

            public TextView tvCode, tvName, tvCount;

            public View v;

            public AccountViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvCode = (TextView) itemView.findViewById(R.id.tvAccountListCode);
                tvName = (TextView) itemView.findViewById(R.id.tvAccountListName);
                tvCount = (TextView) itemView.findViewById(R.id.tvAccountListCount);
            }
//
//            public AccountLookUp.ItemDetails<Long> getItemDetails() {
//                return new ItemDetailsLookup.ItemDetails<Long>() {
//                    @Override
//                    public int getPosition() {
//                        return getAdapterPosition();
//                    }
//
//                    @Nullable
//                    @Override
//                    public Long getSelectionKey() {
//                        return getItemId();
//                    }
//                };
//            }


        }
    }

//    public class AccountLookUp extends ItemDetailsLookup<Long> {
//
//        private RecyclerView view;
//
//        public AccountLookUp(RecyclerView view) {
//            this.view = view;
//        }
//
//        @Nullable
//        @Override
//        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
//            View v = view.findChildViewUnder(e.getX(), e.getY());
//            if (v != null) {
//                AccountAdapter.AccountViewHolder holder = (AccountAdapter.AccountViewHolder) view.getChildViewHolder(v);
//                return holder.getItemDetails();
//            }
//            return null;
//        }
//    }
}