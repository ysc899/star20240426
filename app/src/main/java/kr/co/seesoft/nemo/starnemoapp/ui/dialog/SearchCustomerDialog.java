package kr.co.seesoft.nemo.starnemoapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyanogen.signatureview.SignatureView;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.ui.PaginationScrollListener;
import kr.co.seesoft.nemo.starnemoapp.ui.customer.CustomerFragment;
import kr.co.seesoft.nemo.starnemoapp.ui.customer.CustomerViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;


public class SearchCustomerDialog extends DialogFragment {

    private SearchCustomerDialogViewModel searchCustomerDialogViewModel;

    //리스트뷰 관련
    private RecyclerView rvPlanAddList;
    private VisitPlanAddAdapter rvPlanAddAdapter;
    private RecyclerView.LayoutManager rvPlanAddLayoutManager;
    private LinearLayout emptyView;

    //셀렉박스 관련
    private Spinner spSearchOption;

    // 검색 액션 구분
    // tab = 상단 탭 클릭
    // search = 검색 버튼 클릭
    private String searchMode = "tab";

    private Button btnCancel;
    private Button btnMyHospital, btnBranchHospital, btnRNDHospital, btnSearch;
    private EditText etSearchKeyWord;

    private CustomProgressDialog progressDialog;

    Type visitPlanListType = new TypeToken<ArrayList<NemoHospitalSearchRO>>() {
    }.getType();

    /**
     * 검색 방법 종류 (고객명, 고객 코드, 담당자)
     */
    private int searchType;

    private int currentPage = 1; // 초기 페이지

    Gson gson = new Gson();

    private Context c;
    private Handler handler;

    public static SearchCustomerDialog newInstance(Handler h){
        SearchCustomerDialog dialog = new SearchCustomerDialog();
        dialog.handler = h;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog_search_customer, null);

        searchCustomerDialogViewModel = new ViewModelProvider(this).get(SearchCustomerDialogViewModel.class);

        initUI(dialog);
        init();

        builder.setView(dialog);

        return builder.create();
    }

    private void initUI(View v){

        btnMyHospital = (Button) v.findViewById(R.id.btnMyHospital);
        btnBranchHospital = (Button) v.findViewById(R.id.btnBranchHospital);
        btnRNDHospital = (Button) v.findViewById(R.id.btnRNDHospital);
        btnSearch = (Button) v.findViewById(R.id.btnVisitPlanAddSearch);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);

        rvPlanAddList = (RecyclerView) v.findViewById(R.id.rvVisitPlanAddList);
        rvPlanAddList.setHasFixedSize(true);

        emptyView = (LinearLayout) v.findViewById(R.id.emptyView);

        rvPlanAddLayoutManager = new LinearLayoutManager(this.c);
        rvPlanAddList.setLayoutManager(rvPlanAddLayoutManager);


        spSearchOption = (Spinner) v.findViewById(R.id.spVisitPlanAddSearchOption);
        etSearchKeyWord = (EditText) v.findViewById(R.id.etVisitPlanAddSearchWord);

    }
    private  void init(){

        Click click = new Click();

        btnMyHospital.setOnClickListener(click);
        btnBranchHospital.setOnClickListener(click);
        btnRNDHospital.setOnClickListener(click);
        btnSearch.setOnClickListener(click);

        progressDialog = new CustomProgressDialog(getContext());

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SearchCustomerDialog.this.getDialog().cancel();
            }
        });


        rvPlanAddAdapter = new VisitPlanAddAdapter(new ArrayList<>());

        rvPlanAddList.setAdapter(rvPlanAddAdapter);

        searchCustomerDialogViewModel.getVisitPlanHospitalList().observe(this,new Observer<ArrayList<NemoHospitalSearchRO>>() {
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
                            searchCustomerDialogViewModel.setCurrentPage(currentPage);

                            if( "tab".equals( searchMode ))
                            {
                                searchCustomerDialogViewModel.setHospitals();
                            }
                            else
                            {
                                String searchKeyWord = etSearchKeyWord.getText().toString().trim();

                                if( StringUtils.isEmpty(searchKeyWord) )
                                {
                                    searchKeyWord = "";
                                }

                                searchCustomerDialogViewModel.searchHospitals(searchType, searchKeyWord);
                            }
                        }

                    });

                }


                rvPlanAddList.requestLayout();
            }


        });

        etSearchKeyWord.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                searchHospital();

                return true;
            }
        });

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(getContext(), R.array.search_options, R.layout.adapter_spinner_row_layout);
        spAdapter.setDropDownViewResource(R.layout.adapter_spinner_row2_layout);

        spSearchOption.setAdapter(spAdapter);
        spSearchOption.setOnItemSelectedListener(new SpinnerSelected());

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

        rvPlanAddList.scrollToPosition(0);

        searchMode = "search";

        currentPage = 1;

        searchCustomerDialogViewModel.setCurrentPage(currentPage);
        searchCustomerDialogViewModel.searchHospitals(searchType, searchKeyWord);

    }

    protected void selectTask(NemoHospitalSearchRO t){

        Message msg = new Message();
        msg.what = 1;
        msg.obj = t;
        handler.sendMessage(msg);

        SearchCustomerDialog.this.getDialog().cancel();
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

                    searchCustomerDialogViewModel.setSelectType("my");
                    searchCustomerDialogViewModel.setCurrentPage(1);
                    searchCustomerDialogViewModel.setHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
                    btnMyHospital.setTextColor(Color.parseColor("#E50018"));
                    btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnBranchHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnRNDHospital.setTextColor(Color.parseColor("#FF9BA5B7"));

                    break;
                case R.id.btnBranchHospital:

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    searchCustomerDialogViewModel.setSelectType("br");
                    searchCustomerDialogViewModel.setCurrentPage(1);
                    searchCustomerDialogViewModel.setHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnMyHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
                    btnBranchHospital.setTextColor(Color.parseColor("#E50018"));
                    btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnRNDHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    break;

                case R.id.btnRNDHospital:

                    searchMode = "tab";
                    currentPage = 1;

                    etSearchKeyWord.setText("");

                    searchCustomerDialogViewModel.setSelectType("rnd");
                    searchCustomerDialogViewModel.setCurrentPage(1);
                    searchCustomerDialogViewModel.setHospitals();

                    btnMyHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnMyHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    btnBranchHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_off_top_bottom));
                    btnBranchHospital.setTextColor(Color.parseColor("#FF9BA5B7"));
                    btnRNDHospital.setBackground(getActivity().getDrawable(R.drawable.border_menu_top_bottom));
                    btnRNDHospital.setTextColor(Color.parseColor("#E50018"));
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
        public VisitPlanAddAdapter.VisitPlanAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_customer_layout, parent, false);

            VisitPlanAddAdapter.VisitPlanAddViewHolder holder = new VisitPlanAddAdapter.VisitPlanAddViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull VisitPlanAddAdapter.VisitPlanAddViewHolder holder, int position) {
            NemoHospitalSearchRO t = this.planLists.get(position);

            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
            }
            else
            {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.even_row_color));
            }

            holder.bindItem(t);

        }

        @Override
        public int getItemCount() {
            return this.planLists.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
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
                    selectTask(item);
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
