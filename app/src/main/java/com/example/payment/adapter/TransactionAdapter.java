package com.example.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payment.R;
import com.example.payment.model.TransactionModel;

import java.util.ArrayList;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.viewHolder>{

    private ArrayList<TransactionModel> txnList;
    private Context context;

    public TransactionAdapter(ArrayList<TransactionModel> txnList, Context context) {
        this.txnList = txnList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.viewHolder holder, int position) {

        holder.txnImg.setImageResource(txnList.get(position).getImg_txn_way());
        holder.txnDate.setText(txnList.get(position).getTxn_date());
        holder.txtTxtView.setText(txnList.get(position).getTxn_med());
        holder.txnDealer.setText(txnList.get(position).getTxn_dealer());
        holder.getTxnAmtDebCred.setText(txnList.get(position).getTxn_amy_cd());

    }

    @Override
    public int getItemCount() {
        return txnList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private ImageView txnImg;
        private TextView txnDate,txtTxtView,txnDealer,txnAmt,getTxnAmtDebCred;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txnImg = itemView.findViewById(R.id.imv_transaction_type);
            txnDate = itemView.findViewById(R.id.txt_transaction_date);
            txtTxtView = itemView.findViewById(R.id.txn_transaction_type);
            txnDealer = itemView.findViewById(R.id.txt_transaction_merchant);
            txnAmt= itemView.findViewById(R.id.txn_transaction_type);
            getTxnAmtDebCred = itemView.findViewById(R.id.txn_transaction_cred_deb);

        }
    }

}

