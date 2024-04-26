package kr.co.seesoft.nemo.starnemoapp.ui.camera.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.seesoft.nemo.starnemoapp.R;
import kr.co.seesoft.nemo.starnemoapp.db.vo.PictureVO;
import kr.co.seesoft.nemo.starnemoapp.ui.camera.CameraMainActivity;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;

public class GalleryListFragment extends Fragment {
    private GalleryListViewModel galleryListViewModel;

    //리스트뷰 관련
    private RecyclerView rvGallayList;
    private GallayListAdapter rvGallaylistAdapter;
    private RecyclerView.LayoutManager rvPGallayListManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_gallery, container, false);

        galleryListViewModel = ViewModelProviders.of(this).get(GalleryListViewModel.class);

        initUI(root);
        init();
        return root;
    }

    private void initUI(View view) {

        rvGallayList = (RecyclerView) view.findViewById(R.id.rvGalleryList);
        rvGallayList.setHasFixedSize(true);

        rvPGallayListManager = new GridLayoutManager(getActivity(), 3);
        rvGallayList.setLayoutManager(rvPGallayListManager);

    }

    private void init() {

        String rootDirectory = getArguments().getString("root_directory");

        galleryListViewModel.setRootDirectory(rootDirectory);
        galleryListViewModel.setHospitalCd(CameraMainActivity.Companion.getHospitalCd());
        galleryListViewModel.setToday(CameraMainActivity.Companion.getToday());
        galleryListViewModel.initDB();;

        rvGallaylistAdapter = new GallayListAdapter(new ArrayList<>());
        rvGallayList.setAdapter(rvGallaylistAdapter);

        galleryListViewModel.getHospitalYmdDatas().observe(getViewLifecycleOwner(), new Observer<List<PictureVO>>() {
            @Override
            public void onChanged(List<PictureVO> pictureVOS) {
                rvGallaylistAdapter.setPictureVOS(pictureVOS);
                rvGallaylistAdapter.notifyDataSetChanged();
            }
        });

//        rvGallayList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
//
//        });

    }




    public class GallayListAdapter extends RecyclerView.Adapter<GallayListAdapter.GallayListHolder> {

        private List<PictureVO> pictureVOS;

        public GallayListAdapter(List<PictureVO> pictureVOS) {
            this.pictureVOS = pictureVOS;
            setHasStableIds(true);

        }

        public void setPictureVOS(List<PictureVO> pictureVOS) {
            this.pictureVOS = pictureVOS;
        }

        public PictureVO getItem(int position){
            return pictureVOS.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public GallayListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_gallery_layout, parent, false);

            GallayListHolder holder = new GallayListHolder(adapterView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull GallayListHolder holder, int position) {
            PictureVO t = this.pictureVOS.get(position);

            holder.bindItem(t);
        }

        @Override
        public int getItemCount() {
            return this.pictureVOS.size();
        }

        public class GallayListHolder extends RecyclerView.ViewHolder {

            public TextView tvName;
            public ImageView ivPhoto;
            private PictureVO item;

            public View v;

            public GallayListHolder(@NonNull View itemView) {
                super(itemView);
                v = itemView;
                ivPhoto = (ImageView) itemView.findViewById(R.id.ivAdGalleryImage);
                tvName = (TextView) itemView.findViewById(R.id.tvAdGallayImageName);
//                tvCode = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListCode);
//                tvName = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListName);
//                tvCount = (TextView) itemView.findViewById(R.id.tvVisitPlanAddListCount);

                ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AndroidUtil.log(item.toString());

                        Bundle bundle = new Bundle();


                        bundle.putString("root_directory", galleryListViewModel.getRootDirectory());
                        bundle.putSerializable("file_name", item.filePath);

                        Navigation.findNavController(getView()).navigate(R.id.action_galleryListFragment_to_gallery_fragment, bundle);
                    }
                });
            }

            public void bindItem(PictureVO t){
                this.item = t;
                Glide.with(getContext()).load(new File(t.filePath)).into(this.ivPhoto);
                String fileNameText = "";
                if(t.sendFlag) {
                    if (StringUtils.isNotEmpty(t.saveFileName)) {

                        AndroidUtil.log("saveFileName : " + t.saveFileName);

                        fileNameText = t.saveFileName;

//                        String[] a = t.saveFileName.split("_");
//                        if (a.length > 3) {
//                            fileNameText = a[2];
//                        }
                    }
                }else{
                    fileNameText = "미전송";
                }
                this.tvName.setText(fileNameText);
            }

        }
    }
}