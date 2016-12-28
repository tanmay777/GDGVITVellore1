package com.example.tanmayjha.gdgvitvellore.Entity.Project;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tanmayjha.gdgvitvellore.Entity.model.ProjectModel;
import com.example.tanmayjha.gdgvitvellore.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment {

    RecyclerView mRecyclerView;
    Firebase mRef;
    ProgressDialog mProgressDialog;

    public ProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        mRef = new Firebase("https://gdg-vit-vellore-af543.firebaseio.com/project");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_project);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showProgressDialog();
        FirebaseRecyclerAdapter<ProjectModel, ProjectsViewHolder> adapter=new FirebaseRecyclerAdapter<ProjectModel, ProjectsViewHolder>(
                ProjectModel.class,
                R.layout.card_project,
                ProjectsViewHolder.class,
                mRef.getRef()
        ) {
            @Override
            protected void populateViewHolder(ProjectsViewHolder projectsViewHolder, ProjectModel projectModel, int i) {
                projectsViewHolder.projectName.setText(projectModel.getProjectName());
                projectsViewHolder.projectDescription.setText(projectModel.getProjectDescription());
                projectsViewHolder.projectContributer.setText(projectModel.getProjectContributer());
                Glide.with(getActivity()).load(projectModel.getProjectIcon()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(ProjectsViewHolder.projectIcon);
            }
        };

        mRecyclerView.setAdapter(adapter);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static class ProjectsViewHolder extends RecyclerView.ViewHolder{

        TextView projectName,projectDescription,projectContributer;
        static CircleImageView projectIcon;

        public ProjectsViewHolder(View v) {
            super(v);
            projectName=(TextView)v.findViewById(R.id.project_name);
            projectDescription=(TextView)v.findViewById(R.id.project_description);
            projectContributer=(TextView)v.findViewById(R.id.project_contributor);
            projectIcon=(CircleImageView) v.findViewById(R.id.project_image);
        }
    }


    void showProgressDialog(){
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog(){
        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }
        
}
