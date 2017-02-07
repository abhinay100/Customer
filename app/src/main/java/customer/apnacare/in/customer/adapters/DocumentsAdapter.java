package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.fragments.BasicProfile;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Documents;
import customer.apnacare.in.customer.utils.CircleTransform;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

import static customer.apnacare.in.customer.R.id.LinearLayout;
import static customer.apnacare.in.customer.R.id.container;
import static customer.apnacare.in.customer.R.id.profileLayout;

/**
 * Created by root on 5/2/17.
 */

public class DocumentsAdapter extends RealmBasedRecyclerViewAdapter<Documents, DocumentsAdapter.ViewHolder>  {


    Context mContext;
    View parentView;

    ViewGroup view;
    public DocumentsAdapter(
            Context context,
            RealmResults<Documents> realmResults,
            boolean automaticUpdate,
            boolean animateIdType,
            View view) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;
        parentView = view;
    }




    @Override
    public DocumentsAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        DocumentsAdapter.ViewHolder vh = null;
        try {

            View v = inflater.inflate(R.layout.caregiver_card, viewGroup, false);
            vh = new DocumentsAdapter.ViewHolder(v);


        }catch (Exception e){
            Log.v(Constants.TAG,"onCreateRealmViewHolder Exception: "+e.toString());
        }
        return  vh;
    }

    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.card_view) CardView cardView;
        @BindView(R.id.lblImage) TextView documentName;
        @BindView(R.id.viewButton) TextView viewButton;
//        @BindView(R.id.profileLayout) LinearLayout profileLayout;
//        @BindView(R.id.imageLayout) LinearLayout imageLayout;
//        @BindView(R.id.documentImage)
//        ImageView documentImage;



        public ViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);


        }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindRealmViewHolder(DocumentsAdapter.ViewHolder viewHolder, int position) {

//        final Documents documents = realmResults.get(position);
        Documents documents = Realm.getDefaultInstance().where(Documents.class).findFirst();
        try {
            viewHolder.documentName.setText(documents.getDocumentName());
//            viewHolder.documentImage.setBackground(Drawable.createFromPath(documents.getDocumentURL()));


//            parentView.findViewById(R.id.documentImage).setBackground(Drawable.createFromPath(documents.getDocumentURL()));
//            URL url = new URL(documents.getDocumentURL());
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            View document = parentView.findViewById(R.id.documentImage);
            Picasso.with(parentView.getContext()).load(documents.getDocumentURL()).resize(5000, 5000).into((ImageView) document);
//            parentView.findViewById(R.id.documentImage).setBackground(Drawable.createFromPath(documents.getDocumentURL()));


            viewHolder.viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parentView.findViewById(R.id.profileLayout).setVisibility(View.GONE);
                    parentView.findViewById(R.id.imageLayout).setVisibility(View.VISIBLE);

                    ((Button) parentView.findViewById(R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            parentView.findViewById(R.id.profileLayout).setVisibility(View.VISIBLE);
                            parentView.findViewById(R.id.imageLayout).setVisibility(View.GONE);
                        }
                    });

//                    Toast.makeText(mContext, documents.getDocumentURL(),
//                            Toast.LENGTH_LONG).show();

                }
            });

        }catch (Exception e){
            Log.v(Constants.TAG,"Exception: "+e.toString());
        }

    }



}
