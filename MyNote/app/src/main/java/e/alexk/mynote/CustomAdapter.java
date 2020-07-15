package e.alexk.mynote;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    public ArrayList<DataModel> dataSet;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView NoteTextView;
        TextView CreationDateView;
        TextView TillDeadline;
        TextView DeadlineDate;
        TextView Id;
        CheckBox CheckBoxEncrypted;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.NoteTextView = (TextView) itemView.findViewById(R.id.textViewNoteText);
            this.CreationDateView = (TextView) itemView.findViewById(R.id.textViewCreationDate);
            this.DeadlineDate = (TextView) itemView.findViewById(R.id.textViewDeadlineDate);
            this.CheckBoxEncrypted = (CheckBox) itemView.findViewById(R.id.CheckBoxEncrypted);
            this.TillDeadline = itemView.findViewById(R.id.textViewTillDeadline);
            this.Id = (TextView) itemView.findViewById(R.id.textViewId);

        }
    }

    private Context mContext;

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(Note_list3.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.NoteTextView;
        TextView textCreationDateView = holder.CreationDateView;
        TextView textDeadlineDate = holder.DeadlineDate;
        TextView textTillDeadline = holder.TillDeadline;
        TextView Id = holder.Id;
        CheckBox isEncrypted = holder.CheckBoxEncrypted;


        textViewName.setText(dataSet.get(listPosition).getNote_text());
        textCreationDateView.setText(dataSet.get(listPosition).getCreation_date());
        String note_deadline_date_string = dataSet.get(listPosition).getDeadline_date();
        textDeadlineDate.setText("Deadline:" + note_deadline_date_string);
        textTillDeadline.setText(dataSet.get(listPosition).getTillDeadline());
        Id.setText(dataSet.get(listPosition).getId());
        Id.setVisibility(View.GONE);

        boolean bool_is_encrypted = dataSet.get(listPosition).getEncrypted();

        isEncrypted.setChecked(bool_is_encrypted);

        if (note_deadline_date_string.equals("")) {
            textTillDeadline.setVisibility(View.GONE);
            textDeadlineDate.setVisibility(View.GONE);

        } else {
            String color = dataSet.get(listPosition).getdeadline_color();
            if (color.equals("color1"))
                textTillDeadline.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.color_1)));
            else if (color.equals("color2"))
                textTillDeadline.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.color_2)));
            else if (color.equals("color3"))
                textTillDeadline.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.color_3)));
            else if (color.equals("color4"))
                textTillDeadline.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.color_4)));
            else if (color.equals("color5"))
                textTillDeadline.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.color_5)));
        }


        if (!bool_is_encrypted) {
            isEncrypted.setVisibility(View.GONE);

        }else
        {
            textViewName.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
