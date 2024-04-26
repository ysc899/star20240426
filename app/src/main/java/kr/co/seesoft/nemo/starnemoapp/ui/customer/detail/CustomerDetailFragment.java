package kr.co.seesoft.nemo.starnemoapp.ui.customer.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerDetailListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerDetailNoticeListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoCustomerDetailRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoDepartmentContactListRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoHospitalSearchRO;
import kr.co.seesoft.nemo.starnemoapp.nemoapi.ro.NemoVisitListRO;
import kr.co.seesoft.nemo.starnemoapp.ui.dialog.CustomProgressDialog;
import kr.co.seesoft.nemo.starnemoapp.ui.transaction.agree.TransactionAgreeViewModel;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;
import kr.co.seesoft.nemo.starnemoapp.util.Const;

public class CustomerDetailFragment extends Fragment {


    private CustomerDetailViewModel customerDetailViewModel;


    //리스트뷰 관련
    private RecyclerView rvViewList;
    private ListViewAdapter rvViewAdapter;
    private RecyclerView.LayoutManager rvViewLayoutManager;
    private SelectionTracker<Long> selection;

    private LinearLayout emptyView;

    Type adapterListType = new TypeToken<ArrayList<NemoCustomerDetailListRO>>() {
    }.getType();
    //리스트뷰 관련 끝

    //셀렉박스 관련
    private Spinner spSearchOption;

    /** 병원 정보 */
    private String visitHospital;

    /**
     * 검색 방법 종류 (병원명, 병원 코드)
     */
    private int searchType;
    //셀렉박스 관련 끝

    private TextView tvHospitalName;

    private TextView tvCustCd, tvChgrNm , tvCstatNm, tvCareInstNo, tvBizrNo, tvPmntPragDd, tvAddr, tvCustTelno, tvCustEmalAddr, tvItrnCd;

    private TextView tvNotice;

    private Button btnClear, btnSign;
    private EditText etSearchKeyWord;

    private CustomProgressDialog progressDialog;

    // Title bar button
    private ImageButton btnBack, btnHome;

    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        customerDetailViewModel =
                ViewModelProviders.of(getActivity()).get(CustomerDetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_customer_detail, container, false);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnHome = (ImageButton) view.findViewById(R.id.btnHome);

        tvHospitalName = (TextView) view.findViewById(R.id.tvHospitalName);

        tvCustCd = (TextView) view.findViewById(R.id.tvCustCd);
        tvChgrNm = (TextView) view.findViewById(R.id.tvChgrNm);
        tvCstatNm = (TextView) view.findViewById(R.id.tvCstatNm);
        tvCareInstNo = (TextView) view.findViewById(R.id.tvCareInstNo);
        tvBizrNo = (TextView) view.findViewById(R.id.tvBizrNo);
        tvPmntPragDd = (TextView) view.findViewById(R.id.tvPmntPragDd);
        tvAddr = (TextView) view.findViewById(R.id.tvAddr);
        tvCustTelno = (TextView) view.findViewById(R.id.tvCustTelno);
        tvCustEmalAddr = (TextView) view.findViewById(R.id.tvCustEmalAddr);
        tvItrnCd = (TextView) view.findViewById(R.id.tvItrnCd);

        tvNotice = (TextView) view.findViewById(R.id.tvNotice);

