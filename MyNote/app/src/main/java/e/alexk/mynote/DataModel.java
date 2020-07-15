package e.alexk.mynote;

public class DataModel {


    String note_text;
    String creation_date;

    int deadline_enable;
    String deadline_date;
    String time_till_deadline;
    String deadline_color;
    int encrypted;

    int id_;


    public DataModel(String note_text, String creation_date, int id_,int encrypted) {
        this.note_text = note_text;
        this.creation_date = creation_date;
        this.id_ = id_;
        this.deadline_date = "";
        this.time_till_deadline= "";
        this.encrypted=encrypted;

    }
    public DataModel(String note_text, String creation_date, int id_, String deadline_date,String time_till_deadline,String deadline_color,int encrypted) {
        this.note_text = note_text;
        this.creation_date = creation_date;
        this.id_ = id_;
        this.deadline_date = deadline_date;
        this.time_till_deadline= time_till_deadline;
        this.encrypted=encrypted;
        this.deadline_color=deadline_color;

    }


    public String getNote_text() {
        return note_text;
    }


    public String getCreation_date() {
        return creation_date;
    }

    public String getDeadline_date() {
        return deadline_date;
    }
    public String getTillDeadline()
    {
        return time_till_deadline;
    }
    public String getdeadline_color()
    {
        return deadline_color;
    }
    public boolean getEncrypted(){
        if(encrypted==1)return true;
        else return  false;
    }

    public String getId() {
        return  String.valueOf(id_);
    }
}

