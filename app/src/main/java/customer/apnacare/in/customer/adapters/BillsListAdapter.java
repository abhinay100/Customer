package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.citrus.sdk.ui.utils.CitrusFlowManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.activity.InvoiceActivity;
import customer.apnacare.in.customer.model.Bills;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 28/12/16.
 */

public class BillsListAdapter extends RealmBasedRecyclerViewAdapter<Bills, BillsListAdapter.ViewHolder> {

    Context mContext;

//    public static final String returnUrlLoadMoney = "http://apnacare.in/citrus/redirectUrlLoadCash.html";
    public static String dummyMobile = "9901647100";
    public static String dummyEmail = "abhinay@apnacare.in";
    public String dummyAmount = "1";



    public BillsListAdapter(
            Context context,
            RealmResults<Bills> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;
    }


    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.card_view) CardView cardView;

        @BindView(R.id.lblInvoiceDate) TextView invoiceDate;
        @BindView(R.id.lblInvoiceNo) TextView invoiceNo;
        @BindView(R.id.lblFromPeriod) TextView fromPeriod;
        @BindView(R.id.lblToPeriod) TextView toPeriod;
        @BindView(R.id.lblInvoiceAmount) TextView invoiceAmount;
        @BindView(R.id.lblStatus) TextView status;
        @BindView(R.id.quick_pay) Button btnQuickPay;
        @BindView(R.id.btnDocument) ImageButton btnDocument;


        public ViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }

    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup,int viewtype){
        ViewHolder vh = null;
        try {

            View v = inflater.inflate(R.layout.bill_card, viewGroup, false);
            vh = new ViewHolder(v);

        }catch (Exception e){
            Log.v(Constants.TAG,"onCreateRealmViewHolder Exception: "+e.toString());
        }
        return  vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position)  {

        final Bills bill = realmResults.get(position);
        try{

            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            String inputInvoiceDateStr= bill.getInvoiceDate();
            Date invoiceDate = inputFormat.parse(inputInvoiceDateStr);
            String outputInvoiceDateStr = outputFormat.format(invoiceDate);
            viewHolder.invoiceDate.setText(outputInvoiceDateStr);

            String inputFromPeriodStr= bill.getInvoicePeriodFrom();
            Date invoiceFromDate = inputFormat.parse(inputFromPeriodStr);
            String outputFromPeriodStr = outputFormat.format(invoiceFromDate);
            viewHolder.fromPeriod.setText("From Date: "+ outputFromPeriodStr);

            String inputToPeriodStr= bill.getInvoicePeriodTo();
            Date invoiceToDate = inputFormat.parse(inputToPeriodStr);
            String outputToPeriodStr = outputFormat.format(invoiceToDate);
            viewHolder.toPeriod.setText("To Date: "+ outputToPeriodStr);

            viewHolder.invoiceNo.setText(bill.getInvoiceNo());
            viewHolder.invoiceAmount.setText("Rs. " + bill.getInvoiceAmount());
//            viewHolder.invoiceAmount.setText("Rs." + 1.00);
            viewHolder.status.setText("Status: "+ bill.getStatus());

            viewHolder.btnQuickPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dummyAmount = bill.getInvoiceAmount();

                    CitrusFlowManager.startShoppingFlowStyle(getContext(),
                            dummyEmail, dummyMobile, dummyAmount, R.style.AppTheme, false);
//                    Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                }
            });

            if(bill.getStatus() != null && !bill.getStatus().equals("Pending")) {
                viewHolder.btnQuickPay.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.btnQuickPay.setVisibility(View.VISIBLE);
            }

            viewHolder.btnDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getContext(), InvoiceActivity.class);
                    intent.putExtra("invoiceId",bill.getId());
                    mContext.startActivity(intent);
                }
            });




        }catch (Exception e){
            Log.v(Constants.TAG,"Exception: "+e.toString());
        }

    }



}


