package com.kokabi.p.azmonbaz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Activities.CourseAnswersActivity;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.EventBussObj.GeneralMSB;
import com.kokabi.p.azmonbaz.Help.DateConverter;
import com.kokabi.p.azmonbaz.Objects.HistoryObj;
import com.kokabi.p.azmonbaz.R;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by P.kokabi on 6/20/2016.
 */
public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.ViewHolder> implements DroppyClickCallbackInterface {

    Context context;
    DataBase db;
    ArrayList<HistoryObj> historyList = new ArrayList<>();
    String decimal = "%d : %02d";
    int idTest;

    public HistoryRVAdapter() {
    }

    public HistoryRVAdapter(ArrayList<HistoryObj> dataInput) {
        historyList = dataInput;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv, updateTime_tv, percentage_tv, testTimeLabel_tv, testTime_tv, correctAnswer_tv, unAnswered_tv, incorrectAnswer_tv;
        AppCompatImageButton delete_imgbtn, more_imgbtn;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            updateTime_tv = (TextView) itemView.findViewById(R.id.updateTime_tv);
            percentage_tv = (TextView) itemView.findViewById(R.id.percentage_tv);
            testTimeLabel_tv = (TextView) itemView.findViewById(R.id.testTimeLabel_tv);
            testTime_tv = (TextView) itemView.findViewById(R.id.testTime_tv);
            correctAnswer_tv = (TextView) itemView.findViewById(R.id.correctAnswer_tv);
            unAnswered_tv = (TextView) itemView.findViewById(R.id.unAnswered_tv);
            incorrectAnswer_tv = (TextView) itemView.findViewById(R.id.incorrectAnswer_tv);

            delete_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.delete_imgbtn);
            more_imgbtn = (AppCompatImageButton) itemView.findViewById(R.id.more_imgbtn);

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
            holder.testTime_tv.setText(String.valueOf(historyObj.getTestTime() + " ثانیه"));
        } else {
            holder.testTime_tv.setText(String.format(decimal,
                    TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(historyObj.getTestTime()) * 1000),
                    TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(historyObj.getTestTime()) * 1000) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(historyObj.getTestTime()) * 1000))));
            holder.testTimeLabel_tv.setVisibility(View.VISIBLE);
        }
        /*set Correct Answer*/
        holder.correctAnswer_tv.setText(String.valueOf(historyObj.getAnsweredQuestion() + " پاسخ صحیح "));
        /*set Unanswered*/
        holder.unAnswered_tv.setText(String.valueOf(historyObj.getUnAnsweredQuestion() + " سوال جواب نداده"));
        /*set Incorrect Answer*/
        holder.incorrectAnswer_tv.setText(String.valueOf(historyObj.getIncorrectQuestion() + " پاسخ اشتباه"));

        /*onClickListeners*/
        onClick(holder, position);
        setIdTest(historyObj.getIdTest());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void call(View v, int id) {
        switch (id) {
            case R.id.showAnswers:
                context.startActivity(new Intent(context, CourseAnswersActivity.class).putExtra("idTest", getIdTest()));
                break;
        }
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
                if (historyList.size() == 0) {
                    EventBus.getDefault().post(new GeneralMSB("isEmpty"));
                }
            }
        });
        holder.more_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMenu(holder.more_imgbtn);
            }
        });
    }

    private void initMenu(AppCompatImageButton btn) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, btn);
        DroppyMenuPopup droppyMenu = droppyBuilder.fromMenu(R.menu.history)
                .triggerOnAnchorClick(false)
                .setOnClick(this)
                .build();
        droppyMenu.show();
    }
}
