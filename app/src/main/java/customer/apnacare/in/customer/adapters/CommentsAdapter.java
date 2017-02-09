package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.Comments;
import customer.apnacare.in.customer.model.WorkLog;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 9/2/17.
 */

public class CommentsAdapter extends RealmBasedRecyclerViewAdapter<Comments, CommentsAdapter.ViewHolder> {

    Context mContext;

    public CommentsAdapter(
            Context context,
            RealmResults<Comments> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;
    }

    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.lblCommentName)
        TextView commentName;

        @BindView(R.id.lblDate)
        TextView commentDate;

        @BindView(R.id.lblComment)
        TextView comment;


        public ViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }


    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder vh = null;
        try {
            View v = inflater.inflate(R.layout.comments_card,viewGroup,false);
            vh = new ViewHolder(v);

        }catch (Exception e){
            Log.v(Constants.TAG,"onCreateRealmViewHolder Exception: "+e.toString());
        }
        return  vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindRealmViewHolder(CommentsAdapter.ViewHolder viewHolder, int position) {

        final Comments comments = realmResults.get(position);
        try {

            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

            String inputCommentsDate = comments.getCreatedAt();
            Date commentsDate = inputFormat.parse(inputCommentsDate);
            String outputCommentsDate = outputFormat.format(commentsDate);
            viewHolder.commentDate.setText(outputCommentsDate);


            viewHolder.commentName.setText(comments.getUserName());
            viewHolder.comment.setText(comments.getComment());


        }catch (Exception e){
            Log.v(Constants.TAG,"Exception: "+e.toString());
        }

    }




}
