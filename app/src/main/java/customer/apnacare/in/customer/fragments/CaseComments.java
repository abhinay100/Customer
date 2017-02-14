package customer.apnacare.in.customer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.Comment;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.adapters.CommentsAdapter;
import customer.apnacare.in.customer.model.Comments;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 9/2/17.
 */

public class CaseComments extends Fragment {

    Context mContext;
    RecyclerView mRecyclerView;
    CommentsAdapter commentsAdapter;
    private Realm realm;
    ViewGroup view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_comments, container, false);
        mContext = getContext();
        init_list();
        return view;
    }

    public void init_list(){
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Comments> commentsList = realm.where(Comments.class).findAll();
            Log.v(Constants.TAG, "commentsList " + commentsList);
            mRecyclerView = (RecyclerView)  view.findViewById(R.id.caseCommentsList);
            commentsAdapter = new CommentsAdapter(this.getContext(),commentsList,true,true);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.swapAdapter(commentsAdapter,true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            commentsAdapter.notifyDataSetChanged();

        }catch (Exception e){
            Log.v(Constants.TAG, "CaseComments Recycler Exception: " + e.toString());
        }

    }


}
