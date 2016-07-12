package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.Help.DateConverter;
import com.kokabi.p.azmonbaz.Objects.HistoryObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by P.kokabi on 6/20/2016.
 */
public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.ViewHolder> {

    Context context;
    DataBase db;
    ArrayList<HistoryObj> historyList = new ArrayList<>();
    String decimal = "%.1f";

    public HistoryRVAdapter(ArrayList<HistoryObj> dataInput) {
        historyList = dataInput;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv, updateTime_tv, percentage_tv, testTime_tv, correctAnswer_tv, unAnswered_tv, incorrectAnswer_tv;
        AppCompatImageButton delete_imgbtn;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            updateTime_tv = (TextView) itemView.findViewById(R.id.updateTime_tv);
            percentage_tv = (TextView) itemView.findViewById(R.id.percentage_tv);
            testTime_tv = (TextView) itemView.findViewById(R.id.testTime_tv);
            correctAnswer_tv = (TextView) itemView.findViewById(R.id.correctAnswer_tv);
            unAnswered_tv = (TextView) itemView.findViewById(R.id.unAnswered_tv);
            incorrectAnswer_tv = (TextView) itemView.findViewById(R.id.incorrectAnswer_tv);

            delete_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.delete_imgbtn);

            db = new DataBase(context);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HistoryObj historyObj = historyList.get(position);

        /*set Title*/
        holder.title_tv.setText(historyObj.getTestName());
        /*set Update Time*/
        Date date = new Date(Long.parseLong(historyObj.getUpdateTime()) * 1000);
        int year = Integer.parseInt(DateFormat.format("yyyy", date).toString());
        int month = Integer.parseInt(DateFormat.format("MM", date).toString());
        int day = Integer.parseInt(DateFormat.format("dd", date).toString());
        DateConverter jCal = new DateConverter();
        jCal.GregorianToPersian(year, month, day);
        holder.updateTime_tv.setText(String.valueOf(jCal.getYear() + " / " + jCal.getMonth() + " / " + jCal.getDay()));
        /*set Percentage*/
        holder.percentage_tv.setText(String.valueOf(historyObj.getTestPercentage() + " % "));
        /*set Elapsed Time*/
        float elapsedTime = (float) Integer.parseInt(historyObj.getTestTime()) / 60;
        if (elapsedTime < 1) {
            holder.testTime_tv.setText(String.valueOf(String.format(decimal, elapsedTime * 60) + " ثانیه"));
        } else {
            holder.testTime_tv.setText(String.valueOf(String.format(decimal, elapsedTime) + " دقیقه"));
        }
        /*set Correct Answer*/
        holder.correctAnswer_tv.setText(String.valueOf(historyObj.getAnsweredQuestion() + " پاسخ صحیح "));
        /*set Unanswered*/
        holder.unAnswered_tv.setText(String.valueOf(historyObj.getUnAnsweredQuestion() + " سوال جواب نداده"));
        /*set Incorrect Answer*/
        holder.incorrectAnswer_tv.setText(String.valueOf(historyObj.getIncorrectQuestion() + " پاسخ اشتباه"));

        /*onClickListeners*/
        onClick(holder, position);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /*Clear Method*/
    public void clearHistory() {
        historyList.clear();
        notifyDataSetChanged();
    }

    /*Update Method*/
    public void updateHistory() {
        notifyDataSetChanged();
    }

    /*Click Listener Methods*/
    private void onClick(final ViewHolder holder, int p) {
        final HistoryObj historyObj = historyList.get(p);
        holder.delete_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.historyDelete(historyObj.getIdHistory());
                historyList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }
}
