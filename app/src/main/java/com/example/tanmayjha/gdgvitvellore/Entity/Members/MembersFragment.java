package com.example.tanmayjha.gdgvitvellore.Entity.Members;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tanmayjha.gdgvitvellore.Entity.FAQs.FAQsFragment;
import com.example.tanmayjha.gdgvitvellore.Entity.model.FaqsModel;
import com.example.tanmayjha.gdgvitvellore.Entity.model.MemberModel;
import com.example.tanmayjha.gdgvitvellore.R;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends Fragment {

    RecyclerView mRecyclerView;
    Firebase mRef;

    public MembersFragment() {
        // Required empty public constructor
    }

    public void onStart()
    {
        super.onStart();
        View view=getView();
        mRef=new Firebase("https://gdg-vit-vellore-af543.firebaseio.com/members");
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_member);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerAdapter<MemberModel,MembersFragment.MembersViewHolder> adapter=new FirebaseRecyclerAdapter<MemberModel,MembersViewHolder>(
                MemberModel.class,
                R.layout.card_member,
                MembersFragment.MembersViewHolder.class,
                mRef.getRef()
        ) {
            @Override
            protected void populateViewHolder(MembersFragment.MembersViewHolder membersViewHolder, MemberModel memberModel, int i) {
                membersViewHolder.name.setText(memberModel.getName());
                membersViewHolder.work.setText(memberModel.getWork());
                membersViewHolder.githubid.setText(memberModel.getGithubid());
                Glide.with(getActivity()).load(memberModel.getProfile_pic()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(MembersViewHolder.profile_pic);
            }
        };

        mRecyclerView.setAdapter(adapter);

    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder{
        TextView name,work,githubid;
        static CircleImageView profile_pic;

        public MembersViewHolder(View v) {
            super(v);
            name=(TextView)v.findViewById(R.id.member_name);
            work=(TextView)v.findViewById(R.id.member_work);
            githubid=(TextView)v.findViewById(R.id.member_github_id);
            profile_pic=(CircleImageView)v.findViewById(R.id.member_image);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_members, container, false);
    }

}