        rvViewList = (RecyclerView) view.findViewById(R.id.rvViewList);
        rvViewList.setHasFixedSize(true);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        rvViewLayoutManager = new LinearLayoutManager(getActivity());
        rvViewList.setLayoutManager(rvViewLayoutManager);

    }

    private void init() {


        btnHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_menu_to_home));


        Click click = new Click();

        tvCustTelno.setOnClickListener(click);

        btnBack.setOnClickListener(click);

        progressDialog = new CustomProgressDialog(getContext());
        customerDetailViewModel.getProgressFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        visitHospital = (String) getArguments().getSerializable("visit_hospital_cd");
        String visitHospitalNM = (String) getArguments().getSerializable("visit_hospital_nm");

        AndroidUtil.log("visitHospital : " + visitHospital);

        customerDetailViewModel.setHospital(visitHospital);

        tvHospitalName.setText(visitHospitalNM);

        customerDetailViewModel.getCustomerDetailInfo().observe(getViewLifecycleOwner(), new Observer<NemoCustomerDetailRO>() {
            @Override
            public void onChanged(NemoCustomerDetailRO customerROS) {

                dispCustomerInfo(customerROS);

            }
        });

        tvNotice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

    }

    public void dispCustomerInfo(NemoCustomerDetailRO customerROS)
    {

        if( customerROS != null )
        {
            // 기본 정보 출력
            tvCustCd.setText(customerROS.getCustCd());
            tvChgrNm.setText(customerROS.getChgrNm());
            tvCstatNm.setText(customerROS.getCstatNm());
            tvCareInstNo.setText(customerROS.getCareInstNo());
            tvBizrNo.setText(customerROS.getBizrNo());
            tvPmntPragDd.setText(customerROS.getPmntRttnDcnt());
            tvAddr.setText(customerROS.getAddr());
            tvCustTelno.setText(customerROS.getCustTelno());
            tvCustEmalAddr.setText(customerROS.getCustEmalAddr());
            tvItrnCd.setText(AndroidUtil.getItrnNm(customerROS.getItrnCd()));

            ArrayList<NemoCustomerDetailListRO> cmjprsnList = customerROS.getCmjprsnList();

            ArrayList<NemoCustomerDetailNoticeListRO> salsUisuList = customerROS.getSalsUisuList();

            // 연락처 정보 출력
//            if( cmjprsnList != null && cmjprsnList.size() > 0 )
//            {
//                rvViewAdapter.setadapterLists(cmjprsnList);
//            }

            rvViewAdapter.setadapterLists(cmjprsnList);

            // 메모 정보 출력
            if( salsUisuList != null && salsUisuList.size() > 0 )
            {
                StringBuilder sbList = new StringBuilder();

                for(int i = 0 ; i < salsUisuList.size() ; i++)
                {
                    if( i > 0 )
                    {
                        sbList.append("\n");
                    }

                    sbList.append(salsUisuList.get(i).getUisuDivNm());
                    sbList.append(" : ");
                    sbList.append(salsUisuList.get(i).getUisu());
                }

                tvNotice.setText(sbList.toString());
            }
            else
            {
                tvNotice.setText("");
            }

        }
        else
        {
            tvCustCd.setText("");
            tvChgrNm.setText("");
            tvCstatNm.setText("");
            tvCareInstNo.setText("");
            tvBizrNo.setText("");
            tvPmntPragDd.setText("");
            tvAddr.setText("");
            tvCustTelno.setText("");
            tvCustEmalAddr.setText("");
            tvNotice.setText("");
            tvItrnCd.setText("");

            rvViewAdapter.setadapterLists(null);
        }

    }

    public ArrayList<NemoCustomerDetailListRO> adapterListCopy(ArrayList<NemoCustomerDetailListRO> fromList) {
        String copyJSON = gson.toJson(fromList);
        ArrayList<NemoCustomerDetailListRO> toList = gson.fromJson(copyJSON, adapterListType);
        return toList;
    }

    private void callPhone()
    {
        String moblPhno = AndroidUtil.nullToString(tvCustTelno.getText().toString(),"");

        if( moblPhno.length() > 0 )
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+moblPhno));
            startActivity(callIntent);
        }

    }

    private void callPhone(NemoCustomerDetailListRO t)
    {
        String moblPhno = AndroidUtil.nullToString(t.getCinfo(),"");

        if( moblPhno.length() > 0 )
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+moblPhno));
            startActivity(callIntent);
        }

    }


    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tvCustTelno:

                    callPhone();

                    break;

                case R.id.btnBack:

                    Navigation.findNavController(getView()).popBackStack();

                    break;
            }
        }
    }

    public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

        private ArrayList<NemoCustomerDetailListRO> adapterLists;
        private SelectionTracker<Long> selectionTracker;

        public ListViewAdapter(ArrayList<NemoCustomerDetailListRO> adapterLists) {
            this.adapterLists = adapterListCopy(adapterLists);

            setHasStableIds(true);
        }

        public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        public NemoCustomerDetailListRO getItem(int position){
            return adapterLists.get(position);
        }

        public void setadapterLists(ArrayList<NemoCustomerDetailListRO> adapterLists) {

            if( adapterLists == null || adapterLists.size() == 0)
            {
                emptyView.setVisibility(View.VISIBLE);
                rvViewList.setVisibility(View.GONE);

                return;
            }

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

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_customer_detail_layout, parent, false);

            ListViewHolder holder = new ListViewHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            NemoCustomerDetailListRO t = this.adapterLists.get(position);

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

            public TextView tvCmjprsnNm, tvCcomOfpoNm, tvCinfo, tvCustEmalAddr;

            public View v;

            public NemoCustomerDetailListRO item = null;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                tvCmjprsnNm = (TextView) itemView.findViewById(R.id.tvCmjprsnNm);
                tvCcomOfpoNm = (TextView) itemView.findViewById(R.id.tvCcomOfpoNm);
                tvCinfo = (TextView) itemView.findViewById(R.id.tvCinfo);
                tvCustEmalAddr = (TextView) itemView.findViewById(R.id.tvCustEmalAddr);

                tvCinfo.setOnClickListener(view -> {
                    callPhone(item);
                });

            }

            public void bindItem(NemoCustomerDetailListRO t) {
                tvCmjprsnNm.setText(t.getCmjprsnNm());
                tvCcomOfpoNm.setText(t.getCcomOfpoNm());
                tvCinfo.setText(t.getCinfo());
                tvCustEmalAddr.setText(t.getCustEmalAddr());
                this.item = t;
            }

        }
    }

}